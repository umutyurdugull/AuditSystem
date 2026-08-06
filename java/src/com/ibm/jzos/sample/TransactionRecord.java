package com.ibm.jzos.sample;

public class TransactionRecord {
    String accountId;
    String lastName;
    double amount;
    String txType;
    String status;

    public TransactionRecord(String accountId, String lastName, double amount, String txType, String status) {
        this.accountId = accountId;
        this.lastName = lastName;
        this.amount = amount;
        this.txType = txType;
        this.status = status;
    }

    @Override
    public String toString(){
        return "Account: " + accountId + " | Client: " + lastName + " | Amount: " + amount + " | Type: " + txType + " | Status: " + status;
    }
}
