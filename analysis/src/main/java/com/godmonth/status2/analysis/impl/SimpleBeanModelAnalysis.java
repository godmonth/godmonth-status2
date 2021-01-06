package com.godmonth.status2.analysis.impl;

import com.godmonth.status2.analysis.intf.ModelAnalysis;
import com.godmonth.status2.annotations.Status;
import jodd.bean.BeanUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
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

    private static String getStatusPropertyName(Class modelClass) {
        Field[] fields = FieldUtils.getAllFields(modelClass);
        for (Field field : fields) {
            Status annotation = field.getAnnotation(Status.class);
            if (annotation != null) {
                return field.getName();

            }
        }
        throw new IllegalArgumentException("statusPropertyName is null.");
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

    protected void initStatusClass() {
        Field statusField = FieldUtils.getField(modelClass, statusPropertyName, true);
        Validate.notNull(statusField, "can't get statusField[" + statusPropertyName + "]");
        statusClass = statusField.getType();
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
