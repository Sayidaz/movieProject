package model;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Image {
    private String path;

    public Image(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public boolean exists() {
        Path imagePath = Paths.get(path);
        return Files.exists(imagePath);
    }


}
