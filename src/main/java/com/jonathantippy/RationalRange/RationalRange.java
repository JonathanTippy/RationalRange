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
    private RationalRange(
        RationalBound upperBound
        , RationalBound lowerBound
        ) {
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

    // Multiply

    public static final RationalRange multiply(RationalRange mul1, RationalRange mul2) {
        return new RationalRange(
            RationalBound.multiply(mul1.upperBound, mul2.upperBound, 1)
            , RationalBound.multiply(mul1.lowerBound, mul2.lowerBound, -1)
        );
    }

    // Divide

    public static final RationalRange divide(RationalRange div1, RationalRange div2) {
        return multiply(div1, reciprocate(div2));
    }

    // Add

    public static final RationalRange add(RationalRange add1, RationalRange add2) {
        return new RationalRange(
            RationalBound.add(add1.upperBound, add2.upperBound, 1)
            , RationalBound.add(add1.lowerBound, add2.lowerBound, -1)
        );
    }

    // Subtract
    public static final RationalRange subtract(RationalRange sub1, RationalRange sub2) {
        return add(sub1, negate(sub2));
    }

    // REVERSE FUNCTIONS
    // largest upper bound is big / small
    // and smallest lower bound is small / big

    public static final RationalRange negate(RationalRange input) {
        return new RationalRange(
            RationalBound.negate(input.lowerBound)
            , RationalBound.negate(input.upperBound)
        );
    }

    public static final RationalRange reciprocate(RationalRange input) throws ArithmeticException {
        if (signum(input.upperBound) == signum(input.lowerBound)) {;} else {
            throw new ArithemticException("Possible / by zero"); 
            // this case may be possible but complicates everything
            // by allowing bound to be turned inside out
        }
        return new RationalRange(
            RationalBound.reciprocate(input.lowerBound) // note the upper bound and lower bound trade places
            , RationalBound.reciprocate(input.upperBound)
        );
    }

    // Comparisons

    public QuantifiedBoolean isPositive(RationalRange input) {
        // first do a subtraction (its ok, wont lose any precision)
        //(ok maybe some but remember that rounding can never ruin sign)
        // can get odds by dividing positive share by entire width
        // 
        RationalBound u = input.upperBound;
        RationalBound l = input.lowerBound;
        RationalBound width = subtract(u, l);
        RationalBound prob = divide(u, width);
        if (prob)
        
    }

    public QuantifiedBoolean greaterThan(RationalRange com1, RationalRange com2) {
        RationalRange diff = subtract(com1, com2);
        return isPositive(diff);
    }
}