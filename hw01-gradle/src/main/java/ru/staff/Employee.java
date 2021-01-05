package ru.staff;

public class Employee {
    private final String id;
    private final String name;

    public Employee(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String toString() {
        return "Emp<" + id + "," + name + ">";
    }
}
