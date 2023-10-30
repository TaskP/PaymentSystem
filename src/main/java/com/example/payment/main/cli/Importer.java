package com.example.payment.main.cli;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class Importer {

    protected List<String[]> loadFile(final String[] args) throws IllegalArgumentException {
        if (args == null || args.length == 0 || args[0] == null || args[0].length() == 0) {
            throw new IllegalArgumentException("Error: 1011 - Invalid argument! Expecting CSV file to load.");
        }
        final String fileName = args[0];
        final File csvFile = new File(fileName);
        if (!csvFile.exists()) {
            throw new IllegalArgumentException("Error: 1014 - Invalid argument! File (" + fileName + ") does not exist.");
        }
        if (!csvFile.canRead()) {
            throw new IllegalArgumentException("Error: 1016 - Invalid argument! File (" + fileName + ") is not readable.");
        }
        if (!csvFile.isFile()) {
            throw new IllegalArgumentException("Error: 1018 - Invalid argument! Path (" + fileName + ") is not a file.");
        }
        try (Reader reader = Files.newBufferedReader(csvFile.toPath())) {
            try (CSVReader csvReader = new CSVReader(reader)) {
                final List<String[]> ret = csvReader.readAll();
                if (ret == null || ret.isEmpty()) {
                    throw new IllegalArgumentException("Error: 1019 - Invalid argument! File (" + fileName + ") is empty.");
                }
                return ret;
            } catch (final CsvException e) {
                throw new IllegalArgumentException("Error: 1021 - Invalid argument! Unable to read file (" + fileName + ")", e);
            }
        } catch (final IOException e) {
            throw new IllegalArgumentException("Error: 1023 - Invalid argument! Unable to load file (" + fileName + ")", e);
        }
    }
}
