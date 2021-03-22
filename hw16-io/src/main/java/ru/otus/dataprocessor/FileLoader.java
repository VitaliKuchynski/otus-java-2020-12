package ru.otus.dataprocessor;

import ru.otus.model.Measurement;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileLoader implements Loader {

    private final String fileName;

    public FileLoader(String fileName) {
        this.fileName =  fileName;
    }

    @Override
    public List<Measurement> load() throws FileNotFoundException {

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(fileName)).getFile());
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
