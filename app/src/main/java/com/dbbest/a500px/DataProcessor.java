package com.dbbest.a500px;

public class DataProcessor {


    public static DataProcessor instance() {
        return DataProcessorHolder.instance;
    }

    private static DataProcessor newInstance() {
        return new DataProcessor();
    }

    private static class DataProcessorHolder {
        static final DataProcessor instance = newInstance();
    }
}
