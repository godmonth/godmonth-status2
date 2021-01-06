package com.godmonth.status2.builder.domain;

import com.godmonth.status2.annotations.Trigger;

/**
 * 测试状态
 *
 * @author shenyue
 */
@Trigger(SampleTrigger.class)
public enum SampleStatus {
    /**
     * 已创建
     */
    CREATED("已创建"),

    /**
     * 已付款
     */
    PAID("已付款");

    private String description;

    private SampleStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
