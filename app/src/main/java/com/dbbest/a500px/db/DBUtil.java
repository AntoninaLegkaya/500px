package com.dbbest.a500px.db;

public class DBUtil {

    public static String[] where(Object... args) {
        if (args == null || args.length == 0) {
            return new String[0];
        }

        String[] where = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            where[i] = args[i].toString();
        }
        return where;
    }
}
