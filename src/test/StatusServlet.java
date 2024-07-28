package test;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.atomic.AtomicReference;
import javax.servlet.http.HttpServletResponse;

public class StatusServlet extends HttpServlet{

    public static AtomicReference<String> action = new AtomicReference<>() ;

    public static String getAction(){
        return action.get();
    }
    public static void setAction(String act){
        action.set(act);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
        writer.print("{\"response\" :  \""+ getAction()+ "\"}");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}
