package com.yo4gis.consumersurvey.model;

public class ResponseConsumer {
    private String Data;

    private String message;

    private String status;

    public String getData ()
    {
        return Data;
    }

    public void setData (String Data)
    {
        this.Data = Data;
    }

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }
}
