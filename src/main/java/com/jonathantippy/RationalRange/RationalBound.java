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

        // For a while min value was disallowed
        // In practice, min value means there was a. an honest min value answer
        // or b. an overflow caused by the asymmetry of ints introduced by negatives
        // In both cases the real value is "larger than largest known" if in numerator
        // or "smaller than smallest known" if in denomenator"
        // The directionality is known by what bound it is; zero on the opposite side suffices.
        // The zero destroys the normal sign.

        if (newNumerator!=Integer.MIN_VALUE&&newDenomenator!=Integer.MIN_VALUE) {;} else {
            if (!(newNumerator==Integer.MIN_VALUE&&newDenomenator==Integer.MIN_VALUE)) {;} else {
                newNumerator = 0;
                newDenomenator = 0;
            }
            if (newNumerator==Integer.MIN_VALUE) {
                newDenomenator = 0; 
            } else {
                newNumerator = 0; 
            }
        }

        // if the answer was rounded, the exact value can no longer be considered a possible answer.

        boolean split = (((long)newNumerator)!=(fatNum)||((long)newDenomenator)!=(fatDen));

        this.numerator = newNumerator;
        this.denomenator = newDenomenator;
        this.excluded = (excluded || split);
        //validate is not needed as the overflow handling is already done
    }
    // Validate

    public static final void validate(RationalBound input) {

        // In practice, min value means there was an overflow.
        // directionality is known higher up; this suffices. zero destroys any sign of numerator.

        if (input.numerator!=Integer.MIN_VALUE&&input.denomenator!=Integer.MIN_VALUE) {;} else {
            input.denomenator = 0; 
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

    // Comparison
    public static final boolean greaterThan(RationalBound a, RationalBound b) {
        
        // The common denomenator is left implied
        // the comparison can be made with it;
        // as the implied common denomenator is a scalar, the original sign
        // is multiplied into the numerator so it is comparatively representative
        // in magnitude and direction.
        
        long acdn = 
            (long) a.numerator * (long) b.denomenator 
            * Integer.signum(a.denomenator);

        long bcdn = 
            (long) b.numerator * (long) a.denomenator 
            * Integer.signum(b.denomenator);

        return (acdn > bcdn);
    }

    // UTILS

    public static final RationalBound bySign(RationalBound input, int sign) {
        return new RationalBound(
            util.bySignZ(input.numerator, sign)
            , input.denomenator
            , input.excluded
        );
    }

    public static final boolean mayDiffer(RationalBound comp1, RationalBound comp2) {
        return (
            comp1.numerator != comp2.numerator
            || comp1.denomenator != comp2.denomenator
        );
    }

    public static final int signum(RationalBound input, int roundDirection) {

        // handle cases where simple sign is gone
        // round direction is taken to imply bound direction and thus inclusion / exclusion direction

        if (input.numerator != 0 && input.denomenator != 0) {;} else {
            if (!(input.numerator==0&&input.denomenator==0)) {;} else {
                return 0; //unknowne
            }
            if (input.numerator==0) {
                if (input.excluded) {
                    return -roundDirection;
                } else {
                    return roundDirection;
                }
            } else {
                if (input.excluded) {
                    return roundDirection;
                } else {
                    return -roundDirection;
                }
            }
        }
        return (int) util.bySign(Long.signum(input.numerator),input.denomenator);
    }

    public static final boolean compareToOne(RationalBound input, int direction) {
        return (
            (util.bySign(branchlessAbs(input.numerator), direction)
            > util.bySign(branchlessAbs(input.denomenator), direction))
            && isPositive(input)
            );
    }

    public static final boolean isPositive(RationalBound input, int roundDirection) {
        return (signum(input, roundDirection) > 0);
    }

    public static final boolean isZero(RationalBound input) {
        return ((input.numerator == 0) && !input.excluded);
    }

    public static final boolean isOne(RationalBound input) {
        return (input.numerator == input.denomenator);
    }
}
