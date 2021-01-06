package com.godmonth.status2.advancer.intf;

import com.godmonth.status2.transitor.tx.intf.TriggerBehavior;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * <p></p >
 *
 * @author shenyue
 */
class AdvancedResultTest {
    @Test
    void name() {
        AdvancedResult advancedResult = new AdvancedResult();
        Assertions.assertEquals(NextOperation.ADVANCE, advancedResult.getNextOperation());
    }

    @Test
    void name2() {
        AdvancedResult advancedResult = new AdvancedResult(new TriggerBehavior());
        Assertions.assertEquals(NextOperation.ADVANCE, advancedResult.getNextOperation());
    }

}