package ru.otus;


public class Unit {

    private final BanknoteValue banknoteValue;

    private int count;

    Unit(BanknoteValue value){
        this.banknoteValue = value;
    }

    public int getCount() {
        return count;
    }

    public void addCount(int count) {

        if (count >= 0) {
            this.count += count;
        } else {
            throw new RuntimeException("Incorrect count " + count);
        }
    }

    public BanknoteValue getBanknoteValue() {
        return banknoteValue;
    }

    public int extractCount(int count) {

        if (count >= 0) {
            return this.count -= count;
        } else {
            throw new RuntimeException("Incorrect count " + count);
        }
    }
}
