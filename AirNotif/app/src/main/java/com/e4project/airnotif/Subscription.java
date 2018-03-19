package com.e4project.airnotif;

public class Subscription {

    private String name;

    Subscription(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
