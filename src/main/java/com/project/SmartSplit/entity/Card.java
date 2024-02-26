package com.project.SmartSplit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_card", nullable = false)
    private Long id;

    @Column(nullable = false, length = 64, unique = true)
    private String cardNumbers;

    @Column(nullable = false, length = 64)
    private String cardName;

    @Column(nullable = false, length = 64)
    private String cvv;

    @Column(nullable = false)
    private int cardYear;

    @Column(nullable = false)
    private int cardMonth;

    @Column(nullable = false)
    private int balance;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
