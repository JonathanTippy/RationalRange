package com.jonathantippy.FuzzyFraction;

/*
for now uses longs but eventually want to make hexaLong or octaLong 
(hexaLong would represent integers up to about 10^300)
*/

public class Rational 
{

    public static final Rational ZERO = new Rational(0);
    public static final Rational ONE = new Rational(1);

    private final long numerator;
    private final long denomenator;

    // Constructors with both numerator and denomenator
    public Rational(long numerator, long denomenator) 
    throws ArithmeticException {
        this.numerator = numerator;
        if (!(denomenator==0)) {
            this.denomenator = denomenator;
        } else {
            throw new ArithmeticException("/ by zero");
        }
    }

    // Constructors with integers
    public Rational(long numerator) {
        this.numerator = numerator; 
        this.denomenator = 1;
    }


    // Adaptive constructors
    public Rational(String fraction) 
    throws ArithmeticException, IllegalArgumentException {
        if (fraction.matches("^(-?)\\d+(/(-?)\\d+)?")) {
            if (fraction.contains("/")) {
                String[] terms = fraction.split("/");
                numerator = Long.parseLong(terms[0]);
                if (!(0==(Long.parseLong(terms[1])))) {
                    denomenator = Long.parseLong(terms[1]);
                } else {
                    throw new ArithmeticException("/ by zero");
                }
            } else {
                numerator = Long.parseLong(fraction);
                denomenator = 1;
            }
        } else {
            throw new IllegalArgumentException("Not a fraction");
        }
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

        if (calcSign() >= 0) {;} else {
            numberConstruct.append('-');
        }
        numberConstruct.append(Math.abs(numerator));
        numberConstruct.append('/');
        numberConstruct.append(Math.abs(denomenator));
        return numberConstruct.toString();
    }

    // Division
    public Rational divide(Rational divisor) {
        return new Rational(
            this.numerator * divisor.denomenator
            , this.denomenator * divisor.numerator
            );
    }

    // Multiplication
    public Rational multiply(Rational factor) {
        return new Rational(
            this.numerator * factor.numerator
            , this.denomenator * factor.denomenator
        );
    }

    // Addition
    public Rational add(Rational addend) {
        return new Rational(
            (this.numerator * addend.denomenator)
            + (addend.numerator * this.denomenator)
            , this.denomenator * addend.denomenator
        );
    }

    // Negation
    public Rational negate(Rational input) {
        return new Rational(
            this.numerator * -1
            , this.denomenator
        );
    }

    // Subtraction
    public Rational subtract(Rational minuend) {
        return new Rational(
            (this.numerator * minuend.denomenator)
            - (minuend.numerator * this.denomenator)
            , this.denomenator * minuend.denomenator
        );
    }

    // Absoulte Value
    public Rational abs() {
        return new Rational(
            Math.abs(this.numerator)
            , Math.abs(this.denomenator)
            );
    }


    // UTILS

    // Squeezes (incomplete simplification)
    public Rational twoSimplify() {
        int extraTwos = 
            Math.min(               
            Long.numberOfTrailingZeros(this.numerator)
            , Long.numberOfTrailingZeros(this.denomenator)
            );
        return new Rational(
            this.numerator >> extraTwos
            , this.denomenator >> extraTwos
        );
    }

     // Crushes (approximate simplfication)
     protected Rational bitShiftSimplify(int maxBitLength) {
    
        int unwantedBits = 
            Math.max(
            Math.max(
                (63 - Long.numberOfLeadingZeros(Math.abs(this.numerator))) - (maxBitLength-1)
            , (63 - Long.numberOfLeadingZeros(Math.abs(this.denomenator))) - (maxBitLength-1)
            )
            , 0);

        System.out.println("input: " + this + "\nunwanted bits: " + unwantedBits);
        
        long maybeDenomenator = this.denomenator >> unwantedBits;

        if (! (maybeDenomenator == 0)) {;}
            else {maybeDenomenator = 1;}
        return new Rational(
            numerator >> unwantedBits
            , maybeDenomenator
            );
    }


    private int calcSign() {
        int numeratorSign = Long.signum(this.numerator);
        int denomenatorSign = Long.signum(this.denomenator);
        int sign = numeratorSign*denomenatorSign;
        return sign;
    }


    // FuzzyFractions stuff

    public short[] bitsAfterMultiply(Rational that) {
        return new short[]{
            bitsAfterMultiply(this.numerator, that.numerator)
            , bitsAfterMultiply(this.denomenator, that.denomenator)
            };
    }

    public static short bitsAfterMultiply(long a, long b) {
        return (short) (
            (63 - Long.numberOfLeadingZeros(a))
            + (63 - Long.numberOfLeadingZeros(b))
            );

    }
/*
    public Rational lowMultiply(Rational multiplier) {
        short[] bitsAfterMultiply = this.bitsAfterMultiply(multiplier);
        short numeratorBAM = bitsAfterMultiply[0];
        short denomenatorBAM = bitsAfterMultiply[1];


    }
*/
}
