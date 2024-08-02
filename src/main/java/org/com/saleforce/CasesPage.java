package org.com.saleforce;

import dev.failsafe.internal.util.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class CasesPage extends BasePage{

    @FindBy(xpath = "//one-app-nav-bar-item-root[@data-id='Case']//a[contains(@class, 'slds-context-bar__label-action')]")
    private WebElement casesTab;

    @FindBy(xpath = "(//a[@role='button' and @title='New'])")
    private WebElement newCaseButton;

    @FindBy(xpath = "(//a[@role='button' and @title='Change Owner'])")
    private WebElement changeOwner;

    public CasesPage(WebDriver driver){
        super(driver);
    }

    public void clickCasesTab() throws InterruptedException {
//        isDOMReady();
        isAtHomePage();
        Thread.sleep(Duration.ofSeconds(30));
        WebElement caseTab = this.wait.until(ExpectedConditions.elementToBeClickable(
                (By.xpath("//one-app-nav-bar-item-root[@data-id='Case']//a[contains(@class, 'slds-context-bar__label-action')]"))));
            ac.moveToElement(caseTab).click().build().perform();
//        js.executeScript("arguments[0].click",caseTab);
//        this.casesTab.click();
    }

    @Override
    public boolean isAt(){
        this.wait.until(ExpectedConditions.visibilityOf(newCaseButton));
        return newCaseButton.isDisplayed();
    }

    public void isAtHomePage(){
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
        boolean titleIsPresent = this.wait.until(ExpectedConditions.titleContains("| Salesforce"));
        Assert.isTrue(titleIsPresent,"Home Page title is not present");
    }

    public void createNewCaseWindow(){
        this.wait.until(ExpectedConditions.visibilityOf(newCaseButton));
        this.newCaseButton.click();
    }
}
