package org.com.saleforce;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class NewCases extends BasePage {

    @FindBy(xpath = "(//button[@id='combobox-button-171' and @data-value='New'])")
    private WebElement caseStatus;

    @FindBy(xpath = "(//button[@id='combobox-button-182' and @data-value='Medium'])")
    private WebElement casePriority;

    @FindBy(xpath = "(//button[@id='combobox-button-193' and @data-value='None'])")
    private WebElement caseOrigin;

    @FindBy(xpath = "(//button[@class='slds-button slds-button_brand' and @aria-disabled='false' and @name='SaveEdit'])")
    private WebElement saveButton;

    @FindBy(xpath = "(//a[@class='forceActionLink' and @title='New'])")
    private WebElement newCase;

    @FindBy(xpath = "(//div[@data-target-selection-name='sfdc:RecordField.Case.CaseNumber']//dd/div/span/slot[@name='outputField']/lightning-formatted-text[@data-output-element-id='output-field'])")
    private WebElement caseNumber;

    public NewCases(WebDriver driver){
        super(driver);
    }

    @Override
    public boolean isAt(){
        WebElement newButton = this.wait.until(ExpectedConditions.visibilityOf(newCase));
        return newButton.isDisplayed();
    }

    public void selectStatus(String statusDropdownId,String StatusValue){
        selectDropDownValue(statusDropdownId,StatusValue);
    }

    public void selectPriority(String priorityDropdownId,String priorityValue){
        selectDropDownValue(priorityDropdownId, priorityValue);
    }

    public void selectCaseOrigin(String caseOriginId, String caseValue){
        selectDropDownValue(caseOriginId,caseValue);
    }

    public void selectType(String caseTypeDropdownId, String caseTypeValue){
        selectDropDownValue(caseTypeDropdownId,caseTypeValue);
    }

    public void copyCaseNumber(){
        this.js.executeScript("return document.readyState").toString().equals("complete");
        String caseNum = caseNumber.getText();
        this.values.put("caseNumber",caseNum);
    }

    public void createNewCase(String statusDropdownId,String StatusValue,
                              String priorityDropdownId,String priorityValue,
                              String caseOriginId, String caseValue,
                              String caseTypeDropdownId, String caseTypeValue
                                ){

        selectStatus(statusDropdownId,StatusValue);
        selectPriority(priorityDropdownId,priorityValue);
        selectCaseOrigin(caseOriginId,caseValue);
        selectType(caseTypeDropdownId,caseTypeValue);
        this.saveButton.click();
        copyCaseNumber();

    }



    public void selectDropDownValue(String dropdownName, String value){

//        WebElement dropDownButton = this.wait.until(ExpectedConditions.elementToBeClickable(By.id(dropDownId)));
//        dropDownButton.click();
//        String optionXpath = String.format("//div[@id='dropdown-element-%s']//lightning-base-combobox-item[@data-value=%s]",dropDownId.split("-")[2],value);
//
//        WebElement option = this.wait.until(ExpectedConditions.elementToBeClickable(By.xpath(optionXpath)));
//
//        JavascriptExecutor js = (JavascriptExecutor) this.driver;
//        js.executeScript("arguments[0].click();",option);
//
//        this.wait.until(ExpectedConditions.attributeToBe(By.id(dropDownId),"data-value",value));
        try {
            // Click the dropdown to open it
            String dropDnButtonXpath = String.format("//div[contains(@data-target-selection-name,'sfdc:RecordField.Case.%s')]//lightning-base-combobox//button",dropdownName);
            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(dropDnButtonXpath)));
            js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", dropdown);
            js.executeScript("arguments[0].click();", dropdown);
            // Wait for the dropdown options to be visible
            String optionsLocatorXpath = String.format("//div[contains(@data-target-selection-name,'sfdc:RecordField.Case.%s')]" +
                    "//lightning-base-combobox//lightning-base-combobox-item",dropdownName);
            By optionsLocator = By.xpath(optionsLocatorXpath);
            wait.until(ExpectedConditions.visibilityOfElementLocated(optionsLocator));

            // Find and click the desired option
            String optionLocXpath = String.format("//div[contains(@data-target-selection-name,'sfdc:RecordField.Case.%s')]" +
                    "//lightning-base-combobox//lightning-base-combobox-item[@data-value='%s']",dropdownName,value);
            WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath(optionLocXpath)));
            js.executeScript("arguments[0].click();", option);

            // Wait for the dropdown to close and the selection to be applied
            wait.until(ExpectedConditions.attributeToBe(By.xpath(optionLocXpath), "aria-checked", "true"));

            // Verify the selection
//            String optionTextXpath = String.format("//button[contains(@class,'slds-combobox__input') and @data-value='%s' and @aria-disabled = 'false' and @aria-label='%s']//span[@class='slds-truncate']",value,dropdownName);
//            WebElement selectedValue = wait.until(ExpectedConditions.presenceOfElementLocated(
//                    By.xpath(optionTextXpath)));
//            if (!selectedValue.getText().equals(value)) {
//                throw new IllegalStateException("Failed to select the desired value: " + value);
//            }

        } catch (TimeoutException e) {
            throw new IllegalStateException("Timed out trying to select dropdown value: " + value, e);
        } catch (StaleElementReferenceException e) {
            // If we encounter a stale element, retry the operation once
//            selectDropdownValue(dropdownId, value);
        }
    }
    }


