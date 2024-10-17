package com.jonathantippy.FastRational;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RationalTest {

    @Test
    public void stringPositiveTest() {
        Rational r = new Rational("50/7");
        String s = r.toString();
        assertEquals(s, "50/7");
    }

    @Test
    public void stringNegativeTest() {
        Rational r = new Rational("-50/7");
        String s = r.toString();
        assertEquals(s, "-50/7");
    }

    @Test
    public void stringNegativeSimplifyTest() {
        Rational r = new Rational("-50/-7");
        String s = r.toString();
        assertEquals(s, "50/7");
    }

    @Test
    public void stringIntegerTest() {
        Rational r = new Rational("60");
        String s = r.toString();
        assertEquals(s, "60/1");
    }
}