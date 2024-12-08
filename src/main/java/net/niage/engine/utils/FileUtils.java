package net.niage.engine.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileUtils {

    public static File getFile(String filePath) {
        return new File(filePath);
    }

    public static String readFile(String filePath) throws IOException {
        File file = getFile(filePath);
        return new String(Files.readAllBytes(file.toPath()));
    }

    public static String getFullPath(String filePath) {
        File file = getFile(filePath);
        return file.toPath().toString();
    }

    public static String getFolderPath(String filePath) {
        File file = getFile(filePath);
        return file.toPath().toString().replace(file.getName(), "");
    }

}
