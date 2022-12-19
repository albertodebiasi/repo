package cookies;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class cookies {
    public static void setCsrfToken(HttpServletRequest request, HttpServletResponse response) throws NoSuchAlgorithmException {
	    SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
	    byte[] data = new byte[16];
	    secureRandom.nextBytes(data);
	    String csrfToken = Base64.getEncoder().encodeToString(data);
        Cookie cookie = new Cookie("X-CSRF", csrfToken);
        response.addCookie(cookie);
        HttpSession session = request.getSession();
        session.setAttribute("csrfToken", csrfToken);
    }

    public static void checkCsrf(HttpServletRequest request){
        //read the cookie content
        String cookieClient = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("X-CSRF"))
                .map(Cookie::getValue)
                .findFirst().orElse("");
        HttpSession session = request.getSession();

        String cookieServer = (String)session.getAttribute("csrfToken");

        try {
            if(cookieClient.equals(cookieServer) ){
                System.out.println("token true");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) cookies = new Cookie[]{};
        Arrays.stream(cookies)
            .filter(cookie -> cookie.getName().equals("X-CSRF"))
            .forEach(cookie -> {
                cookie.setValue("");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            });
    }
}