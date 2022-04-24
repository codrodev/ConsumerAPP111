package com.yo4gis.consumersurvey.model;

import java.util.List;

public class ResponseAssignedTaskList {
    private List<AssignedTask> Data;

    private String message;

    private String status;

    public List<AssignedTask> getData ()
    {
        return Data;
    }

    public void setData (List<AssignedTask> Data)
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
