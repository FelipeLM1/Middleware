package ufrn.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ufrn.model.FileBucket;
import ufrn.repository.S3Repository;
import ufrn.annotations.http.GetMapping;
import ufrn.annotations.http.PostMapping;
import ufrn.annotations.http.RequestBody;
import ufrn.utils.ResponseEntity;

public class S3Controller {

    private static final Logger logger = LoggerFactory.getLogger(S3Controller.class);

    private final S3Repository repository = new S3Repository();


    record CreateBucketReq(String bucketName) {
    }


    @PostMapping("/s3")
    public ResponseEntity<?> createBucket(@RequestBody CreateBucketReq bucketName) {
        logger.info("Solicitação para criação de bucket...");
        repository.addBucket(bucketName.bucketName());
        logger.info("Bucket {} Criado com sucesso!", bucketName);
        return new ResponseEntity<>(200, "Bucket " + bucketName + "Criado com sucesso!", null);
    }

    @PostMapping("/s3/upload")
    public ResponseEntity<?> uploadFile(@RequestBody FileBucket bucketName) {
        logger.info("Solicitação para criação de bucket...");
        //repository.addBucket(bucketName.bucketName());
        logger.info("Bucket {} Criado com sucesso!", bucketName);
        return new ResponseEntity<>(200, "Bucket " + bucketName + "Criado com sucesso!", null);
    }

    @GetMapping("/s3")
    public ResponseEntity<String> getAllBuckets() {
        return new ResponseEntity<>(200, "OK", repository.getAllBuckets());
    }
}
