package com.godmonth.status2.analysis.impl;

import com.godmonth.status2.analysis.intf.ModelAnalysis;
import jodd.bean.BeanUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.function.Predicate;

/**
 * @param <MODEL>
 */
@Builder
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleBeanModelAnalysis<MODEL> implements ModelAnalysis<MODEL> {
    protected Class<MODEL> modelClass;

    protected String statusPropertyName;

    protected Class statusClass;

    protected Class triggerClass;

    @Singular("predicate")
    protected List<Predicate<MODEL>> predicateList;

    @Override
    public void validate(MODEL model) {
        Validate.isTrue(modelClass.equals(model.getClass()), "modeClass mismatched,expected:%s,actual:%s", modelClass, model.getClass());
        if (predicateList != null) {
            for (Predicate<MODEL> modelPredicate : predicateList) {
                modelPredicate.test(model);
            }
        }
    }

    @Override
    public <STATUS> STATUS getStatus(MODEL model) {
        return BeanUtil.silent.getProperty(model, statusPropertyName);
    }

    @Override
    public <STATUS> void setStatus(MODEL model, STATUS value) {
        BeanUtil.silent.setProperty(model, statusPropertyName, value);
    }


}
