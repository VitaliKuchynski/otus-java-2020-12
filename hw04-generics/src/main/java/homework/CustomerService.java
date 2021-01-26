package homework;


import java.util.*;

public class CustomerService {

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны
    private final NavigableMap<Customer, String> customersMap = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        //Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        Map.Entry<Customer, String> currentEntry = customersMap.firstEntry();
        Customer copyCustomer = new Customer(currentEntry.getKey());

        return new AbstractMap.SimpleEntry<>(copyCustomer, currentEntry.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return customersMap.higherEntry(customer);
    }

    public void add(Customer customer, String data) {
        customersMap.put(customer, data);
    }
}
