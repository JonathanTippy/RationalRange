package com.jonathantippy.RationalRange;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jonathantippy.RationalRange.RationalBound;

public class RationalRange
{

    private static final Logger log = LogManager.getLogger(RationalRange.class);

    //FIELDS
    private RationalBound upperBound;
    private RationalBound lowerBound;

    // Constructor with both bounds (unsafe)
    private RationalRange(RationalBound upperBound, RationalBound lowerBound) {
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
    }

    // Constructors with both numerator and denomenator
    public RationalRange(int numerator, int denomenator) 
    throws ArithmeticException {

        RationalBound r = new RationalBound(numerator, denomenator);

        this.upperBound = r;
        this.lowerBound = r;
    }

    // Constructors with integers
    public RationalRange(int units) {

        RationalBound r = new RationalBound(units, 1);

        this.upperBound = r; 
        this.lowerBound = r;
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
            RationalBound r = new RationalBound(tnum, tden);
            this.upperBound = r;
            this.lowerBound = r;
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
        );
    }

    // Divide

    // largest upper bound is big / small
    // and smallest lower bound is small / big

    public RationalRange divide(RationalRange that) {
        return new RationalRange(
            this.upperBound.divide(that.lowerBound, 1)
            , this.lowerBound.divide(that.upperBound, -1)
        );
    }

    // Add

    public RationalRange add(RationalRange that) {
        return new RationalRange(
            this.upperBound.add(that.upperBound, 1)
            , this.lowerBound.add(that.lowerBound, -1)
        );
    }

    // Subtract

    // largest upper bound is big - small
    // and smallest lower bound is small - big
    public RationalRange subtract(RationalRange that) {
        return new RationalRange(
            this.upperBound.subtract(that.lowerBound, 1)
            , this.lowerBound.subtract(that.upperBound, -1)
        );
    }
}