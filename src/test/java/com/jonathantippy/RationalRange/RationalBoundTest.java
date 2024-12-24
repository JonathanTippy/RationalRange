package com.jonathantippy.RationalRange;

import static com.jonathantippy.RationalRange.util.branchlessDOZ;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.junit.jupiter.api.Test;

import com.jonathantippy.RationalRange.RationalBound;
import com.jonathantippy.RationalRange.util;

import net.jqwik.api.*;


public class RationalBoundTest {

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
        RationalBound r = new RationalBound(50);
        String s = r.toString();
        assertEquals("50/1", s);
    }

    @Test
    public void intNegativeTest() {
        RationalBound r = new RationalBound(-50);
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
        RationalBound answer = dividend.divide(divisor, 1);
        String s = answer.toString();
        assertEquals("1/3", s);
    }

    @Test
    public void hardDivisionTest() {
        RationalBound dividend = new RationalBound("5/6");
        RationalBound divisor = new RationalBound("2/3");
        RationalBound answer = dividend.divide(divisor, 1);
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
    void multiplyOneDirection(@ForAll int a, @ForAll int b, @ForAll("one") int r) {
        if (a!=0&&b!=0&&a!=Integer.MIN_VALUE&&b!=Integer.MIN_VALUE) {;} else {
            return;
        }

        RationalBound factorOne = new RationalBound(a, a);
        RationalBound factorTwo = new RationalBound(b, b);
        RationalBound answer = factorOne.multiply(factorTwo, r);
        assert(!answer.compareToOne(-r)) // for "or equals"
        : answer + " or in decimal " + answer.toDouble() + " is not greater than one";
    }

    @Property
    void multiplyDivide(@ForAll int a, @ForAll int b, @ForAll("one") int r) {
        if (a!=0&&b!=0&&a!=Integer.MIN_VALUE&&b!=Integer.MIN_VALUE) {;} else {
            return;
        }

        RationalBound factorOne = new RationalBound(a, a);
        RationalBound factorTwo = new RationalBound(b, b);
        RationalBound answer = factorOne.multiply(factorTwo, r);
        assert(!answer.compareToOne(-r)) // for "or equals"
        : answer + " or in decimal " + answer.toDouble() + " is not greater than one";
    }

    @Provide
    Arbitrary<Integer> one() {
        return Arbitraries.integers().filter(v -> v == 1 || v == -1);
    }

    @Test
    public void multTest() {
        RationalBound a = new RationalBound(5);
        RationalBound b = new RationalBound(6);
        RationalBound answer = a.multiply(b, 1);
        String s = answer.toString();
        assertEquals("30/1", s);
    }

    @Test
    public void multTest2() {
        RationalBound a = new RationalBound(-5);
        RationalBound b = new RationalBound(6);
        RationalBound answer = a.multiply(b, 1);
        String s = answer.toString();
        assertEquals("-30/1", s);
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
        RationalBound answer = a.add(b, 1);
        String s = answer.toString();
        assertEquals("11/1", s);
    }

    @Test
    public void subtractionTest() {
        RationalBound a = new RationalBound(5);
        RationalBound b = new RationalBound(6);
        RationalBound answer = a.subtract(b, 1);
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



      //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
                                //UTILS
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////

    @Test
    public void simpleCutTest() {
        assert util.cut(1, 0, 1) == 1: "fail! expected 1, but got " + util.cut(1, 0, 1);
    }

    @Property
    public void fitTest(@ForAll int input, @ForAll("one") int r) {
        assert util.fit((int) input, 31, r) == (int) input;
    }

    @Property
    public void bySignTest(@ForAll int input, @ForAll int sign) {
        if (sign != 0) {;} else {return;}
        assert util.bySign(input, sign) == input * Integer.signum(sign): 
        "Fail! expected " + (input * Integer.signum(sign)) + " but got " + (util.bySign(input, sign));
    }
    @Property
    public void bySignZTest(@ForAll int input, @ForAll int sign) {
        assert util.bySignZ(input, sign) == input * Integer.signum(sign): 
        "Fail! expected " + (input * Integer.signum(sign)) + " but got " + (util.bySign(input, sign));
    }
    @Property
    public void branchlessMaxTest(@ForAll int inputA, @ForAll int inputB) {
        assert util.branchlessMax(inputA, inputB) == Math.max(inputA, inputB):
        "Fail! expected " + Math.max(inputA, inputB) + " but got " + util.branchlessMax(inputA, inputB);
    }




    @Test
    public void branchlessDozTest() {
        ArrayList<Integer> inputs = new ArrayList<Integer>();
        inputs.addAll(Arrays.asList(Integer.MAX_VALUE, Integer.MIN_VALUE, 0, 1, -1));
        for (int i=0; i<100; i++) {
            inputs.add(random.nextInt());
        }
        for (int inputA: inputs) {
            for (int inputB: inputs) {
                assertEquals(doz(inputA, inputB), 
                branchlessDOZ(inputA, inputB), 
                ("ERROR: Input was " + inputA + " and " + inputB
                + " and output was " + branchlessDOZ(inputA, inputB)));
            }
        }
    }

    @Property
    public void gtOneTest(@ForAll int a, @ForAll int b) {
        if (a!=0&&b!=0&&a!=Integer.MIN_VALUE&&b!=Integer.MIN_VALUE) {;} else {
            return;
        }
        RationalBound x = new RationalBound(a, b);
        assert (isGTOne(x) == x.isGreaterThanOne()): 
        "Issue: input was " + a + " and " + b + " slow said " + isGTOne(x) + " while fast said " + x.isGreaterThanOne();
        RationalBound y = new RationalBound(a, a);
        assert (isGTOne(y) == y.isGreaterThanOne()): 
        "Issue: input was " + a + " and " + b + " slow said " + isGTOne(y) + " while fast said " + y.isGreaterThanOne();
        RationalBound z = new RationalBound(b, b);
        assert (isGTOne(z) == z.isGreaterThanOne()): 
        "Issue: input was " + a + " and " + b + " slow said " + isGTOne(z) + " while fast said " + z.isGreaterThanOne();
    }


    private static int doz(int inputA, int inputB) {
        int returned = inputA-inputB;
        if (returned<0) {
            returned=0;
        }
        return returned;
    }

    private static boolean isGTOne(RationalBound input) {
        if (input.getNumerator() == input.getDenomenator()) {
            return false;
        }
        if (input.signum() == 1) {
            return Math.abs(input.getNumerator()) > Math.abs(input.getDenomenator());
        }
        if (input.signum() == -1) {
            return false;
        }
        return false;

    }
}