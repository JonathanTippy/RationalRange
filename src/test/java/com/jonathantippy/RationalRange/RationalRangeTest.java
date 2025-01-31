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
        assertEquals("3/2 to 3/2", s);
    }


}