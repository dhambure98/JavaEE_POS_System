package lk.pos.filters;

import java.io.IOException;

/**
 * @author : A.D.Liyanage
 * @since : 0.1.0
 **/

@WebFilter(urlPatterns = "/*")
public class CORSFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletResponse sr = (HttpServletResponse) servletResponse;
        sr.addHeader("Access-Control-Allow-Origin","*");
        sr.addHeader("Access-Control-Allow-Methods","DELETE,PUT");
        sr.addHeader("Access-Control-Allow-Headers","Content-Type");
        filterChain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() {

    }
}
