package com.godmonth.status2.executor.utils;

import org.apache.commons.lang3.exception.ContextedRuntimeException;

import java.util.concurrent.ExecutionException;

public class ExecutionExceptionUtils {
    public static RuntimeException convert(ExecutionException e) {
        if (e.getCause() != null) {
            if (e.getCause() instanceof RuntimeException) {
                return (RuntimeException) e.getCause();
            } else {
                return new ContextedRuntimeException(e.getCause());
            }
        } else {
            return new ContextedRuntimeException(e);
        }
    }
}
