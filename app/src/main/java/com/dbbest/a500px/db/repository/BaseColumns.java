package com.dbbest.a500px.db.repository;

import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;

@SuppressWarnings({"PMD.ConstantsInInterface", "PMD.AvoidConstantsInterface"})
public interface BaseColumns {

    @PrimaryKey(onConflict = ConflictResolutionType.REPLACE)
    @DataType(DataType.Type.TEXT)
    String ID = "_id";

}