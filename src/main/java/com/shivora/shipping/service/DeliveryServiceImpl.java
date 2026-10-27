
package com.shivora.shipping.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.shivora.shipping.dto.DeliveryOptionDTO;
import com.shivora.shipping.dto.GetDeliveryOptionsRequest;
import com.shivora.shipping.dto.GetDeliveryOptionsResponse;
import com.shivora.shipping.entity.DeliveryRule;
import com.shivora.shipping.entity.ServiceablePincode;
import com.shivora.shipping.repository.DeliveryRuleRepository;
import com.shivora.shipping.repository.ServiceablePincodeRepository;
import java.util.List;

import org.springframework.stereotype.Service;

import com.shivora.shipping.dto.CheckServiceabilityRequest;
import com.shivora.shipping.dto.CheckServiceabilityResponse;
import com.shivora.shipping.dto.GetDeliveryOptionsRequest;
import com.shivora.shipping.dto.GetDeliveryOptionsResponse;
import com.shivora.shipping.entity.ServiceablePincode;
import com.shivora.shipping.repository.ServiceablePincodeRepository;
import com.shivora.shipping.service.DeliveryService;


@Service
public class DeliveryServiceImpl implements DeliveryService {

   
    private final ServiceablePincodeRepository serviceablePincodeRepository;
    private final DeliveryRuleRepository deliveryRuleRepository;

    public DeliveryServiceImpl(
            ServiceablePincodeRepository serviceablePincodeRepository,
            DeliveryRuleRepository deliveryRuleRepository) {

        this.serviceablePincodeRepository = serviceablePincodeRepository;
        this.deliveryRuleRepository = deliveryRuleRepository;
    }

    
    @Override
    public CheckServiceabilityResponse checkServiceability(
            CheckServiceabilityRequest request) {

        String pincode = request.getPincode();

        CheckServiceabilityResponse response =
                new CheckServiceabilityResponse();

        response.setPincode(pincode);

        ServiceablePincode serviceablePincode =
                serviceablePincodeRepository
                        .findByPincodeAndActiveTrue(pincode)
                        .orElse(null);

        if (serviceablePincode == null) {
            response.setServiceable(false);
            response.setReason(
                    "Delivery is not available for this pincode");
            response.setCodAvailable(false);
            response.setDeliveryOptions(List.of());
            return response;
        }

        response.setServiceable(true);
        response.setCity(serviceablePincode.getCity());
        response.setState(serviceablePincode.getState());

        // Use the getter defined in your entity.
        response.setCodAvailable(
                Boolean.TRUE.equals(serviceablePincode.getCodAvailable()));

        response.setDeliveryOptions(List.of("STANDARD"));

        if (serviceablePincode.getZone() != null) {
            response.setZoneCode(
                    serviceablePincode.getZone().getZoneCode());
        }

        return response;
    }
    

@Override
public GetDeliveryOptionsResponse getDeliveryOptions(
        GetDeliveryOptionsRequest request) {

    // 1. Find the pincode
    ServiceablePincode pincode = serviceablePincodeRepository
            .findByPincodeAndActiveTrue(request.getPincode())
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "PINCODE_NOT_SERVICEABLE"));

    // 2. Find active rules for the pincode's zone
    LocalDateTime now = LocalDateTime.now();

    List<DeliveryRule> rules = deliveryRuleRepository
            .findActiveRulesByZone(pincode.getZone().getId(), now);

    if (rules.isEmpty()) {
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "DELIVERY_RULE_NOT_FOUND");
    }

    // 3. Calculate the weight in kilograms
    BigDecimal weightKg = BigDecimal.valueOf(
            request.getTotalWeightGrams())
            .divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);

    List<DeliveryOptionDTO> options = new ArrayList<>();

    // 4. Build an option for each active delivery rule
    for (DeliveryRule rule : rules) {

        BigDecimal baseCharge = rule.getBaseCharge() == null
                ? BigDecimal.ZERO : rule.getBaseCharge();

        BigDecimal perKgCharge = rule.getPerKgCharge() == null
                ? BigDecimal.ZERO : rule.getPerKgCharge();

        BigDecimal orderValue = request.getOrderValue();

        BigDecimal subtotal = baseCharge.add(
                perKgCharge.multiply(weightKg));

        // Apply free delivery when the configured threshold is reached
        boolean freeDeliveryApplied =
                rule.getFreeAboveValue() != null
                && orderValue.compareTo(rule.getFreeAboveValue()) >= 0;

        if (freeDeliveryApplied) {
            subtotal = BigDecimal.ZERO;
        }

        // Add tax to the payable delivery charge
        BigDecimal taxPercent = rule.getTaxPercent() == null
                ? BigDecimal.ZERO : rule.getTaxPercent();

        BigDecimal tax = subtotal.multiply(taxPercent)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        BigDecimal finalCharge = subtotal.add(tax)
                .setScale(2, RoundingMode.HALF_UP);

        // Calculate estimated dates from database ETA values
        int minDays = rule.getEtaMinDays() == null
                ? 0 : rule.getEtaMinDays();

        int maxDays = rule.getEtaMaxDays() == null
                ? minDays : rule.getEtaMaxDays();

        LocalDate today = LocalDate.now();
        LocalDate estimatedMinDate = today.plusDays(minDays);
        LocalDate estimatedMaxDate = today.plusDays(maxDays);

        String code = rule.getDeliveryOption();

        String name;
        if ("STANDARD".equalsIgnoreCase(code)) {
            name = "Standard Delivery";
        } else if ("PRIORITY".equalsIgnoreCase(code)) {
            name = "Priority Delivery";
        } else {
            name = code;
        }

        DeliveryOptionDTO option = new DeliveryOptionDTO(
                code,
                name,
                finalCharge,
                freeDeliveryApplied,
                minDays,
                maxDays,
                estimatedMinDate,
                estimatedMaxDate
        );

        options.add(option);
    }

    // 5. Return the response
    return new GetDeliveryOptionsResponse(options);
}

}