package com.rakilahmed.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rakilahmed.controllers.user.ManagerController;
import com.rakilahmed.models.user.Manager;
import com.rakilahmed.services.user.ManagerDAO;

@WebServlet("/managers")
public class ManagersServlet extends HttpServlet {
    private ObjectMapper objectMapper;
    private ManagerController managerController;

    @Override
    public void init() throws ServletException {
        objectMapper = new ObjectMapper();
        managerController = new ManagerController(new ManagerDAO());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        List<Manager> managers = managerController.getAll();

        if (managers != null) {
            response.getWriter().write(objectMapper.writeValueAsString(managers));
            response.setStatus(200);
        } else {
            response.getWriter().write("{" + "\"message\":\"No managers found\"" + "}");
            response.setStatus(404);
        }
    }
}
