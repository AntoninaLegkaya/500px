package com.dbbest.a500px.db.repository;

import java.util.List;

public interface Repository<V> {
    boolean exists(String id);

    boolean exists(V entry);

    int update(V entry);

    int remove(V entry);

    int remove(String id);

    int removeBatch(String where, Object... args);

    boolean put(V entry);

    V get(String id);

    List<V> select(String where, Object... args);

    int bulk(List<V> entries);

    @SuppressWarnings("PMD.UseVarargs")
    int bulk(V[] entries);

    void removeAll();
}
