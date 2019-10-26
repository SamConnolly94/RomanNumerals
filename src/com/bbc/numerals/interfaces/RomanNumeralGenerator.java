package com.bbc.numerals.interfaces;

import com.bbc.numerals.exceptions.InvalidNumberException;

public interface RomanNumeralGenerator {
    public String generate(int number) throws InvalidNumberException;
}
