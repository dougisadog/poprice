package poprice.wechat.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HttpAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    public HttpAuthenticationSuccessHandler() {

    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.error("用户{}从IP:{}登录成功", authentication.getName(), request.getRemoteAddr().toString());
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
