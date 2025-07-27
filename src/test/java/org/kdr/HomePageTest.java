package org.kdr;

import org.kdr.base.TestBase;
import org.kdr.pages.HomePage;
import org.kdr.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HomePageTest extends TestBase {
    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setUp() {
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
    }

    @Test
    public void tc_05_verify_homepage_logo() {
        loginPage.login(getProp().getProperty("username"), getProp().getProperty("secret"));
        Assert.assertTrue(homePage.isHomePageLogoVisible());
    }
}
