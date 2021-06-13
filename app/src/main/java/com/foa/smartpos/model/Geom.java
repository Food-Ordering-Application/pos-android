package com.foa.smartpos.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Geom {
    @SerializedName("type")
    private String type;
    @SerializedName("coordinates")
    private List<Float> coordinates;
}
