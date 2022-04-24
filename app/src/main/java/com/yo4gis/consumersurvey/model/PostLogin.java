package com.yo4gis.consumersurvey.model;

import com.google.gson.annotations.SerializedName;

public class PostLogin {

    @SerializedName("username")
    private String Username;

    @SerializedName("password")
    private String Password;

    @SerializedName("grant_type")
    private String GrantType;

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getUsername() {
        return Username;
    }

    public String getPassword() {
        return Password;
    }

    public String getGrantType() {
        return GrantType;
    }

    public void setGrantType(String grantType) {
        GrantType = grantType;
    }
}
