

### Overview

This project automates testing for the application and includes configuration for running tests and generating reports. Please follow the instructions below to set up, run tests, and view the results.

### Prerequisites

#### 1. MacOS Setup
If running tests on a Mac, ensure the compatible `chromedriver` is placed at the location:
`/usr/local/bin`

#### 2. Allowing Chromedriver Execution
If you encounter an error stating:

> "chromedriver cannot be opened because the developer cannot be verified"

Run the following command in the terminal to resolve it:

```bash
sudo xattr -d com.apple.quarantine /usr/local/bin/chromedriver
```

## Build and Test Execution
To build and execute tests, run the following Maven command:
`mvn clean test`

## Test Results
After test execution, reports will be generated in the target directory:
Test Report: target/report.html
Car Not Found Details: target/car_not_found.txt contains entries where a car registration returned "We couldn't find a car with that registration."