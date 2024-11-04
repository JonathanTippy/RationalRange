package com.jonathantippy.RationalRange;
import static com.jonathantippy.RationalRange.util.addBits;
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
    private boolean infinite;

    private int tnum; // mutables for testing answers
    private int tden;

    // Constructors with both numerator and denomenator
    public RationalBound(int numerator, int denomenator, boolean infinite) 
    throws ArithmeticException {

        util.validateRationalBound(numerator, denomenator, infinite);
        this.numerator = numerator;
        this.denomenator = denomenator;
        this.infinite = infinite;
    }
    public RationalBound(int numerator, int denomenator) 
    throws ArithmeticException {

        util.validateRationalBound(numerator, denomenator, false);
        this.numerator = numerator;
        this.denomenator = denomenator;
        this.infinite = false;
    }

    // Constructors with integers
    public RationalBound(int numerator) {

        util.validateRationalBound(numerator, 1, false);
        this.numerator = numerator; 
        this.denomenator = 1;
        this.infinite = false;
    }

    // Adaptive constructors
    public RationalBound(String fraction) 
    throws ArithmeticException, IllegalArgumentException {
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
            this.numerator = tnum;
            this.denomenator = tden;
            this.infinite = false;
        } else {
            throw new IllegalArgumentException("Not a fraction");
        }
    }

    // Conversions

    public double toDouble() {
        return (double) (((double) numerator) / ((double) denomenator));
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
        numberConstruct.append(numerator);
        numberConstruct.append('/');
        numberConstruct.append(denomenator);
        return numberConstruct.toString();
    }

    // Multiplication
    public RationalBound multiply(RationalBound multiplier, int roundDirection) {
        int r = roundDirection;
        RationalBound that = multiplier;
        
        int bitsCeiling = util.getBitsCeiling(this.numerator, this.denomenator, that.numerator, that.denomenator);

        RationalBound thi = this.fit(bitsCeiling, r);
        RationalBound tha = that.fit(bitsCeiling, r);
        
        log.debug("Inputs\n------\nthis: " + this + "\nthat: " + that + "\n r: " + r
        + "After Fit\n------\n" + "this: " + thi + "\nthat: " + tha);

        result = new RationalBound(
            thi.numerator * tha.numerator
            , thi.denomenator * tha.denomenator
            , (thi.infinite || tha.infinite)
            );
    }

    // Division
    public RationalBound divide(RationalBound divisor) {
        return new RationalBound(
            this.numerator * divisor.denomenator
            , this.denomenator * divisor.numerator
            );
    }

    // Addition
    public RationalBound add(RationalBound addend) {
        return new RationalBound(
            (this.numerator * addend.denomenator)
            + (addend.numerator * this.denomenator)
            , this.denomenator * addend.denomenator
        );
    }

    // Negation
    public RationalBound negate(RationalBound input) {
        return new RationalBound(
            - this.numerator
            , this.denomenator
        );
    }

    // Subtraction
    public RationalBound subtract(RationalBound minuend) {
        return new RationalBound(
            (this.numerator * minuend.denomenator)
            - (minuend.numerator * this.denomenator)
            , this.denomenator * minuend.denomenator
        );
    }

    // Absoulte Value
    public RationalBound abs() {
        return new RationalBound(
            branchlessAbs(this.numerator)
            , branchlessAbs(this.denomenator)
            );
    }



     // Crushes (approximate simplfication)


    protected RationalBound twoSimplify() {
        int n = this.getNumerator();
        int d = this.getDenomenator();

        int t = Math.min(
            Long.numberOfTrailingZeros(branchlessAbs(n))
            , Long.numberOfTrailingZeros(branchlessAbs(d))
        );
        return new RationalBound(n>>t, d>>t);
    }

    // crush with bias

    protected RationalBound cut(int bitsToDrop, int roundDirection) {
        return new RationalBound(
            util.cut(this.numerator, bitsToDrop, roundDirection)
            , util.cut(this.denomenator, bitsToDrop, -roundDirection)
            );
    }
/*
    RationalBound fit(int maxBits, int roundDirection) {
        return new RationalBound(
            util.fit(this.numerator, maxBits, roundDirection)
            , util.fit(this.denomenator, maxBits, -roundDirection)
            ); 
    }
*/

    RationalBound fit(int maxBits, int roundDirection) {

        // bits removed must be balanced or the franctoion will fall over
        tnum = this.numerator;
        tden = this.denomenator;

        int largerBits = (int) util.branchlessMax(util.bitLength(tnum), util.bitLength(tden));
        int bitsToRemove = (int) util.branchlessDOZ(largerBits, maxBits);

        tnum = util.cut(this.numerator, bitsToRemove, roundDirection);
        tden = util.cut(this.denomenator, bitsToRemove, -roundDirection);

        boolean inf = (tden == 0);

        return new RationalBound(
            tnum
            , tden
            , inf
            );
    }

    protected int handleZero(int a) { 
        if (a!=0) {;} else {
            return 1;
        }
        return a;
    } // medium sized numbers get their denomenators crushed and hit 0 and lose a lot of data



    // UTILS

    public RationalBound bySign(int sign) {
        return new RationalBound(
            util.bySignZ(this.numerator, sign)
            , this.denomenator
        );
    }

    public boolean maybeDiffer(RationalBound that) {
        return (
            branchlessAbs(this.numerator) != branchlessAbs(that.numerator)
            || branchlessAbs(this.denomenator) != branchlessAbs(that.denomenator)
            )&&(this.isPositive() == that.isPositive());
    }

    public int signum() {
        return (int) util.bySign(Long.signum(this.numerator),this.denomenator);
    }

    public boolean isGreaterThanOne() {
        return (
            (branchlessAbs(numerator) > branchlessAbs(denomenator))
            && isPositive()
            );
    }

    public boolean compareToOne(int direction) {
        return (
            (util.bySign(branchlessAbs(numerator), direction)
            > util.bySign(branchlessAbs(denomenator), direction))
            && isPositive()
            );
    }

    public boolean isPositive() {
        return (signum() > 0);
    }

    public boolean isZero() {
        return (numerator == 0);
    }

    public boolean isInfinity() {
        return (denomenator == 0);
    }

    public boolean isOne() {
        return (numerator == denomenator);
    }


    protected static RationalBound handleMinValue(RationalBound input) {
        int maybeNumerator = input.numerator;
        int maybeDenomenator = input.denomenator;
        assert maybeDenomenator != 0 : "CHAOS: / by zero";
        if (!(maybeNumerator==Integer.MIN_VALUE)) {;} else {
            maybeNumerator = -Integer.MAX_VALUE;
        }
        if (!(maybeDenomenator==Integer.MIN_VALUE)) {;} else {
            maybeDenomenator = -Integer.MAX_VALUE;
        }
        return new RationalBound(maybeNumerator, maybeDenomenator);
    } // if min value is found it will result in an error huge

    // FuzzyFractions stuff

    public int[] bitsAfterMultiply(RationalBound that) {
        return new int[]{
            addBits(this.numerator, that.numerator)
            , addBits(this.denomenator, that.denomenator)
            };
    }
}
