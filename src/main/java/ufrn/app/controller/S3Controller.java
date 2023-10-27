package ufrn.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ufrn.app.repository.S3Repository;
import ufrn.middleware.annotations.GetMapping;
import ufrn.middleware.annotations.PostMapping;
import ufrn.middleware.annotations.RequestBody;

public class S3Controller {

    private static final Logger logger = LoggerFactory.getLogger(S3Controller.class);

    private final S3Repository repository = new S3Repository();


    public S3Controller() {
    }

    record CreateBucketReq(String bucketName) {
    }

    @PostMapping("/s3")
    public void createBucket(@RequestBody CreateBucketReq bucketName) {
        logger.info("Solicitação para criação de bucket...");
        repository.addBucket(bucketName.bucketName());
        logger.info("Bucket {} Criado com sucesso!", bucketName);

    }

    @GetMapping("/s3")
    public void getAllBuckets() {
        logger.info("Buckets registrados:");
        logger.info(repository.getAllBuckets());


    }


}
