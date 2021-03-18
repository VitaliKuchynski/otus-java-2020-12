package ru.otus.dataprocessor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.model.Measurement;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileLoader implements Loader {

    private String fileName;

    public FileLoader(String fileName) {
        this.fileName =  fileName;
    }

    @Override
    public List<Measurement> load() throws IOException {

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        String absolutePath = file.getAbsolutePath();

        List<Measurement> measurementsList = new ArrayList<>();

        JsonReader jsonReader = Json.createReader(new FileInputStream(absolutePath));
        JsonArray array = jsonReader.readArray();

        for ( int i = 0; i < array.size(); i++){

            String name = array.getJsonObject(i).getString("name");
            double value =Double.parseDouble(array.getJsonObject(i).get("value").toString());
            measurementsList.add(new Measurement(name, value));
        }
        jsonReader.close();
        return measurementsList;
    }
}
