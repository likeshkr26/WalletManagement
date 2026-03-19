package com.wallet_management.Model;

public class Pair
{
    String name;
    double amount;

    public Pair(String name,double amount)
    {
        this.name=name;
        this.amount=amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Pair() {
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
