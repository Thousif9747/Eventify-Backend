package com.example.eventify;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.net.URLEncoder;

public class WhatsAppSender {
    public static void sendWhatsAppMessageToNumber(String phoneNumber, String message) throws Exception {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        String url = "https://wa.me/" + phoneNumber + "?text=" + URLEncoder.encode(message, "UTF-8");
        WebDriver driver = new ChromeDriver();
        driver.get(url);
        System.out.println("Please scan the QR code in the opened browser window if prompted...");
        Thread.sleep(20000);
        try {
            WebElement continueButton = driver.findElement(By.xpath("//a[contains(@href, 'web.whatsapp.com/send')]"));
            continueButton.click();
            Thread.sleep(5000);
        } catch (Exception e) {
        }
        try {
            WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(20));
            WebElement sendButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@data-icon='send']")));
            sendButton.click();
            System.out.println("WhatsApp message sent to number: " + phoneNumber);
        } catch (Exception e) {
            try {
                WebElement messageBox = driver.findElement(By.xpath("//div[@title='Type a message']"));
                messageBox.sendKeys(Keys.ENTER);
                System.out.println("WhatsApp message sent by pressing ENTER.");
            } catch (Exception ex) {
                System.out.println("Could not send WhatsApp message automatically.");
            }
        }
    }
}
