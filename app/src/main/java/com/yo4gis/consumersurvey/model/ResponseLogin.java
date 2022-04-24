package com.yo4gis.consumersurvey.model;

import com.google.gson.annotations.SerializedName;

public class ResponseLogin {
    @SerializedName("access_token")
    private String AccessToken;

    @SerializedName("access_type")
    private String TokenType;

    @SerializedName("expires_in")
    private String ExpiresIn;

    public String getAccessToken() {
        return AccessToken;
    }

    public void setAccessToken(String accessToken) {
        AccessToken = accessToken;
    }

    public String getTokenType() {
        return TokenType;
    }

    public void setTokenType(String tokenType) {
        TokenType = tokenType;
    }

    public String getExpiresIn() {
        return ExpiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        ExpiresIn = expiresIn;
    }
}
