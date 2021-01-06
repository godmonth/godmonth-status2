package com.godmonth.status2.builder.advancer;

import com.godmonth.status2.advancer.intf.StatusAdvancer;
import com.godmonth.status2.annotations.Advancer;
import com.godmonth.status2.builder.binding.BindingListBuilder;
import lombok.Builder;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <p></p >
 *
 * @author shenyue
 */
@Slf4j
public class AdvancerBindingListBuilder {

    @Builder
    private static List<Pair<Object, StatusAdvancer>> build(@Singular Set<String> packageNames, Class modelClass, Predicate<Class> predicate, AutowireCapableBeanFactory autowireCapableBeanFactory, Function<Class, Object> keyFinder) throws IOException, ClassNotFoundException {
        return BindingListBuilder.<StatusAdvancer>builder().enableAnnotationClass(Advancer.class).packageNames(packageNames).ancestorClass(StatusAdvancer.class).modelClass(modelClass).predicate(predicate).autowireCapableBeanFactory(autowireCapableBeanFactory).keyFinder(keyFinder).build();
    }


}
