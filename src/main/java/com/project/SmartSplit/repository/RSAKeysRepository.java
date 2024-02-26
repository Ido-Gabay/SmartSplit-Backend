package com.project.SmartSplit.repository;

import com.project.SmartSplit.entity.RSAKeysEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RSAKeysRepository extends JpaRepository<RSAKeysEntity, Integer> {

    // Add any custom methods you might need
    // Example: Find the latest key
    RSAKeysEntity findTopByOrderByKidDesc();

}