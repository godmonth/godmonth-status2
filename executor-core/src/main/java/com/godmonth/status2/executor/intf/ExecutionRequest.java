package com.godmonth.status2.executor.intf;

import lombok.Builder;
import lombok.Data;

/**
 * <p></p >
 *
 * @author shenyue
 */
@Builder
@Data
public class ExecutionRequest<MODEL, INST> {
    private MODEL model;

    private INST instruction;

    private Object message;
}
