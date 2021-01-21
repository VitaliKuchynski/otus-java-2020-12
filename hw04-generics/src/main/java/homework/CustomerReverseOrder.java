package homework;


import java.util.ArrayList;
import java.util.List;

public class CustomerReverseOrder {

    //todo: 2. надо реализовать методы этого класса
    //надо подобрать подходящую структуру данных, тогда решение будет в "две строчки"

    private List<Customer> customerList = new ArrayList<>();
    private int listSize;
    private int count = 0;

    public void add(Customer customer) {
        customerList.add(customer);
    }

    public Customer take() {
        listSize = customerList.size();

        if (!(listSize == 0)) {
            count++;
            return customerList.get(listSize - count);
        }
        return null;
    }
}
