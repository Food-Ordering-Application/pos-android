package com.foa.smartpos.network.response;

import com.google.gson.annotations.SerializedName;

public class VerifyAppResponse {
    @SerializedName( "statusCode" )
    private int status;
    @SerializedName( "message" )
    private String message;
    @SerializedName( "data" )
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data{
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
