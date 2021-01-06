package com.godmonth.status.builder.advancer;

import com.godmonth.status.advancer.intf.StatusAdvancer;
import com.godmonth.status.advancer.intf.StatusAdvancer2;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * <p></p >
 *
 * @author shenyue
 */
public class OldAdvancerConverter {

    public static List<Pair<Object, StatusAdvancer2>> convert2Binding(List<StatusAdvancer> advancerList) {
        List<Pair<Object, StatusAdvancer2>> advancerBindingList = new ArrayList<>();
        for (StatusAdvancer statusAdvancer : advancerList) {
            advancerBindingList.add(Pair.of(statusAdvancer.getKey(), statusAdvancer));
        }
        return advancerBindingList;
    }

}
