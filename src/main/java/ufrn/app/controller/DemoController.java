package ufrn.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ufrn.middleware.annotations.GetMapping;
import ufrn.middleware.annotations.PostMapping;
import ufrn.middleware.annotations.RequestBody;
import ufrn.middleware.utils.enums.HttpMethod;

public class DemoController {

    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

    record TestRequest(long id, String value) {
    }

    @GetMapping
    public void getTest() {
        logger.info("chegou na rota get");
    }

    @GetMapping("/findAll")
    public void getTest2() {
        logger.info("chegou na rota get");
    }

    @PostMapping
    public void postTest(@RequestBody TestRequest request) {
        logger.info("chegou na rota post");
        logger.info("request: \n id: {} \n value: {}", request.id(), request.value());
    }

    record TestRequest2(long id, String value, float number) {
    }

    @PostMapping("/postTest")
    public void postTest2(@RequestBody TestRequest2 request) {
        logger.info("chegou na rota post 2");
        logger.info("\n request: \n id: {} \n value: {}, \n number: {}", request.id(), request.value(), request.number());
    }

}
