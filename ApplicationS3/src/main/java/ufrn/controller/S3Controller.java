package ufrn.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ufrn.annotations.http.GetMapping;
import ufrn.annotations.http.PostMapping;
import ufrn.annotations.http.RequestBody;
import ufrn.model.FileBucket;
import ufrn.repository.S3Repository;
import ufrn.utils.ResponseEntity;
import ufrn.utils.enums.DefaultHeaders;
import ufrn.utils.enums.Headers;

import java.util.Map;

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
        return new ResponseEntity<>(201, "Bucket " + bucketName + "Criado com sucesso!", null);
    }

    @PostMapping("/s3/upload")
    public ResponseEntity<?> uploadFile(@RequestBody FileBucket fileBucket) {
        logger.info("Solicitação para criação de bucket...");
        repository.addFile(fileBucket.getBucketName(), fileBucket.getFile());
        logger.info("Arquivo {} Criado com sucesso! {}", fileBucket.getBucketName(), fileBucket.getFile().getPath());
        return new ResponseEntity<>(201, "Arquivo " + fileBucket.getFile().getName() + " criado com sucesso!", null);
    }

    @GetMapping("/s3")
    public ResponseEntity<String> getAllBuckets() {
        return new ResponseEntity<>(200, "OK", repository.getAllBuckets());
    }

    record FileDownloadReq(String bucket, String filename) {
    }

    @PostMapping("/s3/download")
    public ResponseEntity<?> getFile(@RequestBody FileDownloadReq req) {
        FileBucket fileBucket = repository.getFile(req.bucket(), req.filename());
        Map<String, String> headers = DefaultHeaders.DOWNLOAD.getHeaders();
        headers.put(Headers.CONTENT_DISPOSITION.getDescription(), "attachment;filename=" + fileBucket.getFile().getName());
        return new ResponseEntity<>(200, "Solicitação feita com sucesso!", fileBucket.getFile(), headers);
    }

}
