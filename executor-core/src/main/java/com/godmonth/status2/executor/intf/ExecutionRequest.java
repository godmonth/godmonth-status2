package com.godmonth.status2.executor.intf;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p></p >
 *
 * @author shenyue
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExecutionRequest<MODEL, INST> {
    private MODEL model;

    private INST instruction;

    private Object message;

    public ExecutionRequest(MODEL model) {
        this.model = model;
    }
}
