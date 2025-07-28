package org.kdr.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.kdr.actiondriver.ActionDriver;
import org.kdr.utilities.LoggerManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;


public class TestBase {
    protected static Properties prop;
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
    public static final Logger logger = LoggerManager.getLogger(TestBase.class);

    protected ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);

    // Getter method for soft assert
    public SoftAssert getSoftAssert() {
        return softAssert.get();
    }

    @BeforeSuite
    public void loadConfig() throws IOException {
        // Load the configuration file
        prop = new Properties();
        FileInputStream fis = new FileInputStream(
                System.getProperty("user.dir") + "/src/main/resources/config.properties");
        prop.load(fis);
        logger.info("config.properties file loaded");

        // Start the Extent Report
        // ExtentManager.getReporter(); --This has been implemented in TestListener
    }

    @BeforeMethod
    public synchronized void setup() {
        System.out.println("Setting up WebDriver for:" + this.getClass().getSimpleName());
        launchBrowser();
        configureBrowser();
        staticWait(2);
        // Sample logger message
        logger.info("WebDriver Initialized and Browser Maximized");

        // Initialize ActionDriver for the current Thread
        actionDriver.set(new ActionDriver(getDriver()));
        logger.info("ActionDriver initialized for thread: " + Thread.currentThread().getId());
    }

    /*
     * Initialize the WebDriver based on browser defined in config.properties file
     */
    private synchronized void launchBrowser() {
        String browser = prop.getProperty("browser");
        if (browser.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions(); // Create Chrome options
            options.addArguments("--headless"); // Run Chrome in headless mode
            options.addArguments("--disable-gpu"); // Disable GPU for headless mode
            //options.addArguments("--window-size=1920,1080"); // Set window size
            options.addArguments("--disable-notifications"); // Disable browser notifications
            options.addArguments("--no-sandbox"); // Required for some CI environments like Jenkins
            options.addArguments("--disable-dev-shm-usage"); // Resolve issues in resource-limited environments
            // driver = new ChromeDriver();
            driver.set(new ChromeDriver(options)); // New Changes as per Thread
            // ExtentManager.registerDriver(getDriver());
            logger.info("ChromeDriver Instance is created.");
        } else {
            throw new IllegalArgumentException("Browser Not Supported:" + browser);
        }
    }

    /*
     * Configure browser settings such as implicit wait, maximize the browser and
     * navigate to the URL
     */
    private void configureBrowser() {
        // Implicit Wait
        int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        // maximize the browser
        getDriver().manage().window().maximize();
        // Navigate to URL
        getDriver().get(prop.getProperty("url"));
    }

    @AfterMethod
    public synchronized void tearDown() {
        if (getDriver() != null) {
            try {
                getDriver().quit();
            } catch (Exception e) {
                System.out.println("unable to quit the driver:" + e.getMessage());
            }
        }
        logger.info("WebDriver instance is closed.");
        driver.remove();
        actionDriver.remove();
    }

    /*
     *
     * //Driver getter method public WebDriver getDriver() { return driver; }
     */

    // Getter Method for WebDriver
    public static WebDriver getDriver() {
        if (driver.get() == null) {
            System.out.println("WebDriver is not initialized");
            throw new IllegalStateException("WebDriver is not initialized");
        }
        return driver.get();

    }

    // Getter Method for ActionDriver
    public static ActionDriver getActionDriver() {
        if (actionDriver.get() == null) {
            System.out.println("ActionDriver is not initialized");
            throw new IllegalStateException("ActionDriver is not initialized");
        }
        return actionDriver.get();
    }

    // Getter method for prop
    public static Properties getProp() {
        return prop;
    }

    // Driver setter method
    public void setDriver(ThreadLocal<WebDriver> driver) {
        this.driver = driver;
    }

    // Static wait for pause
    public void staticWait(int seconds) {
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
    }
}