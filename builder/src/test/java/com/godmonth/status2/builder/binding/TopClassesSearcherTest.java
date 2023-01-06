package com.godmonth.status2.builder.binding;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Set;

class TopClassesSearcherTest {
    @Test
    void name() throws IOException, ClassNotFoundException {
        Set<Class<?>> classes = TopClassesSearcher.searchTopClasses(getClass().getPackage().getName(), ClassLoader.getSystemClassLoader());
        System.out.println(classes);
    }
}