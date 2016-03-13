package com.example.amit.lookup;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by amit on 03-03-2016.
 */
public class Place  implements Serializable{

    private String name;
    private String category;
    private String open;
    private String address;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
