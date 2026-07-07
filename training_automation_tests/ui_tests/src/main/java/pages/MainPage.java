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

    @FindBy(xpath = "//input[@placeholder='Поиск']")
    private WebElement searchInput;

    @FindBy(xpath = "//button[contains(@class, 'HeaderSearchBar')]")
    private WebElement searchButton;

    public MainPage(WebDriver driver) {
        super(driver);
    }

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
            System.out.println(" Ошибка перехода в PC: " + e.getMessage());
            driver.get("https://igrovoy.rt.ru/games/pc");
            Thread.sleep(2000);
        }
    }

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
            System.out.println(" Ошибка поиска: " + e.getMessage());
            searchInput.sendKeys(Keys.ENTER);
            Thread.sleep(3000);
        }
    }
}