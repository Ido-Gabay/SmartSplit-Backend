package com.project.SmartSplit.repository;

import com.project.SmartSplit.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
}
