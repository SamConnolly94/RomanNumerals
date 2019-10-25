package com.bbc.numerals;

public class Numeral {
    private boolean isDeductable;
    private String value;

    Numeral(String value, boolean isDeductable) {
        this.value = value;
        this.isDeductable = isDeductable;
    }


    public boolean isDeductable() {
        return isDeductable;
    }

    public void setDeductable(boolean deductable) {
        isDeductable = deductable;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
