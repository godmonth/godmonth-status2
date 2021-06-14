package com.godmonth.status2.builder.advancer;

import com.godmonth.status2.advancer.intf.StatusAdvancer;
import com.godmonth.status2.annotations.Advancer;
import com.godmonth.status2.builder.binding.BindingListBuilder;
import lombok.Builder;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <p></p >
 *
 * @author shenyue
 */
@Slf4j
public class AdvancerBindingListBuilder {

    @Builder
    private static List<Pair<Object, StatusAdvancer>> build(ClassLoader classLoader, @Singular Set<String> packageNames, Class modelClass, Predicate<Class> predicate, AutowireCapableBeanFactory autowireCapableBeanFactory, Function<Class, Object[]> bindingKeyFunction) throws IOException, ClassNotFoundException {
        final List<Pair<Object, StatusAdvancer>> advancer = BindingListBuilder.<StatusAdvancer>builder().classLoader(classLoader).enableAnnotationClass(Advancer.class).packageNames(packageNames).ancestorClass(StatusAdvancer.class).modelClass(modelClass).predicate(predicate).autowireCapableBeanFactory(autowireCapableBeanFactory).bindingKeyFunction(bindingKeyFunction).build();
        final long count = advancer.stream().map(Pair::getLeft).distinct().count();
        Validate.isTrue(count == advancer.size(), "advancer keys are duplicated.%s", advancer.stream().map(Pair::getLeft).collect(Collectors.toList()));
        return advancer;
    }


}
