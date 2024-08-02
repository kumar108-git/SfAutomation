package org.com.salesforce.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.com.salesforce.util.Config;
import org.com.salesforce.util.Constants;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractBaseTest {

    protected WebDriver driver;
    private static final Logger log = LoggerFactory.getLogger(AbstractBaseTest.class);

    @BeforeSuite
    public void setupConfig() throws MalformedURLException {
        Config.initialize();
    }

    @BeforeClass
    public void setDriver(ITestContext ctx) throws MalformedURLException {
        driver = configDriver();
        log.info(String.format("Grid enabled %s",Config.get(Constants.GRID_ENABLED)));
        ctx.setAttribute(Constants.DRIVER, driver);
    }

    private WebDriver configDriver() throws MalformedURLException {
        driver = Boolean.parseBoolean(Config.get(Constants.GRID_ENABLED)) ? getRemoteDriver() : getLocalDriver();
        return driver;
    }

    private WebDriver getRemoteDriver() throws MalformedURLException {
        Capabilities capabilities = new ChromeOptions();
        if(Constants.FIREFOX.equalsIgnoreCase(Config.get(Constants.BROWSER))){
            capabilities = new FirefoxOptions();
        }
        String urlFormat = Config.get(Constants.GRID_URL_FORMAT);
        String hubHost = Config.get(Constants.GRID_HUB_HOST);
        String url = String.format(urlFormat, hubHost);
        log.info("grid url: {}", url);
        driver = new RemoteWebDriver(new URL(url), capabilities);
        return driver;
    }
    private WebDriver getLocalDriver(){
        WebDriverManager.chromedriver().setup();
        ChromeOptions chOptions = getChromeOptions();
        chOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        chOptions.setExperimentalOption("useAutomationExtension", false);
        chOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        chOptions.setExperimentalOption("prefs", prefs);
        driver = new ChromeDriver(chOptions);
        return driver;
    }

//    public WebDriver setDriver(){
//        WebDriverManager.chromedriver().setup();
////        driver.manage().window().maximize();
//        return driver;
//    }


    private static ChromeOptions getChromeOptions() {
        ChromeOptions chOptions = new ChromeOptions();
        chOptions.addArguments("--disable-notifications");
        chOptions.addArguments("--start-maximized");
        chOptions.addArguments("disable-infobars");
        chOptions.addArguments("--disable-suggestions-ui");
        chOptions.addArguments("--disable-suggestions-service");
        chOptions.addArguments("--disable-search-engine-choice-screen");
        chOptions.addArguments("--disable-default-apps");
        chOptions.addArguments("--disable-extensions");
        chOptions.addArguments("--disable-autofill-keyboard-accessory-view");
        chOptions.addArguments("--disable-save-password-bubble");
        chOptions.addArguments("--disable-translate");
        return chOptions;
    }

    @AfterClass
    public void quitDriver(ITestContext context){
        WebDriver driver = (WebDriver) context.getAttribute(Constants.DRIVER);
        if(driver != null){
            this.driver.quit();
            log.info("WebDriver instance quit and removed from test context");
        }
        context.removeAttribute(Constants.DRIVER);
    }

}
