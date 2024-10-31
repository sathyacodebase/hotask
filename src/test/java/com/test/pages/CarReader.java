package com.test.pages;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;

import java.io.File;
import java.util.List;

public class CarReader {
    public static List<Car> readFile(File csvFile) throws Exception {
        MappingIterator<Car> personIter = new CsvMapper().readerWithTypedSchemaFor(Car.class).readValues(csvFile);
        return personIter.readAll();
    }
}
