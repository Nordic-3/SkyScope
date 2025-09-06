package com.szte.SkyScope.Services.Impl;

import com.szte.SkyScope.Services.JsonReaderService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class JsonReaderServiceImpl implements JsonReaderService {

    @Override
    public String readJsonFromResources(String file) {
        String json = "";
        try (InputStream jsonFile = getClass()
                .getClassLoader()
                .getResourceAsStream(file)) {
            json = new String(jsonFile.readAllBytes());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return json;
    }
}
