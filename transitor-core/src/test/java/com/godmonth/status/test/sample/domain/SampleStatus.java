package com.godmonth.status.test.sample.domain;

import lombok.Getter;

/**
 * 测试状态
 *
 * @author shenyue
 */

public enum SampleStatus {
    /**
     * 已创建
     */
    CREATED("已创建"),

    /**
     * 已付款
     */
    PAID("已付款");
    @Getter
    private String description;

    private SampleStatus(String description) {
        this.description = description;
    }


}
