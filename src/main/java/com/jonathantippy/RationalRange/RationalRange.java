package com.jonathantippy.RationalRange;

class RationalRange
{

    private static final Logger log = LogManager.getLogger(RationalRange.class);

    //FIELDS
    private RationalBound upperBound;
    private RationalBound lowerBound;

    // Constructors with both numerator and denomenator
    public RationalRange(int numerator, int denomenator) 
    throws ArithmeticException {

        util.validateRationalBound(numerator, denomenator, false);
        this.upperBound = new RationalBound(numerator, denomenator, false);
        this.lowerBound = new RationalBound(numerator, denomenator, false);
    }

    // Constructors with integers
    public RationalRange(int units) {

        util.validateRationalBound(units, 1, false);
        this.upperBound = new RationalBound(units, 1, false);
        this.lowerBound = new RationalBound(units, 1, false);
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
            util.validateRationalBound(tnum, tden, false);
            this.upperBound = new RationalBound(tnum, tden, false);
            this.lowerBound = new RationalBound(tnum, tden, false);
        } else {
            throw new IllegalArgumentException("Not a fraction");
        }
    }

}