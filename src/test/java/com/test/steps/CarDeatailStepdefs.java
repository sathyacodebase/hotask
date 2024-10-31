package com.test.steps;

import com.test.ObjectRepository;
import com.test.pages.HomePage;
import com.test.pages.Navigate;
import com.test.pages.UtilPage;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertFalse;

public class CarDeatailStepdefs extends ObjectRepository {
    private List<String> regNumbersFromInputFile = new ArrayList<>();

    @Given("^user reads the input file \"([^\"]*)\" and extracts the car registration numbers$")
    public void userReadsTheInputFileAndExtractsTheCarRegistrationNumbers(String inputFileName) throws Throwable {
        regNumbersFromInputFile = UtilPage.getInstance().getRegNumbersFromInputFile(inputFileName);
        System.out.println(regNumbersFromInputFile);
    }

    @When("^user enters the extracted registered numbers into cazoo website and saves the details of$")
    public void userEntersTheExtractedRegisteredNumbersIntoCazooWebsiteAndSavesTheDetailsOfMAKEMODEL() throws Exception {
        Navigate.getInstance().toCazooWebSite();
        HomePage.getInstance().getCarDetails(regNumbersFromInputFile);
    }

    @Then("^compare the details with the output file name \"([^\"]*)\" and display any mismatches if they exist$")
    public void compareTheDetailsWithTheOutputFileAndDisplayAnyMismatchesIfExists(String outputFileName) throws Throwable {
        HomePage.getInstance().compareCarDetailsWithOutputFile(outputFileName);
        assertFalse("Mismatches were found in the car details comparison.", HomePage.matchingOrNot.contains(false));
    }
}
