package com.test.pages;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"REGISTRATION", "MAKE", "MODEL"})
public class Car {

    public String REGISTRATION;
    public String MAKE;
    public String MODEL;


    public Car(String REGISTRATION, String MAKE, String MODEL) {
        this.REGISTRATION = REGISTRATION;
        this.MAKE = MAKE;
        this.MODEL = MODEL;

    }

    public Car() {
    }

    public String getREGISTRATION() {
        return REGISTRATION;
    }

    public void setREGISTRATION(String REGISTRATION) {
        this.REGISTRATION = REGISTRATION;
    }

    public String getMAKE() {
        return MAKE;
    }

    public void setMAKE(String MAKE) {
        this.MAKE = MAKE;
    }

    public String getMODEL() {
        return MODEL;
    }

    public void setMODEL(String MODEL) {
        this.MODEL = MODEL;
    }
}
