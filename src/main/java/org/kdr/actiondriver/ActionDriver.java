package org.kdr.actiondriver;

import org.apache.logging.log4j.Logger;
import org.kdr.base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ActionDriver {

    private final WebDriver driver;
    private final WebDriverWait wait;
    public static final Logger logger = TestBase.logger;

    public ActionDriver(WebDriver driver) {
        // this.driver = driver;
        this.driver = TestBase.getDriver();
        int explicitWait = Integer.parseInt(TestBase.getProp().getProperty("explicitWait"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
        logger.info("WebDriver instance is created");
    }

    public void clickElement(By by) {
        String elementDesc = getElementDescription(by);
        try {
            waitForElementToBeClickable(by);
            driver.findElement(by).click();
            logger.info("Element is clicked--> " + elementDesc);
        } catch (Exception e) {
            logger.info("Unable to clikc element: " + e.getMessage());
        }
    }

    public void enterText(By by, String text) {
        String elementDesc = getElementDescription(by);
        try {
            waitForElementToBeVisible(by);
            driver.findElement(by).clear();
            driver.findElement(by).sendKeys(text);
            logger.info("Text entered in--> " + elementDesc);
        } catch (Exception e) {
            logger.info("Unable to enter text: " + e.getMessage());
        }
    }

    public String getElementText(By by) {
        try {
            waitForElementToBeVisible(by);
            return driver.findElement(by).getText();
            
        } catch (Exception e) {
            logger.info("Unable to get text: " + e.getMessage());
            return "";
        }
    }

    public boolean compareText(By by, String text) {
        try {
            waitForElementToBeVisible(by);
            String actual = driver.findElement(by).getText();
            return text.equals(actual);
        } catch (Exception e) {
            logger.info("Unable to compare text: " + e.getMessage());
            return false;
        }
    }

    public boolean isElementDisplayed(By by) {
        try {
            waitForElementToBeVisible(by);
            return driver.findElement(by).isDisplayed();
        } catch (Exception e) {
            logger.info("Element not displayed: " + e.getMessage());
            return false;
        }
    }

    public void waitForPageLoad(int seconds) {
        try {
            wait.withTimeout(Duration.ofSeconds(seconds)).until(
                    WebDriver -> ((JavascriptExecutor) WebDriver)
                            .executeScript("return document.readyState").equals("complete"));
            logger.info("Page loaded successfully");
        } catch (Exception e) {
            logger.info("Page didn't load within: " + seconds + "\n" + e.getMessage());
        }
    }

    public void scrollToElement(By by) {
        try {
            waitForElementToBeVisible(by);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement element = driver.findElement(by);
            js.executeScript("arguments[0],scrollIntoView(true)", element);
        } catch (Exception e) {
            logger.info("Unable to scrollIntoElement: " + e.getMessage());
        }
    }

    public void waitForElementToBeClickable(By by) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(by));
        } catch (Exception e) {
            logger.info("Element not clickable: " + e.getMessage());
        }
    }

    public void waitForElementToBeVisible(By by) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception e) {
            logger.info("Element not visible: " + e.getMessage());
        }
    }

    /* Method to get the description of an element using By locator */
    public String getElementDescription(By by) {
        // Check for null driver or locator
        if (driver == null)
            return "Driver is null";
        if (by == null)
            return "Locator is null";
        try {
            WebElement element = driver.findElement(by);
            // Get element description
            String name = element.getDomAttribute("name");
            String id = element.getDomAttribute("id");
            String text = element.getText();
            String className = element.getDomAttribute("class");
            String placeholder = element.getDomAttribute("placeholder");
            // Return the description based on element attributes
            if (isNotEmpty(name))
                return truncate("Element with name: " + name, 50);
            else if(isNotEmpty(id))
                return truncate("Element with id: " + id, 50);
            else if(isNotEmpty(text))
                return truncate("Element with text: " + text, 50);
            else if(isNotEmpty(className))
                return truncate("Element with className: " + className, 50);
            else
                return truncate("Element with placeholder: " + placeholder, 50);
        } catch (Exception e) {
            return "Unable to find the locator: " + e.getMessage();
        }
    }

    // Utility method to check whether the string is not null or empty
    private boolean isNotEmpty(String text) {
        return text != null && !text.isEmpty();
    }

    // Utility to truncate long String
    private String truncate(String text, int length) {
        if(isNotEmpty(text) && text.length() > length)
            return text.substring(0, length - 3) + "...";
        else return text;
    }
}
