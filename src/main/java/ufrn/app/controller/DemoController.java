package ufrn.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ufrn.middleware.annotations.GetMapping;
import ufrn.middleware.annotations.PostMapping;
import ufrn.middleware.start.MiddlewareApplication;

public class DemoController {

    private static final Logger logger = LoggerFactory.getLogger(MiddlewareApplication.class);

    @GetMapping
    public void getTest() {
        logger.info("chegou na rota get");
    }

    @GetMapping("/findAll")
    public void getTest2() {
        logger.info("chegou na rota get");
    }

    @PostMapping
    public void postTest(Object request) {
        logger.info("chegou na rota post");
    }

}
