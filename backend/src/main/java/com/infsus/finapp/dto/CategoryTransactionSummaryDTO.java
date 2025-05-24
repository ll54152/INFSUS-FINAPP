package com.infsus.finapp.dto;

public class CategoryTransactionSummaryDTO {

    private String categoryName;
    private double expenseAmount;
    private double incomeAmount;

    public CategoryTransactionSummaryDTO(String categoryName, double expenseAmount, double incomeAmount) {
        this.categoryName = categoryName;
        this.expenseAmount = expenseAmount;
        this.incomeAmount = incomeAmount;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public double getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(double expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public double getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(double incomeAmount) {
        this.incomeAmount = incomeAmount;
    }
}
