package Servlets;

import Models.DataBaseModel;
import Models.UserModel;
import Services.DataBaseService;
import Services.InputService;
import Services.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class HomeServlet extends HttpServlet {

    DataBaseModel dataBaseModel;
    DataBaseService dataBaseService;
    UserModel userModel;
    UserService userService;
    InputService inputService;

    public void init(){
        dataBaseModel = StartupServlet.getDataBaseModel();
        userModel = StartupServlet.getUserModel();
        userService = StartupServlet.getUserService();
        dataBaseService = StartupServlet.getDataBaseService();
        inputService = StartupServlet.getInputService();
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());

        RequestDispatcher dispatcher = request.getRequestDispatcher("home.jsp");
        request.setAttribute("currentUser", userModel.GetLogin());
        try {
            request.setAttribute("filelist", dataBaseService.GetMediaList());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        dispatcher.forward(request,response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

    }
}
