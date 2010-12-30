package ru.algorithmist.jquant.engine;

import java.util.Locale;

/**
 * @author "Sergey Edunov"
 * @version 12/30/10
 */
public class Value {

    private double value;
    private ValueStatus status;

    public static final Value NA = new Value(ValueStatus.NOT_AVAILABLE);
    public static final Value TNA = new Value(ValueStatus.TEMPORARY_NOT_AVAILABLE);

    public Value(double value) {
        this.value = value;
        status = ValueStatus.OK;
    }

    public double getValue() {
        return value;
    }

    public ValueStatus getStatus() {
        return status;
    }

    private Value(ValueStatus status) {
        this.status = status;
    }

    public boolean  isOK(){
        return status == ValueStatus.OK;
    }

    public boolean isNA(){
        return status == ValueStatus.NOT_AVAILABLE;
    }

    public boolean isTNA(){
        return status == ValueStatus.TEMPORARY_NOT_AVAILABLE;
    }

    public static Value from(String value){
        if ("NA".equals(value)){
            return NA;
        }else if ("TNA".equals(value)){
            return TNA;
        }else{
            return new Value(Double.parseDouble(value));
        }
    }

    public String toString(){
        if (status == ValueStatus.OK){
            return String.format(Locale.US, "%10.4f", value);
        } else if (status == ValueStatus.NOT_AVAILABLE){
            return "NA";
        } else {
            return "TNA";
        }
    }
}


