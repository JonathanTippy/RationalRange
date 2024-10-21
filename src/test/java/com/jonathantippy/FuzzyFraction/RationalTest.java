package com.jonathantippy.FuzzyFraction;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

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
        Rational r = new Rational(1, 1);
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
        Rational r = new Rational("50/7");
        String s = r.toString();
        assertEquals("50/7", s);
    }

    @Test
    public void stringNegativeTest() {
        Rational r = new Rational("-50/7");
        String s = r.toString();
        assertEquals("-50/7", s);
    }

    @Test
    public void stringNegativeSimplifyTest() {
        Rational r = new Rational("-50/-7");
        String s = r.toString();
        assertEquals("50/7", s);
    }

    @Test
    public void stringIntegerTest() {
        Rational r = new Rational("5");
        String s = r.toString();
        assertEquals("5/1", s);
    }

    @Test
    public void hardStringIntegerTest() {
        Rational r = new Rational("60");
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
        Rational r = new Rational(50L);
        String s = r.toString();
        assertEquals("50/1", s);
    }

    @Test
    public void intNegativeTest() {
        Rational r = new Rational(-50L);
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
        Rational dividend = new Rational(1);
        Rational divisor = new Rational(3);
        Rational answer = dividend.divide(divisor);
        String s = answer.toString();
        assertEquals("1/3", s);
    }

    @Test
    public void hardDivisionTest() {
        Rational dividend = new Rational("5/6");
        Rational divisor = new Rational("2/3");
        Rational answer = dividend.divide(divisor);
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

    @Test
    public void multiplicationTest() {
        Rational factorOne = new Rational(1);
        Rational factorTwo = new Rational(3);
        Rational answer = factorOne.multiply(factorTwo);
        String s = answer.toString();
        assertEquals("3/1", s);
    }

    @Test
    public void hardMultiplicationTest() {
        Rational factorOne = new Rational("5/7");
        Rational factorTwo = new Rational("2/3");
        Rational answer = factorOne.multiply(factorTwo);
        String s = answer.toString();
        assertEquals("10/21", s);
    }

    // multiply round down

    @Test
    public void mrdTest() {
        Rational factorOne = new Rational(1);
        Rational factorTwo = new Rational(3);
        Rational answer = factorOne.multiplyRoundDown(factorTwo);
        String s = answer.toString();
        assertEquals("3/1", s);
    }

    @Test
    public void hardmrdTest() {
        Rational factorOne = new Rational("5/7");
        Rational factorTwo = new Rational("2/3");
        Rational answer = factorOne.multiplyRoundDown(factorTwo);
        String s = answer.toString();
        assertEquals("10/21", s);
    }
/*
    @Test
    public void hardermrdTest() {
        Rational factorOne = new Rational(1<<40);
        Rational factorTwo = new Rational(2<<40);
        Rational answer = factorOne.multiply(factorTwo);
        String s = answer.toString();
        assertEquals("10/21", s);
    }
*/
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
                                //ADDITION
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////

    @Test
    public void additionTest() {
        Rational a = new Rational(5);
        Rational b = new Rational(6);
        Rational answer = a.add(b);
        String s = answer.toString();
        assertEquals("11/1", s);
    }

    @Test
    public void subtractionTest() {
        Rational a = new Rational(5);
        Rational b = new Rational(6);
        Rational answer = a.subtract(b);
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
        Rational a = new Rational("50/20");
        Rational answer = a.twoSimplify();
        String s = answer.toString();
        assertEquals("25/10", s);
    }
    @Test
    public void simplifyTwosTest2() {
        Rational a = new Rational("1/1");
        Rational answer = a.twoSimplify();
        String s = answer.toString();
        assertEquals("1/1", s);
    }
    @Test
    public void simplifyTwosTest3() {
        Rational a = new Rational("0/1");
        Rational answer = a.twoSimplify();
        String s = answer.toString();
        assertEquals("0/1", s);
    }
    @Test
    public void negativeSimplifyTwosTest() {
        Rational a = new Rational("-50/20");
        Rational answer = a.twoSimplify();
        String s = answer.toString();
        assertEquals("-25/10", s);
    }
    @Test
    public void negativeSimplifyTwosTest2() {
        Rational a = new Rational("-50/-20");
        Rational answer = a.twoSimplify();
        String s = answer.toString();
        assertEquals("25/10", s);
    }
    @Test
    public void negativeSimplifyTwosTest3() {
        Rational a = new Rational("50/-20");
        Rational answer = a.twoSimplify();
        String s = answer.toString();
        assertEquals("-25/10", s);
    }

    /*
    @Test
    public void shiftSimplifyTest() {
        Rational a = new Rational("50/20");
        Rational answer = a.bitShiftSimplify(2);
        String s = answer.toString();
        assertEquals("3/1", s);
    }
    @Test
    public void shiftSimplifyTest2() {
        Rational a = new Rational("1/1");
        Rational answer = a.bitShiftSimplify(2);
        String s = answer.toString();
        assertEquals("1/1", s);
    }
    @Test
    public void shiftSimplifyTest3() {
        Rational a = new Rational("0/1");
        Rational answer = a.bitShiftSimplify(2);
        String s = answer.toString();
        assertEquals("0/1", s);
    }
    @Test
    public void negativeShiftSimplifyTest() {
        Rational a = new Rational("-50/20");
        Rational answer = a.bitShiftSimplify(2);
        String s = answer.toString();
        assertEquals("-/1", s);
    }
    @Test
    public void negativeShiftSimplifyTest2() {
        Rational a = new Rational("-50/-20");
        Rational answer = a.bitShiftSimplify(2);
        String s = answer.toString();
        assertEquals("3/1", s);
    }
    @Test
    public void negativeShiftSimplifyTest3() {
        Rational a = new Rational("50/-20");
        Rational answer = a.bitShiftSimplify(2);
        String s = answer.toString();
        assertEquals("-3/1", s);
    }
    */

      //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
                                //UTILS
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////

    @Test
    public void branchlessAbsTest() {
        ArrayList<Long> inputs = new ArrayList<Long>();
        inputs.addAll(Arrays.asList(Long.MIN_VALUE, Long.MAX_VALUE, 0L, 1L, -1L));
        for (int i=0; i<1000; i++) {
            inputs.add(random.nextLong());
        }
        for (long input: inputs) {
            assertEquals(Math.abs(input), Rational.branchlessAbs(input));
            //assertTrue((Rational.branchlessAbs(input) >= 0), 
            //("ERROR: non-positive. input was " + input + " 
            //and output was " + Rational.branchlessAbs(input)));
            // This test ^ is broken because Long.MIN_VALUE = — Long.MIN_VALUE
        }
    }

    @Test
    public void bitsAfterMultiplyTest() {
        ArrayList<Long> inputs = new ArrayList<Long>();
        inputs.addAll(Arrays.asList(Long.MIN_VALUE, Long.MAX_VALUE, 0L, 1L, -1L));
        for (int i=0; i<1000; i++) {
            inputs.add(random.nextLong());
        }
        for (long input: inputs) {
            assertEquals(Math.abs(input), Rational.branchlessAbs(input));
            //assertTrue((Rational.branchlessAbs(input) >= 0), 
            //("ERROR: non-positive. input was " + input + " 
            //and output was " + Rational.branchlessAbs(input)));
            // This test ^ is broken because Long.MIN_VALUE = — Long.MIN_VALUE
        }
    }
/*
    @Test
    public void branchlessMaxTest() {
        ArrayList<Long> inputs = new ArrayList<Long>();
        inputs.addAll(Arrays.asList(Long.MAX_VALUE, Long.MIN_VALUE, 0L, 1L, -1L));
        for (int i=0; i<1; i++) {
            inputs.add(random.nextLong());
        }
        for (long inputA: inputs) {
            for (long inputB: inputs) {
                assertEquals(Math.max(inputA, inputB), Rational.branchlessMax(inputA, inputB), ("ERROR: Input was " + inputA + " and " + inputB
                + " and output was " + Rational.branchlessMax(inputA, inputB)));
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
                assertEquals(doz(inputA, inputB), Rational.branchlessDoz(inputA, inputB), ("ERROR: Input was " + inputA + " and " + inputB
                + " and output was " + Rational.branchlessDoz(inputA, inputB)));
            }
        }
    }

    private static long doz(long inputA, long inputB) {
        long returned = inputA-inputB;
        if (returned<0) {
            returned=0;
        }
        return returned;
    }
*/
}