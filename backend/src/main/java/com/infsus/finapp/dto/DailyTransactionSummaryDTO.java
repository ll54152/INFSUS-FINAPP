package com.infsus.finapp.dto;

import java.util.Date;

public class DailyTransactionSummaryDTO {
    private Date date;
    private double incomeCount;
    private double expenseCount;

    public DailyTransactionSummaryDTO(Date date, double incomeCount, double expenseCount) {
        this.date = date;
        this.incomeCount = incomeCount;
        this.expenseCount = expenseCount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getIncomeCount() {
        return incomeCount;
    }

    public void setIncomeCount(double incomeCount) {
        this.incomeCount = incomeCount;
    }

    public double getExpenseCount() {
        return expenseCount;
    }

    public void setExpenseCount(double expenseCount) {
        this.expenseCount = expenseCount;
    }
}
