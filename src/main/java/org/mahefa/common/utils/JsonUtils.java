package org.mahefa.common.utils;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class JsonUtils {

    public JSONObject readJsonFile(final String path) {
        try(InputStream inputStream = getClass().getResourceAsStream(path)) {
            JSONTokener jsonTokener = new JSONTokener(inputStream);
            JSONObject jsonObject = new JSONObject(jsonTokener);

            return jsonObject;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
