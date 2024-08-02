package org.com.salesforce.tests.salesforce;

import org.com.saleforce.CasesPage;
import org.com.saleforce.EditCases;
import org.com.saleforce.LoginPage;
import org.com.saleforce.NewCases;
import org.com.salesforce.tests.AbstractBaseTest;
import org.com.salesforce.util.Config;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SalesforceTest extends AbstractBaseTest {

    private LoginPage sfLogin;
    private CasesPage casesPage;
    private NewCases newCase;
    private EditCases editCases;

    @BeforeClass
    public void setPageObjects(){
        sfLogin = new LoginPage(driver);
        casesPage = new CasesPage(driver);
        newCase = new NewCases(driver);
        editCases = new EditCases(driver);
    }

    @Test
    public void loginToSf(){
        sfLogin.goToUrl(Config.get("salesforce.url"));
        Assert.assertTrue(sfLogin.isAt());
        sfLogin.login(Config.get("Username"),Config.get("Password"));
    }

    @Test(dependsOnMethods = "loginToSf")
    public void navigateToCases() throws InterruptedException {
        casesPage.clickCasesTab();
        Assert.assertTrue(casesPage.isAt());
        casesPage.createNewCaseWindow();
    }

    @Test(dependsOnMethods = "navigateToCases")
    public void createNewCase(){
        Assert.assertTrue(newCase.isAt());
        newCase.createNewCase("Status","New",
                "Priority","Medium",
                "Origin","Email",
                "Type","Mechanical");
    }

    @Test(dependsOnMethods = "createNewCase")
    public void editCases() throws InterruptedException{
        casesPage.clickCasesTab();
        editCases.isAt();
        editCases.clickByCaseNumber("00001047");
        editCases.clickCaseEditButton();
        editCases.changeCaseStatus("Status","Working");
        editCases.changeCasePriority("Priority","Low");
        editCases.enterCompanyName("SuppliedCompany","Fedex");
        editCases.enterPhoneNumber("SuppliedPhone","111-222-5241");
        editCases.enterWebName("SuppliedName","Fedex Company");
        editCases.addProduct("Product__c","GC1040");
        editCases.clickSave();

    }

}
