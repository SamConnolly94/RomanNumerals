package com.bbc.numerals.converters;

import com.bbc.numerals.datastructures.Numeral;
import com.bbc.numerals.exceptions.InvalidNumberException;
import com.bbc.numerals.interfaces.RomanNumeralGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

public class NumeralConverter implements RomanNumeralGenerator {
    private final int largestNumber = 3999;
    private final int smallestNumber = 1;

    // Define a map of roman numerals, we'll map the value to each numeral object.
    private final SortedMap<Integer, Numeral> numeralMap = new TreeMap<>(Collections.reverseOrder()) {
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
    private ArrayList<Integer> denominations = new ArrayList<>(numeralMap.keySet());

    /**
     * This function is called in order to convert a number into roman numerals.
     *
     * @param number is the number you wish to convert to roman numerals
     * @return A string representing the numeral value of the number passed in
     * @throws InvalidNumberException is thrown when a number which was too small or too large was passed in.
     */
    @Override
    public String generate(int number) throws InvalidNumberException {
        validateNumber(number);

        // Separate the number into an array which divides the number into chunks. e.g. 3999 is
        // 3000 +
        //  900 +
        //   90 +
        //    9
        ArrayList<Integer> numbers = generateRoundedNumbersArray(number);

        // Sort the denominations array so we can guarantee the largest denomination is always at the first element
        Collections.sort(denominations);
        Collections.reverse(denominations);

        // Convert the array of numbers we just calculated into separate numerals
        return exploreNumberSegment(numbers);
    }

    /**
     * Ensure the numbers passed into the numeral converter are within the valid min and max ranges.
     * @param number is the number that is to be converted to numerals.
     * @throws InvalidNumberException is thrown when the number does not sit within the valid range
     */
    private void validateNumber(int number) throws InvalidNumberException {
        // Check if number is too large
        if (number > largestNumber) {
            throw new InvalidNumberException("Largest number supported is "+ String.valueOf(largestNumber) + " but " + String.valueOf(number) + " was passed in.");
        }

        // Check if number is too small
        if (number < smallestNumber) {
            throw new InvalidNumberException("Smallest number supported is "+ String.valueOf(smallestNumber) + " but " + String.valueOf(number) + " was passed in.");
        }
    }

    /**
     * Generate an array which separates a number into it's own thousandths, hundredths, tens, and units.
     * E.g. 3999 becomes
     * [0] 3000
     * [1] 900
     * [2]  90
     * [3]   9
     * @param number the number to be separated into chunks
     * @return An array of numbers that compose the number passed in
     */
    private ArrayList<Integer> generateRoundedNumbersArray(int number) {
        String numberString = String.valueOf(number);
        int numDigits = numberString.length();
        int sigFigs = numDigits - 1;
        ArrayList<Integer> result = new ArrayList<>();

        // For each digit
        for (int digitCount = 0; digitCount < numDigits; digitCount++) {
            // Get the integer from the stringified version of the number passed in
            int digit = Integer.parseInt(numberString.substring(digitCount, digitCount + 1));
            // Raise it to the power of the sig figs
            int newNumber = digit * (int)Math.pow(10, sigFigs);
            // If its a 0 we dont want to generate a numeral, so don't add
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

    /**
     * Finds the index of the next numeral that can be deducted from another numeral in the denominations array.
     * @param denominationIndex the current index of the denomination that is being processed
     * @return The index of the next denomination that can be deducted from the current
     */
    private int getNextDenominationDeductionIndex(int denominationIndex) {
        for (int i = denominationIndex + 1; i < denominations.size(); i++) {
            if (numeralMap.get(denominations.get(i)).isDeductible()) {
                return i;
            }
        }

        // Couldn't find next deductible, just return the current one
        return denominations.size() - 1;
    }

    /**
     * Goes through each number in the array passed in and converts it to a numeral
     * @param numbers the numbers which you wish to convert to numerals
     * @return a concatenated string of numerals representing the numbers in the array passed in
     */
    private String exploreNumberSegment(ArrayList<Integer> numbers) {
        StringBuilder result = new StringBuilder();

        for (Integer numberChunk : numbers) {
            // Add the number to a string
            result.append(exploreDenominations(numberChunk));
        }

        return result.toString();
    }

    /**
     * Go through each denomination to find which denomination the number belongs to
     * @param number the number which is to be should have been split into chunks before it is passed in.
     * @return the string representing the number passed in
     */
    private String exploreDenominations(int number) {
        // Iterate over each denomination index
        for (int denominationIndex = 0; denominationIndex < denominations.size(); denominationIndex++) {
            // If the number we are analysing is smaller than this domination, then continue out
            int deductionValue = denominations.get(denominationIndex) - denominations.get(getNextDenominationDeductionIndex(denominationIndex));

            // If the number is not within the deduction range or the denomination range
            if (number < denominations.get(denominationIndex) && number < deductionValue) {
                continue;
            }

            int numTimesToWriteNumeral;

            // If the number is going to sit within the deduction range (i.e. a numeral sits within another numeral to
            // represent a subtraction
            if (number >= deductionValue && number < denominations.get(denominationIndex)) {
                // Then use the deduction value to work out how many times we should write the numeral
                numTimesToWriteNumeral = number / deductionValue;
            } else {
                // Otherwise use the denomination value to work out how many times we should write the numeral
                numTimesToWriteNumeral = number / denominations.get(denominationIndex);
            }

            // Turn the number into a numeral X amount of times
            String results = convertToNumerals(numTimesToWriteNumeral, denominationIndex, number);

            // We've turned this chunk into a roman numeral, can stop now
            if (!results.isEmpty())
            {
                return results;
            }
        }

        // Couldn't turn this into a numeral, return empty string
        return "";
    }

    /**
     * Convert an individual number into a numeral any number of times
     * @param numTimesToWriteNumeral the number of times which the numeral should be output
     * @param denominationIndex the index of the denomination used to represent the number
     * @param number the number which is to be turned into a numeral
     * @return A stringified numeral (can be two numerals to indicate a deduction)
     */
    private String convertToNumerals(int numTimesToWriteNumeral, int denominationIndex, int number) {
        StringBuilder result = new StringBuilder();

        // Iterate through each numeral which is to be written X amount of times
        for (int i = 0; i < numTimesToWriteNumeral; i++) {

            // If the current number is not directly divisible by the current denomination and we're not looking at the
            // smallest denomination
            if (denominationIndex < denominations.size() - 1 && number % denominations.get(denominationIndex) != 0) {

                // If we think we should append a subtraction numeral before the current numeral is printed
                if (shouldAppendDeduction(denominationIndex, number)) {
                    // Calculate the index of the numeral which should be subtracted
                    int deductionIndex = getNextDenominationDeductionIndex(denominationIndex);
                    // Append the subtraction numeral
                    result.append(numeralMap.get(denominations.get(deductionIndex)).getValue());
                    // Append the original numeral
                    result.append(numeralMap.get(denominations.get(denominationIndex)).getValue());
                } else {
                    // No need to subtract, just append the original numeral
                    result.append(numeralMap.get(denominations.get(denominationIndex)).getValue());
                }
            } else {
                // Append the original numeral
                result.append(numeralMap.get(denominations.get(denominationIndex)).getValue());
            }
        }

        return result.toString();
    }

    /**
     * Work out if we should subtract a numeral from the current numeral
     * @param denominationIndex the current denomination being analysed
     * @param number the number which is being converted to a numeral
     * @return true if we should deduct a numeral before writing the current numeral
     */
    private boolean shouldAppendDeduction(int denominationIndex, int number) {
        int deductionIndex = getNextDenominationDeductionIndex(denominationIndex);
        return number >= denominations.get(denominationIndex) - denominations.get(deductionIndex);
    }
}
