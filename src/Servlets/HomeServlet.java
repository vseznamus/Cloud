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
import java.util.List;

public class HomeServlet extends HttpServlet {

    DataBaseModel dataBaseModel;
    DataBaseService dataBaseService;
    UserModel userModel;
    UserService userService;
    InputService inputService;

    public void init() { // Инициализируем логику (продолжаем ее - берем статик)
        dataBaseModel = StartupServlet.getDataBaseModel();
        userModel = StartupServlet.getUserModel();
        userService = StartupServlet.getUserService();
        dataBaseService = StartupServlet.getDataBaseService();
        inputService = StartupServlet.getInputService();
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());

        RequestDispatcher dispatcher = request.getRequestDispatcher("home.jsp"); // данный сервлет относится к файлу в скобках
        request.setAttribute("currentUser", userModel.GetLogin()); // создаем "переменную" с логином текущего юзера

        // получаем список файлов и записываем его в атрибут для передачи в вёрстку
        // попоутно оборачиваем все в исключения
        try {
            List<String> mediaList = List.of(dataBaseService.GetMediaList());
            request.setAttribute("filelist", mediaList);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        dispatcher.forward(request, response); // перенаправляем запрос
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    }
}
