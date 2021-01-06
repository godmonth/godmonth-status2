package com.godmonth.status.advancer.intf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 同步结果
 *
 * @param <SYMBOL>
 * @author shenyue
 */
@Deprecated
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SyncResult<MODEL, SYMBOL> {
    protected MODEL model;

    protected SYMBOL symbol;

    protected Object value;

    /**
     * @param model
     */
    public SyncResult(MODEL model) {
        this.model = model;
    }

    /**
     * @param model
     * @param symbol
     */
    public SyncResult(MODEL model, SYMBOL symbol) {
        this.model = model;
        this.symbol = symbol;
    }

}
