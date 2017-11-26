package m.wandl.java;

import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Main {
	
	static String[] ps4Keys = new String[] {"PS4", "PS 4", "PlayStation", "Play Station"};
	static String[] airPodsKes = new String[] {"air","pod"};

	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", "/Users/mwandl/Documents/Projects/Selenium/chromedriver");
		System.setProperty("webdriver.gecko.driver", "/Users/mwandl/Documents/Projects/Selenium/geckodriver");
		
		// Create a new instance of the html unit driver
        // Notice that the remainder of the code relies on the interface,
        // not the implementation.
        WebDriver driver = new ChromeDriver();

        driver.get("http://www.blackfridaysale.at");

        // Find the text input element by its name
        WebElement element = driver.findElement(By.className("navbar-nav"));

        List<WebElement> links = element.findElements(By.tagName("a"));
        
        List<Category> categories = new LinkedList<Category>();
        
        for (WebElement link : links) {
			String href = link.getAttribute("href");
			if(href.contains("category")) {
				categories.add(new Category(link.getText(), href));
			}
			
		}
        
        System.out.println("Found "+categories.size()+" categories");
        
        for(Category category : categories) {
	        driver.get(category.getLink());
	        System.out.println(driver.getTitle().toUpperCase());
	        try {
				WebElement productList = driver.findElement(By.id("product-list"));

		        WebElement btnMore = productList.findElement(By.cssSelector("div.buttons-container.clearfix > div"));
				while(btnMore != null && btnMore.isDisplayed()) {
			        btnMore.click();
					btnMore = productList.findElement(By.cssSelector("div.buttons-container.clearfix > div"));
				}
				
				List<WebElement> products = productList.findElements(By.className("product-item__text__title"));
				for (WebElement product : products) {
					if(isRequired(ps4Keys, product.getText())) {
						System.out.println("GEFUNDEN: " + product.getAttribute("href"));
					}
				}
			} catch (Exception e) {
				System.out.println("----- Keine Produkte vorhanden");
			}
	        
	        
        }

        driver.quit();

	}
	
	static boolean isRequired(String[] keys, String product) {
		for (String key : keys) {
			if(product.toLowerCase().contains(key.toLowerCase())) return true;
		}
		return false;
	}

}
