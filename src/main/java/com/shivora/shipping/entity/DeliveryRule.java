
package com.shivora.shipping.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "delivery_rules", schema = "\"UATPebiShop\"")
public class DeliveryRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private DeliveryZone zone;

    @Column(name = "delivery_option", length = 20)
    private String deliveryOption;

    @Column(name = "base_charge", precision = 18, scale = 2)
    private BigDecimal baseCharge;

    @Column(name = "per_kg_charge", precision = 18, scale = 2)
    private BigDecimal perKgCharge;

    @Column(name = "free_above_value", precision = 18, scale = 2)
    private BigDecimal freeAboveValue;

    @Column(name = "cod_charge", precision = 18, scale = 2)
    private BigDecimal codCharge;

    @Column(name = "tax_percent", precision = 18, scale = 2)
    private BigDecimal taxPercent;

    @Column(name = "eta_min_days")
    private Integer etaMinDays;

    @Column(name = "eta_max_days")
    private Integer etaMaxDays;

    @Column(name = "cutoff_time")
    private LocalTime cutoffTime;

    @Column(name = "valid_from", nullable = false)
    private LocalDateTime validFrom;

    @Column(name = "valid_to")
    private LocalDateTime validTo;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DeliveryZone getZone() {
        return zone;
    }

    public void setZone(DeliveryZone zone) {
        this.zone = zone;
    }

    public String getDeliveryOption() {
        return deliveryOption;
    }

    public void setDeliveryOption(String deliveryOption) {
        this.deliveryOption = deliveryOption;
    }

    public BigDecimal getBaseCharge() {
        return baseCharge;
    }

    public void setBaseCharge(BigDecimal baseCharge) {
        this.baseCharge = baseCharge;
    }

    public BigDecimal getPerKgCharge() {
        return perKgCharge;
    }

    public void setPerKgCharge(BigDecimal perKgCharge) {
        this.perKgCharge = perKgCharge;
    }

    public BigDecimal getFreeAboveValue() {
        return freeAboveValue;
    }

    public void setFreeAboveValue(BigDecimal freeAboveValue) {
        this.freeAboveValue = freeAboveValue;
    }

    public BigDecimal getCodCharge() {
        return codCharge;
    }

    public void setCodCharge(BigDecimal codCharge) {
        this.codCharge = codCharge;
    }

    public BigDecimal getTaxPercent() {
        return taxPercent;
    }

    public void setTaxPercent(BigDecimal taxPercent) {
        this.taxPercent = taxPercent;
    }

    public Integer getEtaMinDays() {
        return etaMinDays;
    }

    public void setEtaMinDays(Integer etaMinDays) {
        this.etaMinDays = etaMinDays;
    }

    public Integer getEtaMaxDays() {
        return etaMaxDays;
    }

    public void setEtaMaxDays(Integer etaMaxDays) {
        this.etaMaxDays = etaMaxDays;
    }

    public LocalTime getCutoffTime() {
        return cutoffTime;
    }

    public void setCutoffTime(LocalTime cutoffTime) {
        this.cutoffTime = cutoffTime;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDateTime getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDateTime validTo) {
        this.validTo = validTo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
