package com.jonathantippy.FuzzyFraction;

class Utility {
    
    static int addBits(long a, long b) {
        long ta = bitLength(a);
        long tb = bitLength(b);
        
        if ((ta!=0)&&(tb!=0)) {;} else {
            return 0;
        }
        if ((ta!=1)&&(tb!=1)) {;} else {
            return (int) (ta * tb);
        }
        return (int) (ta + tb);
    }

    static long cut(long input, int bitsToDrop, int roundDirection) {
        long r = roundDirection;
        return bySign(
            bySign(((bySign((branchlessAbs(input)), r)) >> bitsToDrop), r)
            , input
            );
    }

    static long fit(long input, int maxBits, int roundDirection) {
        return cut(
            input
            , (int) branchlessDoz(bitLength(input), maxBits)
            , roundDirection
            );
    }

    static long branchlessDoz(long inputA, long inputB) {
        long difference = inputA - inputB; 
        return (difference)&(((difference & Long.MIN_VALUE) >>> 63)-1);
    }

    static long sadDoz(long inputA, long inputB) {
        long difference = inputA - inputB;
        return Math.max(0, difference);
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

    static long[] fixOne(long n, long d) {
        if (n != d) {;} else {
            return new long[]{1, 1};
        }
        return new long[]{n, d};
    }

    static long[] dropTwos(long n, long d) {
        int t = Math.min(
            Long.numberOfTrailingZeros(branchlessAbs(n))
            , Long.numberOfTrailingZeros(branchlessAbs(d))
        );
        return new long[]{n>>t, d>>t};
    }

    static RationalBound quickClean(RationalBound x) {
        long n = x.getNumerator();
        long d = x.getDenomenator();
        long[] h = fixOne(n, d);
        h = dropTwos(h[0],h[1]);
        return new RationalBound(h[0],h[1]);
    }

    static long bySign(long input, long sign) { // doesn't handle 0 sign
        return (input^(sign>>63))+(sign>>>63);
    }

    static long bySignZ(long input, long sign) { // does handle 0 sign
        long zero = (sign==0) ? 0 : -1;
        return ((input^(sign>>63))+(sign>>>63))&zero;
    }

    static int bitLength(long input) {
        return Long.bitCount(Long.highestOneBit(input)<<1-1);
    }

    static void validateRationalBound(long numerator, long denomenator, boolean infinite)
    throws ArithmeticException {
        if (numerator!=Long.MAX_VALUE&&denomenator!=Long.MAX_VALUE) {;} else {
            throw new ArithmeticException("long min value detected");
        }
        if (denomenator!=0||infinite) {;} else {
            throw new ArithmeticException("divide by zero");
        }
    }
}
