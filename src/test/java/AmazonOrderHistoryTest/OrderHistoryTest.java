package AmazonOrderHistoryTest;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

import org.testng.annotations.BeforeClass;

import org.testng.annotations.Test;
import AmazonOrderHistoryPages.*;
import Utils.ReadExcel;
import Utils.TakeScreenshots;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;

import org.apache.poi.ss.usermodel.DataFormatter;
//import org.apache.log4j.Logger;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;



@SuppressWarnings("unused") 
public class OrderHistoryTest {
    WebDriver driver;
    
    //For Logger
    private static final Logger logger = LogManager.getLogger(OrderHistoryTest.class);
    
 //  For Html reports
    ExtentHtmlReporter htmlReports;
    ExtentReports extent;
    ExtentTest test;
    
    
    //Page objects
    SearchOrderResults SOR;
    HomePage HP ;
    OrderPage OP;
    //Screnshots variable
    int ScreenShotNum;
    
   
    
  @BeforeClass
  public void BrowserSetUp(){
	  
	 System.setProperty("webdriver.chrome.driver", "C:\\S2MavenWorkSpace\\Drivers\\chromedriver.exe");  
	 driver=new ChromeDriver();
	 driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	 driver.get("https://www.amazon.in");
	 driver.manage().window().maximize();
	 driver.manage().deleteAllCookies();
	 System.out.println("Browser setup done");
 
	 
	 String currentDirectory = System.getProperty("user.dir");
	 String filename =currentDirectory+ "\\test-output\\htmlreport.html"   ;
				
  	 htmlReports = new ExtentHtmlReporter(filename);
	 extent = new ExtentReports();
	 extent.attachReporter(htmlReports);
	 htmlReports.config().setReportName("AmazonOrderHistoryReport");
	 htmlReports.config().setTheme(Theme.STANDARD);
	 htmlReports.config().setTestViewChartLocation(ChartLocation.TOP);
	 extent.setSystemInfo("OperatingSystem", "Windows7");
	 extent.setSystemInfo("Browser", "Chrome");
	 extent.setSystemInfo("Application", "AmazonOrderHistory");
	 extent.setSystemInfo("Envoirnment", "Prod");
	 System.out.println("Report setup done");
	
         }
  
    
  @BeforeMethod
  public void TextCasesStarted(){
	  System.out.println("***** Text Cases Started*****" );
  }
  
  
  @Test(priority=1)
  public void YourOrdersTest() throws InterruptedException {
   System.out.println("TC001_To Check user can aceess Order History through Order link from home page");
   test=extent.createTest("TC001_To Check user can aceess Order History through Order link from home page");
    HP =new HomePage(driver); 
    HP.LoginthroughOrderlink();
    String PageTitle=driver.getTitle();
    System.out.println("You are on" +" "+ PageTitle +" " + "Page");
    
    TakeScreenshots.CaptureScreenshots(driver, "TC001_03_Login Successful and user is on Your Orders Page");
    
   Assert.assertEquals(PageTitle, "Your Orders");
  }
  
  @Test(priority=2)
  public void AvailableTabsTest() {
	  System.out.println("TC002_To Check available tabs on Your orders page");
	  test=extent.createTest("TC002_To Check available tabs on Your orders page");
	  HP =new HomePage(driver);
	  String[] TabNames = HP.Tabs();
	 // TS= new TakeScreenshots();
	  TakeScreenshots.CaptureScreenshots(driver, "TC002_01_ Orders ,Buy Again,Open Orders, Cancelled Orders Sections are available on Your Order page");
	   //need to add assertion
	  Assert.assertEquals(TabNames,new String [] {"Orders","Buy Again","Open Orders","Cancelled Orders"});
	  	 	  
	  }
 
  @Test(priority=4,dataProvider="SearchOrder")
  public void SearchProductTest(String OrderText, String ExpectedText, String RunMode)
    {
      System.out.println("TC004_To search orders using diffrent search criteria" + "_" + OrderText);
      test=extent.createTest("TC004_To search orders using diffrent search criteria"+ "_" +OrderText);
      HP =new HomePage(driver);
      HP.SearchOrder(OrderText);
      SOR = new SearchOrderResults(driver);
   
      String ActualSearchOrderText = SOR.SearchResult(OrderText);
      System.out.println("Expected Result is :"+" " +ExpectedText);
      System.out.println("Actual Result is :"+" " + ActualSearchOrderText);
     
      Assert.assertEquals(ActualSearchOrderText, ExpectedText);
   
  }
  
  
  @Test(priority=3)
  public void SearchProductTestForincorrecttext()
     {
      System.out.println("TC003_To search orders using incorrect search criteria");
      test=extent.createTest("TC003_To search orders using incorrect search criteria");
      HP = new HomePage(driver);
      SOR = new SearchOrderResults(driver);
      HP.incorrectsearch();
      String ActualSearchOrderText = SOR.incorrectOrderText();
      String ExpectedResult="You can search by product title, order number, department, address, or recipient.";
      System.out.println("Expected Result is :"+ExpectedResult);
      System.out.println("Actual Result is :"+" " + ActualSearchOrderText);
     
      Assert.assertEquals(ActualSearchOrderText,ExpectedResult);
  }

 @Test(priority=7)
 public void BuyNowTest() {
	
	 System.out.println("TC007_To check that user can Buy Product immediately");
	 test=extent.createTest("TC007_To check that user can Buy Product immediately");
     HP =new HomePage(driver);
     HP.YourOrdersPage();
     OP = new OrderPage(driver);
     OP.Orderfilter();
     String Actualtext = OP.BuyNow();
    System.out.println("User is navigated to Payment method page");
    Assert.assertEquals(Actualtext, "Select a payment method");
 }
 
 @Test(priority=6)
 public void AddtoCartTest() {
	
	 System.out.println("TC006_To check that user can Add product to Cart");
	 test=extent.createTest("TC006_To check that user can Add product to Cart");
     HP =new HomePage(driver);
     
     OP = new OrderPage(driver);
     HP.YourOrdersPage();
     OP.Orderfilter();
     
     String Actualtext = OP.AddtoCart();
     System.out.println("Product is added in cart");
    Assert.assertEquals(Actualtext, "Added to Cart");
     
 }
 
@Test(priority=5,dataProvider="FilterOrder")
public void Filters(String Filter, String ExpectedSearchFilterText)
  {
   System.out.println("TC005_To search orders using Filters" + "_" + Filter);
   test=extent.createTest("TC005_To search orders using Filters" + "_" + Filter);
  
   SOR = new SearchOrderResults(driver);
   OP = new OrderPage(driver);
   HP = new  HomePage(driver);
   HP.YourOrdersPage();
   OP.ClearSearch();
   OP.Orderfilter2(Filter);
   String ActualSearchFilterText = SOR.FilterResult(Filter);
   
   System.out.println("Expected result is"+ " " +ExpectedSearchFilterText);
   System.out.println("Actual Result is :"+ " " + ActualSearchFilterText);
  
   Assert.assertEquals(ActualSearchFilterText,ExpectedSearchFilterText);

}
  
  @AfterMethod
  
  public void CheckResults(ITestResult Results) {
	if(Results.getStatus()==ITestResult.FAILURE) {
		test.log(Status.FAIL, "Test case is failed due to below problem");
		test.log(Status.FAIL,Results.getThrowable());
	}else if (Results.getStatus()==ITestResult.SUCCESS) {
	
		test.log(Status.PASS, "Test case is Pass");
	}else {
		test.log(Status.SKIP,Results.getThrowable());
	}
 	 
  }
  
  @AfterTest
  
  public void teardown() {
	  extent.flush();
  }
  
  @DataProvider
  public Object[][] SearchOrder() throws IOException {
		String currentDirectory = System.getProperty("user.dir");
		String dataFile =currentDirectory+ "\\src\\main\\java\\resources\\DataSheet.xlsx";
		String sheetName = "SearchOrder";
		//System.out.println(dataFile);
		Object[][] myTestData = ReadExcel.readTestData(dataFile, sheetName);
	
		return myTestData;

  }
  
  @DataProvider
  public Object[][] FilterOrder() throws IOException {
		String currentDirectory = System.getProperty("user.dir");
		String dataFile =currentDirectory+ "\\src\\main\\java\\resources\\DataSheet.xlsx";
		String sheetName = "Filters";
		//System.out.println(dataFile);
		Object[][] myTestData = ReadExcel.readTestData(dataFile, sheetName);
	
		return myTestData;

  }
  
  @AfterClass
  public void afterClass() {
	  
	  driver.quit(); 
	  
  }

  
    
 }
