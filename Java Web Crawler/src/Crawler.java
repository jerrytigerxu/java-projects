// For this web crawler, we'll need to use jsoup, a Java HTML parser
// We download the jsoup(1.15.2).jar file and then add it to the class path by going to [File -> Project Structure -> Modules -> Dependencies -> Add JAR]
// Another solution is to put the jar file into a folder called "libs" and then add it as a library
import org.jsoup.Connection;
import org.jsoup.nodes.*;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;

public class Crawler {
    public static void main(String[] args) {
        String url = "https://en.wikipedia.org/";
        crawl(1, url, new ArrayList<String>());
    }

    private static void crawl(int level, String url, ArrayList<String> visited) {
        if (level <= 5) {
            Document doc = request(url, visited);

            if (doc != null) {
                for (Element link : doc.select("a[href]")) {
                    String next_link = link.absUrl("href");
                    if (visited.contains(next_link) == false) {
                        crawl(level++, next_link, visited);
                    }
                }
            }

        }
    }

    private static Document request(String url, ArrayList<String> v) {
        try {
            Connection con = Jsoup.connect(url);
            Document doc = con.get();

            if (con.response().statusCode() == 200) {
                System.out.println("Link: " + url);
                System.out.println(doc.title());
                v.add(url);

                return doc;
            }
            return null;
        } catch (IOException e) {
            return null;
        }

    }



}
