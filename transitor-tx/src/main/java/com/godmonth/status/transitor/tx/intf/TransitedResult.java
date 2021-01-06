package com.godmonth.status.transitor.tx.intf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p></p >
 *
 * @author shenyue
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TransitedResult<MODEL, ACCESSORY> {
    private MODEL model;
    private ACCESSORY accessory;

    public TransitedResult(MODEL model) {
        this.model = model;
    }
}
