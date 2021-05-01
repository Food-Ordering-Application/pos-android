package com.foa.pos.network.response;

import com.foa.pos.model.User;
import com.google.gson.annotations.SerializedName;

public class VerifyAppResponse {
    @SerializedName( "statusCode" )
    private int status;
    @SerializedName( "message" )
    private String message;
    @SerializedName( "data" )
    private LoginResponse.Data data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LoginResponse.Data getData() {
        return data;
    }

    public void setData(LoginResponse.Data data) {
        this.data = data;
    }

    static class Data{
        @SerializedName( "restaurantId" )
        String restaurantId;
        @SerializedName("merchantId")
        String merchantId;

        public Data(String restaurantId, String merchantId) {
            this.restaurantId = restaurantId;
            this.merchantId = merchantId;
        }

        public String getRestaurantId() {
            return restaurantId;
        }

        public void setRestaurantId(String restaurantId) {
            this.restaurantId = restaurantId;
        }

        public String getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(String merchantId) {
            this.merchantId = merchantId;
        }
    }
}
