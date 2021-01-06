package com.godmonth.status.analysis.impl.model;

import jodd.bean.BeanUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.function.Predicate;

/**
 * <p></p >
 *
 * @author shenyue
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TypeFieldPredicate implements Predicate {


    @Setter
    private Object expectedValue;
    @Setter
    private String propertyName;

    @Override
    public boolean test(Object model) {
        Object actualValue = BeanUtil.silent.getProperty(model, propertyName);
        return expectedValue.equals(actualValue);
    }
}
