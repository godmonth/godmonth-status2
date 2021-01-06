package com.godmonth.status.transitor.tx.impl;

/**
 * <p></p >
 *
 * @author shenyue
 */
public interface Merger<MODEL> {
    MODEL mergeInTx(MODEL model);
}
