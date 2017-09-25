package com.dbbest.a500px.di;


public final class GraphImpl implements Graph {

    private final Navigation navigation;

    public GraphImpl() {
        navigation = new Navigation();
    }

    public static GraphImpl instance() {
        return GraphHolder.instance;
    }

    private static GraphImpl newInstance() {
        return new GraphImpl();
    }

    @Override
    public Navigation navigation() {
        return navigation;
    }

    private static class GraphHolder {
        static final GraphImpl instance = newInstance();
    }
}
