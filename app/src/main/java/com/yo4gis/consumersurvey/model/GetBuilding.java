package com.yo4gis.consumersurvey.model;

import com.google.gson.annotations.SerializedName;

public class GetBuilding {

    @SerializedName("x")
    private String Xvalue;

    @SerializedName("y")
    private String Yvalue;

    public String getXvalue() {
        return Xvalue;
    }

    public void setXvalue(String xvalue) {
        Xvalue = xvalue;
    }

    public String getYvalue() {
        return Yvalue;
    }

    public void setYvalue(String yvalue) {
        Yvalue = yvalue;
    }
}
