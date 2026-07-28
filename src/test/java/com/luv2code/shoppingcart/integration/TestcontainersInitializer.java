package com.luv2code.shoppingcart.integration;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.mongodb.MongoDBContainer;

class TestcontainersInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    static MongoDBContainer mongo =
            new MongoDBContainer("mongo:latest")
                    .withReuse(true);

    static {
        Startables.deepStart(mongo).join();
    }

    @Override
    public void initialize(ConfigurableApplicationContext ctx) {
        TestPropertyValues.of(
                "spring.data.mongodb.uri=" + mongo.getReplicaSetUrl()
        ).applyTo(ctx.getEnvironment());
    }
}