package ufrn.repository;

import com.google.gson.Gson;
import ufrn.model.FileBucket;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class S3Repository {

    private static final Map<String, Set<FileBucket>> bucketRepository = new ConcurrentHashMap<>();

    public void addBucket(String bucketName) {
        if (!bucketRepository.containsKey(bucketName)) {
            bucketRepository.put(bucketName, new HashSet<>());
        }
    }

    public void addFile(String bucketName, File file) {
        bucketRepository.get(bucketName).add(new FileBucket(file.getName(), file));
    }

    public FileBucket getFile(String bucketName, String filename) {
        return bucketRepository.get(bucketName)
                .stream()
                .filter(fileBucket -> fileBucket.getBucketName().equals(filename))
                .findFirst().orElseThrow();
    }

    public String getAllBuckets() {
        Map<String, Set<String>> simplifiedMap = new HashMap<>();

        for (Map.Entry<String, Set<FileBucket>> entry : bucketRepository.entrySet()) {
            Set<String> fileNames = new HashSet<>();
            for (FileBucket fileBucket : entry.getValue()) {
                fileNames.add(fileBucket.getFile().getName());
            }
            simplifiedMap.put(entry.getKey(), fileNames);
        }

        Gson gson = new Gson();
        return gson.toJson(simplifiedMap);

    }
}
