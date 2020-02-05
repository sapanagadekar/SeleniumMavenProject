package AmazonOrderHistoryTest;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
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
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import org.apache.poi.ss.usermodel.DataFormatter;
//import org.apache.log4j.Logger;
//import org.apache.logging.log4j.Logger;
//import org.apache.logging.log4j.LogManager;

import org.apache.log4j.xml.DOMConfigurator;

@SuppressWarnings("unused") 
public class OrderHistoryTest {
    WebDriver driver;
    SearchOrderResults SOR;
    HomePage HP ;
  
    OrderPage OP;
    int ScreenShotNum;
    
    //public static Logger Log = Logger.getLogger(OrderHistoryTest.class.getName());
//    private static final Logger logger = LogManager.getLogger(OrderHistoryTest.class);
    
  @BeforeClass
  public void BrowserSetUp(){
	  
	 System.setProperty("webdriver.chrome.driver", "C:\\S2MavenWorkSpace\\Drivers\\chromedriver.exe");  
	 driver=new ChromeDriver();
	 driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	 driver.get("https://www.amazon.in");
	 driver.manage().window().maximize();
	 
	System.out.println("Browser setup done");
         }
  
    
  @BeforeMethod
  public void TextCasesStarted(){
	  System.out.println("***** Text Cases Started*****");
  }
  
  @Test(priority=1)
  public void YourOrdersTest() throws InterruptedException {
   System.out.println("To Check user can aceess Order History through Order link from home page");
    HP =new HomePage(driver); 
    HP.LoginthroughOrderlink();
    String PageTitle=driver.getTitle();
    System.out.println("You are on" +" "+ PageTitle +" " + "Page");
    
    TakeScreenshots.CaptureScreenshots(driver, "TC001_03_Login Successful and user is on Your Orders Page");
    
   Assert.assertEquals(PageTitle, "Your Orders");
  }
  
  @Test(priority=2)
  public void AvailableTabsTest() {
	  System.out.println("To Check available tabs on Your orders page");
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
      System.out.println("To search orders using diffrent search criteria");
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
      System.out.println("To search orders using incorrect search criteria");
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
	
	 System.out.println("To check that user can Buy Product immediately");
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
	
	 System.out.println("To check that user can Add product to Cart");
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
   System.out.println("To search orders using Filters");
  
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
  
  public void screenshots() {
	 
//	  logger.info("*****Text Case Ended*****");	 	 
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
	  driver.manage().deleteAllCookies();
  }

  
    
 }
