package com.poc.merge_poc.repository;

import com.poc.merge_poc.entity.ClientToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientTokenRepository extends JpaRepository<ClientToken, Long> {
    ClientToken findByClientId(String clientId);
}
