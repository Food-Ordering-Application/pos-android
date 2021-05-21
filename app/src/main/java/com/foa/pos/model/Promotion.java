package com.foa.pos.model;

import com.foa.pos.utils.Helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Promotion {
    private String promotionId;
    private String code;
    private Date exp;
    private int type;
    private float value;
    private String desc;
    private int minOrderValue;
    private int rest;
    private boolean isSelected;

    public Promotion(String promotionId, String code, Date exp, int type, float value, String desc, int minOrderValue, int rest, boolean isSelected) {
        this.promotionId = promotionId;
        this.code = code;
        this.exp = exp;
        this.type = type;
        this.value = value;
        this.desc = desc;
        this.minOrderValue = minOrderValue;
        this.rest=rest;
        this.isSelected =isSelected;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getExp() {
        return Helper.dateFormat.format(this.exp);
    }

    public void setExp(Date exp) {
        this.exp = exp;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getMinOrderValue() {
        return minOrderValue;
    }

    public void setMinOrderValue(int minOrderValue) {
        this.minOrderValue = minOrderValue;
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public static List<Promotion> getPromotionListSample(){
        List<Promotion> list = new ArrayList<>();
        list.add(new Promotion("1","NEW20A12", Helper.plusDays(new Date(),10),1,10,"Giam 10% don hang tren 100k",100000,100,false));
        list.add(new Promotion("2","NEW20A12", Helper.plusDays(new Date(),10),1,10,"Giam 10% don hang tren 100k",100000,100,false));
        list.add(new Promotion("3","NEW20A12", Helper.plusDays(new Date(),10),1,10,"Giam 10% don hang tren 100k",100000,100,false));
        list.add(new Promotion("4","NEW20A12", Helper.plusDays(new Date(),10),1,10,"Giam 10% don hang tren 100k",100000,100,false));
        list.add(new Promotion("5","NEW20A12", Helper.plusDays(new Date(),10),1,10,"Giam 10% don hang tren 100k",100000,100,false));
        list.add(new Promotion("6","NEW20A12", Helper.plusDays(new Date(),10),1,10,"Giam 10% don hang tren 100k",100000,100,false));
        return list;
    }
}
