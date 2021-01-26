package com.example.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: demo
 * @description: for response
 * @author: louweiwei
 * @create: 2021-01-24 17:07
 */
public class ResponseResult<T> implements Serializable {

    private static final long serialVersionUID = -5634425573690458952L;

    public ResponseResult() {
        this.status = -1;
        this.succeeded = false;
        this.message = "access fail";
        this.dataForShow = new ArrayList<>();
    }

    public ResponseResult(List<T> dataForShow) {
        this.status = 0;
        this.succeeded = true;
        this.message = "access succeed";
        this.dataForShow = dataForShow;
    }

    public ResponseResult(Integer status, Boolean succeeded, List<T> dataForShow) {
        this.status = status;
        this.succeeded = succeeded;
        this.dataForShow = dataForShow;
    }

    /**
     *  response code
     */
    private Integer status;

    /**
     *  if this request is a success or not
     */
    private Boolean succeeded;

    /**
     *  message
     */
    private String message;

    /**
     *  data for show
     */
    List<T> dataForShow;


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getSucceeded() {
        return succeeded;
    }

    public void setSucceeded(Boolean succeeded) {
        this.succeeded = succeeded;
    }

    public List<T> getDataForShow() {
        return dataForShow;
    }

    public void setDataForShow(List<T> dataForShow) {
        this.dataForShow = dataForShow;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
