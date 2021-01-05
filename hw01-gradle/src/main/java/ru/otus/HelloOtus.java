package ru.otus;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import ru.staff.Employee;

import java.util.Arrays;
import java.util.List;

public class HelloOtus {

    public static void main(String[] args) {

        Function<Object, String> toString = com.google.common.base.Functions.toStringFunction();
        List <Employee> empList = Arrays.asList(emp("1", "John"), emp("2", "Sam"), emp("3", "Test"));

        System.out.println(Collections2.transform(empList, toString));
    }

    private static Employee emp(String id, String name) {
        return new Employee(id, name);
    }

}
