package org.com.saleforce;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    @FindBy(xpath = "(//input[@id='username' and @type='email'])")
    private WebElement userName;

    @FindBy(xpath = "((//input[@id='password' and @type='password']))")
    private WebElement password;

    @FindBy(xpath = "(//input[@id='Login' and @type='submit'])")
    private WebElement loginButton;

    public LoginPage(WebDriver driver){
        super(driver);
    }

    @Override
    public boolean isAt(){
        this.wait.until(ExpectedConditions.visibilityOf(userName));
        return userName.isDisplayed();
    }

    public void goToUrl(String url){
        this.driver.get(url);
    }

    public void login(String userName, String password){
        this.userName.sendKeys(userName);
        this.password.sendKeys(password);
        this.loginButton.click();
    }
}
