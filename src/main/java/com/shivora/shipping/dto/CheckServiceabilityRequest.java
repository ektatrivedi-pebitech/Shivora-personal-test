
package com.shivora.shipping.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CheckServiceabilityRequest {

    @NotBlank(message = "Pincode is required")
    @Pattern(regexp = "^[0-9]{6}$",
             message = "Pincode must contain exactly 6 digits")
    private String pincode;

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
