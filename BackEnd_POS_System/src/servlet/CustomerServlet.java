package servlet;

import java.io.IOException;

/**
 * @author : A.D.Liyanage
 * @since : 0.1.0
 **/

@WebServlet(urlPatterns = "/customerss")
public class CustomerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
        System.out.println("request ");
    }
}
//http://localhost:8080/javaEEpos/customer