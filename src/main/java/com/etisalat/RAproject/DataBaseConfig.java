package com.etisalat.RAproject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DataBaseConfig {

    public static String decryptedPassword = "";
    public static String userName = "";
    public static String url = "";
    public static Properties properties = new Properties();
//    public static void storePassword () throws IOException {
//        properties.setProperty("db.password", encryptedPassword);
//        properties.store(Files.newOutputStream(Path.of("config.properties")), "Database Configuration");
//    }

    public static void restoreDatabaseConfig () throws IOException {
        properties.load(new FileInputStream("config.properties"));
        decryptedPassword = decrypt(properties.getProperty("db.password"));
        userName = properties.getProperty("db.userName");
        url = properties.getProperty("db.url");
    }


    static String decrypt(String cipher) {
        StringBuilder s = new StringBuilder();
        int len = cipher.length();
        for (int i = 0; i < len; i++) {
            char c = (char) (cipher.charAt(i) - 5);
            s.append(c);
        }
        return s.toString();
    }
}
