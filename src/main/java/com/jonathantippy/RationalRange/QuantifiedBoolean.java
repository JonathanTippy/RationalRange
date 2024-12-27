public class QuantifiedBoolean {

    private byte value;

    public QuantifiedBoolean(byte value) {
        this.value = value;
    }
    public static final byte getValue(QuantifiedBoolean input) {
        return input.value;
    }
    public static final QuantifiedBoolean and(QuantifiedBoolean input1, QuantifiedBoolean input2) {
        return new QuantifiedBoolean((byte)(((input1.value & 0xFF) * (input2.value & 0xFF))/255));
    }
    public static final QuantifiedBoolean not(QuantifiedBoolean input) {
        return new QuantifiedBoolean((byte) (255 - (input.value & 0xFF)));
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