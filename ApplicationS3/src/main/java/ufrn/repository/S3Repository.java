package ufrn.repository;

import ufrn.model.FileBucket;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class S3Repository {

    static Map<String, Set<FileBucket>> bucketRepository = new HashMap<>();

    public void addBucket(String bucketName) {
        if (!bucketRepository.containsKey(bucketName)) {
            bucketRepository.put(bucketName, new HashSet<>());
        }
    }

    public boolean addFile(String bucketName, File file) {
        bucketRepository.get(bucketName).add(new FileBucket(file.getName(), file));
        return true;
    }

    public FileBucket getFile(String bucketName, String filename) {
        return bucketRepository.get(bucketName)
                .stream()
                .filter(fileBucket -> fileBucket.getBucketName().equals(filename))
                .findFirst().orElseThrow();
    }

    public String getAllBuckets() {
        return bucketRepository.toString();
    }


}
