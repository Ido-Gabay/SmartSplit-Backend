package com.project.SmartSplit.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class PaymentDTO {
    private Long id;

    @NotNull(message = "Price cannot be empty")
    private int price;

    @NotEmpty(message = "place cannot be empty")
    private String place;

    private boolean paySuccess;

    private Long groupId;
    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
