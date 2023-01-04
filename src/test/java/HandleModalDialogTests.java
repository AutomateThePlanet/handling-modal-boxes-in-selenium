import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.Wait;

import java.time.Duration;
import java.util.Iterator;
import java.util.Set;
import java.utilities.ComplexModalDialog;

public class HandleModalDialogTests {
    private final int WAIT_FOR_ELEMENT_TIMEOUT = 30;
    private ChromeDriver driver;
    private WebDriverWait webDriverWait;

    @BeforeAll
    public static void setUpClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_FOR_ELEMENT_TIMEOUT));
    }

    @Test
    public void verifyModalDialogBox() {
        driver.get("https://www.lambdatest.com/selenium-playground/bootstrap-modal-demo");

        var modalButton = driver.findElement(By.xpath("//button[@data-target='#myModal']"));
        modalButton.click();

        WebElement modalContainer = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("modal-dialog")));

        WebElement modalContentBody = modalContainer.findElement(By.xpath(".//div[@class='modal-body']"));
        Assertions.assertEquals(modalContentBody.getText(),
                "This is the place where the content for the modal dialog displays", "Verify modal body message");

        WebElement modalAcceptButton = modalContainer.findElement(By.xpath(".//button[contains(text(),'Save Changes')]"));
        modalAcceptButton.click();
    }

    @Test
    public void verifyMultipleModalDialogBoxes() {
        driver.get("https://www.lambdatest.com/selenium-playground/bootstrap-modal-demo");

        var modalButton = driver.findElement(By.xpath("//div[text()='Multiple Modal Example']/following-sibling::button"));
        modalButton.click();

        WebElement modalContainer = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("myMultiModal")));

        WebElement modalContentBody = modalContainer.findElement(By.xpath(".//div[@class='modal-body']"));
        Assertions.assertTrue(modalContentBody.getText().contains("Click launch modal button to launch second modal."));

        var secondLaunchButton = modalContentBody.findElement(By.xpath("//button[@data-target='#mySecondModal']"));
        secondLaunchButton.click();

        WebElement secondModalContainer = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("mySecondModal")));
        WebElement secondModalContentBody = secondModalContainer.findElement(By.xpath(".//div[@class='modal-body']"));
        Assertions.assertTrue(secondModalContentBody.getText().contains("This is the place where the content for the modal dialog displays."));

        WebElement secondModalAcceptButton = secondModalContainer.findElement(By.xpath(".//button[contains(text(),'Save Changes')]"));
        secondModalAcceptButton.click();

        WebElement modalAcceptButton = modalContainer.findElement(By.xpath(".//button[contains(text(),'Save Changes')]"));
        modalAcceptButton.click();
    }

    @Test
    public void verifyComplexDialogBoxes() {
        driver.get("https://getbootstrap.com/docs/4.0/components/modal/");

        var modalButton = driver.findElement(By.xpath("//button[text()='Open modal for @mdo']"));
        modalButton.click();

        var complexDialog = new ComplexModalDialog(driver);

        complexDialog.setRecipient("Anton");
        complexDialog.setMessage("test message");

        complexDialog.sendMessage();
        complexDialog.close();
    }

    @Test
    public void verifyAlerts() {
        driver.get("https://www.lambdatest.com/selenium-playground/javascript-alert-box-demo");

        var openAlertButton = driver.findElement(By.xpath("(//button[contains(text(),'Click Me')])[3]"));
        openAlertButton.click();

        webDriverWait.until(ExpectedConditions.alertIsPresent());

        String alertBodyText = driver.switchTo().alert().getText();
        Assertions.assertEquals(alertBodyText, "Please enter your name", "Verify alert body content");

        driver.switchTo().alert().sendKeys("LambdaTest");

        driver.switchTo().alert().accept();
    }

    @Test
    public void verifyPopups() {
        driver.get("https://www.lambdatest.com/selenium-playground/window-popup-modal-demo");

        String mainWindowHandle = driver.getWindowHandle();

        WebElement followButtonOnMainWindow = driver.findElement(By.xpath("//a[contains(@title,'Twitter')]"));
        followButtonOnMainWindow.click();

        Set<String> windowHandles = driver.getWindowHandles();
        Assertions.assertEquals(windowHandles.size(), 2);

        for (var currentWindow : windowHandles){
            if (!mainWindowHandle.equalsIgnoreCase(currentWindow)) {
                driver.switchTo().window(currentWindow);
            }
        }
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}