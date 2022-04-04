package linechrordbot.service.selenium;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class SeleniumTest {

	@Test
	public void testGetGooglePage() {
		log.info("here  i am for chrome test");

		String uri = "http://quote.eastmoney.com/sh600036.html";

		// 設定 chromedirver 的存放位置
		System.getProperties().setProperty("webdriver.chrome.driver",
				"src/main/resources/chromedriver.exe");

		// 設定瀏覽器引數123
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("--no-sandbox");// 禁用沙箱
		chromeOptions.addArguments("--disable-dev-shm-usage");// 禁用開發者shm
		chromeOptions.addArguments("--headless"); // 無頭瀏覽器，這樣不會開啟瀏覽器視窗
		WebDriver webDriver = new ChromeDriver(chromeOptions);

		webDriver.get(uri);
		WebElement webElements = webDriver.findElement(By.id("price9"));
		String stockPrice = webElements.getText();
		log.info("最新股價為 >>> {}", stockPrice);
		webDriver.close();
		assertEquals(1, 1);
	}

}
