package org.kdr.pages;

import org.kdr.actiondriver.ActionDriver;
import org.kdr.base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    private final ActionDriver actionDriver;
    public LoginPage(WebDriver driver) {
        // this.actionDriver = new ActionDriver(driver);
        this.actionDriver = TestBase.getActionDriver();
    }

    private final By loginField = By.name("username");
    private final By passwordField = By.xpath("//*[@name='password']");
    private final By loginBtn = By.cssSelector("button[type='submit']");
    private final By errorMsg = By.xpath("//*[contains(@class, 'oxd-alert-content-text')]");

    /*Method to perform Login*/
    public void login(String username, String password) {
        actionDriver.enterText(loginField, username);
        actionDriver.enterText(passwordField, password);
        actionDriver.clickElement(loginBtn);
    }

    /*Method to perform error message*/
    public boolean isErrorMessageDisplayed() {
        return actionDriver.isElementDisplayed(errorMsg);
    }

    /*Method to get the error message*/
    public String getErrorMessage() {
        return actionDriver.getElementText(errorMsg);
    }

    /*Verify if error message is correct*/
    public boolean verifyErrorMessage(String expcError) {
        return actionDriver.compareText(errorMsg, expcError);
    }
}
