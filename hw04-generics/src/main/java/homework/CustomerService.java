package homework;


import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны
    TreeMap<Customer, String> customersMap = new TreeMap<>();

    public Map.Entry<Customer, String> getSmallest() {
        //Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        return customersMap.entrySet().iterator().next();
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return customersMap.higherEntry(customer);
    }

    public void add(Customer customer, String data) {
        customersMap.put(customer, data);
    }
}
