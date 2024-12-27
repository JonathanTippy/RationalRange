public class QuantifiedBoolean {
    private byte value;
    public QuantifiedBoolean(byte value) {
        this.value = value;
    }
    public byte getValue() {
        return value;
    }
    public QuantifiedBoolean and(QuantifiedBoolean that) {
        return new QuantifiedBoolean(((this.value & 0xFF) * (that.value & 0xFF))/255);
    }
    public final QuantifiedBoolean not() {
        return 255 - (this.value & 0xFF);
    }
    public QuantifiedBoolean or(QuantifiedBoolean that) {
        return not(this.nor(that));
    }
    public QuantifiedBoolean nor(QuantifiedBoolean that) {
        return not(this).and(not(that));
    }
    public QuantifiedBoolean nand(QuantifiedBoolean that) {
        return
    }
    
}