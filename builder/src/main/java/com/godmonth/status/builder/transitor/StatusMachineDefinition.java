package com.godmonth.status.builder.transitor;

import lombok.Data;

import java.util.List;

/**
 * 状态定义
 *
 * @param <STATUS>
 * @param <TRIGGER>
 * @author shenyue
 */
@Data
public class StatusMachineDefinition<STATUS, TRIGGER> {

	private STATUS status;

    private List<TriggerDefinition<TRIGGER, STATUS>> triggers;

}
