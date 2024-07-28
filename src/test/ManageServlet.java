package test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ManageServlet extends HttpServlet{


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getHeader("action");
        StatusServlet.setAction(action);;
        resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            System.out.println("action is "+ action);
        resp.getWriter().print("{\"response\":\""+ StatusServlet.getAction() + "\"}");
    }
}
