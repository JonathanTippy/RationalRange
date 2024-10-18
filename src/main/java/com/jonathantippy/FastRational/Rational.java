package com.jonathantippy.FastRational;

import java.math.BigInteger;

public class Rational 
{

    public static final Rational ZERO = new Rational(0);
    public static final Rational ONE = new Rational(1);


    private BigInteger numerator;
    private BigInteger denomenator;

    // Constructors with both numerator and denomenator
    public Rational(BigInteger numerator, BigInteger denomenator) throws ArithmeticException {
        this.numerator = numerator; 
        if (!denomenator.equals(BigInteger.ZERO)) {
            this.denomenator = denomenator;
        } else {
            throw new ArithmeticException("/ by zero");
        }
    }
    public Rational(long numerator, long denomenator) throws ArithmeticException {
        this.numerator = BigInteger.valueOf(numerator);
        if (!(denomenator==0)) {
            this.denomenator = BigInteger.valueOf(denomenator);
        } else {
            throw new ArithmeticException("/ by zero");
        }
    }

    // Constructors with integers
    public Rational(BigInteger numerator) {
        this.numerator = numerator; 
        this.denomenator = BigInteger.ONE;
    }
    public Rational(long numerator) {
        this.numerator = BigInteger.valueOf(numerator);
        this.denomenator = BigInteger.ONE;
    }

    // Adaptive constructors
    public Rational(String fraction) throws ArithmeticException, IllegalArgumentException {

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

        
    }

    // Accessors and mutators
    public BigInteger getNumerator() {
        return this.numerator;
    }
    public BigInteger getDenomenator() {
        return this.denomenator;
    }

    // Display
    @Override
    public String toString() {
        int numeratorSign = this.numerator.compareTo(BigInteger.ZERO);
        int denomenatorSign = this.denomenator.compareTo(BigInteger.ZERO);
        int sign = numeratorSign*denomenatorSign;
        if (sign >= 0) {
            return this.numerator.abs().toString() + "/" + this.denomenator.abs().toString();
        } else {
            return "-" + this.numerator.abs().toString() + "/" + this.denomenator.abs().toString();
        }
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
        int extraTwos = Math.min(
            this.numerator.getLowestSetBit()
            , this.denomenator.getLowestSetBit()
            );
        return new Rational(
            this.numerator.shiftRight(extraTwos)
            , this.denomenator.shiftRight(extraTwos)
        );
    }

    // Crushes (approximate simplfication)
    public Rational bitApproxSimplify(int maxBitLength) {
        int unwantedBits = Math.max(
            this.numerator.bitLength() - maxBitLength
            , this.denomenator.bitLength() - maxBitLength
        );
        return new Rational(
            this.numerator.shiftRight(unwantedBits)
            , this.denomenator.shiftRight(unwantedBits)
        );
    }

    // Addition
    public Rational add(Rational addend) {
        return new Rational(
            this.numerator.multiply(addend.denomenator).add(addend.numerator.multiply(this.denomenator))
            ,this.denomenator.multiply(addend.denomenator)
        );
    }
}
