package ru.algorithmist.jquant.engine;

/**
 * @author "Sergey Edunov"
 * @version 12/30/10
 */
public enum ValueStatus{
    OK("OK"),
    TEMPORARY_NOT_AVAILABLE("TNA"),
    NOT_AVAILABLE("NA");

    private String key;

    ValueStatus(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }
}
