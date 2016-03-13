package com.example.amit.lookup;

/**
 * Created by amit on 12-03-2016.
 */
public class PlaceData {

    String name;
    String info;

    public PlaceData(String name, String info){
        this.name = name;
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
