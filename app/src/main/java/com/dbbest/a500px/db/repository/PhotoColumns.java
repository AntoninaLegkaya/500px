package com.dbbest.a500px.db.repository;

import net.simonvt.schematic.annotation.DataType;

public interface PhotoColumns extends BaseColumns {

    @DataType(DataType.Type.INTEGER)
    String USER_ID = "user_id";
}
