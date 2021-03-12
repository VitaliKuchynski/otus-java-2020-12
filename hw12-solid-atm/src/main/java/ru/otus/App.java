package ru.otus;

public class App {
    public static void main(String[] args) {

        ATM atm = new ATM();
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

