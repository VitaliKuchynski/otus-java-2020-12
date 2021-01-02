import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import java.util.Arrays;
import java.util.List;

public class HelloOtus {

    public static void main(String[] args) {

        Function<Object, String> toString = com.google.common.base.Functions.toStringFunction();
        List empList = Arrays.asList(emp("1", "John"), emp("2", "Sam"), emp("3", "Test"));

        System.out.println(Collections2.transform(empList, toString));
    }

    private static Employee emp(String id, String name) {
        return new Employee(id, name);
    }

    private static class Employee {
        private String id;
        private String name;

        Employee(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String toString() {
            return "Emp<" + id + "," + name + ">";
        }
    }
}
