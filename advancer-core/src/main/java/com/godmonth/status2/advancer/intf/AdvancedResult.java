package com.godmonth.status2.advancer.intf;

import com.godmonth.status2.transitor.tx.intf.TriggerBehavior;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 推进结果.当跃迁参数为空时,查看同步结果
 *
 * @param <MODEL>
 * @author shenyue
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdvancedResult<MODEL, TRIGGER> {
    /**
     * 推进的同步结果
     */
    public SyncData syncData;

    /**
     * 跃迁参数
     */
    private TriggerBehavior<TRIGGER, MODEL> triggerBehavior;
    /**
     * 跃迁完成后动作
     */
    @Builder.Default
    private NextOperation nextOperation = NextOperation.ADVANCE;
    /**
     * 丢弃指令
     */
    private boolean dropInstruction;
  
    /**
     * @param triggerBehavior
     */
    public AdvancedResult(TriggerBehavior<TRIGGER, MODEL> triggerBehavior) {
        this.triggerBehavior = triggerBehavior;
        //必须写一次
        nextOperation = NextOperation.ADVANCE;
    }

    /**
     * @param triggerBehavior
     * @param nextOperation
     */
    @Deprecated
    public AdvancedResult(TriggerBehavior<TRIGGER, MODEL> triggerBehavior, NextOperation nextOperation) {
        this.triggerBehavior = triggerBehavior;
        this.nextOperation = nextOperation;
    }

}
