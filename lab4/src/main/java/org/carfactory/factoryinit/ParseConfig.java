package org.carfactory.factoryinit;

import org.carfactory.parseexceptions.ConfigFactoryNotFoundException;
import org.carfactory.parseexceptions.ParseConfigException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ParseConfig {
    private final String configName;

    public ParseConfig(String configName){
        this.configName = configName;
    }

    public void parse(Map<String, String> configMap) throws ParseConfigException {
        try(InputStream inputStream = getClass().getClassLoader().getResourceAsStream(configName)) {
            if (inputStream == null) {
                throw new ConfigFactoryNotFoundException("Can`t find factory config");
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null){
                System.out.println(line + "||");
                workWithStrings(line, configMap);
            }
        }
        catch (IOException e){
            throw new ParseConfigException("Error while reading factory config file");
        }
    }

    private void workWithStrings(String line, Map<String, String> configMap){
        String[] lineDiv = line.split("=");
        configMap.put(lineDiv[0], lineDiv[1]);
    }
}
