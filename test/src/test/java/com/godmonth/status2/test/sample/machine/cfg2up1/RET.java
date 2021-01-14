package com.godmonth.status2.test.sample.machine.cfg2up1;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.propertyeditors.InputStreamEditor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceEditor;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * <p></p >
 *
 * @author shenyue
 */
public class RET {
    @Test
    void name() throws IOException {
        String s = "/sample-status.json";
        final ClassPathResource classPathResource = new ClassPathResource(s);
        System.out.println(classPathResource);
        final InputStream inputStream = classPathResource.getInputStream();
        final String s1 = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        System.out.println(s1);
    }

    @Test
    void name2() throws IOException {
        ResourceEditor resourceEditor = new ResourceEditor();
        resourceEditor.setAsText("classpath:/sample-status.json");
        final Object value = resourceEditor.getValue();
        InputStreamEditor inputStreamEditor = new InputStreamEditor();
        inputStreamEditor.setAsText("classpath:/sample-status.json");
        final InputStream inputStream = (InputStream) inputStreamEditor.getValue();
        final String s1 = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        System.out.println(s1);
    }


    @Test
    void name3() throws IOException {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        final Resource resource = resourceLoader.getResource("classpath:/sample-status.json");
        System.out.println(resource);
        final InputStream inputStream = resource.getInputStream();
        final String s1 = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        System.out.println(s1);
    }
}
