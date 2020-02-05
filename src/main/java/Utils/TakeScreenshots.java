package Utils;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.commons.io.FileUtils;
@SuppressWarnings("unused")

public class TakeScreenshots {

	
	public static void CaptureScreenshots(WebDriver driver,String ScreenshotsName) {
		
		TakesScreenshot Screenshot =(TakesScreenshot)driver;
		 
		File Source = Screenshot.getScreenshotAs(OutputType.FILE);
		
		try {
			FileUtils.copyFile(Source, new File("./Screenshots/"+ ScreenshotsName + ".png"));
			
		} catch (IOException e) {
			
			System.out.println("Error Occured while taking screenshot"+e.getMessage());
		}
		
		
		

	}

}
