package com.godmonth.status.test.sample.machine.cfg2;

import com.godmonth.status.advancer.intf.StatusAdvancer2;
import com.godmonth.status.analysis.impl.model.AnnotationBeanModelAnalysis;
import com.godmonth.status.analysis.impl.model.TypeFieldPredicate;
import com.godmonth.status.analysis.impl.sm.AnnotationStateMachineAnalysis;
import com.godmonth.status.analysis.intf.StateMachineAnalysis;
import com.godmonth.status.builder.advancer.AdvancerBindingListBuilder;
import com.godmonth.status.builder.binding.AnnotationField;
import com.godmonth.status.builder.binding.BindingKeyUtils;
import com.godmonth.status.builder.entry.StatusEntryBindingListBuilder;
import com.godmonth.status.builder.transitor.JsonDefinitionBuilder;
import com.godmonth.status.executor.impl.DefaultOrderExecutor;
import com.godmonth.status.executor.intf.OrderExecutor2;
import com.godmonth.status.test.sample.domain.SampleModel;
import com.godmonth.status.test.sample.domain.SampleStatus;
import com.godmonth.status.test.sample.machine.advancer2.SampleStatusBinding;
import com.godmonth.status.test.sample.machine.inst.SampleInstructionBinding;
import com.godmonth.status.test.sample.machine.trigger.SampleTrigger;
import com.godmonth.status.transitor.core.impl.SimpleStatusTransitor;
import com.godmonth.status.transitor.core.intf.StatusTransitor;
import com.godmonth.status.transitor.tx.impl.TxStatusTransitorImpl;
import com.godmonth.status.transitor.tx.intf.StatusEntry;
import com.godmonth.status.transitor.tx.intf.TxStatusTransitor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.transaction.support.TransactionOperations;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * <p></p >
 *
 * @author shenyue
 */
@ComponentScan
@Configuration
public class SampleOrderExecutorConfig2 {

    private static AnnotationField sampleStatusBindingField = AnnotationField.builder().annoClass(SampleStatusBinding.class).build();
    private static AnnotationField samplInstBindingField = AnnotationField.builder().annoClass(SampleInstructionBinding.class).build();
    public static Function<Class, Object> KEY_BINDING = componentClass -> BindingKeyUtils.getBindingKey(componentClass, sampleStatusBindingField, samplInstBindingField);

    @Bean
    public StateMachineAnalysis sampleStateMachineAnalysis() {
        AnnotationBeanModelAnalysis modelAnalysis = AnnotationBeanModelAnalysis.<SampleModel>annoBuilder().modelClass(SampleModel.class).predicateList(Arrays.asList(TypeFieldPredicate.builder().propertyName("type").expectedValue("test").build())).build();
        return AnnotationStateMachineAnalysis.annoBuilder().modelAnalysis(modelAnalysis).build();
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
    @Bean
    public OrderExecutor2<SampleModel, Object> sampleModelOrderExecutor(AutowireCapableBeanFactory beanFactory, @Qualifier("sampleStateMachineAnalysis") StateMachineAnalysis sampleStateMachineAnalysis, @Qualifier("sampleStatusTxStatusTransitor") TxStatusTransitor txStatusTransitor) throws IOException, ClassNotFoundException {
        List<Pair<Object, StatusAdvancer2>> pairList = AdvancerBindingListBuilder.builder().autowireCapableBeanFactory(beanFactory).modelClass(SampleModel.class).packageName("com.godmonth.status.test.sample.machine.advancer2").keyFinder(KEY_BINDING).build();
        OrderExecutor2<SampleModel, Object> oe = DefaultOrderExecutor.<SampleModel, Object, Object>builder().modelAnalysis(sampleStateMachineAnalysis.getModelAnalysis()).advancerBindingList(pairList).txStatusTransitor(txStatusTransitor).build();
        return oe;
    }

    /**
     * entry package 可以定义在参数
     *
     * @param entityManager
     * @param transactionOperations
     * @param statusTransitor
     * @param sampleStateMachineAnalysis
     * @param beanFactory
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Bean
    public TxStatusTransitor sampleStatusTxStatusTransitor(EntityManager entityManager, TransactionOperations transactionOperations, @Qualifier("sampleStatusTransitor") StatusTransitor statusTransitor, @Qualifier("sampleStateMachineAnalysis") StateMachineAnalysis sampleStateMachineAnalysis, AutowireCapableBeanFactory beanFactory) throws IOException, ClassNotFoundException {
        List<Pair<Object, StatusEntry>> pairList = StatusEntryBindingListBuilder.builder().autowireCapableBeanFactory(beanFactory).packageName("com.godmonth.status.test.sample.machine.entry2").keyFinder(KEY_BINDING).build();
        return TxStatusTransitorImpl.builder().modelMerger(entityManager::merge).transactionOperations(transactionOperations).modelAnalysis(sampleStateMachineAnalysis.getModelAnalysis()).statusTransitor(statusTransitor).statusEntryBindList(pairList).build();
    }

    @Bean
    public StatusTransitor<SampleStatus, SampleTrigger> sampleStatusTransitor(@Value("classpath:/sample-status.json") Resource configResource, @Qualifier("sampleStateMachineAnalysis") StateMachineAnalysis sampleStateMachineAnalysis) throws IOException {
        Function<SampleStatus, Function<SampleTrigger, SampleStatus>> function = JsonDefinitionBuilder.<SampleStatus, SampleTrigger>builder().resource(configResource).statusClass(sampleStateMachineAnalysis.getModelAnalysis().getStatusClass()).triggerClass(sampleStateMachineAnalysis.getTriggerClass()).build();
        return new SimpleStatusTransitor(function);
    }

}
