package com.godmonth.status2.advancer.intf;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p></p >
 *
 * @author shenyue
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NextInstruction {

    /**
     * 丢弃指令.如果有下一个指令，那么默认drop掉当前指令
     */
    private boolean drop;

    /**
     * 下个指令
     */
    private Object instruction;

    /**
     * 下一个指令消.仅在有下一个指令时有效
     */
    private Object message;
}
