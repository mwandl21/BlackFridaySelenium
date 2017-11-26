package m.wandl.java;

import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Main {
	
	static String[] ps4Keys = new String[] {"PS4", "PS 4", "PlayStation", "Play Station"};
	static String[] airPodsKeys = new String[] {"airpod", "pod"};

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
        int index = 0;
        while(true) {
        		int i = index % categories.size();
        		driver.get(categories.get(i).getLink());
    	        System.out.println(driver.getTitle().toUpperCase() + " - " + index);
    	        try {
    				WebElement productList = driver.findElement(By.id("product-list"));

    		        WebElement btnMore = productList.findElement(By.cssSelector("div.buttons-container.clearfix > div"));
    				while(btnMore != null && btnMore.isDisplayed()) {
    					try {
    						btnMore.click();
    					}
    					catch (Exception e) {
    						//nichts zu machen, nochmal probieren
    					}
    					btnMore = productList.findElement(By.cssSelector("div.buttons-container.clearfix > div"));
    				}
    				
    				List<WebElement> titles = productList.findElements(By.className("product-item__text__title"));
    				
    				for (WebElement title : titles) {			
    					if(isRequired(airPodsKeys, title.getText())) {
    						System.out.println("---------------------");
    						System.out.println(title.getText());
    						System.out.println(title.getAttribute("href"));
    						System.out.println("---------------------");
    					}
    				}
    			} catch (Exception e) {
    				if(e.getMessage() == null || !e.getMessage().contains("product-list")) {
    					System.out.println(e);
    				}
    			}
    	        
        		index++;
        }

	}
	
	static boolean isRequired(String[] keys, String product) {
		for (String key : keys) {
			if(product.toLowerCase().contains(key.toLowerCase())) return true;
		}
		return false;
	}

}
