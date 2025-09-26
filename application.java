package DataDrivenTesting;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class application {

	WebDriver driver;
	WebDriverWait wait;

	@BeforeClass
	public void openapp() {

		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		driver.get("https://www.dummyticket.com/");
	}

	@Test(priority = 1)
	public void clickele() {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

		WebElement Buy_Ticket = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("/html/body/div[1]/div/div/section[1]/div/div/div/div/div/div/a/span")));

		Buy_Ticket.click();

	}

	@Test(priority = 2)
	public void ticketType() {

		driver.findElement(By.xpath("/html/body/div[1]/div/div/section[2]/div/div/div/div/div[2]/ul/li[1]")).click();
	}

	@Test(priority = 3)
	public void detailsofC() {

		driver.findElement(By.name("travname")).sendKeys("Jeevan");
		driver.findElement(By.name("travlastname")).sendKeys("kumar");
	}

	public Month convertmonth(String month) {

		HashMap<String, Month> monthmap = new HashMap<String, Month>();

		monthmap.put("Jan", Month.JANUARY);
		monthmap.put("Feb", Month.FEBRUARY);
		monthmap.put("Mar", Month.MARCH);
		monthmap.put("Apr", Month.APRIL);
		monthmap.put("May", Month.MAY);
		monthmap.put("Jun", Month.JUNE);
		monthmap.put("Jul", Month.JULY);
		monthmap.put("Aug", Month.AUGUST);
		monthmap.put("Sep", Month.SEPTEMBER);
		monthmap.put("Oct", Month.OCTOBER);
		monthmap.put("Nov", Month.NOVEMBER);
		monthmap.put("Dec", Month.DECEMBER);

		Month vmonth = monthmap.get(month);

		if (vmonth == null) {
			System.out.print("Invalid month");
		}
		return vmonth;
	}

	@Test(priority = 4, dataProvider = "data1")
	public void Selectdate(String requiredyear, String requiredmonth, String requireddate) {
		driver.findElement(By.id("dob")).click();
		while (true) {
			// select year

			WebElement currentyear = driver.findElement(By.xpath("//select[@class='ui-datepicker-year']"));
			Select select1 = new Select(currentyear);
			select1.selectByVisibleText(requiredyear);

			// select month

//		WebElement currentmonth=driver.findElement(By.xpath("//select[@class='ui-datepicker-month']"));
			Select select = new Select(driver.findElement(By.xpath("//select[@class='ui-datepicker-month']")));
			String currentmonth = select.getFirstSelectedOption().getText();

			// coverting

			Month displaymonth = convertmonth(currentmonth);
			Month expectedmonth = convertmonth(requiredmonth);

			// coompare months

			int result = expectedmonth.compareTo(displaymonth);

			// action

			if (result < 0) {

				driver.findElement(By.xpath("//a[@data-handler='prev']")).click();
			} else if (result > 0) {
				driver.findElement(By.xpath("//a[@data-handler='next']")).click();

			} else {
				break;
			}

		}
		// Capture all the dates in the calender
		List<WebElement> alldates = driver
				.findElements(By.xpath("//table[@class='ui-datepicker-calendar']//tbody//tr//td/a"));

		for (WebElement dt : alldates) {

			if (dt.getText().equals(requireddate)) {
				dt.click();
				break;
			}
		}
	}

	@DataProvider(name = "data1")
	public Object[][] data1() {

		Object[][] data1 = { { "1998", "Aug", "10" } };
		return data1;
	}

	@Test(priority = 5)
	public void radio() {
		driver.findElement(By.xpath("//input[@name=\"sex\"][1]")).click();
		driver.findElement(By.id("fromcity")).sendKeys("Hyderabad");
		driver.findElement(By.id("tocity")).sendKeys("Goa");
	}

	@Test(priority = 6, dataProvider = "data2")
	public void depaturedate(String dmonth, String dyear, String ddate) {

		driver.findElement(By.id("departon")).click();

		while (true) {
			Select dpsel = new Select(driver.findElement(By.className("ui-datepicker-year")));
			String currentyear = dpsel.getFirstSelectedOption().getText();

			Select dsp2 = new Select(driver.findElement(By.className("ui-datepicker-month")));
			String currentMonth = dsp2.getFirstSelectedOption().getText();

			if (currentyear.equals(dyear) && currentMonth.equals(dmonth)) {

				break;
			}
			driver.findElement(By.xpath("//span[@class='ui-icon ui-icon-circle-triangle-e']")).click();
		}
		List<WebElement> allthedates = driver
				.findElements(By.xpath("//table[@class=\"ui-datepicker-calendar\"]//tr//td"));

		for (WebElement date1 : allthedates) {
			if (date1.getText().equals(ddate)) {
				wait.until(ExpectedConditions.elementToBeClickable(date1)).click();
				break;
			}
		}
	}

	@DataProvider(name = "data2")
	public Object[][] data2() {

		Object[][] data2 = { { "Dec", "2025", "25" } };

		return data2;
	}

	@Test(priority = 7)
	public void note() {
		driver.findElement(By.id("notes")).sendKeys(" I am going for a trip");
	}

	@Test(priority = 8)
	public void appointment() {

		driver.findElement(By.id("select2-reasondummy-container")).click();

		List<WebElement> options = driver.findElements(By.xpath("//select[@id='reasondummy']/option"));

		for (WebElement op : options) {
			if (op.getText().equals("Office work place needs it")) {
				op.click();
				break;
			}

		}

	}

	@Test(priority = 9)
	public void radio1() {
		driver.findElement(By.id("deliverymethod_2")).click();
		driver.findElement(By.id("billname")).sendKeys("Jeevankumar");
		driver.findElement(By.id("billing_phone")).sendKeys("8309660434");
		driver.findElement(By.id("billing_email")).sendKeys("Jeevankumar@gmail.com");

		driver.findElement(By.id("select2-billing_country-container")).click();
//		driver.findElement(By.xpath("//input[@class='select2-search__field']")).click();

		List<WebElement> options = driver.findElements(By.xpath("//span[@class='select2-results']/ul/li"));

		for (WebElement op : options) {

			if (op.getText().equals("Bolivia")) {
				WebElement ele = wait.until(ExpectedConditions.elementToBeClickable(op));
				ele.click();
				break;
			}
		}
	}

//	public void DataDriven() throws InvalidFormatException, IOException {
//
//		FileInputStream file = new FileInputStream("C:\\Users\\siree\\Downloads\\dummydata.xlsx");
//
//		XSSFWorkbook workbook = new XSSFWorkbook(file);
//		
//		XSSFSheet sheet=workbook.getSheet("Sheet1");
//		
//		int totalrows=sheet.getLastRowNum();
//		
//		int totalcells=sheet.getRow(1).getLastCellNum();
//		
//		for(int i=0;i<=totalrows;i++) {
//			
//			XSSFRow row=sheet.getRow(i);
//			
//			for(int j=0;j<=totalcells;j++) {
//				XSSFCell cell=row.getCell(j);
//			
//				if(cell!=null) {
//					switch(cell.getCellType()) {
//					case STRING:cell.getStringCellValue();
//					case NUMERIC:cell.getNumericCellValue();
//					}
//				}
//			}
//		}
//	}
	@Test(priority = 10, dataProvider = "data3")
	public void billdetails(String name, String num, String st) {

		driver.findElement(By.id("billing_address_1")).sendKeys(name);
		;
		driver.findElement(By.id("billing_address_2")).sendKeys(num);
		;
		driver.findElement(By.id("billing_city")).sendKeys(st);
		;
	}

	@DataProvider(name = "data3")
	public Object[][] data3() {

		Object[][] data3 = { { "townkotharoad", "18", "vizagin" } };

		return data3;
	}

	@Test(priority = 11)
	public void department() {
		driver.findElement(By.id("select2-billing_state-container")).click();

		List<WebElement> options = driver.findElements(By.xpath("//span[@class='select2-results']/ul/li"));

		for (WebElement dp : options) {

			if (dp.getText().equals("Pando")) {
				dp.click();
				break;
			}
		}

	}

	@Test(priority = 12)
	public void placeorder() {

//		WebElement ting = driver.findElement(By.id("payment"));
//		driver.switchTo().frame(ting);
		WebElement placeorder = wait.until(ExpectedConditions.elementToBeClickable(By.id("place_order")));
		placeorder.click();
	}

}
