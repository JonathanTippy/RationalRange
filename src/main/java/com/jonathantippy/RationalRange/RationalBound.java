package com.jonathantippy.RationalRange;
import static com.jonathantippy.RationalRange.util.branchlessAbs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class RationalBound 
{
    private static final Logger log = LogManager.getLogger(RationalBound.class);

    // CONSTS
    public static final RationalBound ZERO 
    = new RationalBound(0, 1, false);
    public static final RationalBound ONE 
    = new RationalBound(1, 1, false);

    public static final RationalBound MAX_VALUE 
    = new RationalBound(Integer.MAX_VALUE, 1, false);
    public static final RationalBound MIN_VALUE 
    = new RationalBound(-Integer.MAX_VALUE, 1, false);

    public static final RationalBound MIN_POSITIVE_VALUE 
    = new RationalBound(1, Integer.MAX_VALUE, false);
    public static final RationalBound MAX_NEGATIVE_VALUE 
    = new RationalBound(-1, Integer.MAX_VALUE, false);

    // FIELDS
    private int numerator;
    private int denomenator;
    private boolean excluded;

    // Constructors with both numerator and denomenator
    public RationalBound(
        int numerator
        , int denomenator
        , boolean excluded
        ) throws ArithmeticException {

        this.numerator = numerator;
        this.denomenator = denomenator;
        this.excluded = excluded;
        validate(this);
    }
    public RationalBound(int numerator, int denomenator) 
    throws ArithmeticException {

        this.numerator = numerator;
        this.denomenator = denomenator;
        this.excluded = false;
        validate(this);
    }

    // Constructors with integers
    public RationalBound(int units) {

        this.numerator = units; 
        this.denomenator = 1;
        this.excluded = false;
        validate(this);
    }

    // Adaptive constructors
    public RationalBound(String fraction) 
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
            this.numerator = tnum;
            this.denomenator = tden;
            this.excluded = false;
        } else {
            throw new IllegalArgumentException("Not a fraction");
        }
        validate(this);
    }

    // Simplification

    private RationalBound(
        long fatNum
        , long fatDen
        , int r
        , boolean excluded
        ) {

        int maxBitLength = Math.max(
            util.bitLength(fatNum)
            , util.bitLength(fatDen)
        );

        int bitsToDrop = util.branchlessDOZ(maxBitLength, 31);

        int newNumerator = (int) util.cut(fatNum, bitsToDrop, r);
        int newDenomenator = (int) util.cut(fatDen, bitsToDrop, -r);

        boolean squashed = (fatDen!=0&&newDenomenator==0);

        this.numerator = newNumerator;
        this.denomenator = newDenomenator;
        this.excluded = (excluded || squashed);
        validate(this);
    }
    // Validate

    public static final void validate(RationalBound input) throws ArithmeticException {

        if (input.numerator!=Integer.MIN_VALUE&&input.denomenator!=Integer.MIN_VALUE) {;} else {
            throw new ArithmeticException("int min value detected");
        }
        if (input.denomenator!=0||input.excluded) {;} else {
            throw new ArithmeticException("/ by zero");
        }
    }

    // Conversions

    public static final double toDouble(RationalBound input) {
        return (double) (((double) input.numerator) / ((double) input.denomenator));
    }


    // Accessors
    public int getNumerator() {
        return this.numerator;
    }
    public int getDenomenator() {
        return this.denomenator;
    }

    // Display
    @Override
    public String toString() {

        StringBuilder numberConstruct = new StringBuilder();
        if (!excluded) {;} else {
            numberConstruct.append("excluded ");
        }
        numberConstruct.append(numerator);
        numberConstruct.append('/');
        numberConstruct.append(denomenator);
        return numberConstruct.toString();
    }

    // Multiplication
    public static final RationalBound multiply(RationalBound fac1, RationalBound fac2, int roundDirection) {
        
        long fatNumerator = (long) fac1.numerator * (long) fac2.numerator;
        long fatDenomenator = (long) fac1.denomenator * (long) fac2.denomenator;
        
        boolean isExcluded = (fac1.excluded || fac2.excluded);

        return new RationalBound(
            fatNumerator
            , fatDenomenator
            , roundDirection
            , isExcluded
        );
    }

    // Division
    public static final RationalBound divide(RationalBound div1, RationalBound div2, int roundDirection) {
        return multiply(div1, reciprocate(div2), roundDirection);
    }

    // Addition
    public static final RationalBound add(RationalBound add1, RationalBound add2, int roundDirection) {

        long fatNumerator = 
            ((long) add1.numerator * (long) add2.denomenator)
            + ((long) add2.numerator * (long) add1.denomenator);
        long fatDenomenator = 
            (long) add1.denomenator * (long) add2.denomenator;

        boolean isExcluded = (add1.excluded || add2.excluded);

        return new RationalBound(
            fatNumerator
            , fatDenomenator
            , roundDirection
            , isExcluded
        );
    }

    // Negation
    public static final RationalBound negate(RationalBound input) {
        return new RationalBound(
            - input.numerator
            , input.denomenator
            , input.excluded
        );
    }

    // Reciprocal
    public static final RationalBound reciprocate(RationalBound input) {
        return new RationalBound(
            input.denomenator
            , input.numerator
            , input.excluded
        );
    }

    // Subtraction
    public static final RationalBound subtract(RationalBound sub1, RationalBound sub2, int roundDirection) {
        return add(sub1, negate(sub2), roundDirection);
    }

    // Absoulte Value
    public static final RationalBound abs(RationalBound input) {
        return new RationalBound(
            branchlessAbs(input.numerator)
            , branchlessAbs(input.denomenator)
            );
    }

    // UTILS

    public static final RationalBound bySign(RationalBound input, int sign) {
        return new RationalBound(
            util.bySignZ(input.numerator, sign)
            , input.denomenator
            , input.excluded
        );
    }

    public static final boolean maybeDiffer(RationalBound comp1, RationalBound comp2) {
        return (
            branchlessAbs(comp1.numerator) != branchlessAbs(comp2.numerator)
            || branchlessAbs(comp1.denomenator) != branchlessAbs(comp2.denomenator)
            )&&(isPositive(comp1) == isPositive(comp2));
    }

    public static final int signum(RationalBound input) {
        return (int) util.bySign(Long.signum(input.numerator),input.denomenator);
    }

    public static final boolean isGreaterThanOne(RationalBound input) {
        return (
            (branchlessAbs(input.numerator) > branchlessAbs(input.denomenator))
            && isPositive(input)
            );
    }

    public static final boolean compareToOne(RationalBound input, int direction) {
        return (
            (util.bySign(branchlessAbs(input.numerator), direction)
            > util.bySign(branchlessAbs(input.denomenator), direction))
            && isPositive(input)
            );
    }

    public static final boolean isPositive(RationalBound input) {
        return (signum(input) > 0);
    }

    public static final boolean isZero(RationalBound input) {
        return ((input.numerator == 0) && !input.excluded);
    }

    public static final boolean isOne(RationalBound input) {
        return (input.numerator == input.denomenator);
    }
}
