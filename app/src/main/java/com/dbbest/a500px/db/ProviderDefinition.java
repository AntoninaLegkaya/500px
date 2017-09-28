package com.dbbest.a500px.db;

import android.net.Uri;

import com.dbbest.a500px.BuildConfig;

import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

@SuppressWarnings({"PMD.UseUtilityClass", "PMD.UncommentedEmptyConstructor",
        "PMD.ConstantsInInterface", "PMD.AvoidConstantsInterface"})
//@ContentProvider(authority = ProviderDefinition.AUTHORITY,
//        database = DatabaseDefinition.class,
//        packageName = "com.dbbest.a500px.db",
//        name = "AppContentProvider")
public class ProviderDefinition {
    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".provider.AppContentProvider";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    private static final String VND_HMNI_ITEM = "vnd.hmni.item/";

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    interface Path {
        String PHOTO = "photo";
        String USER = "user";
    }

    @TableEndpoint(table = DatabaseDefinition.PHOTO_TABLE)
    public static class PhotoEntry {
        @ContentUri(
                path = Path.PHOTO,
                type = VND_HMNI_ITEM + Path.PHOTO
        )
        public static final Uri URI = buildUri(Path.PHOTO);
    }

    @TableEndpoint(table = DatabaseDefinition.USER_TABLE)
    public static class UserEntry {
        @ContentUri(
                path = Path.USER,
                type = VND_HMNI_ITEM + Path.USER
        )
        public static final Uri URI = buildUri(Path.USER);
    }
}
