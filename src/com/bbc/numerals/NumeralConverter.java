package com.bbc.numerals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

public class NumeralConverter implements RomanNumeralGenerator {
    // Define a map of roman numerals, we'll map the value to each numeral object.
    private final SortedMap<Integer, Numeral> numeralMap = new TreeMap<Integer, Numeral>(Collections.reverseOrder()) {
        {
            put(1000, new Numeral("M", true));
            put(500, new Numeral("D", false));
            put(100, new Numeral("C", true));
            put(50, new Numeral("L", false));
            put(10, new Numeral("X", true));
            put(5, new Numeral("V", false));
            put(1, new Numeral("I", true));
        }
    };

    // Define an array of denominations which are found from within our numeral map. This is here as it is useful for
    // sorting purposes.
    private ArrayList<Integer> denominations = new ArrayList<Integer>(numeralMap.keySet());

    @Override
    public String generate(int number) {
        StringBuilder result = new StringBuilder();
        ArrayList<Integer> numbers = generateRoundedNumbersArray(number);

        Collections.sort(denominations);
        Collections.reverse(denominations);

        result.append(exploreNumberSegment(numbers));
        return result.toString();
    }

    private ArrayList<Integer> generateRoundedNumbersArray(int number) {
        String numberString = String.valueOf(number);
        int numDigits = numberString.length();
        int sigFigs = numDigits - 1;
        ArrayList<Integer> result = new ArrayList<Integer>();

        for (int digitCount = 0; digitCount < numDigits; digitCount++) {
            int digit = Integer.valueOf(numberString.substring(digitCount, digitCount + 1));
            int newNumber = digit * (int)Math.pow(10, sigFigs);
            if (newNumber != 0) {
                result.add(newNumber);
            }
            sigFigs--;
        }

        // Sort by largest first.
        Collections.sort(result);
        Collections.reverse(result);

        return result;
    }

    private int getNextDenominationDeductionIndex(int denominationIndex) {
        for (int i = denominationIndex + 1; i < denominations.size(); i++) {
            if (numeralMap.get(denominations.get(i)).isDeductable()) {
                return i;
            }
        }

        return denominations.size() - 1;
    }

    private int getNextLargestDenominationIndex(int number) {
        int largestDenominationIndex = 0;

        for (int i = 0; i < denominations.size(); i++) {
            if (denominations.get(i) > number)
            {
                largestDenominationIndex = i;
            }
        }

        // Couldn't find a denomination larger, return the smallest denomination.
        return largestDenominationIndex;
    }

    private String exploreNumberSegment(ArrayList<Integer> numbers) {
        StringBuilder result = new StringBuilder();

        for (Integer numberChunk : numbers) {
            result.append(exploreDenominations(numberChunk));
        }

        return result.toString();
    }

    private String exploreDenominations(int number) {
        for (int denominationIndex = 0; denominationIndex < denominations.size(); denominationIndex++) {
            // If the number we are analysing is smaller than this domination, then continue out
            int deductionValue = denominations.get(denominationIndex) - denominations.get(getNextDenominationDeductionIndex(denominationIndex));
            if (number < denominations.get(denominationIndex) && number < deductionValue) {
                continue;
            }

            int numTimesToWriteNumeral;
            if (number >= deductionValue && number < denominations.get(denominationIndex)) {
                numTimesToWriteNumeral = number / deductionValue;
            } else {
                numTimesToWriteNumeral = number / denominations.get(denominationIndex);
            }

            String results = convertToNumerals(numTimesToWriteNumeral, denominationIndex, number);

            // We've turned this chunk into a roman numeral, can stop now
            if (!results.isEmpty())
            {
                return results;
            }
        }

        return "";
    }

    private String convertToNumerals(int numTimesToWriteNumeral, int denominationIndex, int number) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < numTimesToWriteNumeral; i++) {
            // If greater than the half way point between next denomination and this denomination
            if (denominationIndex < denominations.size() - 1 && number % denominations.get(denominationIndex) != 0) {

                if (shouldAppendDeduction(denominationIndex, number)) {
                    int deductionIndex = getNextDenominationDeductionIndex(denominationIndex);
                    result.append(numeralMap.get(denominations.get(deductionIndex)).getValue());
                    result.append(numeralMap.get(denominations.get(denominationIndex)).getValue());
                } else {
                    result.append(numeralMap.get(denominations.get(denominationIndex)).getValue());
                }
            } else {
                result.append(numeralMap.get(denominations.get(denominationIndex)).getValue());
            }
        }

        return result.toString();
    }

    private boolean shouldAppendDeduction(int denominationIndex, int number) {
        // Don't want to deduct if we're the largest denomination
        if (denominationIndex == 0) {
            return false;
        }

        //
        int deductionIndex = getNextDenominationDeductionIndex(denominationIndex);
        return number >= denominations.get(denominationIndex) - denominations.get(deductionIndex);
    }
}
