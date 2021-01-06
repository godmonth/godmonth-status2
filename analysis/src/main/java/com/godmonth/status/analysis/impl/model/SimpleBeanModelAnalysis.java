package com.godmonth.status.analysis.impl.model;

import com.godmonth.status.analysis.intf.ModelAnalysis;
import jodd.bean.BeanUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Predicate;

/**
 * @param <MODEL>
 */
@ToString
@Getter
public class SimpleBeanModelAnalysis<MODEL> implements ModelAnalysis<MODEL> {
    protected Class<MODEL> modelClass;

    protected String statusPropertyName;

    protected List<Predicate<MODEL>> predicateList;

    protected Class statusClass;

    public SimpleBeanModelAnalysis(Class<MODEL> modelClass, String statusPropertyName) {
        this(modelClass, statusPropertyName, null);
    }

    @Builder
    public SimpleBeanModelAnalysis(Class<MODEL> modelClass, String statusPropertyName, List<Predicate<MODEL>> predicateList) {
        this.modelClass = modelClass;
        this.statusPropertyName = statusPropertyName;
        this.predicateList = predicateList;
        initStatusClass();
    }

    protected void initStatusClass() {
        Field statusField = FieldUtils.getField(modelClass, statusPropertyName, true);
        Validate.notNull(statusField, "can't get statusField[" + statusPropertyName + "]");
        statusClass = statusField.getType();
    }


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
