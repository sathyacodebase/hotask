package com.test.configs;

import org.apache.commons.io.IOUtils;

import java.io.*;

public class FileUtil {

    public static String readFileToString(String filePath) {
        return readFileToString(filePath, null);
    }

    public static String readFileToString(String filePath, String encoding) {
        try {
            if (encoding == null)
                return IOUtils.toString(getInputStreamFromPathOrClasspath(filePath));
            else
                return IOUtils.toString(getInputStreamFromPathOrClasspath(filePath), encoding);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + filePath + " to string", e);
        }
    }

    public static InputStream getInputStreamFromPathOrClasspath(String filePath) {
        InputStream is = null;
        try {
            if (new File(filePath).exists()) {
                is = new FileInputStream(filePath);
            } else {
                is = FileUtil.class.getClassLoader().getResourceAsStream(filePath);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Failed to read input stream from file: " + filePath, e);
        }
        if (is == null) {
            throw new RuntimeException("Failed to read input stream from file: " + filePath);
        }
        return is;
    }
}


