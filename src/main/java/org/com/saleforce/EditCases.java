package org.com.saleforce;

import org.openqa.selenium.*;
import org.openqa.selenium.json.JsonException;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

public class EditCases extends BasePage {

    @FindBy(xpath = "(//ul[@class='branding-actions slds-button-group slds-m-left--xx-small oneActionsRibbon forceActionsContainer']/li[@data-target-selection-name='sfdc:StandardButton.Case.NewCase']/a[@title='New'])")
    private WebElement newButton;

    @FindBy(xpath = "(//ul[@class='slds-button-group-list']/li[@data-target-selection-name='sfdc:StandardButton.Case.Edit']//lightning-button/button[@name='Edit'])")
    private WebElement caseEditButton;

    @FindBy(xpath = "(//button[@class='slds-button slds-button_brand' and @aria-disabled='false' and @name='SaveEdit'])")
    private WebElement saveButton;

    @FindBy(xpath = "(//div[contains(@data-target-selection-name,'sfdc:RecordField.Case.Status')]//lightning-base-combobox//button)")
    private WebElement caseStatusDropdown;

    public EditCases(WebDriver driver){
        super(driver);
    }

    public void clickByCaseNumber(String caseNumber){
        String caseNumberXpath = String.format("//a[@data-refid='recordId' and contains(@title,'%s')]",caseNumber);
        WebElement caseNumEle = this.wait.until(ExpectedConditions.elementToBeClickable(By.xpath(caseNumberXpath)));
        this.js.executeScript("arguments[0].click()",caseNumEle);
    }

    public void changeCaseStatus(String dropdownId, String caseStatusValue){
        selectDropDownValue(dropdownId,caseStatusValue);
    }

    public void changeCasePriority(String dropdownId, String priorityValue){
        selectDropDownValue(dropdownId,priorityValue);
    }

    public void addProduct(String dropdownId, String productValue){
        selectDropDownValue(dropdownId,productValue);
    }

    public void enterWebName(String webEleName,String webNameValue){
        enterInputText(webEleName,webNameValue);
    }

    public void enterCompanyName(String compEleName,String compNameValue){
        enterInputText(compEleName,compNameValue);
    }

    public void enterPhoneNumber(String phoneEleName,String phoneNumbValue){
        enterInputText(phoneEleName,phoneNumbValue);
    }

    public void clickSave(){

        saveButton.click();
    }

    @Override
    public boolean isAt(){
        this.js.executeScript("return document.readyState").toString().equals("complete");
        WebElement newBut = this.wait.until(ExpectedConditions.visibilityOf(newButton));
        return newBut.isDisplayed();
    }

    public void clickCaseEditButton() {
        try {
            // Wait for the unordered list to be present
            this.wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//ul[contains(@class, 'slds-button-group-list')]")
            ));

            // Find all Edit buttons within the unordered list
            List<WebElement> editButtons = this.driver.findElements(
                    By.xpath("//ul[contains(@class, 'slds-button-group-list')]//button[contains(@class, 'slds-button') and (normalize-space(text())='Edit' or @name='Edit')]")
            );

            if (editButtons.isEmpty()) {
                throw new NoSuchElementException("No Edit buttons found");
            }

            WebElement buttonToClick = null;

            // Try to find the visible and enabled button
            for (WebElement button : editButtons) {
                if (button.isDisplayed() && button.isEnabled()) {
                    buttonToClick = button;
                    break;
                }
            }

            // If no visible and enabled button found, we will try to click both the elements and see if any one works
            if (buttonToClick == null) {

                buttonToClick = editButtons.getFirst();
                // Scroll the button into view
                this.js.executeScript("arguments[0].scrollIntoView(true);", buttonToClick);
                // Wait for the button to be clickable
                wait.until(ExpectedConditions.elementToBeClickable(buttonToClick));
                // Try to click using Selenium
                try {
                    buttonToClick.click();
                } catch (ElementClickInterceptedException e) {
                    // If Selenium click fails, try JavaScript click
                    this.js.executeScript("arguments[0].click();", buttonToClick);
                }

                WebElement editPageStatusEle = this.wait.until(ExpectedConditions.visibilityOf(caseStatusDropdown));
                if(!(editPageStatusEle.isDisplayed() && editPageStatusEle.isEnabled())){
                    buttonToClick = editButtons.getLast();
                    // Scroll the button into view
                    this.js.executeScript("arguments[0].scrollIntoView(true);", buttonToClick);
                    // Wait for the button to be clickable
                    wait.until(ExpectedConditions.elementToBeClickable(buttonToClick));
                    // Try to click using Selenium
                    try {
                        buttonToClick.click();
                    } catch (ElementClickInterceptedException e) {
                        // If Selenium click fails, try JavaScript click
                        this.js.executeScript("arguments[0].click();", buttonToClick);
                    }
                }
            // if the edit button is visible and displayed click the element
            } else {
                // Scroll the button into view
                this.js.executeScript("arguments[0].scrollIntoView(true);", buttonToClick);
                // Wait for the button to be clickable
                wait.until(ExpectedConditions.elementToBeClickable(buttonToClick));
                // Try to click using Selenium
                try {
                    buttonToClick.click();
                } catch (ElementClickInterceptedException e) {
                    // If Selenium click fails, try JavaScript click
                    this.js.executeScript("arguments[0].click();", buttonToClick);
                }
            }
            System.out.println("Successfully clicked the Edit button.");
        } catch (Exception e) {
            System.out.println("Failed to find or click the Edit button: " + e.getMessage());

            // Print the page source for debugging
            System.out.println("Page source: " + driver.getPageSource());
        }
    }

//    public void clickCaseEditButton(){
//        WebElement editBar = this.wait.until(ExpectedConditions.elementToBeClickable(
////                By.xpath("//div[@class='slds-col slds-no-flex slds-grid slds-grid_vertical-align-center horizontal actionsContainer']"))
//                By.cssSelector("button[name='Edit']"))
//        );
//        this.jsClick(editBar);
//        this.caseEditButton.click();
//
//    }

//    public void clickCaseEditButton() {
//        try {
//            // Wait for the unordered list to be present
//            WebElement ulElement = this.wait.until(ExpectedConditions.presenceOfElementLocated(
//                    By.xpath("//ul[contains(@class, 'slds-button-group-list')]")
//            ));
//
//            // Find the Edit button within the unordered list
//            WebElement editButton = ulElement.findElement(
//                    By.xpath(".//button[@name='Edit']")
//            );
//
//            // Scroll the button into view
//            this.js.executeScript("arguments[0].scrollIntoView(true);", editButton);
//
//            // Wait for the button to be clickable
//            wait.until(ExpectedConditions.elementToBeClickable(editButton));
//
//            // Click the button
//            editButton.click();
//
//            System.out.println("Successfully clicked the Edit button.");
//        } catch (Exception e) {
//            System.out.println("Failed to find or click the Edit button: " + e.getMessage());
//
//            // Optionally, print the page source for debugging
////            System.out.println("Page source: " + driver.getPageSource());
//        }
//    }

//    public void clickCaseEditButton() {
//        analyzeDOMStructure();
//        waitForPageLoad();
//
//        WebElement editButton = null;
//
//        try {
//            editButton = findEditButtonInShadowDOM();
//        } catch (Exception e) {
//            System.out.println("Failed to find Edit button in Shadow DOM: " + e.getMessage());
//        }
//
//        if (editButton == null) {
//            try {
//                clickEditButtonUsingLightningAPI();
//                System.out.println("Attempted to click Edit button using Lightning API");
//                return;
//            } catch (Exception e) {
//                System.out.println("Failed to click Edit button using Lightning API: " + e.getMessage());
//            }
//        }
//
//        if (editButton == null) {
//            analyzeNetworkRequests();
//            throw new NoSuchElementException("Edit button not found after exhaustive search");
//        }
//
//        editButton.click();
//    }

//    public void clickCaseEditButton() {
//        // Wait for the page to load completely
//        this.wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete';"));
//
//        // Try multiple locator strategies
//        WebElement casesEditButton = null;
//        try {
//            // Try by XPath (simplified)
//            casesEditButton = this.wait.until(ExpectedConditions.elementToBeClickable(
//                    By.xpath("//button[contains(@class, 'slds-button') and @name='Edit']")));
//        } catch (TimeoutException e) {
//            try {
//                // Try by button text
//                casesEditButton = this.wait.until(ExpectedConditions.elementToBeClickable(
//                        By.xpath("//button[text()='Edit']")));
//            } catch (TimeoutException e2) {
//                // If still not found, try JavaScript executor
//                casesEditButton = (WebElement) this.js.executeScript(
//                        "return document.querySelector('button[name=\"Edit\"]')");
//            }
//        }
//
//        if (casesEditButton == null) {
//            throw new NoSuchElementException("Edit button not found");
//        }
//
//        // Scroll the button into view and click
//        this.js.executeScript("arguments[0].scrollIntoView(true);", casesEditButton);
//        this.wait.until(ExpectedConditions.elementToBeClickable(casesEditButton));
//        casesEditButton.click();
//    }

//    public void clickCaseEditButton(){
//
//        WebElement casesEditbutton = this.wait.until(ExpectedConditions.elementToBeClickable
//                (By.xpath("//ul[@class='slds-button-group-list']/li[@data-target-selection-name='sfdc:StandardButton.Case.Edit']//lightning-button/button[@name='Edit']")));
//        this.ac.moveToElement(casesEditbutton).click().build().perform();
//    }

    public void readTableContents(){
        List<Map<String,String>> tableData = parseTableData();
    }

    public List<Map<String, String>> parseTableData() {
        List<Map<String, String>> rowDataList = new ArrayList<>();

        // Wait for the table to be present
        WebElement table = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table[role=grid]")));

        // Extract column headers
        List<WebElement> headerElements = table.findElements(By.cssSelector("th"));
        List<String> columnNames = new ArrayList<>();
        for (WebElement header : headerElements) {
            WebElement spanElement = header.findElement(By.cssSelector("span.slds-truncate"));
            String headerText = spanElement != null ? spanElement.getText() : header.getText();
            if (!headerText.isEmpty()) {
                columnNames.add(headerText);
            }
        }

        // Extract row data
        List<WebElement> rows = table.findElements(By.cssSelector("tbody > tr"));
        for (WebElement row : rows) {
            Map<String, String> rowData = new HashMap<>();
            List<WebElement> cells = row.findElements(By.cssSelector("td, th"));

            int columnIndex = 0;
            for (WebElement cell : cells) {
                if (columnIndex >= columnNames.size()) break;

                String cellValue = extractCellValue(cell);

                if (!cellValue.isEmpty()) {
                    rowData.put(columnNames.get(columnIndex), cellValue);
                    columnIndex++;
                }
            }

            if (!rowData.isEmpty()) {
                rowDataList.add(rowData);
            }
        }

        return rowDataList;
    }

    private String extractCellValue(WebElement cell) {
        try {
            WebElement anchor = cell.findElement(By.tagName("a"));
            return anchor.getText();
        } catch (Exception e) {
            try {
                WebElement span = cell.findElement(By.cssSelector("span.slds-truncate"));
                return span.getText();
            } catch (Exception e2) {
                return cell.getText();
            }
        }
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

    public void enterInputText(String inputId, String inputValue){
        try {
            String inputElementXpath = String.format("//div[contains(@data-target-selection-name,'sfdc:RecordField.Case.%s')]" +
                    "//lightning-input/lightning-primitive-input-simple/div//div/input[@class='slds-input']", inputId);
            WebElement inputElement = this.wait.until(ExpectedConditions.elementToBeClickable(By.xpath(inputElementXpath)));
            js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", inputElement);
            inputElement.sendKeys(inputValue);

        } catch (TimeoutException e){
            throw new IllegalStateException("Time out trying to select input text value: " + inputValue,e);
        }
    }

    public void analyzeDOMStructure() {
        String pageSource = driver.getPageSource();
        System.out.println("Page source: " + pageSource);

        // Search for Edit button or related elements
        if (pageSource.contains("Edit")) {
            System.out.println("'Edit' text found in page source");
        } else {
            System.out.println("'Edit' text not found in page source");
        }
    }

    public WebElement findEditButtonInShadowDOM() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return (WebElement) js.executeScript(
                "return document.querySelector('one-record-home-flexipage2')." +
                        "shadowRoot.querySelector('forcegenerated-adg-rollup_component___force-generated__flexipage_-record-page___-case___-c-a-s-e__c___-case___-e-d-i-t')." +
                        "shadowRoot.querySelector('lightning-button-menu')." +
                        "shadowRoot.querySelector('lightning-button.menu-item-button[data-name=\"Edit\"]')");
    }

    public void waitForPageLoad() {

        this.wait.until(driver -> {
            boolean isPageLoaded = this.js.executeScript("return document.readyState").equals("complete");
            boolean isEditButtonPresent = !driver.findElements(By.xpath("//*[contains(text(), 'Edit')]")).isEmpty();
            return isPageLoaded && isEditButtonPresent;
        });
    }

    public void clickEditButtonUsingLightningAPI() {
        this.js.executeScript(
                "var event = new CustomEvent('pressButton', { detail: { name: 'Edit' } });" +
                        "document.dispatchEvent(event);"
        );
    }

    public void analyzeNetworkRequests() {
        LogEntries logs = driver.manage().logs().get(LogType.PERFORMANCE);
        logs.forEach(entry -> {
            try {
                JSONObject json = new JSONObject(entry.getMessage());
                JSONObject message = json.getJSONObject("message");
                String method = message.getString("method");
                if (method.equals("Network.responseReceived")) {
                    System.out.println("Network request: " + message);
                }
            } catch (JsonException e) {
                e.printStackTrace();
            }
        });
    }

}
