package org.testcase;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import caw.util.*;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class DynamicTableTest {
    private WebDriver driver;

    @BeforeClass
    public void setUp() {
        // Initialize the WebDriver
    	driver = new ChromeDriver(new ChromeOptions());
        
        
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @Test
    public void testDynamicTable() {
        // 1. Navigate to the URL
        driver.get(InputTestData.URL);

        // 2. Click the 'Table Data' button to reveal the input field
        WebElement tableDataButton = driver.findElement(By.xpath("/html/body/div/div[3]/details"));
        tableDataButton.click();

        // 3. Insert the JSON data in the input box
        WebElement inputBox = driver.findElement(By.id("jsondata"));
        inputBox.clear();
        inputBox.sendKeys(InputTestData.TABLE_DATA.toString());

        // 4. Click the 'Refresh Table' button
        WebElement refreshButton = driver.findElement(By.id("refreshtable"));
        refreshButton.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("dynamictable")));
        ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");


        // 5. Validate the table data
        List<WebElement> rows = driver.findElements(By.xpath("//table[@id='dynamictable']/tbody/tr"));

        Assert.assertEquals(rows.size(), InputTestData.TABLE_DATA.size(),5, "Row count mismatch");

        // Validate each row's content
        for (int i = 0; i < rows.size(); i++) {
            List<WebElement> columns = rows.get(i).findElements(By.tagName("td"));
            Map<String, String> expectedData = InputTestData.TABLE_DATA.get(i);

            Assert.assertEquals(columns.get(0).getText(), expectedData.get("name").toString(), "Name mismatch");
            Assert.assertEquals(columns.get(1).getText(), expectedData.get("age").toString(), "Age mismatch");
            Assert.assertEquals(columns.get(2).getText(), expectedData.get("gender").toString(), "Gender mismatch");
        }
    }

    @AfterClass
    public void tearDown() {
        // Close the browser after the test
        if (driver != null) {
            driver.quit();
        }
    }
}

