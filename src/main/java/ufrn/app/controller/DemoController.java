package ufrn.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ufrn.middleware.annotations.GetMapping;
import ufrn.middleware.annotations.PostMapping;
import ufrn.middleware.annotations.RequestBody;

public class DemoController {

    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

    record TestRequest(long id, String value) {
    }


    @GetMapping("/findAll")
    public void getTest2() {
        logger.info("chegou na rota get");
    }

    record TestRequest2(long id, String value, float number) {
    }

    record uploadFileReq(String bucketName, String nameFile, byte[] data) {
    }

    @PostMapping("/postTest2")
    public void postTest2(@RequestBody uploadFileReq request) {
        logger.info("chegou na rota post 2");

    }

    @PostMapping("/postTest")
    public void postTest2(@RequestBody TestRequest2 request) {
        logger.info("chegou na rota post 2");
        logger.info("\n request: \n id: {} \n value: {}, \n number: {}", request.id(), request.value(), request.number());
    }

}
