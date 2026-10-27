
package com.shivora.shipping.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shivora.shipping.dto.ApiResponse;
import com.shivora.shipping.dto.CheckServiceabilityRequest;
import com.shivora.shipping.dto.CheckServiceabilityResponse;
import com.shivora.shipping.service.DeliveryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping("/check-serviceability")
    public ResponseEntity<ApiResponse<CheckServiceabilityResponse>>
            checkServiceability(
                    @Valid @RequestBody CheckServiceabilityRequest request) {

        CheckServiceabilityResponse data =
                deliveryService.checkServiceability(request);

        ApiResponse<CheckServiceabilityResponse> response =
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Serviceability checked successfully",
                        data);

        return ResponseEntity.ok(response);
    }
}
