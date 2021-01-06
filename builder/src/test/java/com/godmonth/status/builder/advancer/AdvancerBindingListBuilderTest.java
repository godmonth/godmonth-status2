package com.godmonth.status.builder.advancer;

import com.godmonth.status.advancer.intf.StatusAdvancer2;
import com.godmonth.status.builder.domain.SampleModel;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import java.io.IOException;
import java.util.List;

/**
 * <p></p >
 *
 * @author shenyue
 */
class AdvancerBindingListBuilderTest {
    private StatusAdvancer2 statusAdvancer = new PayAdvancer();

    @Test
    void name() throws IOException, ClassNotFoundException {
        AutowireCapableBeanFactory factory = Mockito.mock(AutowireCapableBeanFactory.class);
        Mockito.when(factory.autowire(Mockito.same(PayAdvancer.class), Mockito.anyInt(), Mockito.anyBoolean())).thenReturn(statusAdvancer);
        List<Pair<Object, StatusAdvancer2>> build = AdvancerBindingListBuilder.builder().autowireCapableBeanFactory(factory).modelClass(SampleModel.class).packageName("com.godmonth.status.builder.advancer").build();
        System.out.println(build);
    }

}