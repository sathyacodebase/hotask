package com.test;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        monochrome = true,
        plugin = {"pretty", "html:target/cucumber.html",
                "json:target/report.json",
                "junit:target/cucumber.xml",
                "com.cucumber.listener.ExtentCucumberFormatter:target/report.html"},
        glue = {"classpath:com/test/steps", "classpath:com/test/configs/hooks"},
        features = "classpath:com/test/features")
public class TestRunner {

}
