package com.splitbuddy.settlement_service.service;

import com.splitbuddy.settlement_service.dto.UserExpenseSummary;
import com.splitbuddy.settlement_service.dto.UserExpenseDetailedDTO;
import com.splitbuddy.settlement_service.dto.AppUserDTO;
import com.splitbuddy.settlement_service.entity.Expense;
import com.splitbuddy.settlement_service.repository.ExpenseRepository;
import com.splitbuddy.settlement_service.client.GroupServiceClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SettlementService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private GroupServiceClient groupServiceClient;

    // ✅ Method 1: Breakdown — updated to include all group members
    public List<UserExpenseSummary> calculateUserBreakdown(Long groupId) {
        List<Expense> expenses = expenseRepository.findByGroupId(groupId);
        if (expenses.isEmpty()) return new ArrayList<>();

        double total = 0;
        Map<Long, Double> userPaidMap = new HashMap<>();

        // Calculate total and how much each user paid
        for (Expense exp : expenses) {
            total += exp.getAmount();
            userPaidMap.put(
                exp.getPaidByUserId(),
                userPaidMap.getOrDefault(exp.getPaidByUserId(), 0.0) + exp.getAmount()
            );
        }

        // ✅ Get all userIds in the group — even if they didn't pay
        List<AppUserDTO> groupMembers = groupServiceClient.getMembersOfGroup(groupId);
        Set<Long> userIds = new HashSet<>();
        for (AppUserDTO user : groupMembers) {
            userIds.add(user.getId());
        }

        int userCount = userIds.size();
        double fairShare = total / userCount;

        List<UserExpenseSummary> summaryList = new ArrayList<>();
        for (Long userId : userIds) {
            double paid = userPaidMap.getOrDefault(userId, 0.0);
            double balance = paid - fairShare;
            summaryList.add(new UserExpenseSummary(userId, paid, fairShare, balance));
        }

        return summaryList;
    }

    // ✅ Method 2: Debt Calculation — untouched
    public Map<Long, Map<Long, Double>> calculateDebts(Long groupId) {
        List<Expense> expenses = expenseRepository.findByGroupId(groupId);

        Map<Long, Double> userTotalPaid = new HashMap<>();
        double totalAmount = 0.0;

        for (Expense expense : expenses) {
            userTotalPaid.put(
                expense.getPaidByUserId(),
                userTotalPaid.getOrDefault(expense.getPaidByUserId(), 0.0) + expense.getAmount()
            );
            totalAmount += expense.getAmount();
        }

        // ✅ Same as above, fetch full list of users
        List<AppUserDTO> groupMembers = groupServiceClient.getMembersOfGroup(groupId);
        Set<Long> userIds = new HashSet<>();
        for (AppUserDTO user : groupMembers) {
            userIds.add(user.getId());
        }

        int userCount = userIds.size();
        double perUserShare = totalAmount / userCount;

        Map<Long, Double> userNetBalance = new HashMap<>();
        for (Long userId : userIds) {
            double paid = userTotalPaid.getOrDefault(userId, 0.0);
            userNetBalance.put(userId, paid - perUserShare);
        }

        Map<Long, Map<Long, Double>> debts = new HashMap<>();
        List<Map.Entry<Long, Double>> creditors = new ArrayList<>();
        List<Map.Entry<Long, Double>> debtors = new ArrayList<>();

        for (Map.Entry<Long, Double> entry : userNetBalance.entrySet()) {
            if (entry.getValue() > 0) {
                creditors.add(entry);
            } else if (entry.getValue() < 0) {
                debtors.add(entry);
            }
        }

        int i = 0, j = 0;
        while (i < debtors.size() && j < creditors.size()) {
            Long debtor = debtors.get(i).getKey();
            Long creditor = creditors.get(j).getKey();

            double debtAmount = Math.min(
                -debtors.get(i).getValue(),
                creditors.get(j).getValue()
            );

            debts.putIfAbsent(debtor, new HashMap<>());
            debts.get(debtor).put(creditor, debtAmount);

            debtors.get(i).setValue(debtors.get(i).getValue() + debtAmount);
            creditors.get(j).setValue(creditors.get(j).getValue() - debtAmount);

            if (Math.abs(debtors.get(i).getValue()) < 0.01) i++;
            if (Math.abs(creditors.get(j).getValue()) < 0.01) j++;
        }

        return debts;
    }

    // ✅ Method 3: Return detailed summary
    public List<UserExpenseDetailedDTO> getUserExpenseSummary(Long groupId) {
        List<UserExpenseSummary> summaryList = calculateUserBreakdown(groupId);

        List<UserExpenseDetailedDTO> result = new ArrayList<>();
        for (UserExpenseSummary summary : summaryList) {
            AppUserDTO user = groupServiceClient.getUserById(summary.getUserId());

            result.add(new UserExpenseDetailedDTO(
                summary.getUserId(),
                user.getName(),
                user.getEmail(),
                summary.getPaid(),
                summary.getFairShare(),
                summary.getBalance()
            ));
        }

        return result;
    }
}
