package com.godmonth.status.advancer.intf;

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
public class SyncData {
    private Object symbol;

    private Object value;

}
