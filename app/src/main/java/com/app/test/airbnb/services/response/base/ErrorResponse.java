package com.app.test.airbnb.services.response.base;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Juan on 09/03/2017.
 */
public class ErrorResponse implements Serializable {

    @SerializedName("code")
    public String code;
    @SerializedName("message")
    public String message;
}
