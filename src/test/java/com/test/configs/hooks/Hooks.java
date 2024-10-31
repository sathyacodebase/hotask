package com.test.configs.hooks;

import com.test.configs.ConfigFileReader;
import com.test.configs.WebDriverManager;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.openqa.selenium.WebDriver;

public class Hooks {


    @Before
    public void create() {

        ConfigFileReader configFileReader = new ConfigFileReader();
    }

    @After
    public void tearDown() {
        WebDriverManager webDriverManager = new WebDriverManager();
        WebDriver driver = webDriverManager.getDriver();
        driver.quit();
    }

}
