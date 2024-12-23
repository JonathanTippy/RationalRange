package com.jonathantippy.RationalRange;

class RationalRange
{

    private static final Logger log = LogManager.getLogger(RationalRange.class);

    //FIELDS
    private RationalBound upperBound;
    private RationalBound lowerBound;

    // Constructors with both numerator and denomenator
    public RationalRange(int numerator, int denomenator) 
    throws ArithmeticException {

        util.validateRationalBound(numerator, denomenator, false);
        this.upperBound = new RationalBound(numerator, denomenator, false);
        this.lowerBound = new RationalBound(numerator, denomenator, false);
    }

    // Constructors with integers
    public RationalRange(int units) {

        util.validateRationalBound(units, 1, false);
        this.upperBound = new RationalBound(units, 1, false);
        this.lowerBound = new RationalBound(units, 1, false);
    }

    // Adaptive constructors
    public RationalRange(String fraction) 
    throws ArithmeticException, IllegalArgumentException {
        int tnum;
        int tden;
        if (fraction.matches("^(-?)\\d+(/(-?)\\d+)?")) {
            if (fraction.contains("/")) {
                String[] terms = fraction.split("/");
                tnum = Integer.parseInt(terms[0]);
                tden = Integer.parseInt(terms[1]);
            } else {
                tnum = Integer.parseInt(fraction);
                tden = 1;
            }
            util.validateRationalBound(tnum, tden, false);
            this.upperBound = new RationalBound(tnum, tden, false);
            this.lowerBound = new RationalBound(tnum, tden, false);
        } else {
            throw new IllegalArgumentException("Not a fraction");
        }
    }

    // Accessors
    public RationalBound getUpperBound() {
        return this.upperBound;
    }
    public RationalBound getLowerBound() {
        return this.lowerBound;
    }

    // Display
    @Override
    public String toString() {
        StringBuilder numberConstruct = new StringBuilder();
        numberConstruct.append(upperBound.toString());
        numberConstruct.append(" to ");
        numberConstruct.append(lowerBound.toString());
        return numberConstruct.toString();
    }

    // UTILS
    public boolean isExact() {
        return lowerBound.equals(upperBound);
    }

    // Multiply

    public RationalRange multiply(RationalRange that) {
        return new RationalRange(
            this.upperBound.multiply(that.upperBound, 1)
            , this.lowerBound.multiply(that.lowerBound, -1)
        )
    }

    // Divide

    // largest upper bound is big / small
    // and smallest lower bound is small / big

    public RationalRange divide(RationalRange that) {
        return new RationalRange(
            this.upperBound.divide(that.lowerBound, 1)
            , this.lowerBound.divide(that.upperBound, -1)
        )
    }

    // Add

    public RationalRange add(RationalRange that) {
        return new RationalRange(
            this.upperBound.add(that.upperBound, 1)
            , this.lowerBound.add(that.lowerBound, -1)
        )
    }

    // Subtract

    // largest upper bound is big - small
    // and smallest lower bound is small - big
    public RationalRange subtract(RationalRange that) {
        return new RationalRange(
            this.upperBound.subtract(that.lowerBound, 1)
            , this.lowerBound.subtract(that.upperBound, -1)
        )
    }
}