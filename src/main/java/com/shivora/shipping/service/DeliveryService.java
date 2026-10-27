
package com.shivora.shipping.service;

import com.shivora.shipping.dto.CheckServiceabilityRequest;
import com.shivora.shipping.dto.CheckServiceabilityResponse;
import com.shivora.shipping.dto.GetDeliveryOptionsRequest;
import com.shivora.shipping.dto.GetDeliveryOptionsResponse;

public interface DeliveryService {

    CheckServiceabilityResponse checkServiceability(
            CheckServiceabilityRequest request);
    
    // API 2: Get available delivery options and charges
    GetDeliveryOptionsResponse getDeliveryOptions(
            GetDeliveryOptionsRequest request);

}
