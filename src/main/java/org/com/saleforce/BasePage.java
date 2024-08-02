package org.com.saleforce;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;

public abstract class BasePage {
    protected final WebDriver driver;
    protected WebDriverWait wait;
    protected JavascriptExecutor js;
    protected Actions ac ;
    protected HashMap<String,String> values;

    public BasePage(WebDriver driver){
        this.driver = driver;
        if(driver != null){
            this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            this.js = (JavascriptExecutor) this.driver;
            this.ac = new Actions(this.driver);
        }
        PageFactory.initElements(driver,this);
        this.values = new HashMap<>();
    }

    public abstract boolean isAt();

    public boolean isDOMReady(){
       return this.wait.until( driver -> {
            return js.executeScript("return document.readyState").equals("complete");
        });
    }

    public void jsClick(WebElement element){
        this.js.executeScript("arguments[0].click()",element);
    }

    }