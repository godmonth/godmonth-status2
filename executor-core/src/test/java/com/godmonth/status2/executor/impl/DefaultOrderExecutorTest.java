package com.godmonth.status2.executor.impl;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.function.Function;

/**
 * <p></p >
 *
 * @author shenyue
 */
class DefaultOrderExecutorTest {
    @Test
    void name() {
        DefaultOrderExecutor defaultOrderExecutor = new DefaultOrderExecutor((Function)null, null, null, null);
        System.out.println(defaultOrderExecutor);
    }
}