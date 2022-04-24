package com.yo4gis.consumersurvey.listeners;

public interface PollResponseNavigator {
    public void onSuccess();
    public void onPollListSuccess();
    public void onNINIdSuccess();
    public void onSectionListSuccess();
    public void onEmpty(String Msg);
    public void onFailure(String Msg);
}
