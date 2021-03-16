package ru.otus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ATM implements ATMInterface {

    List<Unit> unitList;

    private int balance;

    ATM(List<Unit> unitList) {
        this.unitList = unitList;
    }

    public int withdrawal(int sum) {

        if (isBalanceAvailable(sum) != -1) {
            balance -= sum;
            return sum;
        }
        System.out.println("No balance/banknote available");
        return 0;
    }

    public void deposit(int sum) {
        balance = sum;

        for (int i = 0; i < unitList.size(); i++) {
            Unit unit = unitList.get(i);
            int currentBanknoteValue = unit.getBanknoteValue().getValue();
            unit.addCount(sum / currentBanknoteValue);
            sum %= currentBanknoteValue;
        }
    }

    public int isBalanceAvailable(int sum) {

        if (balance >= sum) {

            Map<Unit, Integer> potentialWithdrawal = new HashMap<>();

            for (int i = 0; i < unitList.size(); i++) {
                Unit unit = unitList.get(i);
                int currentBanknoteValue = unit.getBanknoteValue().getValue();
                int withValueCount = sum / currentBanknoteValue;

                if (unit.getCount() >= withValueCount) {
                    potentialWithdrawal.put(unit, withValueCount);
                    sum %= currentBanknoteValue;


                } else if (unit.getCount() < withValueCount && unit.getCount() != 0) {
                    int cr = unit.getCount();
                    potentialWithdrawal.put(unit, cr);
                    sum -= cr * currentBanknoteValue;
                }
            }

            if (sum == 0) {
                for (var entry : potentialWithdrawal.entrySet()) {
                    entry.getKey().extractCount(entry.getValue());
                }
            }
        }
        return sum > 0 ? -1 : 0;
    }

    public void printBalance() {
        System.out.println("Current Balance: " + balance);
    }
}
