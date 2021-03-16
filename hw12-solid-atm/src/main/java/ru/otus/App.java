package ru.otus;

import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {

        List<Unit> unitList = new ArrayList<>();
        unitList.add(new Unit(BanknoteValue.TWENTY));
        unitList.add(new Unit(BanknoteValue.FIFTEEN));
        unitList.add(new Unit(BanknoteValue.TEN));
        unitList.add(new Unit(BanknoteValue.FIVE));
        unitList.add(new Unit(BanknoteValue.TWO));
        unitList.add(new Unit(BanknoteValue.ONE));

        ATM atm = new ATM(unitList);
        atm.deposit(145);
        System.out.println(" Deposit-------------------------------------");
        for (Unit unit : atm.unitList) {
            System.out.println(unit.getBanknoteValue().toString() + " " + " " + unit.getCount());
        }

        System.out.println(" Withdrawal -------------------------------------");
        System.out.println(atm.withdrawal(65));
        for (Unit unit : atm.unitList) {
            System.out.println(unit.getBanknoteValue().toString() + " " + " " + unit.getCount());
        }

        atm.printBalance();
    }
}

