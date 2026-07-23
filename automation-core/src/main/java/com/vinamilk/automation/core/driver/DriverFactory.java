package com.vinamilk.automation.core.driver;

import com.vinamilk.automation.core.config.ConfigManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Builds a {@link WebDriver} for the browser/mode selected via configuration:
 * local (WebDriverManager-resolved binary) or remote (Selenium Grid), so
 * individual tests never touch driver setup.
 */
public final class DriverFactory {

    private static final Logger LOG = LoggerFactory.getLogger(DriverFactory.class);

    private DriverFactory() {
    }

    public static WebDriver create() {
        ConfigManager config = ConfigManager.getInstance();
        BrowserType browser = BrowserType.fromString(config.get("browser", "chrome"));
        boolean headless = config.getBoolean("browser.headless", false);
        boolean gridEnabled = config.getBoolean("selenium.grid.enabled", false);

        MutableCapabilities options = buildOptions(browser, headless);

        if (gridEnabled) {
            return createRemoteDriver(config.get("selenium.grid.url"), options);
        }
        return createLocalDriver(browser, options);
    }

    private static MutableCapabilities buildOptions(BrowserType browser, boolean headless) {
        switch (browser) {
            case FIREFOX:
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("-headless");
                }
                return firefoxOptions;
            case EDGE:
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) {
                    edgeOptions.addArguments("--headless=new");
                }
                return edgeOptions;
            case CHROME:
            default:
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--remote-allow-origins=*", "--window-size=1920,1080",
                        "--disable-blink-features=AutomationControlled");
                chromeOptions.setExperimentalOption("excludeSwitches", new String[] {"enable-automation"});
                chromeOptions.setExperimentalOption("useAutomationExtension", false);
                if (headless) {
                    chromeOptions.addArguments("--headless=new");
                }
                return chromeOptions;
        }
    }

    private static WebDriver createLocalDriver(BrowserType browser, MutableCapabilities options) {
        switch (browser) {
            case FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                return new FirefoxDriver((FirefoxOptions) options);
            case EDGE:
                WebDriverManager.edgedriver().setup();
                return new EdgeDriver((EdgeOptions) options);
            case CHROME:
            default:
                WebDriverManager.chromedriver().setup();
                return new ChromeDriver((ChromeOptions) options);
        }
    }

    private static WebDriver createRemoteDriver(String gridUrl, MutableCapabilities options) {
        try {
            LOG.info("Connecting to Selenium Grid at {}", gridUrl);
            URL url = URI.create(gridUrl).toURL();
            return new RemoteWebDriver(url, options);
        } catch (MalformedURLException | IllegalArgumentException e) {
            throw new IllegalStateException("Invalid Selenium Grid URL: " + gridUrl, e);
        }
    }
}
