package com.caueobm.casahub.model;
import com.google.gson.annotations.SerializedName;

public class JwtAuthResponse {
    @SerializedName("accessToken") // Corresponde ao nome do campo no JSON de resposta
    private String accessToken;

    private String tokenType = "Bearer";

    // Getter
    public String getAccessToken() {
        return accessToken;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    public String getTokenType() {
        return tokenType;
    }
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}