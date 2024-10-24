package com.jonathantippy.FuzzyFraction;

class Utility {
    
    static int addBits(long a, long b) {
        return (
            bitLength(a)
            + bitLength(b)
            );
    }

    static final long spreadSign(long x) {
        return x >> 63;
    }

    static final int bitLength(long a) {
        return Math.max((63 - Long.numberOfLeadingZeros(a)), 0);
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
        long signBit = (input & Long.MIN_VALUE) >>> 63;
        long signMask = signBit * (-1);
        return (input ^ signMask) + signBit;
    }

    static int countFreeTwos(long numerator, long denomenator) {
        return Math.min(               
            Long.numberOfTrailingZeros(numerator)
            , Long.numberOfTrailingZeros(denomenator)
            );
    }
}
