package org.kdr;

import org.kdr.base.TestBase;
import org.kdr.pages.HomePage;
import org.kdr.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginPageTest extends TestBase {

    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setUp() {
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
    }

    @Test
    public void tc_03_verify_valid_login() {
        loginPage.login(prop.getProperty("username"), prop.getProperty("secret"));
        Assert.assertTrue(homePage.isHomePageLogoVisible());
        homePage.logoutHRM();
    }

    @Test
    public void tc_04_verify_invalid_login() {
        loginPage.login(prop.getProperty("username"), "admin");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed());
        Assert.assertTrue(loginPage.verifyErrorMessage("Invalid credentials"));
    }
}
