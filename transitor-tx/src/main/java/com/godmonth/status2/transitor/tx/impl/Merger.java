package com.godmonth.status2.transitor.tx.impl;

/**
 * <p></p >
 *
 * @author shenyue
 */
public interface Merger<MODEL> {
    MODEL mergeInTx(MODEL model);
}
