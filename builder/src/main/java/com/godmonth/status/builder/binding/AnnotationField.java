package com.godmonth.status.builder.binding;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.lang.annotation.Annotation;

/**
 * <p></p >
 *
 * @author shenyue
 */
@Builder
@Data
public class AnnotationField {

    @NonNull
    private Class<? extends Annotation> annoClass;

    @Builder.Default
    @NonNull
    private String propertyName = "value";
}
