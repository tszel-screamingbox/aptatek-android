package com.aptatek.aptatek.data;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class PinCodeTest {

    private PinCode pinCode;


    @Before
    public void setUp() {
        pinCode = new PinCode("pincode".getBytes());
    }


    @Test
    public void testPinsAreSame() {
        final PinCode samePin = new PinCode("pincode".getBytes());
        assertTrue(pinCode.isTheSame(samePin));
    }

    @Test
    public void testPinsAreDifferent() {
        final PinCode differentPin = new PinCode("different".getBytes());
        assertTrue(!pinCode.isTheSame(differentPin));
    }

    @Test(expected = RuntimeException.class)
    public void testBytesOfClearedPin() {
        pinCode.clear();
        pinCode.getBytes();
    }

    @Test(expected = RuntimeException.class)
    public void testCharsOfClearedPin() {
        pinCode.clear();
        pinCode.getChars();
    }
}