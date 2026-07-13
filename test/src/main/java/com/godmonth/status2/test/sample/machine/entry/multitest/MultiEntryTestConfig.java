package com.godmonth.status2.test.sample.machine.entry.multitest;

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
 * <p>多 Entry 排序测试专用配置</p>
 *
 * @author shenyue
 */
@Configuration
@OrderExecutor(beanName = "multiEntryTestOrderExecutor", stateMachineDefinitionResource = "classpath:/sample-status.json", advancerBasePackages = "com.godmonth.status2.test.sample.machine.advancer", entryBasePackages = "com.godmonth.status2.test.sample.machine.entry.multitest")
public class MultiEntryTestConfig {

    @Bean
    StateMachineAnalysis<SampleModel> multiEntryTestStateMachineAnalysis() {
        final AnnotationBeanModelAnalysis<SampleModel> analysis = AnnotationBeanModelAnalysis.<SampleModel>annoBuilder().modelClass(SampleModel.class).build();
        return StateMachineAnalysis.<SampleModel>builder().modelAnalysis(analysis).statusBindingField(AnnotationField.builder().annoClass(SampleStatusBinding.class).build()).instBindingField(AnnotationField.builder().annoClass(SampleInstructionBinding.class).build()).build();
    }
}
