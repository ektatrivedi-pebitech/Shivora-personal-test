
package com.shivora.shipping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shivora.shipping.entity.DeliveryZone;

@Repository
public interface DeliveryZoneRepository
        extends JpaRepository<DeliveryZone, Long> {

}
