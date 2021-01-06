package com.godmonth.status.builder.entry;

import com.godmonth.status.annotations.Entry;
import com.godmonth.status.builder.binding.BindingListBuilder;
import com.godmonth.status.transitor.tx.intf.StatusEntry;
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
public class StatusEntryBindingListBuilder {

    @Builder
    private static List<Pair<Object, StatusEntry>> build(@Singular Set<String> packageNames, Class modelClass, Predicate<Class> predicate, AutowireCapableBeanFactory autowireCapableBeanFactory, Function<Class, Object> keyFinder) throws IOException, ClassNotFoundException {
        return BindingListBuilder.<StatusEntry>builder().enableAnnotationClass(Entry.class).packageNames(packageNames).ancestorClass(StatusEntry.class).modelClass(modelClass).predicate(predicate).autowireCapableBeanFactory(autowireCapableBeanFactory).keyFinder(keyFinder).build();
    }
}
