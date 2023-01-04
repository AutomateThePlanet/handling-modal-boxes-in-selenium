package java.utilities;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

// https://getbootstrap.com/docs/4.0/components/modal/
// Varying modal content
public class ComplexModalDialog {

    private WebDriver driver;
    private By by;

    public ComplexModalDialog(WebDriver driver) {
        this.driver = driver;
        this.by = By.className("modal-dialog");
    }

    public ComplexModalDialog(WebDriver driver, By by) {
        this.driver = driver;
        this.by = by;
    }

    public void close() {
        var sendMessageButton = getModalContentBody().findElement(By.xpath("//button[text()='Close']"));
        sendMessageButton.click();
    }

    public void sendMessage() {
        var sendMessageButton = getModalContentBody().findElement(By.xpath("//button[text()='Send message']"));
        sendMessageButton.click();
    }

    public void setMessage(String message) {
        var messageInput = getModalContentBody().findElement(By.id("message-text"));
        messageInput.clear();
        messageInput.sendKeys(message);
    }

    public void setRecipient(String recipient) {
        var recipientInput = getModalContentBody().findElement(By.id("recipient-name"));
        recipientInput.clear();
        recipientInput.sendKeys(recipient);
    }

    public String getContent() {
        return getModalContentBody().getText();
    }

    private WebElement getModalContentBody() {
        return getModalContainer().findElement(By.xpath(".//div[@class='modal-body']"));
    }

    private WebElement getModalContainer() {
        var wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }
}
