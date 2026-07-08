package ui;

import config.WebDriverConfig;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.GamesPage;
import pages.MainPage;

public class GamePortalTests {
    private WebDriver driver;
    private MainPage mainPage;
    private GamesPage gamesPage;

    @BeforeMethod
    public void setup() {
        driver = WebDriverConfig.createDriver();
        driver.get("https://igrovoy.rt.ru/");
        driver.manage().window().maximize();
        mainPage = new MainPage(driver);
        gamesPage = new GamesPage(driver);
    }

    @AfterMethod
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }


    // КЕЙС 1: Поиск игры Pioner и проверка цены


    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Кейс 1: Поиск игры Pioner и проверка цены 699р")
    public void testSearchGameAndCheckPrice() throws InterruptedException {  
        mainPage.goToPCGames();
        mainPage.searchForGame("Pioner");
        String actualPrice = gamesPage.getPriceInSearchResults("Pioner");
            System.out.println("💰 Найдена цена: " + actualPrice);
    Assert.assertTrue(actualPrice.contains("699"), "Цена не соответствует ожидаемой. Найдено: " + actualPrice);
    }


    // КЕЙС 2: Проверка минимальных требований LEGO Batman


    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Кейс 2: Проверка минимальных требований LEGO Batman")
    public void testCheckMinRequirements() throws InterruptedException {  
        mainPage.goToPCGames();
        gamesPage.openGameCardByName("LEGO Batman: Legacy of the Dark Knight");
        boolean requirementsMet = gamesPage.checkMinRequirements();
        Assert.assertTrue(requirementsMet, "Минимальные требования не соответствуют");
    }


    // КЕЙС 3: Проверка ссылок на скачивание CarX Drift Racing 2

@Test
@Severity(SeverityLevel.NORMAL)
@Description("Кейс 3: Проверка ссылок на скачивание CarX Drift Racing 2 (раздел Подписки)")
public void testCheckDownloadLinks() throws InterruptedException {
    // 1. Навести на "Подписки" и кликнуть на CarX Drift Racing 2 в подменю
    mainPage.goToCarXDriftRacing2();
    
    // 2. Проверить ссылки на Google Play и App Store
    boolean hasLinks = gamesPage.hasDownloadLinks();
    Assert.assertTrue(hasLinks, "Отсутствуют ссылки на Google Play или App Store");
}
}