package com.godmonth.status.executor.intf;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 同步结果
 *
 * @param <SYMBOL>
 * @author shenyue
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SyncResult<MODEL, SYMBOL> {
    protected MODEL model;

    protected SYMBOL symbol;

    protected Object value;

}
