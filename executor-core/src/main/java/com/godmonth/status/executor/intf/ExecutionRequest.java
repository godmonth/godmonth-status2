package com.godmonth.status.executor.intf;

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
