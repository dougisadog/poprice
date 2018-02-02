package poprice.wechat.web.mvc.filter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qq.wechat.WechatHaoHelper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * 微信的请求处理，登录与否
 */
public class WxSecurityFilter implements Filter {
    private final Logger log = LoggerFactory.getLogger(WxSecurityFilter.class);

    public WxSecurityFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {
        // Nothing to destroy
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(true);

        String targetURI = httpRequest.getRequestURI();
        if (targetURI.endsWith(".ico") || targetURI.endsWith(".css") || targetURI.endsWith(".js") || targetURI.endsWith(".jpg") || targetURI.endsWith(".png")) {
            chain.doFilter(request, response);
            return;
        }
        if (!targetURI.contains("/wx")) {
            chain.doFilter(request, response);
            return;
        }

        //已经登录，直接放过
        if (session.getAttribute(WechatHaoHelper.WX_LOGIN_KEY) != null) {
            chain.doFilter(request, response);
            return;
        } else {

            Map<String, String[]> params = request.getParameterMap();
            if (params.get("code") != null) {
                if (!StringUtils.isBlank(params.get("code")[0])) {
                    String openId = WechatHaoHelper.getInstance().getWechat().base().openId(params.get("code")[0]);
                    session.setAttribute(WechatHaoHelper.WX_LOGIN_KEY, openId);
                }
            }
            String url = "http://" + httpRequest.getHeader("HOST") + "/wx";
            String to = WechatHaoHelper.getInstance().getRedirectUrl(url);
            httpResponse.sendRedirect(to);
        }

        //非ajax请求，直接忽略给js和controller处理
        chain.doFilter(request, response);
    }


}
