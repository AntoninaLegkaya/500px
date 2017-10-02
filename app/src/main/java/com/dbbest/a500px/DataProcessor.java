package com.dbbest.a500px;

import com.dbbest.a500px.db.repository.RepositoryProvider;

public class DataProcessor {

    private final RepositoryProvider repository;

    private DataProcessor() {
        repository = new RepositoryProvider();
    }

    public static DataProcessor instance() {
        return DataProcessorHolder.instance;
    }

    private static DataProcessor newInstance() {
        return new DataProcessor();
    }

    private static class DataProcessorHolder {
        static final DataProcessor instance = newInstance();
    }
    public RepositoryProvider repository() {
        return repository;
    }
}
