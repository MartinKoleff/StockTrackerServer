package com.koleff.stockserver;

import org.junit.rules.ExternalResource;

public class TestResources extends ExternalResource {
    protected void before() {
        // Setup logic that used to be in @BeforeClass
    }
    protected void after() {
        // Setup logic that used to be in @AfterClass
    }
}