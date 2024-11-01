package com.jonathantippy.RationalRange;
import static com.jonathantippy.RationalRange.Utility.addBits;
import static com.jonathantippy.RationalRange.Utility.branchlessAbs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class RationalBound 
{
    private static final Logger log = LogManager.getLogger(RationalBound.class);

    public static final RationalBound ZERO 
    = new RationalBound(0L, 1L, false);
    public static final RationalBound ONE 
    = new RationalBound(1L, 1L, false);

    public static final RationalBound MAX_VALUE 
    = new RationalBound(Long.MAX_VALUE, 1L, false);
    public static final RationalBound MIN_VALUE 
    = new RationalBound(-Long.MAX_VALUE, 1L, false);

    public static final RationalBound MIN_POSITIVE_VALUE 
    = new RationalBound(1L, Long.MAX_VALUE, false);
    public static final RationalBound MAX_NEGATIVE_VALUE 
    = new RationalBound(-1L, Long.MAX_VALUE, false);

    private long numerator;
    private long denomenator;
    private boolean infinite;

    private long tnum; // mutables for testing answers
    private long tden;

    // Constructors with both numerator and denomenator
    public RationalBound(long numerator, long denomenator, boolean infinite) 
    throws ArithmeticException {

        Utility.validateRationalBound(numerator, denomenator, infinite);
        this.numerator = numerator;
        this.denomenator = denomenator;
        this.infinite = infinite;
    }
    public RationalBound(long numerator, long denomenator) 
    throws ArithmeticException {

        Utility.validateRationalBound(numerator, denomenator, false);
        this.numerator = numerator;
        this.denomenator = denomenator;
        this.infinite = false;
    }

    // Constructors with integers
    public RationalBound(long numerator) {

        Utility.validateRationalBound(numerator, 1L, false);
        this.numerator = numerator; 
        this.denomenator = 1L;
        this.infinite = false;
    }

    // Adaptive constructors
    public RationalBound(String fraction) 
    throws ArithmeticException, IllegalArgumentException {
        if (fraction.matches("^(-?)\\d+(/(-?)\\d+)?")) {
            if (fraction.contains("/")) {
                String[] terms = fraction.split("/");
                tnum = Long.parseLong(terms[0]);
                tden = Long.parseLong(terms[1]);
            } else {
                tnum = Long.parseLong(fraction);
                tden = 1L;
            }
            Utility.validateRationalBound(tnum, tden, false);
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
    public long getNumerator() {
        return this.numerator;
    }
    public long getDenomenator() {
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
        long n = this.getNumerator();
        long d = this.getDenomenator();

        int t = Math.min(
            Long.numberOfTrailingZeros(branchlessAbs(n))
            , Long.numberOfTrailingZeros(branchlessAbs(d))
        );
        return new RationalBound(n>>t, d>>t);
    }


    //crush with bias

    protected RationalBound cut(int bitsToDrop, int roundDirection) {
        return new RationalBound(
            Utility.cut(this.numerator, bitsToDrop, roundDirection)
            , Utility.cut(this.denomenator, bitsToDrop, -roundDirection)
            );
    }

    RationalBound fit(int maxBits, int roundDirection) {
        return new RationalBound(
            Utility.fit(this.numerator, maxBits, roundDirection)
            , Utility.fit(this.denomenator, maxBits, -roundDirection)
            ); 
    }


    protected long handleZero(long a) { 
        if (a!=0) {;} else {
            return 1;
        }
        return a;
    } // medium sized numbers get their denomenators crushed and hit 0 and lose a lot of data



    // UTILS

    public RationalBound bySign(int sign) {
        return new RationalBound(
            Utility.bySignZ(this.numerator, sign)
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
        return (int) Utility.bySign(Long.signum(this.numerator),this.denomenator);
    }

    public boolean isGreaterThanOne() {
        return (
            (branchlessAbs(numerator) > branchlessAbs(denomenator))
            && isPositive()
            );
    }

    public boolean compareToOne(int direction) {
        return (
            (Utility.bySign(branchlessAbs(numerator), direction)
            > Utility.bySign(branchlessAbs(denomenator), direction))
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
        long maybeNumerator = input.numerator;
        long maybeDenomenator = input.denomenator;
        assert maybeDenomenator != 0 : "CHAOS: / by zero";
        if (!(maybeNumerator==Long.MIN_VALUE)) {;} else {
            maybeNumerator = -Long.MAX_VALUE;
        }
        if (!(maybeDenomenator==Long.MIN_VALUE)) {;} else {
            maybeDenomenator = -Long.MAX_VALUE;
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

    public RationalBound multiply(RationalBound multiplier, int roundDirection) {
        int r = roundDirection;
        RationalBound that = multiplier;
        
        RationalBound thi = this.fit(31, r);
        RationalBound tha = that.fit(31, r);
        
        log.debug("Inputs\n------\nthis: " + this + "\nthat: " + that + "\n r: " + r
        + "After Fit\n------\n" + "this: " + thi + "\nthat: " + tha);

        return new RationalBound(
            thi.numerator * tha.numerator
            , thi.denomenator * tha.denomenator
            );
    }

   
}
