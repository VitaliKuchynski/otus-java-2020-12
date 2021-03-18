package ru.otus.dataprocessor;

import ru.otus.model.Measurement;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        Map<String, Double> measurementMap = new TreeMap<>();

        for (Measurement m : data) {
            if(!measurementMap.containsKey(m.getName())){
                measurementMap.put(m.getName(), m.getValue());
            } else {
                measurementMap.put(m.getName(), measurementMap.get(m.getName()) + m.getValue());
            }
        }
        return measurementMap;
    }
}
