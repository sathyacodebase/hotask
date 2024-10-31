package com.test.pages;

import com.test.configs.ConfigFileReader;

import static com.test.pages.HomePage.ACCEPT_ALL_COOKIES_BUTTON;

public class Navigate extends UtilPage {

    private static Navigate navigate;

    public static Navigate getInstance() {
        return (navigate == null) ? new Navigate() : navigate;
    }

    public void toCazooWebSite() {
        driver.get(ConfigFileReader.getInstance().getApplicationUrl());
        if (findElement(ACCEPT_ALL_COOKIES_BUTTON).isDisplayed())
            findElement(ACCEPT_ALL_COOKIES_BUTTON).click();
    }

    public void toCazooSiteWithoutCookies() {
        driver.get(ConfigFileReader.getInstance().getApplicationUrl());
    }

}
