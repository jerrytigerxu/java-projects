// This is a more improved version of the regular web crawler program because now there are multiple threads to run on for the web crawlers
package MTWebCrawler;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<MTWebCrawler.WebCrawler> bots = new ArrayList<>();
        bots.add(new MTWebCrawler.WebCrawler("https://abcnews.go.com", 1));
        bots.add(new MTWebCrawler.WebCrawler("https://www.npr.org", 2));
        bots.add(new MTWebCrawler.WebCrawler("https://www.nytimes.com", 3));

        for (WebCrawler w : bots) {
            try {
                w.getThread().join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
