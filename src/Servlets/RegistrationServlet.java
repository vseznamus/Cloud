package Servlets;

import Exceptions.CustomException;
import Models.DataBaseModel;
import Models.UserModel;
import Services.DataBaseService;
import Services.InputService;
import Services.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class RegistrationServlet extends HttpServlet {

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


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.getWriter().append("Served at: ").append(request.getContextPath());

        RequestDispatcher dispatcher = request.getRequestDispatcher("regis.jsp");
        dispatcher.forward(request,response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String repeat = request.getParameter("repeatPassword");

        try {
            if(request.getParameter("signupbutton") != null) {
                inputService.Try_SignUp(login,password,repeat);
                response.sendRedirect("/home");
            }
            if(request.getParameter("signinbutton") != null) {
                response.sendRedirect("/");
            }
        } catch (CustomException e) {
            response.sendRedirect("/registration");
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
