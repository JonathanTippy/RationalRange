package com.jonathantippy.RationalRange;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QuantifiedBoolean {

    private static final Logger log = LogManager.getLogger(RationalRange.class);

    //casting from int to byte does what we want (give last 8 bits)
    // from byte to int we have to convince it not to grab the 2's compliment bit
    // so we and it with all ones

    // a little weird, no exact mid value and no shifting because 255 instead of 256

    public static final QuantifiedBoolean tossUp = new QuantifiedBoolean((byte) 127);
    public static final QuantifiedBoolean heads = new QuantifiedBoolean((byte) 0);
    public static final QuantifiedBoolean tails = new QuantifiedBoolean((byte) -1);


    private byte value;

    public QuantifiedBoolean(byte value) {
        this.value = value;
    }

    public QuantifiedBoolean(boolean value) {
        if (value) {
            this.value = (byte) 255;
        } else {
            this.value = (byte) 0;
        }
    }
/*
    public static final byte getValue(QuantifiedBoolean input) {
        return input.value;
    }
*/
    public static final int getIntValue(QuantifiedBoolean input) {
        return input.value & 0xFF;
    }

    public static final QuantifiedBoolean and(QuantifiedBoolean input1, QuantifiedBoolean input2) {
        return new QuantifiedBoolean(
            (byte) (
              (
                (getIntValue(input1)) 
              * (getIntValue(input2))
              ) / 255
            )
            );
    }
    public static final QuantifiedBoolean not(QuantifiedBoolean input) {
        return new QuantifiedBoolean(
            (byte) (255 - (getIntValue(input)))
            );
    }
    public static final QuantifiedBoolean or(QuantifiedBoolean input1, QuantifiedBoolean input2) {
        return not(nor(input1, input2));
    }
    public static final QuantifiedBoolean nor(QuantifiedBoolean input1, QuantifiedBoolean input2) {
        return and(not(input1),(not(input2)));
    }
    public static final QuantifiedBoolean nand(QuantifiedBoolean input1, QuantifiedBoolean input2) {
        return not(and(input1, input2));
    }
    public static final QuantifiedBoolean xor(QuantifiedBoolean input1, QuantifiedBoolean input2) {
        return and(or(input1, input2), not(and(input1, input2)));
    }
}