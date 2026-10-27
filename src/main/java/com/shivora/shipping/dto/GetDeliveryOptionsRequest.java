
package com.shivora.shipping.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

public class GetDeliveryOptionsRequest {

    @NotBlank(message = "Pincode is required")
    @Pattern(regexp = "^[0-9]{6}$",
             message = "Pincode must contain exactly 6 digits")
    private String pincode;

    @NotNull(message = "Order value is required")
    @DecimalMin(value = "0.00",
                message = "Order value cannot be negative")
    private BigDecimal orderValue;

    @NotNull(message = "Total weight is required")
    @PositiveOrZero(message = "Total weight cannot be negative")
    private Integer totalWeightGrams;

    public GetDeliveryOptionsRequest() {
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public BigDecimal getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(BigDecimal orderValue) {
        this.orderValue = orderValue;
    }

    public Integer getTotalWeightGrams() {
        return totalWeightGrams;
    }

    public void setTotalWeightGrams(Integer totalWeightGrams) {
        this.totalWeightGrams = totalWeightGrams;
    }
}
