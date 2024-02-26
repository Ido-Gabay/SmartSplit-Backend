package com.project.SmartSplit.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class CardDTO {
    private Long id;

    @NotEmpty(message = "Card Number cannot be empty")
    private String cardNumbers;

    @NotEmpty(message = "Card Name cannot be empty")
    private String cardName;

    @NotEmpty(message = "CVV cannot be empty")
    private String cvv;

    @NotNull(message = "Card Year cannot be empty")
    private int cardYear;

    @NotNull(message = "Card Month cannot be empty")
    private int cardMonth;

    @NotNull(message = "Balance cannot be empty")
    private int balance;

    private Long userId;
}
