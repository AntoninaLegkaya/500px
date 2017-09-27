package com.dbbest.a500px.db.repository;

import com.dbbest.a500px.db.BaseColumns;

import net.simonvt.schematic.annotation.DataType;

public interface UserColumns extends BaseColumns {
    @DataType(DataType.Type.TEXT)
    String USER_NAME = "username";
}
