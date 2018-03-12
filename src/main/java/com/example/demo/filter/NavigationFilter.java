package com.example.demo.filter;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;

@ControllerAdvice
public class NavigationFilter {
    static String[] NAVS = {
            // url, icon, name, title
            "/", "home", "Dashboard", "Dashboard",
            "/air", "file-text", "Passenger registration", "Passenger registration",
    };

    @ModelAttribute("__navigation")
    public Navigation auth(HttpServletRequest request) {
        return new Navigation(request.getRequestURI());
    }

    public static class Navigation implements Iterable<Navigation.Nav> {

        ArrayList<Nav> navs = new ArrayList<>();

        public Nav active;

        Navigation(String currentUrl) {
            for (int i = 0; i < NAVS.length; i += 4) {
                boolean isActive = NAVS[i].equals(currentUrl) || currentUrl.startsWith(NAVS[i] + "/");
                Nav nav = new Nav(
                        NAVS[i],
                        NAVS[i + 1],
                        NAVS[i + 2],
                        NAVS[i + 3],
                        isActive ? "active" : "");
                navs.add(nav);
                if (isActive) {
                    active = nav;
                }
            }
        }

        @Override
        public Iterator<Nav> iterator() {
            return navs.iterator();
        }

        @AllArgsConstructor
        public static class Nav {
            public final String url, icon, name, title, active;
        }
    }
}
