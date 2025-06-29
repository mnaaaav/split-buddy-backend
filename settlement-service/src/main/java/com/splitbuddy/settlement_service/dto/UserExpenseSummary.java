package com.splitbuddy.settlement_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserExpenseSummary {
    private Long userId;
    private double amountPaid;
    private double fairShare;
    private double balance;

    public double getPaid() {
    return amountPaid;
}

}
