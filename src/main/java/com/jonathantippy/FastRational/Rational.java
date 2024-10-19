package com.jonathantippy.FastRational;

import java.math.BigInteger;
import static java.lang.Math.max;
import static java.lang.Math.min;


/*
for now uses longs but eventually want to make hexaLong or octaLong 
(hexaLong would represent integers up to about 10^300)
*/

public class Rational 
{

    public static final Rational ZERO = new Rational(0);
    public static final Rational ONE = new Rational(1);

    private final Rational plusOrMinus;
    private final BigInteger numerator;
    private final BigInteger denomenator;
    private final int sign;

    // Constructors with both numerator and denomenator
    public Rational(BigInteger numerator, BigInteger denomenator) 
    throws ArithmeticException {
        this.numerator = numerator;
        if (!denomenator.equals(BigInteger.ZERO)) {
            this.denomenator = denomenator;
        } else {
            throw new ArithmeticException("/ by zero");
        }
        this.sign = this.calcSign();
        this.plusOrMinus = Rational.ZERO;
    }
    public Rational(long numerator, long denomenator) 
    throws ArithmeticException {
        this.numerator = BigInteger.valueOf(numerator);
        if (!(denomenator==0)) {
            this.denomenator = BigInteger.valueOf(denomenator);
        } else {
            throw new ArithmeticException("/ by zero");
        }
        this.sign = this.calcSign();
        this.plusOrMinus = Rational.ZERO;
    }

    // Constructors with integers
    public Rational(BigInteger numerator) {
        this.numerator = numerator; 
        this.denomenator = BigInteger.ONE;
        this.sign = this.calcSign();
        this.plusOrMinus = Rational.ZERO;
    }
    public Rational(long numerator) {
        this.numerator = BigInteger.valueOf(numerator);
        this.denomenator = BigInteger.ONE;
        this.sign = this.calcSign();
        this.plusOrMinus = Rational.ZERO;
    }

    // Adaptive constructors
    public Rational(String fraction) 
    throws ArithmeticException, IllegalArgumentException {
        if (fraction.matches("^(-?)\\d+(/(-?)\\d+)?")) {
            if (fraction.contains("/")) {
                String[] terms = fraction.split("/");
                numerator = new BigInteger(terms[0]);
                if (!BigInteger.ZERO.equals(new BigInteger(terms[1]))) {
                    denomenator = new BigInteger(terms[1]);
                } else {
                    throw new ArithmeticException("/ by zero");
                }
            } else {
                numerator = new BigInteger(fraction);
                denomenator = BigInteger.ONE;
            }
        } else {
            throw new IllegalArgumentException("Not a fraction");
        }
        this.sign = this.calcSign();
        this.plusOrMinus = Rational.ZERO;
    }

    // Constructors with plusOrMinus

    public Rational(BigInteger numerator, BigInteger denomenator, Rational plusOrMinus) 
    throws ArithmeticException {
        this.numerator = numerator; 
        if (!denomenator.equals(BigInteger.ZERO)) {
            this.denomenator = denomenator;
        } else {
            throw new ArithmeticException("/ by zero");
        }
        this.sign = this.calcSign();
        this.plusOrMinus = plusOrMinus;
        assert(plusOrMinus.plusOrMinus.equals(Rational.ZERO));
    }

    // Accessors
    public BigInteger getNumerator() {
        return this.numerator;
    }
    public BigInteger getDenomenator() {
        return this.denomenator;
    }
    public int getSign() {
        return this.sign;
    }
    public Rational getPlusOrMinus() {
        return this.plusOrMinus;
    }

    // Display
    @Override
    public String toString() {
        StringBuilder numberConstruct = new StringBuilder();

        if (sign >= 0) {;} else {
            numberConstruct.append('-');
        }
        numberConstruct.append(numerator.toString());
        numberConstruct.append('/');
        numberConstruct.append(denomenator.toString());
        if (plusOrMinus.equals(Rational.ZERO)) {;} else {
            numberConstruct.append('±');
            numberConstruct.append(plusOrMinus.toString());
        }
        return numberConstruct.toString();
    }

    // Division
    public Rational divide(Rational divisor) {
        return new Rational(
            this.numerator.multiply(divisor.denomenator)
            , this.denomenator.multiply(divisor.numerator)
            );
    }

    // Multiplication
    public Rational multiply(Rational factor) {
        return new Rational(
            this.numerator.multiply(factor.numerator)
            , this.denomenator.multiply(factor.denomenator)
        );
    }

    // Simplification
    public Rational slowSimplify() {
        BigInteger GCD = this.numerator.gcd(this.denomenator);
        return new Rational(
            this.numerator.divide(GCD)
            , this.denomenator.divide(GCD)
        );
    }

    // Squeezes (incomplete simplification)
    public Rational twoSimplify() {
        int extraTwos = max(    //this max is done because in the case of 0,
            min(                //BigInteger.getLowestSetBit returns -1
            this.numerator.getLowestSetBit()
            , this.denomenator.getLowestSetBit()
            )
            , 0);
        return new Rational(
            this.numerator.shiftRight(extraTwos)
            , this.denomenator.shiftRight(extraTwos)
        );
    }

    // Crushes (approximate simplfication)
    public Rational bitShiftSimplify(int maxBitLength) {
    
        BigInteger absNumerator = this.numerator.abs();
        BigInteger absDenomenator = this.denomenator.abs();

        int unwantedBits = max(
            max(
            absNumerator.bitLength() - maxBitLength
            , absDenomenator.bitLength() - maxBitLength
            )
            , 0);
        
        BigInteger maybeDenomenator = absDenomenator.shiftRight(unwantedBits);
        BigInteger returnedNumerator;
        if (sign >=0) {returnedNumerator = absNumerator.shiftRight(unwantedBits);}
        else {returnedNumerator = absNumerator.shiftRight(unwantedBits).negate();}
        if (!maybeDenomenator.equals(BigInteger.ZERO)) {
            return new Rational(
                returnedNumerator
                , maybeDenomenator
                );
        } else {
            return new Rational(
                returnedNumerator
                , BigInteger.ONE
                );
        }
    }

    // Addition
    public Rational add(Rational addend) {
        return new Rational(
            this.numerator.multiply(addend.denomenator)
            .add(addend.numerator.multiply(this.denomenator))
            ,this.denomenator.multiply(addend.denomenator)
        );
    }

    // Absoulte Value
    public Rational abs() {
        return new Rational(
            this.numerator.abs()
            , this.denomenator.abs()
            );
    }


    // UTILS

    private int calcSign() {
        int numeratorSign = this.numerator.compareTo(BigInteger.ZERO);
        int denomenatorSign = this.denomenator.compareTo(BigInteger.ZERO);
        int sign = numeratorSign*denomenatorSign;
        return sign;
    }
}
