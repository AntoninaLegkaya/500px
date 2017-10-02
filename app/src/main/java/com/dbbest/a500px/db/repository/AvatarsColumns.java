package com.dbbest.a500px.db.repository;


import com.dbbest.a500px.db.DatabaseDefinition;

import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.ForeignKeyConstraint;

@SuppressWarnings({"PMD.ConstantsInInterface", "PMD.AvoidConstantsInterface"})
@ForeignKeyConstraint(columns = AvatarsColumns.ID, referencedTable = DatabaseDefinition.USER_TABLE, referencedColumns = UserColumns.ID,
        onConflict = ConflictResolutionType.REPLACE)
public interface AvatarsColumns extends BaseColumns {

    @DataType(DataType.Type.TEXT)
    String URL_DEFAULT = "def";

    @DataType(DataType.Type.TEXT)
    String URL_LARGE = "large";

    @DataType(DataType.Type.TEXT)
    String URL_SMALL = "small";

    @DataType(DataType.Type.TEXT)
    String URL_TINY = "tiny";


}
