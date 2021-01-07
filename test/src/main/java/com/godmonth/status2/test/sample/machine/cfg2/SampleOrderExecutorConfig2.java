package com.godmonth.status2.test.sample.machine.cfg2;

import com.godmonth.status2.advancer.intf.StatusAdvancer;
import com.godmonth.status2.analysis.impl.AnnotationBeanModelAnalysis;
import com.godmonth.status2.analysis.impl.AnnotationField;
import com.godmonth.status2.analysis.impl.TypeFieldPredicate;
import com.godmonth.status2.analysis.intf.StateMachineAnalysis;
import com.godmonth.status2.builder.advancer.AdvancerBindingListBuilder;
import com.godmonth.status2.builder.entry.StatusEntryBindingListBuilder;
import com.godmonth.status2.builder.transitor.JsonDefinitionBuilder;
import com.godmonth.status2.executor.impl.DefaultOrderExecutor;
import com.godmonth.status2.executor.intf.OrderExecutor;
import com.godmonth.status2.test.sample.domain.SampleModel;
import com.godmonth.status2.test.sample.domain.SampleStatus;
import com.godmonth.status2.test.sample.machine.advancer.SampleStatusBinding;
import com.godmonth.status2.test.sample.machine.inst.SampleInstructionBinding;
import com.godmonth.status2.test.sample.machine.trigger.SampleTrigger;
import com.godmonth.status2.transitor.core.impl.SimpleStatusTransitor;
import com.godmonth.status2.transitor.tx.impl.TxStatusTransitorImpl;
import com.godmonth.status2.transitor.tx.intf.StatusEntry;
import com.godmonth.status2.transitor.tx.intf.TxStatusTransitor;
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

    @Bean
    public StateMachineAnalysis<SampleModel> sampleStateMachineAnalysis() {
        final AnnotationBeanModelAnalysis modelAnalysis = AnnotationBeanModelAnalysis.<SampleModel>annoBuilder().modelClass(SampleModel.class).predicate(TypeFieldPredicate.builder().propertyName("type").expectedValue("test").build()).build();
        AnnotationField sampleStatusBindingField = AnnotationField.builder().annoClass(SampleStatusBinding.class).build();
        AnnotationField samplInstBindingField = AnnotationField.builder().annoClass(SampleInstructionBinding.class).build();
        return StateMachineAnalysis.<SampleModel>builder().modelAnalysis(modelAnalysis).statusBindingField(sampleStatusBindingField).instBindingField(samplInstBindingField).build();
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
    public OrderExecutor<SampleModel, Object> sampleModelOrderExecutor(AutowireCapableBeanFactory beanFactory, @Qualifier("sampleStateMachineAnalysis") StateMachineAnalysis sampleStateMachineAnalysis, @Qualifier("sampleStatusTxStatusTransitor") TxStatusTransitor txStatusTransitor) throws IOException, ClassNotFoundException {
        List<Pair<Object, StatusAdvancer>> advancerBindingList = AdvancerBindingListBuilder.builder().autowireCapableBeanFactory(beanFactory).modelClass(SampleModel.class).packageName("com.godmonth.status2.test.sample.machine.advancer").bindingKeyFunction(sampleStateMachineAnalysis.getBindingKeyFunction()).build();
        //advancerBindingList.add(xxx);增加你需要定制的推进器
        return DefaultOrderExecutor.builder().modelAnalysis(sampleStateMachineAnalysis.getModelAnalysis()).advancerBindingList(advancerBindingList).txStatusTransitor(txStatusTransitor).build();
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
    @Bean
    public TxStatusTransitor sampleStatusTxStatusTransitor(AutowireCapableBeanFactory beanFactory, EntityManager entityManager, TransactionOperations transactionOperations, @Qualifier("sampleStateMachineAnalysis") StateMachineAnalysis sampleStateMachineAnalysis, @Value("classpath:/sample-status.json") Resource configResource) throws IOException, ClassNotFoundException {
        Function<SampleStatus, Function<SampleTrigger, SampleStatus>> function = JsonDefinitionBuilder.<SampleStatus, SampleTrigger>builder().resource(configResource).statusClass(sampleStateMachineAnalysis.getModelAnalysis().getStatusClass()).triggerClass(sampleStateMachineAnalysis.getModelAnalysis().getTriggerClass()).build();
        final SimpleStatusTransitor simpleStatusTransitor = new SimpleStatusTransitor(function);
        List<Pair<Object, StatusEntry>> pairList = StatusEntryBindingListBuilder.builder().autowireCapableBeanFactory(beanFactory).packageName("com.godmonth.status2.test.sample.machine.entry").bindingKeyFunction(sampleStateMachineAnalysis.getBindingKeyFunction()).build();
        //statusEntryBindList.add(vvv);加你需要定制的入口回调
        return TxStatusTransitorImpl.builder().modelMerger(entityManager::merge).transactionOperations(transactionOperations).modelAnalysis(sampleStateMachineAnalysis.getModelAnalysis()).statusTransitor(simpleStatusTransitor).statusEntryBindList(pairList).build();
    }


}
