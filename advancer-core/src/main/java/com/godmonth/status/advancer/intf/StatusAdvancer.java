package com.godmonth.status.advancer.intf;

/**
 * 状态推进器
 *
 * @param <MODEL> MODEL
 * @author shenyue
 */
@Deprecated
public interface StatusAdvancer<MODEL, INST, TRIGGER> extends StatusAdvancer2<MODEL, INST, TRIGGER> {
    Object getKey();
}
