package ru.otus;


public class Unit {

    private BanknoteValue banknoteValue;

    private int count;

    Unit(BanknoteValue value){
        this.banknoteValue = value;
    }

    public int getCount() {
        return count;
    }

    public void addCount(int count) {
        this.count += count;
    }

    public BanknoteValue getBanknoteValue() {
        return banknoteValue;
    }

    public int extractCount(int count) {
        return this.count -= count;
    }
}
