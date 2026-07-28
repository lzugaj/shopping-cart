package com.luv2code.shoppingcart.integration;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.mongodb.MongoDBContainer;

public class TestcontainersInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    static MongoDBContainer mongo =
            new MongoDBContainer("mongo:latest");

    static {
        mongo.start();
    }

    @Override
    public void initialize(ConfigurableApplicationContext ctx) {
        String uri = mongo.getReplicaSetUrl();

        System.out.println("========== Mongo URI ==========");
        System.out.println(uri);
        System.out.println("===============================");

        TestPropertyValues.of(
                "spring.data.mongodb.uri=" + mongo.getReplicaSetUrl()
        ).applyTo(ctx.getEnvironment());
    }
}