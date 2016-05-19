package com.example.bahl.Model;

/**
 * Created by bahl on 5/5/2016.
 */
//import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("LOVRow")
    @Expose
    private String LOVRow;
    @SerializedName("LOVValue")
    @Expose
    private String LOVValue;

    /**
     * @return The LOVRow
     */
    public String getLOVRow() {
        return LOVRow;
    }

    /**
     * @param LOVRow The LOVRow
     */
    public void setLOVRow(String LOVRow) {
        this.LOVRow = LOVRow;
    }

    /**
     * @return The LOVValue
     */
    public String getLOVValue() {
        return LOVValue;
    }

    /**
     * @param LOVValue The LOVValue
     */
    public void setLOVValue(String LOVValue) {
        this.LOVValue = LOVValue;
    }
}