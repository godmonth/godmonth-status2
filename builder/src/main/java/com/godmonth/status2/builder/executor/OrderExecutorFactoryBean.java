package com.godmonth.status2.builder.executor;

import com.godmonth.status2.advancer.intf.StatusAdvancer;
import com.godmonth.status2.analysis.intf.StateMachineAnalysis;
import com.godmonth.status2.builder.advancer.AdvancerBindingListBuilder;
import com.godmonth.status2.builder.entry.StatusEntryBindingListBuilder;
import com.godmonth.status2.builder.transitor.JsonDefinitionBuilder;
import com.godmonth.status2.executor.impl.DefaultOrderExecutor;
import com.godmonth.status2.executor.intf.OrderExecutor;
import com.godmonth.status2.transitor.core.impl.SimpleStatusTransitor;
import com.godmonth.status2.transitor.tx.impl.TxStatusTransitorImpl;
import com.godmonth.status2.transitor.tx.intf.StatusEntry;
import com.godmonth.status2.transitor.tx.intf.TxStatusTransitor;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ContextedRuntimeException;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.transaction.support.TransactionOperations;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * <p></p >
 *
 * @author shenyue
 */
public class OrderExecutorFactoryBean implements FactoryBean<OrderExecutor>, ApplicationContextAware {
    @Setter
    private Map<String, Object> annotationAttributes;

    @Setter
    private Class underlyClass;

    private ApplicationContext applicationContext;


    @Override
    public com.godmonth.status2.executor.intf.OrderExecutor getObject() throws Exception {
        final Package defaultPackage = underlyClass.getPackage();

        final String stateMachineAnalysisRef = (String) annotationAttributes.get("stateMachineAnalysisRef");
        StateMachineAnalysis stateMachineAnalysis = null;
        if (StringUtils.isNotBlank(stateMachineAnalysisRef)) {
            stateMachineAnalysis = (StateMachineAnalysis) applicationContext.getBean(stateMachineAnalysisRef);
        } else {
            stateMachineAnalysis = applicationContext.getBean(StateMachineAnalysis.class);
        }

        final String entityManagerRef = (String) annotationAttributes.get("entityManagerRef");
        EntityManager entityManager = null;
        if (StringUtils.isNotBlank(entityManagerRef)) {
            entityManager = (EntityManager) applicationContext.getBean(entityManagerRef);
        } else {
            entityManager = applicationContext.getBean(EntityManager.class);
        }

        final String transactionOperationsRef = (String) annotationAttributes.get("transactionOperationsRef");
        TransactionOperations transactionOperations = null;
        if (StringUtils.isNotBlank(entityManagerRef)) {
            transactionOperations = (TransactionOperations) applicationContext.getBean(transactionOperationsRef);
        } else {
            transactionOperations = applicationContext.getBean(TransactionOperations.class);
        }


        final String stateMachineDefinitionResourceStr = (String) annotationAttributes.get("stateMachineDefinitionResource");
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        final Resource stateMachineDefinitionResource = resourceLoader.getResource(stateMachineDefinitionResourceStr);

        String[] entryBasePackages = (String[]) annotationAttributes.get("entryBasePackages");

        Set<String> entryPackages = new HashSet<>();
        if (ArrayUtils.isNotEmpty(entryBasePackages)) {
            entryPackages.addAll(Arrays.asList(entryBasePackages));
        } else {
            entryPackages.add(defaultPackage.getName());
        }

        TxStatusTransitor txStatusTransitor = null;
        try {
            txStatusTransitor = txStatusTransitor(applicationContext.getAutowireCapableBeanFactory(), entityManager, transactionOperations, stateMachineAnalysis, stateMachineDefinitionResource, entryPackages);
        } catch (IOException | ClassNotFoundException e) {
            throw new ContextedRuntimeException(e);
        }
        String[] advancerBasePackages = (String[]) annotationAttributes.get("advancerBasePackages");

        Set<String> advancerPackages = new HashSet<>();
        if (ArrayUtils.isNotEmpty(advancerBasePackages)) {
            advancerPackages.addAll(Arrays.asList(advancerBasePackages));
        } else {
            advancerPackages.add(defaultPackage.getName());
        }
        com.godmonth.status2.executor.intf.OrderExecutor orderExecutor = null;
        try {
            orderExecutor = orderExecutor(applicationContext.getAutowireCapableBeanFactory(), stateMachineAnalysis, txStatusTransitor, advancerPackages);
        } catch (IOException | ClassNotFoundException e) {
            throw new ContextedRuntimeException(e);
        }
        return orderExecutor;
    }

    @Override
    public Class<?> getObjectType() {
        return OrderExecutor.class;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * advancer package 可以定义在参数
     *
     * @param beanFactory
     * @param txStatusTransitor
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private com.godmonth.status2.executor.intf.OrderExecutor orderExecutor(AutowireCapableBeanFactory beanFactory, StateMachineAnalysis machineAnalysis, TxStatusTransitor txStatusTransitor, Set<String> advancerPacakgeNames) throws IOException, ClassNotFoundException {
        List<Pair<Object, StatusAdvancer>> advancerBindingList = AdvancerBindingListBuilder.builder().autowireCapableBeanFactory(beanFactory).modelClass(machineAnalysis.getModelAnalysis().getModelClass()).packageNames(advancerPacakgeNames).bindingKeyFunction(machineAnalysis.getBindingKeyFunction()).build();
        //advancerBindingList.add(xxx);增加你需要定制的推进器
        return DefaultOrderExecutor.builder().modelAnalysis(machineAnalysis.getModelAnalysis()).advancerBindingList(advancerBindingList).txStatusTransitor(txStatusTransitor).build();
    }

    /**
     * entry package 可以定义在参数
     *
     * @param entityManager
     * @param transactionOperations
     * @param beanFactory
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private TxStatusTransitor txStatusTransitor(AutowireCapableBeanFactory beanFactory, EntityManager entityManager, TransactionOperations transactionOperations, StateMachineAnalysis analysis, Resource configResource, Set<String> entryPackages) throws IOException, ClassNotFoundException {
        Function function = JsonDefinitionBuilder.builder().resource(configResource).statusClass(analysis.getModelAnalysis().getStatusClass()).triggerClass(analysis.getModelAnalysis().getTriggerClass()).build();
        final SimpleStatusTransitor simpleStatusTransitor = new SimpleStatusTransitor(function);
        List<Pair<Object, StatusEntry>> pairList = StatusEntryBindingListBuilder.builder().autowireCapableBeanFactory(beanFactory).packageNames(entryPackages).bindingKeyFunction(analysis.getBindingKeyFunction()).build();
        return TxStatusTransitorImpl.builder().modelMerger(entityManager::merge).transactionOperations(transactionOperations).modelAnalysis(analysis.getModelAnalysis()).statusTransitor(simpleStatusTransitor).statusEntryBindList(pairList).build();
    }
}
