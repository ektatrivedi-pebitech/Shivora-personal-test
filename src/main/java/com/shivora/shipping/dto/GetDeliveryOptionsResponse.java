
package com.shivora.shipping.dto;

import java.util.ArrayList;
import java.util.List;

public class GetDeliveryOptionsResponse {

    private List<DeliveryOptionDTO> options = new ArrayList<>();

    public GetDeliveryOptionsResponse() {
    }

    public GetDeliveryOptionsResponse(List<DeliveryOptionDTO> options) {
        this.options = options;
    }

    public List<DeliveryOptionDTO> getOptions() {
        return options;
    }

    public void setOptions(List<DeliveryOptionDTO> options) {
        this.options = options;
    }
}
