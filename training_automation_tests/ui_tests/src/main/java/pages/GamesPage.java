package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class GamesPage extends BasePage {

    private final By showMoreButton = By.xpath("//button[contains(text(), 'Показать еще')]");
    private final int MAX_ATTEMPTS = 20;

    public GamesPage(WebDriver driver) {
        super(driver);
    }

    // =========================================================
    // 1. ЗАГРУЗКА ИГР 
    // =========================================================

    private boolean loadMoreGamesIfNeeded(String searchText) throws InterruptedException {
        int attempts = 0;
        
        while (attempts < MAX_ATTEMPTS) {

            List<WebElement> cards = driver.findElements(By.xpath("//div[contains(@class, 'ProductCard')]"));
            for (WebElement card : cards) {
                if (card.getText().toLowerCase().contains(searchText.toLowerCase())) {
                    System.out.println(" Игра найдена!");
                    return true;
                }
            }
            
            List<WebElement> buttons = driver.findElements(showMoreButton);
            if (buttons.isEmpty()) {
                System.out.println(" Кнопка 'Показать еще' не найдена, игра отсутствует");
                return false;
            }
            
            WebElement button = buttons.get(0);
            
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});", 
                button
            );
            

            Thread.sleep(500);
            
            try {

                WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                shortWait.until(ExpectedConditions.elementToBeClickable(button)).click();
            } catch (Exception e) {
                System.out.println(" Обычный клик не сработал, пробуем JavaScript клик...");
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
            }
            
            System.out.println(" Нажимаем 'Показать еще'... Попытка " + (attempts + 1));

            int oldSize = cards.size();
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            boolean hasNewCards = shortWait.until(driverContext -> {
                List<WebElement> newCards = driverContext.findElements(By.xpath("//div[contains(@class, 'ProductCard')]"));
                return newCards.size() > oldSize;
            });
            
            if (!hasNewCards) {
                System.out.println(" Новые карточки не загрузились");
                return false;
            }
            
            attempts++;
        }
        
        System.out.println(" Достигнут лимит попыток загрузки");
        return false;
    }

    // =========================================================
    // 2. КЕЙС 1: Поиск игры по цене
    // =========================================================

    public void openGameCardWithPrice(String expectedPrice) throws InterruptedException {
        loadMoreGamesIfNeeded(expectedPrice);
        
        List<WebElement> cards = driver.findElements(By.xpath("//div[contains(@class, 'ProductCard')]"));
        System.out.println(" Найдено карточек: " + cards.size());
        
        for (WebElement card : cards) {
            List<WebElement> priceElements = card.findElements(
                By.xpath(".//span[contains(@class, 'rt-Text') and not(contains(@class, 'crossed-price'))]")
            );
            
            for (WebElement price : priceElements) {
                String priceText = price.getText().replaceAll("[^0-9]", "");
                if (priceText.contains(expectedPrice)) {
                    System.out.println(" Нашли карточку с ценой " + expectedPrice);
                    
                    ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({block: 'center'});", 
                        card
                    );

                    WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                    shortWait.until(ExpectedConditions.elementToBeClickable(card)).click();
                    return;
                }
            }
        }
        
        throw new RuntimeException("Игра с ценой " + expectedPrice + " не найдена. Найдено карточек: " + cards.size());
    }

    public String getGamePrice() {
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement price = shortWait.until(ExpectedConditions.visibilityOf(
            driver.findElement(By.xpath(
                "//span[contains(@class, 'rt-Text') and contains(text(), '₽') and not(contains(@class, 'crossed-price'))]"
            ))
        ));
        return price.getText().trim();
    }

    // =========================================================
    // 3. КЕЙС 1: Получение цены из результатов поиска
    // =========================================================

    public String getPriceInSearchResults(String gameName) throws InterruptedException {
        loadMoreGamesIfNeeded(gameName);
        
        List<WebElement> cards = driver.findElements(By.xpath("//div[contains(@class, 'ProductCard')]"));
        System.out.println(" Найдено карточек: " + cards.size());
        
        for (WebElement card : cards) {
            List<WebElement> titleElements = card.findElements(
                By.xpath(".//a[contains(@class, 'ProductCard') and contains(@class, 'title')]")
            );
            
            for (WebElement title : titleElements) {
                String titleText = title.getText().trim();
                System.out.println(" Название: " + titleText);
                
                if (titleText.toLowerCase().contains(gameName.toLowerCase())) {
                    System.out.println(" Нашли игру: " + gameName);
                    
                    List<WebElement> priceElements = card.findElements(
                        By.xpath(".//span[contains(@class, 'rt-Text') and contains(text(), '₽') and not(contains(@class, 'crossed-price'))]")
                    );
                    
                    if (!priceElements.isEmpty()) {
                        String priceText = priceElements.get(0).getText().trim();
                        System.out.println(" Цена в карточке: " + priceText);
                        return priceText;
                    }
                    return "";
                }
            }
        }
        
        throw new RuntimeException("Игра " + gameName + " не найдена в результатах поиска");
    }

    // =========================================================
    // 4. КЕЙС 2: Поиск игры по названию
    // =========================================================

    public void openGameCardByName(String gameName) throws InterruptedException {
    loadMoreGamesIfNeeded(gameName);
    
    List<WebElement> cards = driver.findElements(By.xpath("//div[contains(@class, 'ProductCard')]"));
    System.out.println("🔍 Найдено карточек: " + cards.size());
    
    for (WebElement card : cards) {
        List<WebElement> titleElements = card.findElements(
            By.xpath(".//a[contains(@class, 'ProductCard') and contains(@class, 'title')]")
        );
        
        for (WebElement title : titleElements) {
            String titleText = title.getText().trim();
            System.out.println(" Название: " + titleText);
            
            if (titleText.toLowerCase().contains(gameName.toLowerCase())) {
                System.out.println(" Нашли игру: " + gameName);
                

                ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});", 
                    title
                );
                Thread.sleep(500);
                

                try {
                    wait.until(ExpectedConditions.elementToBeClickable(title)).click();
                } catch (Exception e) {
                    System.out.println("Обычный клик не сработал, используем JavaScript клик...");
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", title);
                }
                return;
            }
        }
    }
    
    throw new RuntimeException("Игра " + gameName + " не найдена. Найдено карточек: " + cards.size());
}

    // =========================================================
    // 5. КЕЙС 2: Проверка минимальных требований
    // =========================================================

    public boolean checkMinRequirements() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            shortWait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//h4[contains(text(), 'Минимальные')]")
            ));
            
            List<WebElement> items = driver.findElements(
                By.xpath("//span[contains(@class, 'SystemRequirements-module-scss-module__NEb1Ga__item')]")
            );
            
            System.out.println(" Найдено элементов с требованиями: " + items.size());
            
            StringBuilder fullText = new StringBuilder();
            for (WebElement item : items) {
                fullText.append(item.getText()).append(" ");
            }
            
            String text = fullText.toString();
            
            boolean hasWindows = text.contains("Windows 11");
            boolean hasCPU = text.contains("Intel CPU Core i5-9600K");
            boolean hasRAM = text.contains("16 Гб");
            boolean hasGPU = text.contains("NVIDIA GeForce RTX 2070");
            boolean hasDisk = text.contains("50 ГБ");
            
            System.out.println(" Windows 11: " + hasWindows);
            System.out.println(" CPU: " + hasCPU);
            System.out.println(" RAM: " + hasRAM);
            System.out.println(" GPU: " + hasGPU);
            System.out.println(" Disk: " + hasDisk);
            
            return hasWindows && hasCPU && hasRAM && hasGPU && hasDisk;
            
        } catch (Exception e) {
            System.out.println(" Ошибка при проверке требований: " + e.getMessage());
            return false;
        }
    }

    // =========================================================
    // 6. КЕЙС 3: Ссылки на скачивание (не актуал)
    // =========================================================

    /*public void clickGameInFooter(String gameName) throws InterruptedException {
        try {
            WebElement link = driver.findElement(By.xpath("//footer//a[contains(text(), '" + gameName + "')]"));
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", 
                link
            );
            Thread.sleep(500);
            waitForElementClickable(link);
            link.click();
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println(" Ссылка в футере не найдена, ищем везде...");
            WebElement link = driver.findElement(By.xpath("//a[contains(text(), '" + gameName + "')]"));
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", 
                link
            );
            Thread.sleep(500);
            waitForElementClickable(link);
            link.click();
            Thread.sleep(1000);
        }
    }*/

    public boolean hasDownloadLinks() {
        try {
            return driver.findElements(By.xpath("//a[contains(@href, 'play.google.com')]")).size() > 0 &&
                   driver.findElements(By.xpath("//a[contains(@href, 'apps.apple.com')]")).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void scrollToBottom() {
        super.scrollToBottom();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // =========================================================
// КЕЙС 3: Поиск игры в разделе "Подписки" 
// =========================================================

public void clickGameInSubscriptions(String gameName) throws InterruptedException {
    try {
        
        WebElement gameLink = driver.findElement(By.xpath(
            "/html/body/div[2]/header/div/div/div/div[4]/div[6]/div/div[2]/div[1]/div[2]/a[1]"
        ));
        
        // Прокручиваем к элементу
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});", 
            gameLink
        );
        Thread.sleep(500);
        
        wait.until(ExpectedConditions.elementToBeClickable(gameLink)).click();
        System.out.println(" Кликнули по CarX Drift Racing 2");
        
    } catch (Exception e) {
        System.out.println(" Ошибка: " + e.getMessage());

        try {
            WebElement gameLink = driver.findElement(By.xpath(
                "//a[contains(text(), '" + gameName + "')]"
            ));
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});", 
                gameLink
            );
            Thread.sleep(500);
            wait.until(ExpectedConditions.elementToBeClickable(gameLink)).click();
            System.out.println(" Кликнули по CarX Drift Racing 2 (по тексту)");
            
        } catch (Exception ex) {
            throw new RuntimeException("Игра " + gameName + " не найдена в разделе 'Подписки'");
        }
    }
}
}