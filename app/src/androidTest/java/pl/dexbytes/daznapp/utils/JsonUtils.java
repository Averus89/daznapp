package pl.dexbytes.daznapp.utils;

import java.io.IOException;
import java.io.InputStream;

import androidx.test.core.app.ApplicationProvider;

import static pl.dexbytes.daznapp.utils.AssetReaderUtil.inputStreamToString;

public class JsonUtils {
    public static String getResponseFromJsonFile(String path) {
        StringBuilder buf = new StringBuilder();
        try {
            InputStream inputStream = ApplicationProvider.getApplicationContext()
                    .getAssets().open("body_files/" + path);
            String jsonString = inputStreamToString(inputStream, "UTF-8");
            return jsonString;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
