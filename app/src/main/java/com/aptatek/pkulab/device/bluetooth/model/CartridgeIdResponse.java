package com.aptatek.pkulab.device.bluetooth.model;

public class CartridgeIdResponse {

    private String cartridgeId;

    public String getCartridgeId() {
        return cartridgeId;
    }

    public void setCartridgeId(String cartridgeId) {
        this.cartridgeId = cartridgeId;
    }

    @Override
    public String toString() {
        return "CartridgeIdResponse{" +
                "cartridgeId='" + cartridgeId + '\'' +
                '}';
    }
}
