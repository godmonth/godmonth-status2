package com.godmonth.status2.test.sample.domain;

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
    PAID("已付款"),
    /**
     * 已发货
     */
    DELIVERED("已发货"),
    /**
     * 已评价
     */
    EVALUATED("已评价");

    private String description;


    private SampleStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
