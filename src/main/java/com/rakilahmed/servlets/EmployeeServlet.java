package com.rakilahmed.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rakilahmed.controllers.user.EmployeeController;
import com.rakilahmed.models.user.Employee;
import com.rakilahmed.services.user.EmployeeDAO;

@WebServlet("/employees/*")
public class EmployeeServlet extends HttpServlet {
    private ObjectMapper objectMapper;
    private EmployeeController employeeController;

    @Override
    public void init() throws ServletException {
        objectMapper = new ObjectMapper();
        employeeController = new EmployeeController(new EmployeeDAO());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        String parameter = request.getRequestURI().split("/")[3];

        try {
            int id = Integer.parseInt(parameter);

            if (employeeController.get(id) != null) {
                response.getWriter().write(objectMapper.writeValueAsString(employeeController.get(id)));
                response.setStatus(200);
            } else {
                response.getWriter().write("{" + "\"message\":\"Employee not found\"" + "}");
                response.setStatus(404);
            }
        } catch (NumberFormatException e) {
            response.getWriter().write(
                    "{" + "\"message\":\"Invalid request, to get an employee, use /employees/{id} endpoint\"" + "}");
            response.setStatus(404);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String parameter = req.getRequestURI().split("/")[3];

        try {
            if (!parameter.equals("new")) {
                resp.getWriter().write(
                        "{" + "\"message\":\"Invalid request, to create a new employee, use the /employees/new endpoint\""
                                + "}");
                resp.setStatus(404);
                return;
            }

            Employee employee = objectMapper.readValue(req.getReader(), Employee.class);
            String message = employeeController.register(employee);

            if (message.contains("success")) {
                resp.getWriter().write(objectMapper.writeValueAsString(employeeController.getCurrentEmployee()));
                resp.setStatus(201);
            } else {
                resp.getWriter().write("{" + "\"message\":\"" + message + "\"" + "}");
                resp.setStatus(400);
            }
        } catch (NumberFormatException e) {
            resp.getWriter().write(
                    "{" + "\"message\":\"Invalid request, to create a new employee, use the /employees/new endpoint\""
                            + "}");
            resp.setStatus(404);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String idParameter = req.getRequestURI().split("/")[3];
        String operationParameter = req.getRequestURI().split("/")[4];

        try {
            int id = Integer.parseInt(idParameter);

            if (!operationParameter.equals("update")) {
                resp.getWriter().write(
                        "{" + "\"message\":\"Invalid request, to update an employee, use the /employees/{id}/update endpoint\""
                                + "}");
                resp.setStatus(404);
                return;
            }

            if (employeeController.get(id) != null) {
                Employee employee = objectMapper.readValue(req.getReader(), Employee.class);
                String message = employeeController.updateProfile(id, employee.getUsername(),
                        employee.getPassword(), employee.getFullName(), employee.getEmail());

                if (message.contains("success")) {
                    resp.getWriter().write(objectMapper.writeValueAsString(employeeController.get(id)));
                    resp.setStatus(200);
                } else {
                    resp.getWriter().write("{" + "\"message\":\"" + message + "\"" + "}");
                    resp.setStatus(400);
                }
            } else {
                resp.getWriter().write("{" + "\"message\":\"Employee not found\"" + "}");
                resp.setStatus(404);
            }
        } catch (NumberFormatException e) {
            resp.getWriter().write(
                    "{" + "\"message\":\"Invalid request, to update an employee, use the /employees/{id}/update endpoint\""
                            + "}");
            resp.setStatus(404);
        }
    }
}
