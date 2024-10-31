package com.test;

import com.test.pages.HomePage;
import com.test.pages.Navigate;
import com.test.pages.UtilPage;

public class ObjectRepository {

    public ObjectRepository() {
        //Empty constructor
    }

    protected HomePage homePage() {
        return HomePage.getInstance();
    }

    protected Navigate navigate() {
        return Navigate.getInstance();
    }

    protected UtilPage utilPage() {
        return UtilPage.getInstance();
    }
}
