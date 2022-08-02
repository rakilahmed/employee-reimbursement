package com.rakilahmed.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rakilahmed.controllers.user.EmployeeController;
import com.rakilahmed.models.user.Employee;
import com.rakilahmed.services.user.EmployeeDAO;

@WebServlet("/employees")
public class EmployeesServlet extends HttpServlet {
    private ObjectMapper objectMapper;
    private EmployeeController employeeController;

    @Override
    public void init() {
        objectMapper = new ObjectMapper();
        employeeController = new EmployeeController(new EmployeeDAO());
    }

    @Override
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json");
        List<Employee> employees = employeeController.getAll();

        if (employees != null) {
            response.getWriter().write(objectMapper.writeValueAsString(employees));
            response.setStatus(200);
        } else {
            response.getWriter().write("{" + "\"message\":\"No employees found\"" + "}");
            response.setStatus(404);
        }
    }
}
