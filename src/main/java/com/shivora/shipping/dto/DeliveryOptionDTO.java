
package com.shivora.shipping.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DeliveryOptionDTO {

    private String code;
    private String name;
    private BigDecimal charge;
    private boolean freeDeliveryApplied;
    private Integer minDays;
    private Integer maxDays;
    private LocalDate estimatedMinDate;
    private LocalDate estimatedMaxDate;

    public DeliveryOptionDTO() {
    }

    public DeliveryOptionDTO(
            String code,
            String name,
            BigDecimal charge,
            boolean freeDeliveryApplied,
            Integer minDays,
            Integer maxDays,
            LocalDate estimatedMinDate,
            LocalDate estimatedMaxDate) {
        this.code = code;
        this.name = name;
        this.charge = charge;
        this.freeDeliveryApplied = freeDeliveryApplied;
        this.minDays = minDays;
        this.maxDays = maxDays;
        this.estimatedMinDate = estimatedMinDate;
        this.estimatedMaxDate = estimatedMaxDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getCharge() {
        return charge;
    }

    public void setCharge(BigDecimal charge) {
        this.charge = charge;
    }

    public boolean isFreeDeliveryApplied() {
        return freeDeliveryApplied;
    }

    public void setFreeDeliveryApplied(boolean freeDeliveryApplied) {
        this.freeDeliveryApplied = freeDeliveryApplied;
    }

    public Integer getMinDays() {
        return minDays;
    }

    public void setMinDays(Integer minDays) {
        this.minDays = minDays;
    }

    public Integer getMaxDays() {
        return maxDays;
    }

    public void setMaxDays(Integer maxDays) {
        this.maxDays = maxDays;
    }

    public LocalDate getEstimatedMinDate() {
        return estimatedMinDate;
    }

    public void setEstimatedMinDate(LocalDate estimatedMinDate) {
        this.estimatedMinDate = estimatedMinDate;
    }

    public LocalDate getEstimatedMaxDate() {
        return estimatedMaxDate;
    }

    public void setEstimatedMaxDate(LocalDate estimatedMaxDate) {
        this.estimatedMaxDate = estimatedMaxDate;
    }
}
