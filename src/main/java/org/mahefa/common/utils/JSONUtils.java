package org.mahefa.common.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;
import java.io.InputStream;

public class JSONUtils {

    public static JsonParser read(final String filepath) {
        JsonParser jsonParser = null;

        try(InputStream inputStream = JSONUtils.class.getResourceAsStream(filepath)) {
            JsonFactory jsonFactory = new JsonFactory();
            jsonParser = jsonFactory.createParser(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonParser;
    }
}
