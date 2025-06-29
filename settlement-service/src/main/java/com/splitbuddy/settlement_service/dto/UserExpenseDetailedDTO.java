package com.splitbuddy.settlement_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserExpenseDetailedDTO {
    private Long userId;
    private String name;
    private String email;
    private double paid;
    private double fairShare;
    private double balance;
}
