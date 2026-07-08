package pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class MainPage extends BasePage {

    @FindBy(xpath = "//a[contains(text(), 'Игры')]")
    private WebElement gamesMenu;

    @FindBy(xpath = "//a[contains(text(), 'PC')]")
    private WebElement pcCategory;
    @FindBy(xpath = "//a[contains(text(), 'Подписки')]")
    private WebElement subscriptionsMenu;

    @FindBy(xpath = "//a[contains(text(), 'CarX Drift Racing 2')]")
    private WebElement carxCategory;
    @FindBy(xpath = "//input[@placeholder='Поиск']")
    private WebElement searchInput;

    @FindBy(xpath = "//button[contains(@class, 'HeaderSearchBar')]")
    private WebElement searchButton;

    public MainPage(WebDriver driver) {
        super(driver);
    }

    // =========================================================
    // ПЕРЕХОД В "ИГРЫ → PC"
    // =========================================================

    public void goToPCGames() throws InterruptedException {
        try {
            Actions actions = new Actions(driver);
            wait.until(ExpectedConditions.visibilityOf(gamesMenu));
            actions.moveToElement(gamesMenu).perform();
            Thread.sleep(500);
            wait.until(ExpectedConditions.elementToBeClickable(pcCategory));
            pcCategory.click();
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("❌ Ошибка перехода в PC: " + e.getMessage());
            driver.get("https://igrovoy.rt.ru/games/pc");
            Thread.sleep(2000);
        }
    }

    // =========================================================
    // ПОИСК ИГРЫ
    // =========================================================

    public void searchForGame(String gameName) throws InterruptedException {
        try {
            wait.until(ExpectedConditions.visibilityOf(searchInput));
            searchInput.clear();
            searchInput.sendKeys(gameName);
            Thread.sleep(500);
            wait.until(ExpectedConditions.elementToBeClickable(searchButton));
            searchButton.click();
            Thread.sleep(3000);
        } catch (Exception e) {
            System.out.println("❌ Ошибка поиска: " + e.getMessage());
            searchInput.sendKeys(Keys.ENTER);
            Thread.sleep(3000);
        }
    }

    // =========================================================
    // ПЕРЕХОД В "ПОДПИСКИ"
    // =========================================================

public void goToCarXDriftRacing2() throws InterruptedException {
        try {
            Actions actions = new Actions(driver);
            wait.until(ExpectedConditions.visibilityOf(subscriptionsMenu));
            actions.moveToElement(subscriptionsMenu).perform();
            System.out.println("✅ Навели курсор на 'Подписки'");
            
            Thread.sleep(500);
            
            wait.until(ExpectedConditions.elementToBeClickable(carxCategory));
            carxCategory.click();
            System.out.println("✅ Кликнули по CarX Drift Racing 2");
            
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("❌ Ошибка перехода в CarX Drift Racing 2: " + e.getMessage());
            driver.get("https://igrovoy.rt.ru/subscriptions/carx-drift-racing-2");
            Thread.sleep(2000);
        }
    }
}
