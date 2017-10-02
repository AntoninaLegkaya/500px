package com.dbbest.a500px.db.repository;

import net.simonvt.schematic.annotation.DataType;

@SuppressWarnings({"PMD.ConstantsInInterface", "PMD.AvoidConstantsInterface"})
public interface UserColumns extends BaseColumns {

    @DataType(DataType.Type.TEXT)
    String USER_NAME = "username";
}
