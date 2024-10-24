package com.jonathantippy.FuzzyFraction;

class Utility {
    
    static int addBits(long a, long b) {
        long ta = bitLength(a);
        long tb = bitLength(b);
        
        if ((tb!=0)&&(tb!=0)) {;} else {
            return 0;
        }
        if ((tb!=1)&&(tb!=1)) {;} else {
            return (int) (ta * tb);
        }
        return (int) (ta + tb);
    }

    static final int bitLength(long a) {
        long x = a^(a>>63);
        return 64 - Long.numberOfLeadingZeros(x);
    }

    static long branchlessDoz(long inputA, long inputB) {
        long difference = inputA - inputB; 
        return (difference)&(((difference & Long.MIN_VALUE) >>> 63)-1);
    }

    static long sadDoz(long inputA, long inputB) {
        long difference = inputA - inputB;
        return Math.max(0, difference);
    }

    static int unwantedBits(long input, int maxBitLength) {
        return Math.max(0, bitLength(input) - maxBitLength);
    }

    static long branchlessAbs(long input) {
        long signMask = input >> 63;
        long signBit = -signMask;
        return (input ^ signMask) + signBit;
    }

    static int countFreeTwos(long numerator, long denomenator) {
        return Math.min(               
            Long.numberOfTrailingZeros(numerator)
            , Long.numberOfTrailingZeros(denomenator)
            );
    }
}
