
package com.shivora.shipping.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shivora.shipping.entity.ServiceablePincode;

@Repository
public interface ServiceablePincodeRepository
        extends JpaRepository<ServiceablePincode, Long> {

    Optional<ServiceablePincode> findByPincodeAndActiveTrue(String pincode);
}
