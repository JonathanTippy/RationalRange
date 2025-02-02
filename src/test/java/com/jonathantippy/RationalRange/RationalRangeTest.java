package com.jonathantippy.RationalRange;

import static com.jonathantippy.RationalRange.util.branchlessDOZ;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.junit.jupiter.api.Test;

import com.jonathantippy.RationalRange.RationalRange;
import static com.jonathantippy.RationalRange.RationalRange.*;
import com.jonathantippy.RationalRange.util;

import net.jqwik.api.*;


public class RationalRangeTest {

     //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
                                //STRINGS
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////

    @Test
    public void stringPositiveTest() {
        RationalRange r = new RationalRange("50/7");
        String s = r.toString();
        assertEquals("50/7 to 50/7", s);
    }

    @Test
    public void stringNegativeTest() {
        RationalRange r = new RationalRange("-50/7");
        String s = r.toString();
        assertEquals("-50/7 to -50/7", s);
    }

    @Test
    public void stringIntegerTest() {
        RationalRange r = new RationalRange("5");
        String s = r.toString();
        assertEquals("5/1 to 5/1", s);
    }

    @Test
    public void hardStringIntegerTest() {
        RationalRange r = new RationalRange("60");
        String s = r.toString();
        assertEquals("60/1 to 60/1", s);
    }

    @Test
    public void decimalTest() {
        RationalRange r = new RationalRange("1.5");
        String s = r.toString();
        assertEquals("15/10 to 15/10", s);
    }

/*
    @Test
    public void decimalTest2() {
        RationalRange r = new RationalRange("2148.100000");
        String s = r.toString();
        assert(RationalRange.contains(r, Double.parseDouble("2148.100000")))
        : r + " does not contain " + Double.parseDouble("2148.100000");
    }
*/
/*
    @Property
    void decimalComplete(@ForAll int a, @ForAll int b) {
        if (a!=Integer.MIN_VALUE&&b!=Integer.MIN_VALUE) {;} else {
            return;
        }

        String integerPart = Integer.toString(a);
        String decimalPart = Integer.toString(Math.abs(b));
        String number = integerPart + '.' + decimalPart;

        RationalRange result = new RationalRange(number);

        assert(RationalRange.contains(result, Double.parseDouble(number)))
        : result + " does not contain " + Double.parseDouble(number);
    }
*/
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
                                //MULTIPLICATION
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    
    @Property
    void reciprocity(@ForAll int a, @ForAll int b) {
        if (a!=Integer.MIN_VALUE&&b!=Integer.MIN_VALUE) {;} else {
            return;
        }

        RationalRange r = new RationalRange(a, b);

        RationalRange recip = RationalRange.reciprocate(r);

        RationalRange result = RationalRange.multiply(r, recip);

        assert(RationalRange.contains(result, 1))
        : result + " does not contain 1";
    }
}