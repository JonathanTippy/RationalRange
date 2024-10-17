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
        if (!this.denomenator.equals(BigInteger.ZERO)) {
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

        if (fraction.matches("^(-?)\\d+(/?(-?)\\d+)")) {
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
        return Rational.ZERO;
    }
}
