package com.dbbest.a500px.db.repository;

import com.dbbest.a500px.db.DatabaseDefinition;

import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.ForeignKeyConstraint;

@SuppressWarnings({"PMD.ConstantsInInterface", "PMD.AvoidConstantsInterface"})
@ForeignKeyConstraint(columns = PhotoColumns.USER_ID, referencedTable = DatabaseDefinition.USER_TABLE, referencedColumns = UserColumns.ID,
        onConflict = ConflictResolutionType.REPLACE)
public interface PhotoColumns extends BaseColumns {

    @DataType(DataType.Type.INTEGER)
    String USER_ID = "user_id";

    @DataType(DataType.Type.TEXT)
    String IMAGE_URL = "image_url";
}
