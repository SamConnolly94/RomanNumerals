package com.bbc.numerals.datastructures;

public class Numeral {
    private boolean isDeductible;
    private String value;

    // DI constructor
    public Numeral(String value, boolean isDeductible) {
        this.value = value;
        this.isDeductible = isDeductible;
    }

    public boolean isDeductible() {
        return isDeductible;
    }

    public void setDeductible(boolean deductible) {
        isDeductible = deductible;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
