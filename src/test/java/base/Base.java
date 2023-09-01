package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.FileInputStream;
import java.time.Duration;
import java.util.Properties;

public class Base {

    public Base() {
        loadProperties();
    }

    public static WebDriver driver;
    public static Properties prop;

    private static final ThreadLocal<WebDriver> tDriver = new ThreadLocal<>();

    private static void setDriver(WebDriver driver) {

        tDriver.set(driver);
    }

    private static WebDriver getDriver() {

        return tDriver.get();
    }

    public static void loadProperties() {
        prop = new Properties();

        try {
            FileInputStream fis = new FileInputStream(".\\src\\test\\resources\\config.properties");
            prop.load(fis);
        } catch (Exception e) {
            System.out.println("[!] Properties file didn't load properly.");
            System.out.println("Exception Caused By: " + e.getCause());
        }
    }

    private static WebDriver getBrowser(String browserName) {

        if (browserName.equalsIgnoreCase("Chrome")) {
            driver = new ChromeDriver();
            setDriver(driver);
        } else if (browserName.equalsIgnoreCase("Firefox")) {
            driver = new FirefoxDriver();
            setDriver(driver);
        } else {
            System.out.println("[!] Wrong input. Initiating Chrome as default.");
            driver = new ChromeDriver();
            setDriver(driver);
        }

        return getDriver();

    }

    @BeforeSuite
    public static void setUp() {
        driver = getBrowser(prop.getProperty("browser"));
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get(prop.getProperty("url"));
    }

    @AfterSuite
    public static void cleanUp() {

        getDriver().quit();

    }
}
