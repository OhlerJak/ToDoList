package todo;

import database.Database;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


@WebServlet(name = "ToDo", value = "/ToDo" )
public class ToDo extends HttpServlet {
    private static final String List = "itemlist";
    private static final String ITEM = "item";


    private List<String> todos;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request,response);
    }


    void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

            response.setContentType("text/html,charset=UTF-8");
            PrintWriter pwout = response.getWriter();
            HttpSession session = request.getSession();
             todos = (List<String>) session.getAttribute(List);
            if(todos ==null){
                try {
                    todos = Database.getInstance().getallData();
                }catch (SQLException e) {

                }
                session.setAttribute(List,todos);
            }

            String item = request.getParameter(ITEM);
            if(item!=null&& !item.trim().isEmpty()){
                todos.add(item);
            }

            //Form einfügen
        BufferedReader br = new BufferedReader(new InputStreamReader(getServletContext().getResourceAsStream("/myfiles/Form&Header.html")));
        while (br.ready()) {
            pwout.println(br.readLine());
        }

            if(todos.isEmpty()){
                pwout.println("<h2> Keine Eintraege </h2>");
            }else {
                pwout.println("<h2> Eintraege: </h2>");
                for (int i=0;i<todos.size(); i++) {
                    pwout.println(todos.get(i)+"<br>");
                }
            }

            pwout.println("</body>");
            pwout.println("</html>");
    }

    @Override
    public void init() throws ServletException {
        super.init();


    }
    @Override
    public void destroy() {
        super.destroy();
        try {
            Database.getInstance().saveData(todos);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
