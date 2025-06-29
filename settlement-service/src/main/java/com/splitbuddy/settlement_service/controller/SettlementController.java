package com.splitbuddy.settlement_service.controller;

import com.splitbuddy.settlement_service.dto.UserExpenseSummary;
import com.splitbuddy.settlement_service.dto.UserExpenseDetailedDTO;
import com.splitbuddy.settlement_service.entity.Expense;
import com.splitbuddy.settlement_service.repository.ExpenseRepository;
import com.splitbuddy.settlement_service.service.SettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/settlements")
public class SettlementController {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private SettlementService settlementService;

    @PostMapping("/expenses")
    public Expense addExpense(@RequestBody Expense expense) {
        return expenseRepository.save(expense);
    }

    @GetMapping("/group/{groupId}/expenses")
    public List<Expense> getExpensesForGroup(@PathVariable Long groupId) {
        return expenseRepository.findByGroupId(groupId);
    }

    // 🔹 NEW: Breakdown API
    @GetMapping("/group/{groupId}/breakdown")
    public List<UserExpenseSummary> getUserBreakdown(@PathVariable Long groupId) {
        return settlementService.calculateUserBreakdown(groupId);
    }

    // 🔹 NEW: Debt API
    @GetMapping("/group/{groupId}/debts")
    public Map<Long, Map<Long, Double>> getUserDebts(@PathVariable Long groupId) {
        return settlementService.calculateDebts(groupId);
    }

    @GetMapping("/group/{groupId}/summary")
public List<UserExpenseDetailedDTO> getGroupSummary(@PathVariable Long groupId) {
    return settlementService.getUserExpenseSummary(groupId);
}

}
