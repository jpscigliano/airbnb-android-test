package com.app.test.airbnb.services.response.base;

import com.app.test.airbnb.services.response.base.ErrorResponse;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
/**
 * Created by Juan on 09/03/2017.
 */
public class StatusResponse implements Serializable {

    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_ERROR = "error";

    @SerializedName("status")
    public String status;


    @SerializedName("error")
    public ErrorResponse error;


    public boolean isSuccess(){
        return STATUS_SUCCESS.equals(status);
    }
}
