package org.com.saleforce;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LogOut extends BasePage{

    @FindBy(xpath = "//span[contains(@class,'oneUserProfileCardTrigger')]//button")
    private WebElement userProfileIcon;

    @FindBy(xpath = "//div[@class='oneUserProfileCard']//div[@class='profile-card-toplinks']/a[@class='profile-link-label logout uiOutputURL']")
    private WebElement logoutLinkXpath;

    public LogOut(WebDriver driver){
        super(driver);
    }

    @Override
    public boolean isAt(){
        return true;
    }

    public void logoutFromSF(){
        WebElement userIconEle = this.wait.until(ExpectedConditions.elementToBeClickable(userProfileIcon));
        userIconEle.click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        WebElement logoutLink = this.wait.until(ExpectedConditions.elementToBeClickable(logoutLinkXpath));
        logoutLink.click();
    }
}
