package poprice.wechat.web.filter;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.filter.GenericFilterBean;

import poprice.wechat.security.SecurityUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * This filter is used in production, to serve static resources generated by "grunt build".
 * <p/>
 * <p>
 * It is configured to serve resources from the "dist" directory, which is the Grunt
 * destination directory.
 * </p>
 */
public class ApiSecurityFilter extends GenericFilterBean {
    public static final String REST_LOGIN_URI = "/api/v1.0/authenticate";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        //System.out.println(":::URI:::" + httpRequest.getRequestURI());

        HttpSession session = httpRequest.getSession();
        SecurityContext securityContext = (SecurityContext)session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);


        boolean isAuthURI = httpRequest.getRequestURI().startsWith(REST_LOGIN_URI);
        boolean authenticated = securityContext != null;

        if (!authenticated && !isAuthURI) {
            //System.out.println(":::未登录");
        	httpResponse.setHeader("WWW-Authenticate", "xBasic realm=\"\"");
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        if (securityContext != null) {
            SecurityContextHolder.setContext(securityContext);
        }

        //System.out.println(":::已登录:::" + SecurityUtils.isAuthenticated());
        chain.doFilter(request, response);

    }
}
