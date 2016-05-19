package com.example.bahl.Model;

/**
 * Created by bahl on 5/2/2016.
 */
/*public class LOVModel {
   public int  SpinnerId;
   public String LOVName;

    public LOVModel(int spinnerId, String lovName){
        SpinnerId = spinnerId;
        LOVName =lovName;
    }
}*/


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class LOVModel {

    public int SpinnerId;
    public String LOVName;
    @SerializedName("data")
    @Expose
    private List<Datum> data = new ArrayList<Datum>();

    public LOVModel(int spinnerId, String lovName) {
        SpinnerId = spinnerId;
        LOVName = lovName;
    }

    /**
     * @return The data
     */
    public List<Datum> getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(List<Datum> data) {
        this.data = data;
    }


}