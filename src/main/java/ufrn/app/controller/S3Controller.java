package ufrn.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ufrn.app.repository.S3Repository;
import ufrn.middleware.annotations.GetMapping;
import ufrn.middleware.annotations.PostMapping;
import ufrn.middleware.annotations.RequestBody;
import ufrn.middleware.utils.ResponseEntity;

public class S3Controller {

    private static final Logger logger = LoggerFactory.getLogger(S3Controller.class);

    private final S3Repository repository = new S3Repository();


    public S3Controller() {
    }

    record CreateBucketReq(String bucketName) {
    }

    @PostMapping("/s3")
    public ResponseEntity<?> createBucket(@RequestBody CreateBucketReq bucketName) {
        logger.info("Solicitação para criação de bucket...");
        repository.addBucket(bucketName.bucketName());
        logger.info("Bucket {} Criado com sucesso!", bucketName);
        return new ResponseEntity<>(200, "Bucket " + bucketName + "Criado com sucesso!", null);
    }

    @GetMapping("/s3")
    public ResponseEntity<String> getAllBuckets() {
        return new ResponseEntity<>(200, "OK", repository.getAllBuckets());
    }

}
