package com.yfedyna.taskdataox.service.library;

import com.yfedyna.taskdataox.dto.JobFunction;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

@Component
public class JobPageScraper {
    private WebDriver webDriver;

    public String getCurrentSiteUrl(JobFunction jobFunction) {
        String siteUrl = "https://jobs.techstars.com/jobs";
        if (jobFunction == null) {
            return siteUrl;
        }

        System.setProperty("webdriver.chrome.driver", "selenium/chromedriver");
        webDriver = new ChromeDriver();
        webDriver.get("https://jobs.techstars.com/jobs");
        applyJobFunctionFilter(jobFunction.getJobFunctions());

        return webDriver.getCurrentUrl();
    }

    private void applyJobFunctionFilter(String jobFunctionName) {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10L));
        Actions actions = new Actions(webDriver);

        // Find and click the dropdown arrow to expand the list of filters under "Job function".
        WebElement dropdownArrow = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[@class='sc-beqWaB cktMLM'][text()='Job function']")));
        actions.click(dropdownArrow).build().perform();

        // Find the search filter input field and enter the job function name in it.
        WebElement searchFilterInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("[data-testid='search-filter'] input[type='text']")));
        searchFilterInput.sendKeys(jobFunctionName);

        // Find the filtered category element (job function button) based on the given name.
        // Scroll the page to make the filtered category element visible.
        WebElement filteredCategoryElement = webDriver.findElement(
                By.xpath("//button[text()='" + jobFunctionName + "']"));
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", filteredCategoryElement);

        // Find the visible category element (job function button) and click on it.
        WebElement categoryElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//button[text()='" + jobFunctionName + "']")));
        actions.click(categoryElement).build().perform();

        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("[data-testid='job-list-item']"), 0));
    }
}

