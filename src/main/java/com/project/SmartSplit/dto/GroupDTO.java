package com.project.SmartSplit.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class GroupDTO {

    private Long id;

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotEmpty(message = "Purpose cannot be empty")
    private String purpose;

    @NotNull(message = "Open Date cannot be empty")
    private Date openDate;

    @NotNull(message = "Money Amount cannot be empty")
    private int moneyAmount;

    private List<Long> userIds;

    @NotNull(message = "User ID cannot be empty")
    private Long userId;

    @NotNull(message = "Default Card ID cannot be empty")
    private Long defaultCardId;

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

}
