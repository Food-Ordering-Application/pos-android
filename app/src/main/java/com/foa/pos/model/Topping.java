package com.foa.pos.model;

import java.util.ArrayList;
import java.util.List;

public class Topping {
    String toppingId;
    String name;

    public Topping(String toppingId, String name) {
        this.toppingId = toppingId;
        this.name = name;
    }

    public String getToppingId() {
        return toppingId;
    }

    public void setToppingId(String toppingId) {
        this.toppingId = toppingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static List<Topping> getSampleList(){
        List<Topping> toppingList = new ArrayList<>();
        toppingList.add(new Topping("1","Topping name 1"));
        toppingList.add(new Topping("2","Topping name 2"));
        toppingList.add(new Topping("3","Topping name 3"));
        return toppingList;
    }
}
