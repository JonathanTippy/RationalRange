package com.jonathantippy.RationalRange;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jonathantippy.RationalRange.RationalBound;
import com.jonathantippy.RationalRange.QuantifiedBoolean;


public class RationalRange
{

    private static final Logger log = LogManager.getLogger(RationalRange.class);

    //FIELDS
    private RationalBound upperBound;
    private RationalBound lowerBound;
    private boolean reciprocated;

    // Constructor with both bounds (unsafe)
    private RationalRange(
        RationalBound upperBound
        , RationalBound lowerBound
        , boolean reciprocated
        ) {
        
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
        this.reciprocated = reciprocated;

        // if inverted value imploded, set to indeterminate value

        if ( !(reciprocated && RationalBound.greaterThan(upperBound, lowerBound))) {;} else {
            this.upperBound = new RationalBound(0, 0);
            this.lowerBound = new RationalBound(0, 0);
        }
        
    }

    // Constructors with both numerator and denomenator
    public RationalRange(int numerator, int denomenator) 
    throws ArithmeticException {

        RationalBound r = new RationalBound(numerator, denomenator);

        this.upperBound = r;
        this.lowerBound = r;
        this.reciprocated = false;
    }

    // Constructors with integers
    public RationalRange(int units) {

        RationalBound r = new RationalBound(units, 1);

        this.upperBound = r; 
        this.lowerBound = r;
        this.reciprocated = false;
    }

    // Adaptive constructors
    public RationalRange(String input) 
    throws ArithmeticException, IllegalArgumentException {
        int tnum;
        int tden;
        if (input.matches("^(-?)\\d+(/(-?)\\d+)?")) {
            if (input.contains("/")) {
                String[] terms = input.split("/");
                tnum = Integer.parseInt(terms[0]);
                tden = Integer.parseInt(terms[1]);
            } else {
                tnum = Integer.parseInt(input);
                tden = 1;
            }
            RationalBound r = new RationalBound(tnum, tden);
            this.upperBound = r;
            this.lowerBound = r;
            this.reciprocated = false;
        } else {
            if (input.matches("^(-?)\\d+(\\.\\d+)?")) {
                String[] parts = input.split(".");
                System.out.println("parts 0:" + parts[0] + " parts 1:" + parts[1]);
                int tint = Integer.parseInt(parts[0]);
                int tdec = Integer.parseInt(parts[1]);
                RationalBound tintb = new RationalBound(tint);
                int decDen = (int) Math.pow(10,parts[1].length()+1);
                RationalBound tdecb = new RationalBound(tdec, decDen);
                this.upperBound = RationalBound.add(tintb, tdecb, 1);
                this.lowerBound = RationalBound.add(tintb, tdecb, -1);
                this.reciprocated = false;
            } else {
                throw new IllegalArgumentException("Not a fraction or decimal");
            }
        }
    }

    // Accessors
    public RationalBound getUpperBound() {
        return this.upperBound;
    }
    public RationalBound getLowerBound() {
        return this.lowerBound;
    }
    public boolean getReciprocated() {
        return this.reciprocated;
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
            , (mul1.reciprocated||mul2.reciprocated)
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
            , (add1.reciprocated||add2.reciprocated)
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
            , input.reciprocated
        );
    }

    public static final RationalRange reciprocate(RationalRange input) {
        // note the upper bound and lower bound trade places
        return new RationalRange(
            RationalBound.reciprocate(input.lowerBound) 
            , RationalBound.reciprocate(input.upperBound)
            , (!input.reciprocated)
        );
    }

    // Comparisons

    public QuantifiedBoolean isPositive(RationalRange input) {
        
        if (!input.reciprocated) {;} else {
            return QuantifiedBoolean.tossUp;
        }
        if (RationalBound.signum(input.lowerBound,-1)>0 || RationalBound.signum(input.upperBound,-1)<0) {
            return new QuantifiedBoolean(RationalBound.signum(input.upperBound,-1)<0);
        }

        // can get odds by dividing positive share by entire width
    
        RationalBound u = input.upperBound;
        RationalBound l = input.lowerBound;
        RationalBound width = RationalBound.subtract(u, l,-1);
        RationalBound prob = RationalBound.divide(u, width,-1);
        if (RationalBound.greaterThanOne(prob, -1)) { //TODO: not sure this is right
            return new QuantifiedBoolean(
                (byte) RationalBound.toInt(RationalBound.multiply(RationalBound.reciprocate(prob), new RationalBound(255),-1))
            );
        } else {
            return new QuantifiedBoolean(
                (byte) RationalBound.toInt(RationalBound.multiply(prob, new RationalBound(255),-1))
            );
        }
        
    }

    public QuantifiedBoolean greaterThan(RationalRange com1, RationalRange com2) {
        // first do a subtraction (its ok, wont lose any precision)
        // (ok maybe some but remember that rounding can never ruin sign)
        // also as both bounds approach zero or infinity the change in probability
        // will be minor 
        // TODO: minimize uncertainty
        RationalRange diff = subtract(com1, com2);
        return isPositive(diff);
    }
}