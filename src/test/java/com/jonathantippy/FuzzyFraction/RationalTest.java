package com.jonathantippy.FuzzyFraction;

import static com.jonathantippy.FuzzyFraction.Utility.addBits;
import static com.jonathantippy.FuzzyFraction.Utility.branchlessDoz;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.junit.jupiter.api.Test;
import net.jqwik.api.*;


public class RationalTest {

    static Random random = new Random();

    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
                                //BASIC CONSTRUCTOR
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////

    @Test
    public void basicConstructorTest() {
        RationalBound r = new RationalBound(1, 1);
        String s = r.toString();
        assertEquals("1/1", s);
    }


    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
                                //STRINGS
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////

    @Test
    public void stringPositiveTest() {
        RationalBound r = new RationalBound("50/7");
        String s = r.toString();
        assertEquals("50/7", s);
    }

    @Test
    public void stringNegativeTest() {
        RationalBound r = new RationalBound("-50/7");
        String s = r.toString();
        assertEquals("-50/7", s);
    }

    @Test
    public void stringIntegerTest() {
        RationalBound r = new RationalBound("5");
        String s = r.toString();
        assertEquals("5/1", s);
    }

    @Test
    public void hardStringIntegerTest() {
        RationalBound r = new RationalBound("60");
        String s = r.toString();
        assertEquals("60/1", s);
    }


    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
                                //INTEGERS
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////

    @Test
    public void intPositiveTest() {
        RationalBound r = new RationalBound(50L);
        String s = r.toString();
        assertEquals("50/1", s);
    }

    @Test
    public void intNegativeTest() {
        RationalBound r = new RationalBound(-50L);
        String s = r.toString();
        assertEquals("-50/1", s);
    }

    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
                                //DIVISION
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////

    @Test
    public void divisionTest() {
        RationalBound dividend = new RationalBound(1);
        RationalBound divisor = new RationalBound(3);
        RationalBound answer = dividend.divide(divisor);
        String s = answer.toString();
        assertEquals("1/3", s);
    }

    @Test
    public void hardDivisionTest() {
        RationalBound dividend = new RationalBound("5/6");
        RationalBound divisor = new RationalBound("2/3");
        RationalBound answer = dividend.divide(divisor);
        String s = answer.toString();
        assertEquals("15/12", s);
    }

    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
                                //MULTIPLICATION
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////

    
    @Property
    void multiplyOneDirection(@ForAll long a, @ForAll long b, @ForAll("one") int r) {
        if (a!=0&&b!=0&&a!=Long.MIN_VALUE&&b!=Long.MIN_VALUE) {;} else {
            return;
        }

        RationalBound factorOne = new RationalBound(a, a);
        RationalBound factorTwo = new RationalBound(b, b);
        RationalBound answer = factorOne.multiply(factorTwo, r);
        assert(answer.bySign(r).isGreaterThanOne())
        : answer + " or in decimal " + answer.toDouble() + " is greater than one";
    }

    @Provide
    Arbitrary<Integer> one() {
        return Arbitraries.integers().filter(v -> v == 1 || v == -1);
    }


    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
                                //ADDITION
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////

    @Test
    public void additionTest() {
        RationalBound a = new RationalBound(5);
        RationalBound b = new RationalBound(6);
        RationalBound answer = a.add(b);
        String s = answer.toString();
        assertEquals("11/1", s);
    }

    @Test
    public void subtractionTest() {
        RationalBound a = new RationalBound(5);
        RationalBound b = new RationalBound(6);
        RationalBound answer = a.subtract(b);
        String s = answer.toString();
        assertEquals("-1/1", s);
    }

     //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
                                //SIMPLIFICATION
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////

    @Test
    public void simplifyTwosTest() {
        RationalBound a = new RationalBound("50/20");
        RationalBound answer = a.twoSimplify();
        String s = answer.toString();
        assertEquals("25/10", s);
    }
    @Test
    public void simplifyTwosTest2() {
        RationalBound a = new RationalBound("1/1");
        RationalBound answer = a.twoSimplify();
        String s = answer.toString();
        assertEquals("1/1", s);
    }
    @Test
    public void simplifyTwosTest3() {
        RationalBound a = new RationalBound("0/1");
        RationalBound answer = a.twoSimplify();
        String s = answer.toString();
        assertEquals("0/1", s);
    }
    @Test
    public void negativeSimplifyTwosTest() {
        RationalBound a = new RationalBound("-50/20");
        RationalBound answer = a.twoSimplify();
        assert(!answer.maybeDiffer(new RationalBound(50, -20)));
    }
    @Test
    public void negativeSimplifyTwosTest2() {
        RationalBound a = new RationalBound("-50/-20");
        RationalBound answer = a.twoSimplify();
        String s = answer.toString();
        assert(!answer.maybeDiffer(new RationalBound(25/10)));
    }
    @Test
    public void negativeSimplifyTwosTest3() {
        RationalBound a = new RationalBound("50/-20");
        RationalBound answer = a.twoSimplify();
        assert(!answer.maybeDiffer(new RationalBound(-50, 20)));
    }

      //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
                                //UTILS
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////

    @Test
    public void addBitsTest() {
        ArrayList<Long> inputs = new ArrayList<Long>();
        inputs.addAll(Arrays.asList(Long.MAX_VALUE, Long.MIN_VALUE, 0L, 1L, -1L));
        for (int i=0; i<100; i++) {
            inputs.add(random.nextLong());
        }
        for (long inputA: inputs) {
            for (long inputB: inputs) {
                assert(addBits(inputA, inputB) <= 126);
                assert(addBits(inputA, inputB) >= 0);
            }
        }
    }


    @Test
    public void branchlessDozTest() {
        ArrayList<Long> inputs = new ArrayList<Long>();
        inputs.addAll(Arrays.asList(Long.MAX_VALUE, Long.MIN_VALUE, 0L, 1L, -1L));
        for (int i=0; i<100; i++) {
            inputs.add(random.nextLong());
        }
        for (long inputA: inputs) {
            for (long inputB: inputs) {
                assertEquals(doz(inputA, inputB), 
                branchlessDoz(inputA, inputB), 
                ("ERROR: Input was " + inputA + " and " + inputB
                + " and output was " + branchlessDoz(inputA, inputB)));
            }
        }
    }

    @Property
    public void gtOneTest(@ForAll long a, @ForAll long b) {
        if (a!=0&&b!=0&&a!=Long.MIN_VALUE&&b!=Long.MIN_VALUE) {;} else {
            return;
        }
        RationalBound x = new RationalBound(a, b);
        assert isGTOne(x) == (x.isGreaterThanOne()): 
        "Issue: input was " + a + " and " + b + " slow said " + isGTOne(x) + " while fast said " + x.isGreaterThanOne();
    }


    private static long doz(long inputA, long inputB) {
        long returned = inputA-inputB;
        if (returned<0) {
            returned=0;
        }
        return returned;
    }

    private static boolean isGTOne(RationalBound x) {
        if (Long.signum(x.getNumerator()) == 1) {
            if (Long.signum(x.getDenomenator()) == 1) {
                return (x.getNumerator() > x.getDenomenator());
            } else {
                return false;
            }
        } else {
            if (Long.signum(x.getDenomenator()) == 1) {
                return false;
            } else {
                return (x.getDenomenator() > x.getNumerator());
            }
        }
    }

}