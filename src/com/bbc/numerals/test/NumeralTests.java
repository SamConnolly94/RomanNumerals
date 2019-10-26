package com.bbc.numerals.test;

import com.bbc.numerals.converters.NumeralConverter;
import com.bbc.numerals.exceptions.InvalidNumberException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NumeralTests {
    private final NumeralConverter numeralConverter = new NumeralConverter();

    @Before
    public void Setup() {
        // Do any setup here
    }

    @Test
    public void Zero() throws Exception {
        try {
            Assert.assertEquals(numeralConverter.generate(0), "INVALID");
        } catch (InvalidNumberException e) {
            // Success. we expected an exception to be thrown
            return;
        }

        throw new Exception("Should have thrown exception while trying to generate an invalid number");
    }

    @Test
    public void One() throws InvalidNumberException {
        Assert.assertEquals(numeralConverter.generate(1), "I");
    }

    @Test
    public void Two() throws InvalidNumberException {
        Assert.assertEquals(numeralConverter.generate(2), "II");
    }

    @Test
    public void Three() throws InvalidNumberException {
        Assert.assertEquals(numeralConverter.generate(3), "III");
    }

    @Test
    public void Four() throws InvalidNumberException {
        Assert.assertEquals(numeralConverter.generate(4), "IV");
    }
    @Test
    public void Five() throws InvalidNumberException {
        Assert.assertEquals(numeralConverter.generate(5), "V");
    }

    @Test
    public void Ten() throws InvalidNumberException {
        Assert.assertEquals(numeralConverter.generate(10), "X");
    }

    @Test
    public void Twenty() throws InvalidNumberException {
        Assert.assertEquals(numeralConverter.generate(20), "XX");
    }

    @Test
    public void OneHundredNine() throws InvalidNumberException {
        Assert.assertEquals(numeralConverter.generate(109), "CIX");
    }

    @Test
    public void Ninety() throws InvalidNumberException {
        Assert.assertEquals(numeralConverter.generate(90), "XC");
    }

    @Test
    public void OneThousandFourHundredNinetyNine() throws InvalidNumberException {
        Assert.assertEquals(numeralConverter.generate(1499), "MCDXCIX");
    }

    @Test
    public void TwoThousandFourHundredThirtyTwo() throws InvalidNumberException {
        Assert.assertEquals(numeralConverter.generate(2432), "MMCDXXXII");
    }

    @Test
    public void ThreeThousandNineHundredNinetyNine() throws InvalidNumberException {
        Assert.assertEquals(numeralConverter.generate(3999), "MMMCMXCIX");
    }

    @Test
    public void FourThousand() throws Exception {
        try {
            Assert.assertEquals(numeralConverter.generate(4000), "INVALID");
        } catch (InvalidNumberException e) {
            // Success. we expected an exception to be thrown
            return;
        }

        throw new Exception("Should have thrown exception while trying to generate an invalid number");
    }
}
