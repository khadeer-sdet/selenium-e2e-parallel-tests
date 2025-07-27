package org.kdr.pages;

import org.kdr.actiondriver.ActionDriver;
import org.kdr.base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {
    private final ActionDriver actionDriver;

    public HomePage(WebDriver driver) {
        // this.actionDriver = new ActionDriver(driver);
        this.actionDriver = TestBase.getActionDriver();
    }

    private final By adminTab = By.xpath("//*[text()='Admin']");
    private final By userIdButton = By.className("oxd-userdropdown-name");
    private final By logoutButton = By.xpath("//*[text()='Logout']");
    private final By orangeHRMLogo = By.xpath("//*[@class='oxd-brand-banner']//img");

    public boolean isAdminTabVisible() {
        return actionDriver.isElementDisplayed(adminTab);
    }

    public boolean isHomePageLogoVisible() {
        return actionDriver.isElementDisplayed(orangeHRMLogo);
    }

    public void logoutHRM() {
        actionDriver.clickElement(userIdButton);
        actionDriver.clickElement(logoutButton);
    }
}
