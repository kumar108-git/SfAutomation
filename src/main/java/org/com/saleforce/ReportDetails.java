package org.com.saleforce;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ReportDetails extends BasePage{

    public ReportDetails(WebDriver driver){
        super(driver);
    }

    @Override
    public boolean isAt(){
        return true;
    }

    public boolean checkReportName(String reportName){
        boolean isTextPresent = false;
        if(isDOMReady()) {
            try {
                String reportTextXpath = String.format("//h1/span[@class='slds-page-header__title slds-truncate' and @title='%s']",reportName.trim(),reportName.trim());
                WebElement textEle = this.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(reportTextXpath)));
                isTextPresent = textEle != null && textEle.getText().trim().equals(reportName.trim());
                if (!isTextPresent) {
                    // If not found, try a more general approach
                    String generalXpath = String.format("//*[contains(@class, 'slds-page-header__title') and contains(text(), '%s')]", reportName.trim());
                    textEle = this.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(generalXpath)));
                    isTextPresent = textEle != null && textEle.getText().trim().equals(reportName.trim());
                }
            } catch (TimeoutException e){
                System.out.println("Element not found within the specified time: " + e.getMessage());
            }
        }
        return isTextPresent;
    }
}
