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
import java.io.PrintWriter;
import java.sql.SQLException;


public class StartupServlet extends HttpServlet {

    private static DataBaseModel dataBaseModel;
    protected static DataBaseService dataBaseService;
    protected static UserModel userModel;
    protected static UserService userService;
    protected static InputService inputService;

    public void init(){
        dataBaseModel = new DataBaseModel();
        userModel = new UserModel();
        userService = new UserService(userModel);
        dataBaseService = new DataBaseService(dataBaseModel,userService);
        inputService = new InputService(dataBaseService,userModel,userService);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.getWriter().append("Served at: ").append(request.getContextPath());

        RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
        dispatcher.forward(request,response);
    }

    public static DataBaseModel getDataBaseModel() {
        return dataBaseModel;
    }

    public static DataBaseService getDataBaseService() {
        return dataBaseService;
    }

    public static UserModel getUserModel() {
        return userModel;
    }

    public static UserService getUserService() {
        return userService;
    }

    public static InputService getInputService() {
        return inputService;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        try {
            if(request.getParameter("signupbutton") != null) {
                response.sendRedirect("/registration");
            }
            if(request.getParameter("signinbutton") != null) {
                inputService.Try_SignIn(login, password);
                response.sendRedirect("/home");
            }
        } catch (CustomException e) {
            response.sendRedirect("/");
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
