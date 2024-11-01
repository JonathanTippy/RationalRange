package com.jonathantippy.RationalRange;

public class RationalRange {

    private final long lowerBoundNumerator;
    private final long lowerBoundDenomenator;
    private final long upperBoundNumerator;
    private final long upperBoundDenomenator;

    // Private Constructors (upper & lower bound not completely checked)

    private RationalRange(
        long lowerBoundNumerator
        , long lowerBoundDenomenator
        , long upperBoundNumerator
        , long upperBoundDenomenator
    ) throws ArithmeticException {

        if (!(lowerBoundDenomenator==0 || upperBoundDenomenator==0)) {;} else {
            throw new ArithmeticException("/ by 0");
        }

        // Assuming that the upper bound is greater than the lower bound
        // Simple assert that will catch some but not all cases:
        assert lowerBoundNumerator<=upperBoundNumerator
            && lowerBoundDenomenator>=upperBoundDenomenator :
            "ERROR: Lower bound is greater than upper bound. "
            + "This should never happen. Please report.";

        this.lowerBoundNumerator = lowerBoundNumerator;
        this.lowerBoundDenomenator = lowerBoundDenomenator;
        this.upperBoundNumerator = upperBoundNumerator;
        this.upperBoundDenomenator = upperBoundDenomenator;

    }

    // Constructors with both numerator and denomenator

    public RationalRange(
        long numerator
        , long denomenator
    ) throws ArithmeticException {

        if (!(denomenator == 0)) {;} else {
            throw new ArithmeticException("/ by 0");
        }

        this.lowerBoundNumerator = numerator;
        this.lowerBoundDenomenator = denomenator;
        this.upperBoundNumerator = numerator;
        this.upperBoundDenomenator = denomenator;

    }

    // Constructors with integers

    public RationalRange(
        long integer
    ) {

        this.lowerBoundNumerator = integer;
        this.lowerBoundDenomenator = 1;
        this.upperBoundNumerator = integer;
        this.upperBoundDenomenator = 1;

    }

    // String Constructor

    public RationalRange(
        String expression
    ) {

        RationalBound r = new RationalBound(expression);
        this.lowerBoundNumerator = r.getNumerator();
        this.lowerBoundDenomenator = r.getDenomenator();
        this.upperBoundNumerator = r.getNumerator();
        this.upperBoundDenomenator = r.getDenomenator();

    }

    // Accessors

    public long getLowerBoundNumerator() {
        return lowerBoundNumerator;
    }
    public long getLowerBoundDenomenator() {
        return lowerBoundDenomenator;
    }
    public long getUpperBoundNumerator() {
        return upperBoundNumerator;
    }
    public long getUpperBoundDenomenator() {
        return upperBoundDenomenator;
    }

    // Private accessors

    private RationalBound getLowerBound() {
        return new RationalBound(lowerBoundNumerator, lowerBoundDenomenator);
    }
    private RationalBound getUpperBound() {
        return new RationalBound(upperBoundNumerator, upperBoundDenomenator);
    }

    // Display
    @Override
    public String toString() {
        RationalBound lowerBound = this.getLowerBound();
        RationalBound upperBound = this.getUpperBound();
        StringBuilder numberConstruct = new StringBuilder();

        numberConstruct.append(lowerBound.toString());
        numberConstruct.append(" to ");
        numberConstruct.append(upperBound.toString());
        return numberConstruct.toString();
    }




    // Multiply
/*
    public multiply(FuzzyFraction multiplier) {

        short[][] bitsAfterMultiply = this.bitsAfterMultiply(multiplier);

        short[] lowerBAM = bitsAfterMultiply[0];
        short[] upperBAM = bitsAfterMultiply[1];

        short lowerBAMMax = (short) Math.max(lowerBAM[0], lowerBAM[1]);
        short upperBAMMax = (short) Math.max(upperBAM[0], upperBAM[1]);


    }
*/
    public int[][] bitsAfterMultiply(RationalRange that) {
        RationalBound thisLowerBound = new RationalBound(
            this.lowerBoundNumerator, this.lowerBoundDenomenator);
        RationalBound thisUpperBound = new RationalBound(
            this.upperBoundNumerator, this.upperBoundDenomenator);

        return new int[][]{
            thisLowerBound.bitsAfterMultiply(
                new RationalBound(that.lowerBoundNumerator, 
                that.lowerBoundDenomenator)
            )
            , thisUpperBound.bitsAfterMultiply(
                new RationalBound(that.upperBoundNumerator, 
                that.upperBoundDenomenator)
            )
        };
    }
}
