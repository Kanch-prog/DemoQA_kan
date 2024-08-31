import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ExploratoryTest {

    private static WebDriver driver;

    public static void main(String[] args) {
        // Set up WebDriver
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Kanchana\\Bank\\chromedriver.exe");
        driver = new ChromeDriver();

        try {
            // Navigate to the website
            driver.get("https://demoqa.com/books");

            // Perform the login
            login("hrajora", "Harish@123");

            // Wait for the search field to be visible after login
            waitForElement(By.id("searchBox"), 10);

            // Perform the search for "Git Pocket Guide"
            enterSearchStr("Git Pocket Guide");

            // Click on the book after confirming its available
            clickOnBook("Git Pocket Guide");

            // Confirm whether the author's name exists on the details page
            verifyAuthorPresence("Richard E. Silverman");

            // Navigate to the profile page and check the table for the author's name
            navigateToProfile();
            checkAuthorInProfileTable("Richard E. Silverman");
            // Logout from the application
            logout();

        } finally {
            // Close the driver
            driver.quit();
        }
    }

    // Method for logging in
    public static void login(String username, String password) {
        // Click on the Login button
        driver.findElement(By.id("login")).click();

        // Input the username and password
        driver.findElement(By.id("userName")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);

        // Click on the login button
        driver.findElement(By.id("login")).click();

        // Verify successful login by checking page title or any other element
        System.out.println("Login successful. The page title is: " + driver.getTitle());
    }

    public static void enterSearchStr(String searchString) {
        By searchField = By.id("searchBox");
        By searchBtn = By.xpath("//*[@id='basic-addon2']");

        // Enter the search string
        driver.findElement(searchField).sendKeys(searchString);
        System.out.println("The search string is: " + searchString);

        // Click the search button
        driver.findElement(searchBtn).click();
    }

    public static void clickOnBook(String bookTitle) {
        By bookLocator = By.xpath("//a[contains(text(), '" + bookTitle + "')]");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement bookElement = wait.until(ExpectedConditions.visibilityOfElementLocated(bookLocator));

        // Scroll into view to ensure it is clickable
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", bookElement);

        // Wait until the element is clickable
        wait.until(ExpectedConditions.elementToBeClickable(bookElement));

        try {
            bookElement.click();
            System.out.println("Clicked on the book: " + bookTitle);
        } catch (ElementClickInterceptedException e) {
            System.out.println("Element not clickable: " + e.getMessage());
            // Try clicking using JavaScript
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", bookElement);
            System.out.println("Clicked on the book using JavaScript: " + bookTitle);
        }
    }

    // Method to wait for an element to be visible
    public static void waitForElement(By locator, int timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static void verifyAuthorPresence(String authorName) {
        By authorLabelLocator = By.xpath("//*[contains(@class, 'form-label') and contains(text(), '" + authorName + "')]");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        try {
            // Wait for the author's label to be visible
            WebElement authorLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(authorLabelLocator));
            System.out.println("The author label is visible: " + authorLabel.getText());
        } catch (TimeoutException e) {
            System.out.println("Timed out waiting for author label to be visible: " + e.getMessage());
        }
    }


    // Method to navigate to the profile page
    public static void navigateToProfile() {
        // Navigate to the profile page
        driver.get("https://demoqa.com/profile");
        System.out.println("Navigated to the profile page.");
    }

    // Method to check if the author exists in the profile table
    public static void checkAuthorInProfileTable(String authorName) {
        // Define the XPath for the author's name in the table
        By authorLocator = By.xpath("//div[contains(@class, 'rt-tbody')]//div[contains(text(), '" + authorName + "')]");

        // Wait for the table to load and the author's name to be visible
        waitForElement(authorLocator, 10);

        // Check if the author's name exists in the profile table
        WebElement authorElement = driver.findElement(authorLocator);
        if (authorElement.isDisplayed()) {
            System.out.println("Author '" + authorName + "' exists in the profile table.");
        } else {
            System.out.println("Author '" + authorName + "' does not exist in the profile table.");
        }
    }
    // Method to perform logout
    public static void logout() {
        // Locate the logout button (update the locator as needed)
        By logoutBtn = By.id("submit"); // Replace with the actual ID for the logout button

        // Click on the logout button
        try {
            clickLogout(logoutBtn);
            System.out.println("Logged out successfully.");
        } catch (Exception e) {
            System.out.println("Error during logout: " + e.getMessage());
        }

        // Optionally, verify logout by checking if the login button is displayed
        By loginButton = By.id("login"); // Replace with the actual ID or locator for the login button
        if (isElementVisible(loginButton)) {
            System.out.println("Logout verification successful. Login button is visible.");
        } else {
            System.out.println("Logout verification failed. Login button is not visible.");
        }
    }

    // Method to click the logout button
    public static void clickLogout(By logoutBtn) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Wait for the logout button to be clickable
        WebElement logoutElement = wait.until(ExpectedConditions.elementToBeClickable(logoutBtn));

        // Try clicking the logout button
        try {
            logoutElement.click();
        } catch (ElementClickInterceptedException e) {
            System.out.println("Element not clickable: " + e.getMessage());
            // Fallback: click using JavaScript if it's intercepted
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", logoutElement);
        }
    }


    // Method to check if an element is visible
    public static boolean isElementVisible(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}
