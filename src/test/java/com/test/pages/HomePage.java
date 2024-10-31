package com.test.pages;

import com.test.configs.ConfigFileReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePage extends UtilPage {

    private static final Logger logger = LoggerFactory.getLogger(HomePage.class);

    private static final By REG_TEXT_FIELD = By.cssSelector("input[name='vrm']");
    private static final By MILEAGE_TXT_FIELD = By.cssSelector("input[name='mileage']");
    private static final By POSTCODE_TXT_FIELD = By.cssSelector("input[name='postcode']");
    private static final By VALUE_MY_CAR_BUTTON = By.cssSelector("button[type='submit']");
    public static final By ACCEPT_ALL_COOKIES_BUTTON = By.cssSelector("#onetrust-accept-btn-handler");
    public static final By REG_NUMBER = By.cssSelector(".vyv__number-plate");
    public static final By OTHER_ATTRIBUTES = By.cssSelector("ul[class*='vyv__vehicle-details'] li");

    public static List<Boolean> matchingOrNot = new ArrayList<>();
    private static Map<String, List<String>> actualCarDetails = new HashMap<>();

    private static HomePage homePage;

    public static synchronized HomePage getInstance() {

        if (homePage == null) {
            homePage = new HomePage();
        }
        return homePage;
    }

    public void getCarDetails(List<String> regNumbersFromInputFile) {
        regNumbersFromInputFile.forEach(this::processCarSearch);
    }

    private void processCarSearch(String regNumber) {
        performSearch(regNumber);
        List<String> carDetails = vehicleNotFound() ?
                List.of("We couldn't find a car with that registration.") : fetchCarAttributes();
        actualCarDetails.put(regNumber, carDetails);
        Navigate.getInstance().toCazooSiteWithoutCookies();
    }

    private void performSearch(String regNumber) {
        enterText(REG_TEXT_FIELD, regNumber.replace(" ", ""));
        enterText(MILEAGE_TXT_FIELD, "100000");
        enterText(POSTCODE_TXT_FIELD, "E14 9UY");
        clickOn(VALUE_MY_CAR_BUTTON);
    }

    private List<String> fetchCarAttributes() {
        String make = getMake();
        String model = getModel();
        String otherAttributes = getOtherAttributes();
        List<String> carDetails = new ArrayList<>();
        carDetails.add(make);
        carDetails.add(model + " " + otherAttributes);
        return carDetails;
    }

    private String getMake() {
        String[] result = splitFirstAndRemaining(getText(By.cssSelector("h3")));
        return result[0];
    }

    private String getModel() {
        String[] result = splitFirstAndRemaining(getText(By.cssSelector("h3")));
        return result[1];
    }

    public String getOtherAttributes() {
        List<WebElement> elements = driver.findElements(OTHER_ATTRIBUTES);
        StringBuilder firstThreeTexts = new StringBuilder();

        for (int i = 0; i < 3 && i < elements.size(); i++) {
            firstThreeTexts.append(elements.get(i).getText()).append(" ");
        }
        String ltrs = getText(By.cssSelector("h4"));
        return ltrs + " " + firstThreeTexts.toString().trim();
    }

    public static String[] splitFirstAndRemaining(String input) {
        input = input.trim();
        int firstSpaceIndex = input.indexOf(" ");
        if (firstSpaceIndex == -1) {
            return new String[]{input, ""};
        }
        String firstWord = input.substring(0, firstSpaceIndex);
        String remainingWords = input.substring(firstSpaceIndex + 1).trim();

        return new String[]{firstWord, remainingWords};
    }

    private boolean vehicleNotFound() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        return driver.getPageSource().contains("Vehicle could not be found");
    }

    private void validateAndLogCarDetails(String regNumber, List<Car> cars) {
        Car expectedCar = findCarInFile(regNumber, cars);
        if (expectedCar != null) {
            Map<String, Boolean> matchResults = compareAttributes(regNumber, expectedCar);
            logComparisonResults(regNumber, matchResults);
        } else {
            logMissingCarDetails(regNumber);
        }
    }

    private Car findCarInFile(String regNumber, List<Car> cars) {
        return cars.stream()
                .filter(car -> car.getREGISTRATION().equals(regNumber.replace(" ", "")))
                .findFirst()
                .orElse(null);
    }

    private Map<String, Boolean> compareAttributes(String regNumber, Car expectedCar) {
        Map<String, Boolean> matches = new HashMap<>();
        List<String> actualDetails = actualCarDetails.get(regNumber);
        if (actualDetails.size() > 1) {
            matches.put("make", actualDetails.get(0).equalsIgnoreCase(expectedCar.getMAKE()));
            matches.put("model", isMatches(expectedCar.getMODEL(), actualDetails.get(1)));
        }
        matchingOrNot.addAll(matches.values());
        return matches;
    }

    public static boolean isMatches(String str1, String str2) {
        String[] words1 = str1.split("\\s+");
        String[] words2 = str2.split("\\s+");

        int matchCount = 0;
        for (String word1 : words1) {
            for (String word2 : words2) {
                if (word1.equalsIgnoreCase(word2)) {
                    matchCount++;
                    break;
                }
            }
        }
        double matchPercentage = (double) matchCount / words1.length;
        return matchPercentage >= 0.4;
    }

    private void logComparisonResults(String regNumber, Map<String, Boolean> matchResults) {
        logger.info("\n{} Details:\n", regNumber);
        matchResults.forEach((attribute, isMatched) -> {
            if (isMatched) {
                logger.info("Car {} matches between the website and output file", attribute);
            } else {
                logger.warn("Car with registration {} has a {} that does NOT match with the website data", regNumber, attribute);
            }
        });
    }

    private void logMissingCarDetails(String regNumber) {
        logger.info("\n{} Details: \n", regNumber);
        logger.warn("{} details are not in output file", regNumber);
        if (actualCarDetails.get(regNumber).size() < 2) {
            logger.warn("{} details are not found on the website", regNumber);
        }
    }

    public void compareCarDetailsWithOutputFile(String fileName) throws Exception {
        writeToFileIfValueMatches();
        String path = ConfigFileReader.getInstance().getFilePath();
        File testFile = new File(path + fileName);
        List<Car> expectedCars = CarReader.readFile(testFile);
        actualCarDetails.keySet().forEach(regNumber -> validateAndLogCarDetails(regNumber, expectedCars));

        if (matchingOrNot.contains(false)) {
            logger.warn("There were mismatches in the car details comparison.");
        } else {
            logger.info("All car details matched successfully.");
        }
    }

    public static void writeToFileIfValueMatches() {
        File file = new File("target/car_not_found_details.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Map.Entry<String, List<String>> entry : actualCarDetails.entrySet()) {
                String key = entry.getKey();
                List<String> values = entry.getValue();
                for (String value : values) {
                    if (value.contains("We couldn't find a car with that registration.")) {
                        writer.write("Key: " + key + ", Value: " + value);
                        writer.newLine();
                        break;
                    }
                }
            }
            System.out.println("File written to target directory.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}