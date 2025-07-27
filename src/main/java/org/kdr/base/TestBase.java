package org.kdr.base;

import org.apache.logging.log4j.Logger;
import org.kdr.actiondriver.ActionDriver;
import org.kdr.utilities.LoggerManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class TestBase {
    protected static Properties prop;
//    protected static WebDriver driver;
//    private static ActionDriver actionDriver;
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
    public static final Logger logger = LoggerManager.getLogger(TestBase.class);

    /*Load configuration file*/
    @BeforeSuite
    public void loadConfig() throws IOException {
        prop = new Properties();
        FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
        prop.load(fis);
        logger.info("config.properties file loaded!");
    }

    @BeforeMethod
    public void setup() {
        logger.info("Driver setup for: " + this.getClass().getSimpleName());
        launchBrowser();
        configBrowser();
        logger.info("WebDriver Initialized and Browser Maximised");
        /*// Initialize the actionDriver only once
        if (actionDriver == null) {
            actionDriver = new ActionDriver(driver);
        }*/
        // Initialize action for the current thread
        actionDriver.set(new ActionDriver(getDriver()));
        logger.info("ActionDriver Initialized");
    }

    /*Initialize the WebDriver based on browse defined in properties file*/
    public void launchBrowser() {
        String browser = prop.getProperty("browser");
        if (browser.equalsIgnoreCase("chrome")) {
            // driver = new ChromeDriver();
            driver.set(new ChromeDriver());
        } else if (browser.equalsIgnoreCase("firefox")) {
            // driver = new FirefoxDriver();
            driver.set(new FirefoxDriver());
        } else if (browser.equalsIgnoreCase("edge")) {
            // driver = new EdgeDriver();
            driver.set(new EdgeDriver());
        } else if (browser.equalsIgnoreCase("ie")) {
            // driver = new InternetExplorerDriver();
            driver.set(new InternetExplorerDriver());
        } else {
            throw new IllegalArgumentException("Undefined/invalid browser selected");
        }
    }

    /*Apply waits, maximize browser, open URL*/
    private void configBrowser() {
        int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        getDriver().manage().window().maximize();
        String url = prop.getProperty("url");
        getDriver().get(url);
    }

    public static WebDriver getDriver() {
        if (driver.get() == null) {
            logger.info("WebDriver is not initialized");
            throw new RuntimeException("WebDriver is not initialized");
        }
        return driver.get();
    }

    public static Properties getProp() {
        return prop;
    }

    public static ActionDriver getActionDriver() {
        if (actionDriver.get() == null) {
            logger.info("ActionDriver is not initialized");
            throw new RuntimeException("ActionDriver is not initialized");
        }
        return actionDriver.get();
    }

    public void staticWait(int seconds) {
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
    }

    @AfterMethod
    public void teardown() {
        if (driver != null) {
            driver.get().quit();
        }
        driver = null;
        actionDriver = null;
    }
}
