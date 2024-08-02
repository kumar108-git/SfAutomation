package org.com.salesforce.tests.salesforce;

import org.com.saleforce.LogOut;
import org.com.saleforce.LoginPage;
//import org.com.saleforce.ReportDetails;
import org.com.saleforce.Reports;
import org.com.salesforce.tests.AbstractBaseTest;
import org.com.salesforce.util.Config;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ReportTest extends AbstractBaseTest {


    private LoginPage sfLogin;
    private Reports reports;
//    private ReportDetails reportDetails;
    private LogOut logout;

    @BeforeClass
    public void setUpTestPages(){
        sfLogin = new LoginPage(driver);
        reports = new Reports(driver);
//        reportDetails = new ReportDetails(driver);
        logout = new LogOut(driver);
    }

    @Test
    public void loginToSf(){
        sfLogin.goToUrl(Config.get("salesforce.url"));
        Assert.assertTrue(sfLogin.isAt());
        sfLogin.login(Config.get("Username"),Config.get("Password"));
    }

    @Test(dependsOnMethods = "loginToSf")
    public void navigateToReport() throws Throwable{
        reports.isAtHomePage();
        reports.clickReportsTab();
        reports.isAt();
        reports.navigateToReportOptions("All Reports");
        reports.navigateToActionButton("Total Partner Marketing Budget");
        logout.logoutFromSF();
//        reports.navigateToReport("Total Partner Marketing Budget");
//        Assert.assertTrue(reportDetails.checkReportName("Total Partner Marketing Budget"));
    }




}
