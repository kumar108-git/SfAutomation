package org.com.saleforce;

import dev.failsafe.internal.util.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;

public class Reports extends BasePage {

    @FindBy(xpath = "(//nav[@class='slds-context-bar__secondary navCenter']/div/one-app-nav-bar-item-root" +
            "[@data-target-selection-name='sfdc:TabDefinition.standard-report']/a[@title='Reports'])")
    private WebElement reportTabElement;

    @FindBy(xpath = "(//div[@class='slds-page-header slds-has-bottom-magnet']//div[@class='slds-breadcrumb__item slds-line-height_reset']/span)")
    private WebElement reportTextElement;

    public Reports(WebDriver driver){
        super(driver);
    }

    @Override
    public boolean isAt(){
        WebElement reportTextEle = this.wait.until(ExpectedConditions.visibilityOf(reportTextElement));
        return reportTextEle.getText().equals("Reports");
    }

    public void isAtHomePage(){
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
        boolean titleIsPresent = this.wait.until(ExpectedConditions.titleIs("Home | Salesforce"));
        Assert.isTrue(titleIsPresent,"Home Page title is not present");
    }

    public void clickReportsTab(){
        if(isDOMReady()){
            this.ac.moveToElement(reportTabElement).click().build().perform();
        }

    }

    public void navigateToReportOptions(String optionName){
        if(isDOMReady()) {
            String navgiateSidebarXpath = String.format("//nav[@class='slds-nav-vertical']//ul//li/a[@class='slds-nav-vertical__action' and @title='%s']", optionName);
            WebElement navOptionEle = this.wait.until(ExpectedConditions.elementToBeClickable(By.xpath(navgiateSidebarXpath)));
            jsClick(navOptionEle);
        }
    }

    public void navigateToReport(String reportName) throws Throwable {
        if(isDOMReady()) {
            String reportXpath = String.format("//a[contains(text(),'%s')]", reportName);
            WebElement tableEle = this.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='slds-scrollable_y']")));

            scrollTableToBottom(tableEle);
//            By locateEle = By.xpath(reportXpath);
//            clickLazyLoadedElement(locateEle,5);
            WebElement reportNameLinkEle = driver.findElement(By.xpath(reportXpath));
            this.js.executeScript("arguments[0].scrollIntoView(true);", reportNameLinkEle);
            WebElement reportNameToClick = this.wait.until(ExpectedConditions.elementToBeClickable(By.linkText(reportName)));
            this.js.executeScript("arguments[0].click()",reportNameToClick);
        }
    }

    public void navigateToActionButton(String reportName) throws InterruptedException {
        Integer rowNum = getRowNumberByReportName(reportName);
        String actionButtonEle = String.format("//tr[@data-row-number=%s]//td[@data-col-key-value='6-action-6']",String.valueOf(rowNum));
        WebElement actionButton = this.wait.until(ExpectedConditions.elementToBeClickable(By.xpath(actionButtonEle)));
        js.executeScript("arguments[0].scrollIntoView(true);", actionButton);
        actionButton.click();
        String menuEleXpath = String.format("//tr[@data-row-number=%s]//td[@data-col-key-value='6-action-6']" +
                "//slot//a/span[contains(text(),'Run')]",rowNum);
        WebElement menuRunEle = this.wait.until(ExpectedConditions.elementToBeClickable(By.xpath(menuEleXpath)));
        this.ac.moveToElement(actionButton).moveToElement(menuRunEle).click().perform();

    }

    public void clickLazyLoadedElement(By locator, int maxScrollAttempts) {

        for (int i = 0; i < maxScrollAttempts; i++) {
            try {
                // Try to find the element
                WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
                // If element is found, scroll to it and click
                js.executeScript("arguments[0].scrollIntoView(true);", element);
                wait.until(ExpectedConditions.elementToBeClickable(element));
                element.click();
                System.out.println("Successfully clicked the element.");
                return;
            } catch (TimeoutException e) {
                // If element is not found, scroll down
//                js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
                this.ac.moveToElement(this.driver.findElement(By.tagName("body"))).sendKeys(Keys.END).perform();
                System.out.println("Scrolling down... Attempt " + (i + 1));

                // Wait for content to load
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        throw new NoSuchElementException("Element not found after " + maxScrollAttempts + " scroll attempts.");
    }

    public void scrollTableToBottom(WebElement scrollableElement) {

        long lastHeight = (long) this.js.executeScript("return arguments[0].scrollHeight", scrollableElement);

        while (true) {
            this.js.executeScript("arguments[0].scrollTop = arguments[0].scrollHeight", scrollableElement);

            try {
                Thread.sleep(1000); // Wait for potential lazy-loading
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            long newHeight = (long) this.js.executeScript("return arguments[0].scrollHeight", scrollableElement);
            if (newHeight == lastHeight) {
                break; // Exit if no new content is loaded
            }
            lastHeight = newHeight;
        }
    }

    public Integer getRowNumberByReportName(String reportName){
        Integer rowNumber = 0;
        WebElement tableEle = this.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='slds-scrollable_y']")));
        scrollTableToBottom(tableEle);

        List<WebElement> rows = this.driver.findElements(By.xpath("//tr[@lwc-392cvb27u8q]"));
        for(int i=0; i < rows.size(); i++ ){
            WebElement row = rows.get(i);
            List<WebElement> reportNameElements = row.findElements(By.xpath(".//lightning-formatted-url/a"));
            if(!reportNameElements.isEmpty()){
                String currentReportName = reportNameElements.getFirst().getText();
                if(currentReportName.equals(reportName)){
                    rowNumber = i;
                }
            }
        }
        return rowNumber;
    }

}
