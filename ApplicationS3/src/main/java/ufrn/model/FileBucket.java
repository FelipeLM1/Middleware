package ufrn.model;

import java.io.File;

public class FileBucket {

    private final String name;
    private final File file;

    public FileBucket(String name, File file) {
        this.name = name;
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }
}
