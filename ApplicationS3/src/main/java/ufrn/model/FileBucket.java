package ufrn.model;

import java.io.File;

public class FileBucket {

    private final String bucketName;
    private final File file;

    public FileBucket(String bucketName, File file) {
        this.bucketName = bucketName;
        this.file = file;
    }

    public String getBucketName() {
        return bucketName;
    }

    public File getFile() {
        return file;
    }

    @Override
    public String toString() {
        return "{" +
                "bucketName='" + bucketName + '\'' +
                ", file=" + file.getName() +
                '}';
    }
}
