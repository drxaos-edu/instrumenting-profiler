package com.example.demo.filter;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;

@ControllerAdvice
public class NavigationFilter {

    public static Navigation NAVIGATION = new Navigation(
            "", "", "404", "Page Not Found",
            "/", "home", "Dashboard", "Main page",
            "/one", "file-text", "Example #1 (Lucene)", "Russian spelling",
            "/two", "help-circle", "Example #2 (???)", "Next example"
    );

    @ModelAttribute("__navigation")
    public Navigation auth(HttpServletRequest request) {
        return NAVIGATION.withCurrentUrl(request.getRequestURI());
    }

    public static class Navigation implements Iterable<Navigation>, Iterator<Navigation> {
        String[] navs;
        String currentUrl;
        int iter = -1;

        Navigation(String... navs) {
            this.navs = navs;
        }

        Navigation(String[] navs, String currentUrl) {
            this.navs = navs;
            this.currentUrl = currentUrl;
        }

        Navigation(String[] navs, String currentUrl, int iter) {
            this.navs = navs;
            this.currentUrl = currentUrl;
            this.iter = iter;
        }

        public Navigation withCurrentUrl(String currentUrl) {
            return new Navigation(navs, currentUrl);
        }

        public Navigation findActive() {
            for (int i = 0; i < navs.length / 4; i++) {
                if (navs[i * 4].equals(currentUrl)) {
                    return new Navigation(navs, currentUrl, i);
                }
            }
            return new Navigation(navs, currentUrl, 0);
        }

        @Override
        public Iterator<Navigation> iterator() {
            return new Navigation(navs, currentUrl, 1);
        }

        @Override
        public boolean hasNext() {
            return iter < navs.length / 4;
        }

        @Override
        public Navigation next() {
            return new Navigation(navs, currentUrl, iter++);
        }

        public String getUrl() {
            return navs[iter * 4];
        }

        public String getIcon() {
            return navs[iter * 4 + 1];
        }

        public String getName() {
            return navs[iter * 4 + 2];
        }

        public String getTitle() {
            return navs[iter * 4 + 3];
        }

        public String getNavClass() {
            return navs[iter * 4].equals(currentUrl) ? "active" : "";
        }
    }
}