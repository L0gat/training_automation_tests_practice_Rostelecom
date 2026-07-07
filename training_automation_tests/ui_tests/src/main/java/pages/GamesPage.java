package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;

public class GamesPage extends BasePage {

    private final By showMoreButton = By.xpath("//button[contains(text(), 'Показать еще')]");

    public GamesPage(WebDriver driver) {
        super(driver);
    }


    // МЕТОД: Загружать игры пока не появится нужная


    private boolean loadMoreGamesIfNeeded(String searchText) throws InterruptedException {
        int maxAttempts = 20;
        int attempts = 0;
        
        while (attempts < maxAttempts) {
            // Проверяем, появилась ли нужная игра
            List<WebElement> cards = driver.findElements(By.xpath("//div[contains(@class, 'ProductCard')]"));
            for (WebElement card : cards) {
                String cardText = card.getText().toLowerCase();
                if (cardText.contains(searchText.toLowerCase())) {
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
            
            ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});", button);
            Thread.sleep(500);

            try {
                wait.until(ExpectedConditions.elementToBeClickable(button)).click();
            } catch (Exception e) {
                System.out.println(" Обычный клик не сработал, пробуем JavaScript клик...");
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
            }
            
            System.out.println(" Нажимаем 'Показать еще'... Попытка " + (attempts + 1));
            Thread.sleep(1500);
            attempts++;
        }
        System.out.println(" Достигнут лимит попыток загрузки");
        return false;
    }

    // КЕЙС 1: Поиск игры по цене

    
    public void openGameCardWithPrice(String expectedPrice) throws InterruptedException {
        loadMoreGamesIfNeeded(expectedPrice);
        
        List<WebElement> cards = driver.findElements(By.xpath("//div[contains(@class, 'ProductCard')]"));
        System.out.println("🔍 Найдено карточек: " + cards.size());
        
        for (WebElement card : cards) {
            List<WebElement> priceElements = card.findElements(By.xpath(".//span[contains(@class, 'rt-Text')]"));
            for (WebElement price : priceElements) {
                String priceText = price.getText().replaceAll("[^0-9]", "");
                if (priceText.contains(expectedPrice)) {
                    System.out.println("✅ Нашли карточку с ценой " + expectedPrice);
                    ((JavascriptExecutor) driver)
                        .executeScript("arguments[0].scrollIntoView({block: 'center'});", card);
                    Thread.sleep(500);
                    waitForElementClickable(card);
                    card.click();
                    return;
                }
            }
        }
        throw new RuntimeException("Игра с ценой " + expectedPrice + " не найдена. Найдено карточек: " + cards.size());
    }

    public String getGamePrice() {
        WebElement price = wait.until(ExpectedConditions.visibilityOf(
            driver.findElement(By.xpath("//span[contains(@class, 'rt-Text') and contains(text(), '₽')]"))
        ));
        return price.getText().trim();
    }


    // КЕЙС 2: Поиск игры по названию


    public void openGameCardByName(String gameName) throws InterruptedException {
        loadMoreGamesIfNeeded(gameName);
        
        List<WebElement> cards = driver.findElements(By.xpath("//div[contains(@class, 'ProductCard')]"));
        System.out.println("🔍 Найдено карточек: " + cards.size());
        
        for (WebElement card : cards) {
            List<WebElement> titleElements = card.findElements(By.xpath(".//a[contains(@class, 'ProductCard') and contains(@class, 'title')]"));
            for (WebElement title : titleElements) {
                String titleText = title.getText().trim();
                System.out.println(" Название: " + titleText);
                if (titleText.toLowerCase().contains(gameName.toLowerCase())) {
                    System.out.println(" Нашли игру: " + gameName);
                    ((JavascriptExecutor) driver)
                        .executeScript("arguments[0].scrollIntoView({block: 'center'});", title);
                    Thread.sleep(500);
                    waitForElementClickable(title);
                    title.click();
                    return;
                }
            }
        }
        throw new RuntimeException("Игра " + gameName + " не найдена. Найдено карточек: " + cards.size());
    }

  
    // КЕЙС 2: Проверка минимальных требований
  

    public boolean checkMinRequirements() {
        try {
            List<WebElement> items = driver.findElements(By.xpath(
                "//span[contains(@class, 'SystemRequirements-module-scss-module__NEb1Ga__item')]"
            ));
            
            System.out.println("🔍 Найдено элементов с требованиями: " + items.size());
            
            StringBuilder fullText = new StringBuilder();
            for (WebElement item : items) {
                String text = item.getText();
                fullText.append(text).append(" ");
                System.out.println(" Требование: " + text);
            }
            
            String text = fullText.toString();
            
            boolean hasWindows = text.contains("Windows 11");
            boolean hasCPU = text.contains("Intel CPU Core i5-9600K");
            boolean hasRAM = text.contains("16 Гб");
            boolean hasGPU = text.contains("NVIDIA GeForce RTX 2070");
            boolean hasDisk = text.contains("50 ГБ");
            
            System.out.println("🔍 Windows 11: " + hasWindows);
            System.out.println("🔍 CPU: " + hasCPU);
            System.out.println("🔍 RAM: " + hasRAM);
            System.out.println("🔍 GPU: " + hasGPU);
            System.out.println("🔍 Disk: " + hasDisk);
            
            return hasWindows && hasCPU && hasRAM && hasGPU && hasDisk;
            
        } catch (Exception e) {
            System.out.println(" Ошибка при проверке требований: " + e.getMessage());
            return false;
        }
    }

  
    // КЕЙС 3: Ссылки на скачивание
   

    public void clickGameInFooter(String gameName) throws InterruptedException {
        try {
            WebElement link = driver.findElement(By.xpath("//footer//a[contains(text(), '" + gameName + "')]"));
            ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block: 'center'});", link);
            Thread.sleep(500);
            waitForElementClickable(link);
            link.click();
            Thread.sleep(1000);
        } catch (Exception e) {
            WebElement link = driver.findElement(By.xpath("//a[contains(text(), '" + gameName + "')]"));
            ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block: 'center'});", link);
            Thread.sleep(500);
            waitForElementClickable(link);
            link.click();
            Thread.sleep(1000);
        }
    }

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
    
// КЕЙС 1: Получение цены из результатов поиска 


public String getPriceInSearchResults(String gameName) throws InterruptedException {
    // Загружаем все игры, чтобы найти Pioner
    loadMoreGamesIfNeeded(gameName);
    
    // Находим все карточки
    List<WebElement> cards = driver.findElements(By.xpath("//div[contains(@class, 'ProductCard')]"));
    System.out.println("🔍 Найдено карточек: " + cards.size());
    
    for (WebElement card : cards) {
        // Проверяем название игры в карточке
        List<WebElement> titleElements = card.findElements(By.xpath(".//a[contains(@class, 'ProductCard') and contains(@class, 'title')]"));
        for (WebElement title : titleElements) {
            String titleText = title.getText().trim();
            System.out.println(" Название: " + titleText);
            
            if (titleText.toLowerCase().contains(gameName.toLowerCase())) {
                System.out.println(" Нашли игру: " + gameName);
                
                // Ищем цену в этой же карточке (не зачеркнутую)
                List<WebElement> priceElements = card.findElements(By.xpath(
                    ".//span[contains(@class, 'rt-Text') and contains(text(), '₽')]" +
                    "[not(contains(@class, 'crossed-price'))]"
                ));
                
                if (!priceElements.isEmpty()) {
                    String priceText = priceElements.get(0).getText().trim();
                    System.out.println(" Цена в карточке: " + priceText);
                    return priceText;
                } else {
                    System.out.println(" Цена не найдена в карточке");
                    return "";
                }
            }
        }
    }
    
    throw new RuntimeException("Игра " + gameName + " не найдена в результатах поиска");
}
}