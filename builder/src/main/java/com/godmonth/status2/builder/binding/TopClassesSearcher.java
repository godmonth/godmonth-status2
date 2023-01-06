package com.godmonth.status2.builder.binding;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class TopClassesSearcher {

    public static Set<Class<?>> searchTopClasses(String basePackageName, ClassLoader classLoader) throws IOException, ClassNotFoundException {
        log.debug("start search topClass:{}", basePackageName);
        ResourceLoader resourceLoader = new DefaultResourceLoader(classLoader);
        String folderName = RegExUtils.replacePattern(basePackageName, "\\.", "/");
        Resource[] resources = new PathMatchingResourcePatternResolver(resourceLoader).getResources("classpath*:" + folderName + "/**/*");
        Set<Class<?>> topClassSet = new HashSet<>();
        for (Resource resource : resources) {
            URI uri = resource.getURI();
            String path = uri.toString();
            String subPath = StringUtils.substringAfterLast(path, folderName);
            if (StringUtils.isNotBlank(subPath) && subPath.endsWith(".class") && !subPath.contains("$")/*ignore inner class*/) {
                String subClassPath = StringUtils.substringBeforeLast(subPath, ".class");
                String subClassName = RegExUtils.replacePattern(subClassPath, "/", "\\.");
                String fullClassName = basePackageName + subClassName;
                log.trace("topLevelClassName:{}", fullClassName);
                Class<?> aClass = classLoader.loadClass(fullClassName);
                if (aClass != null) {
                    topClassSet.add(aClass);
                }
            }
        }
        log.debug("finished search topClass.size:{}", topClassSet.size());
        return topClassSet;
    }
}
