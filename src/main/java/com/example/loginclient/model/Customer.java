package com.example.loginclient.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true) 
public class Customer {
    @JsonProperty("CUSTOMER_ID")
    private int id;

    @JsonProperty("CUSTOMER_NAME")
    private String name;

    @JsonProperty("CUSTOMER_EMAIL")
    private String email;

    @JsonProperty("TELEPHONE")
    private String telephone;

    @JsonProperty("REG_DTTM")
    private String registeredDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(String registeredDate) {
        this.registeredDate = registeredDate;
    }
}
