import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Set;
import java.utilities.ComplexModalDialog;

public class HandleModalDialogLambdaTestTests {
    private final int WAIT_FOR_ELEMENT_TIMEOUT = 30;
    private WebDriver driver;
    private WebDriverWait webDriverWait;

    @BeforeAll
    public static void setUpClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() throws MalformedURLException {
        String username = System.getenv("LT_USERNAME");
        String authkey = System.getenv("LT_ACCESSKEY");
        String hub = "@hub.lambdatest.com/wd/hub";

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", "Chrome");
        capabilities.setCapability("browserVersion", "latest");
        HashMap<String, Object> ltOptions = new HashMap<String, Object>();
        ltOptions.put("user", username);
        ltOptions.put("accessKey", authkey);
        ltOptions.put("build", "Selenium 4");
        ltOptions.put("name",this.getClass().getName());
        ltOptions.put("platformName", "Windows 10");
        ltOptions.put("seCdp", true);
        ltOptions.put("selenium_version", "4.0.0");
        capabilities.setCapability("LT:Options", ltOptions);

        driver = new RemoteWebDriver(new URL("https://" + username + ":" + authkey + hub), capabilities);
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

        var alertButton = driver.findElement(By.xpath("//div[text()='Java Script Alert Box']/following-sibling::button"));
        alertButton.click();

        Assertions.assertEquals("I am an alert box!", driver.switchTo().alert().getText());

        driver.switchTo().alert().accept();

        var alertConfirmation = driver.findElement(By.xpath("//div[text()='Java Script Confirm Box']/following-sibling::button"));
        alertConfirmation.click();

        driver.switchTo().alert().dismiss();

        var alertTextButton = driver.findElements(By.xpath("//div[text()='Java Script Alert Box']/following-sibling::button")).get(1);
        alertTextButton.click();

        driver.switchTo().alert().sendKeys("LambdaTest");

        var confirmationParagraph = driver.findElement(By.id("prompt-demo"));
        Assertions.assertEquals("You have entered 'LambdaTest' !", confirmationParagraph.getText());
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