package com.jonathantippy.RationalRange;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class util {
    
    private static final Logger log = LogManager.getLogger(RationalBound.class);


    static int getBitsCeiling(
        int thisNumerator
        , int thisDenomenator
        , int thatNumerator
        , int thatDenomenator
        ) {

            int thisNumXB = (int)
            branchlessDOZ(
                bitLength(thisNumerator)
                , (31-bitLength(thatNumerator))
                );

            int shorterNum = (int) branchlessMin(bitLength(thisNumerator), bitLength(thatNumerator));
            int ceilNum = (int) branchlessMax(31, 31-shorterNum);

            int shorterDen = (int) branchlessMin(bitLength(thisDenomenator), bitLength(thatDenomenator));
            int ceilDen = (int) branchlessMax(31, 31-shorterDen);
            return 31;
    }


    static int addBits(int a, int b) {
        int ta = bitLength(a);
        int tb = bitLength(b);
        
        if ((ta!=0)&&(tb!=0)) {;} else {
            return 0;
        }
        if ((ta!=1)&&(tb!=1)) {;} else {
            return (int) (ta * tb);
        }
        return (int) (ta + tb);
    }

    static int cut(int input, int bitsToDrop, int roundDirection) {
        int r = roundDirection;
        return bySign(
            bySign(((bySign((branchlessAbs(input)), r)) >> bitsToDrop), r)
            , input
            );
    }

   

    static int fit(int input, int maxBits, int roundDirection) {
        return cut(
            input
            , (int) branchlessDOZ(bitLength(input), maxBits)
            , roundDirection
            );
    }

    static int branchlessDOZ(int inputA, int inputB) {
        int difference = inputA - inputB; 
        return (difference)&(((difference & Integer.MIN_VALUE) >>> 31)-1);
    }

    static int sadDoz(int inputA, int inputB) {
        int difference = inputA - inputB;
        return Math.max(0, difference);
    }

    static int branchlessAbs(int input) {
        int signMask = input >> 31;
        int signBit = -signMask;
        return (input ^ signMask) + signBit;
    }

    static int branchlessMax(int inputA, int inputB) { // ocd, see?
        int o = (inputA>>31) ^ (inputB>>31); //obvious
        int oc = inputB>>31; //obvious choice
        int d = inputB - inputA; //difference
        int c = d>>31; //choice
        log.debug("inputA: " + inputA + "\ninputB: " + inputB + "\nobvious: " + o + "\nobchoice: " + oc + "\ndiff: " + d + "\nchoice: " + c);
        return 
        (~o&(
            (c&inputA)
            |((~c)&inputB)
            ))
        |(o&(
            (oc&inputA)
            |((~oc)&inputB)
            ));
    }

    static int branchlessMin(int inputA, int inputB) { // ocd, see?
        int o = (inputA>>31) ^ (inputB>>31); //obvious
        int oc = inputB>>31; //obvious choice
        int d = inputB - inputA; //difference
        int c = d>>31; //choice
        log.debug("inputA: " + inputA + "\ninputB: " + inputB + "\nobvious: " + o + "\nobchoice: " + oc + "\ndiff: " + d + "\nchoice: " + c);
        return 
        (~o&(
            ((~c)&inputA)
            |(c&inputB)
            ))
        |(o&(
            ((~oc)&inputA)
            |(oc&inputB)
            ));
    }

    static int countFreeTwos(int numerator, int denomenator) {
        return Math.min(               
            Integer.numberOfTrailingZeros(numerator)
            , Integer.numberOfTrailingZeros(denomenator)
            );
    }

    static int[] fixOne(int n, int d) {
        if (n != d) {;} else {
            return new int[]{1, 1};
        }
        return new int[]{n, d};
    }

    static int[] dropTwos(int n, int d) {
        int t = Math.min(
            Integer.numberOfTrailingZeros(branchlessAbs(n))
            , Integer.numberOfTrailingZeros(branchlessAbs(d))
        );
        return new int[]{n>>t, d>>t};
    }

    static RationalBound quickClean(RationalBound x) {
        int n = x.getNumerator();
        int d = x.getDenomenator();
        int[] h = fixOne(n, d);
        h = dropTwos(h[0],h[1]);
        return new RationalBound(h[0],h[1]);
    }

    static int bySign(int input, int sign) { // doesn't handle 0 sign
        return (input^(sign>>31))+(sign>>>31);
    }

    static int bySignZ(int input, int sign) { // does handle 0 sign
        int zero = (sign==0) ? 0 : -1;
        return ((input^(sign>>31))+(sign>>>31))&zero;
    }

    static int bitLength(int input) {
        return Integer.bitCount(branchlessHighestOneBit(input)<<1-1);
    }

    static int branchlessHighestOneBit(int input) {
        int tupni = Integer.reverse(input);
        return Integer.reverse(tupni&(-tupni));
    }

    static int freeBits(int input) {
        return 31 - bitLength(input);
    }

    static void validateRationalBound(int numerator, int denomenator, boolean infinite)
    throws ArithmeticException {
        if (numerator!=Integer.MIN_VALUE&&denomenator!=Integer.MIN_VALUE) {;} else {
            throw new ArithmeticException("int min value detected");
        }
        if (denomenator!=0||infinite) {;} else {
            throw new ArithmeticException("divide by zero");
        }
    }
}