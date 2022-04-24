package com.yo4gis.consumersurvey.listeners;

public interface CommonResponseNavigator {
    public void onSuccess();
    public void onSaveconsumer();
    public void onEmpty(String Msg);
    public void onFailure(String Msg);
}
