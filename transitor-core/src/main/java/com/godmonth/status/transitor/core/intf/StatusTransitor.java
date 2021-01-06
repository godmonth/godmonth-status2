package com.godmonth.status.transitor.core.intf;

/**
 * 状态跃迁器
 * 
 * @author shenyue
 *
 * @param <TRIGGER>
 * @param <STATUS>
 */
public interface StatusTransitor<STATUS, TRIGGER> {

	STATUS transit(STATUS status, TRIGGER trigger);

}
