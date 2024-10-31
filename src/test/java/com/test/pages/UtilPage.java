package com.test.pages;

import com.test.configs.ConfigFileReader;
import com.test.configs.FileUtil;
import com.test.configs.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilPage {

    private static UtilPage utilPage;

    public static synchronized UtilPage getInstance() {
        if (utilPage == null) {
            utilPage = new UtilPage();
        }
        return utilPage;
    }

    private final WebDriverManager webDriverManager = new WebDriverManager();
    protected final WebDriver driver = webDriverManager.getDriver();

    protected WebElement findElement(By locator) {
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(4))
                .ignoring(NoSuchElementException.class);
        return wait.until(driver -> driver.findElement(locator));
    }

    protected String getText(By locator) {
        return findElement(locator).getText();
    }

    protected List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }

    public void enterText(By locator, String text) {
        WebElement element = findElement(locator);
        element.clear();
        element.sendKeys(text);
    }

    public void clickOn(By locator) {
        findElement(locator).click();
    }

    public List<String> getRegNumbersFromInputFile(String fileName) {
        String path = ConfigFileReader.getInstance().getFilePath();
        String inputText = FileUtil.readFileToString(path + fileName);
        Pattern pattern = Pattern.compile("([a-zA-Z]{2}[0-9]{2}\\s?[a-zA-Z]{3})");
        return extractMatches(pattern, inputText);
    }

    private List<String> extractMatches(Pattern pattern, String str) {
        List<String> matches = new ArrayList<>();
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            matches.add(matcher.group(1));
        }
        return matches;
    }
}
