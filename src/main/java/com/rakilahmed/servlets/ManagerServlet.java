package com.rakilahmed.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rakilahmed.controllers.user.ManagerController;
import com.rakilahmed.models.user.Manager;
import com.rakilahmed.services.user.ManagerDAO;

@WebServlet("/managers/*")
public class ManagerServlet extends HttpServlet {
    private ObjectMapper objectMapper;
    private ManagerController managerController;

    @Override
    public void init() throws ServletException {
        objectMapper = new ObjectMapper();
        managerController = new ManagerController(new ManagerDAO());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws javax.servlet.ServletException, IOException {
        response.setContentType("application/json");
        String parameter = request.getRequestURI().split("/")[3];

        try {
            int id = Integer.parseInt(parameter);

            if (managerController.get(id) != null) {
                response.getWriter().write(objectMapper.writeValueAsString(managerController.get(id)));
                response.setStatus(200);
            } else {
                response.getWriter().write("{" + "\"message\":\"Manager not found\"" + "}");
                response.setStatus(404);
            }
        } catch (NumberFormatException e) {
            response.getWriter().write(
                    "{" + "\"message\":\"Invalid request, to get a manager, use /managers/{id} endpoint\"" + "}");
            response.setStatus(404);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws javax.servlet.ServletException, IOException {
        resp.setContentType("application/json");
        String parameter = req.getRequestURI().split("/")[3];

        try {
            if (!parameter.equals("new")) {
                resp.getWriter().write(
                        "{" + "\"message\":\"Invalid request, to create a new manager, use the /managers/new endpoint\""
                                + "}");
                resp.setStatus(404);
                return;
            }

            Manager manager = objectMapper.readValue(req.getReader(),
                    com.rakilahmed.models.user.Manager.class);
            String message = managerController.register(manager);

            if (message.contains("success")) {
                resp.getWriter().write(objectMapper.writeValueAsString(managerController.getCurrentManager()));
                resp.setStatus(201);
            } else {
                resp.getWriter().write("{" + "\"message\":\"" + message + "\"" + "}");
                resp.setStatus(400);
            }
        } catch (NumberFormatException e) {
            resp.getWriter().write("{"
                    + "\"message\":\"Invalid request, to create a new manager, use the /managers/new endpoint\"" + "}");
            resp.setStatus(404);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws javax.servlet.ServletException, IOException {
        resp.setContentType("application/json");
        String idParameter = req.getRequestURI().split("/")[3];
        String operationParameter = req.getRequestURI().split("/")[4];

        try {
            int id = Integer.parseInt(idParameter);

            if (!operationParameter.equals("update")) {
                resp.getWriter().write("{"
                        + "\"message\":\"Invalid request, to update a manager, use the /managers/{id}/update endpoint\""
                        + "}");
                resp.setStatus(404);
                return;
            }

            if (managerController.get(id) != null) {
                Manager manager = objectMapper.readValue(req.getReader(), Manager.class);
                String message = managerController.updateProfile(id, manager.getUsername(),
                        manager.getPassword(), manager.getFullName(), manager.getEmail());

                if (message.contains("success")) {
                    resp.getWriter().write(objectMapper.writeValueAsString(managerController.get(id)));
                    resp.setStatus(200);
                } else {
                    resp.getWriter().write("{" + "\"message\":\"" + message + "\"" + "}");
                    resp.setStatus(400);
                }
            } else {
                resp.getWriter().write("{" + "\"message\":\"Manager not found\"" + "}");
                resp.setStatus(404);
            }
        } catch (NumberFormatException e) {
            resp.getWriter().write(
                    "{" + "\"message\":\"Invalid request, to update a manager, use the /managers/{id}/update endpoint\""
                            + "}");
            resp.setStatus(404);
        }
    }
}
