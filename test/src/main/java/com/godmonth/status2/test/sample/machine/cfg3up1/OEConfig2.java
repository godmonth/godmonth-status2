package com.godmonth.status2.test.sample.machine.cfg3up1;

import com.godmonth.status2.analysis.impl.AnnotationBeanModelAnalysis;
import com.godmonth.status2.analysis.impl.AnnotationField;
import com.godmonth.status2.analysis.intf.StateMachineAnalysis;
import com.godmonth.status2.builder.executor.OrderExecutor;
import com.godmonth.status2.test.sample.domain.SampleModel;
import com.godmonth.status2.test.sample.machine.advancer.SampleStatusBinding;
import com.godmonth.status2.test.sample.machine.inst.SampleInstructionBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p></p >
 *
 * @author shenyue
 */
@Configuration
@OrderExecutor(beanName = "orderExecutor", stateMachineDefinitionResource = "classpath:/sample-status.json", advancerBasePackages = "com.godmonth.status2.test.sample.machine.advancer", entryBasePackages = "com.godmonth.status2.test.sample.machine.entry", transactionOperationsRef = "tt2", entityManagerRef = "e2")
public class OEConfig2 {
    @Bean
    StateMachineAnalysis stateMachineAnalysis() {
        final AnnotationBeanModelAnalysis<SampleModel> analysis = AnnotationBeanModelAnalysis.<SampleModel>annoBuilder().modelClass(SampleModel.class).build();

        final StateMachineAnalysis<SampleModel> analysis1 = StateMachineAnalysis.<SampleModel>builder().modelAnalysis(analysis).statusBindingField(AnnotationField.builder().annoClass(SampleStatusBinding.class).build()).instBindingField(AnnotationField.builder().annoClass(SampleInstructionBinding.class).build()).build();
        return analysis1;
    }


}
