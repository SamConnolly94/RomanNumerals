package com.bbc.numerals.test;

import com.bbc.numerals.converters.NumeralConverter;
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
    public void One() {
        Assert.assertEquals(numeralConverter.generate(1), "I");
    }

    @Test
    public void Two() {
        Assert.assertEquals(numeralConverter.generate(2), "II");
    }

    @Test
    public void Three() {
        Assert.assertEquals(numeralConverter.generate(3), "III");
    }

    @Test
    public void Four() {
        Assert.assertEquals(numeralConverter.generate(4), "IV");
    }
    @Test
    public void Five() {
        Assert.assertEquals(numeralConverter.generate(5), "V");
    }

    @Test
    public void Ten() {
        Assert.assertEquals(numeralConverter.generate(10), "X");
    }

    @Test
    public void Twenty() {
        Assert.assertEquals(numeralConverter.generate(20), "XX");
    }

    @Test
    public void OneHundredNine() {
        Assert.assertEquals(numeralConverter.generate(109), "CIX");
    }

    @Test
    public void Ninety() {
        Assert.assertEquals(numeralConverter.generate(90), "XC");
    }

    @Test
    public void ThreeThousandNineHundredNinetyNine(){
        Assert.assertEquals(numeralConverter.generate(3999), "MMMCMXCIX");
    }

    @Test
    public void TwoThousandFourHundredThirtyTwo() {
        Assert.assertEquals(numeralConverter.generate(2432), "MMCDXXXII");
    }

    @Test
    public void OneThousandFourHundredNinetyNine() {
        Assert.assertEquals(numeralConverter.generate(1499), "MCDXCIX");
    }

}
