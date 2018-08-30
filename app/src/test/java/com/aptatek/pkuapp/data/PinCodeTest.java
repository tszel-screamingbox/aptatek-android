package com.aptatek.pkuapp.data;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @test.layer Data
 * @test.feature Pincode operations
 * @test.type Unit tests
 */
public class PinCodeTest {

    private PinCode pinCode;


    /**
     * Setting up the required variable
     */
    @Before
    public void setUp() {
        pinCode = new PinCode("pincode".getBytes());
    }


    /**
     * Compares two, equals {@link  PinCode  PinCode}.
     *
     * @test.expected The PIN codes are equal, no error.
     */
    @Test
    public void testPinsAreSame() {
        final PinCode samePin = new PinCode("pincode".getBytes());
        assertTrue(pinCode.equals(samePin));
    }

    /**
     * Compares two, different {@link  PinCode  PinCode}.
     *
     * @test.expected The PIN codes are not the same, no error.
     */
    @Test
    public void testPinsAreDifferent() {
        final PinCode differentPin = new PinCode("different".getBytes());
        assertTrue(!pinCode.equals(differentPin));
    }

    /**
     * Clears {@link  PinCode  PinCode} value.
     *
     * @test.expected Calling  {@link  PinCode#getBytes()  getBytes()}
     * method on a cleared PIN, throws RuntimeException.
     */
    @Test(expected = RuntimeException.class)
    public void testBytesOfClearedPin() {
        pinCode.clear();
        pinCode.getBytes();
    }

    /**
     * Clears {@link  PinCode  PinCode} value.
     *
     * @test.expected Calling  {@link  PinCode#getChars()  getChars()}
     * method on a cleared PIN, throws RuntimeException.
     */
    @Test(expected = RuntimeException.class)
    public void testCharsOfClearedPin() {
        pinCode.clear();
        pinCode.getChars();
    }
}