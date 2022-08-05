package com.rakilahmed.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rakilahmed.controllers.reimbursement.ReimbursementController;
import com.rakilahmed.controllers.user.EmployeeController;
import com.rakilahmed.models.reimbursement.Reimbursement;
import com.rakilahmed.models.user.Employee;
import com.rakilahmed.services.reimbursement.ReimbursementDAO;
import com.rakilahmed.services.user.EmployeeDAO;

@WebServlet("/employees/*")
public class EmployeeServlet extends HttpServlet {
    private ObjectMapper objectMapper;
    private EmployeeController employeeController;
    private ReimbursementController reimbursementController;
    private final Logger logger = LogManager.getLogger(EmployeeServlet.class);

    /**
     * Private helper method to validate user's login status.
     * 
     * @param request  The request object.
     * @param response The response object.
     * @param id       The user's id.
     * @return True if user is logged in, false otherwise.
     */
    private boolean isLoggedIn(HttpServletRequest request, HttpServletResponse response, int id) {
        logger.info("Validating login status for employee: " + id);

        if (id <= 0) {
            logger.warn("Invalid employee id: " + id);
            response.setStatus(401);
            return true;
        }

        if (request.getSession().getAttribute("employee") == null
                || (int) request.getSession().getAttribute("employee") != id) {
            logger.warn("Employee is not logged in: " + id);
            response.setStatus(401);
            return true;
        }

        logger.info("Employee is logged in: " + id);
        return false;
    }

    /**
     * Private helper to write a JSON response if employee is not logged in.
     * 
     * @param response The response object.
     * @throws IOException If an error occurs while writing to the response.
     */
    private void employeeNotLoggedInJSON(HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.getWriter().write(
                "You are not logged in, but you are trying to access an employee's profile.\n\n");
        response.getWriter().write("Allowed endpoints & methods you need to use:\n");
        response.getWriter()
                .write("- (POST) /login : to login, provide your username and password\n");
        response.getWriter().write(
                "- (POST) /new   : to create a new account, provide your username, password, full name, and email\n");
        response.setStatus(401);
    }

    @Override
    public void init() {
        objectMapper = new ObjectMapper();
        employeeController = new EmployeeController(new EmployeeDAO());
        reimbursementController = new ReimbursementController(new ReimbursementDAO());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json");
        String[] parameters = request.getPathInfo().split("/");

        if (parameters.length == 2 && parameters[1].equals("login")) {
            logger.info("Employee trying to login");
            response.getWriter().write("{"
                    + "\"message\":\"To login, make a POST request to this endpoint with your username and password in the body of the request.\""
                    + "}");
            response.setStatus(400);
            return;
        } else if (parameters.length == 2 && parameters[1].equals("new")) {
            logger.info("Employee trying to create a new account");
            response.getWriter().write("{"
                    + "\"message\":\"To create a new employee, make a POST request to this endpoint with your username, password, full name, and email in the body of the request.\""
                    + "}");
            response.setStatus(400);
            return;
        } else if (parameters.length >= 2) {
            int id;

            if (parameters.length == 2) {
                try {
                    id = Integer.parseInt(parameters[1]);
                    logger.info("Employee is trying to go to dashboard: " + id);
                } catch (NumberFormatException e) {
                    logger.warn("Invalid employee id: " + parameters[1]);
                    response.getWriter().write("{" + "\"message\":\"Employee id must be an integer\"" + "}");
                    response.setStatus(400);
                    return;
                }

                if (isLoggedIn(request, response, id)) {
                    employeeNotLoggedInJSON(response);
                    return;
                }

                if (employeeController.get(id) != null) {
                    logger.info("Employee reached dashboard: " + id);
                    response.setContentType("text/plain");
                    response.getWriter().write("You are successfully logged in, your id is " + id + ".\n\n");
                    response.getWriter().write("Allowed endpoints & method you need to use:\n");
                    response.getWriter().write("- (GET)  /" + id + "/profile  : to view your profile\n");
                    response.getWriter().write("- (PUT)  /" + id + "/update   : to update your profile\n");
                    response.getWriter().write("- (POST) /" + id + "/request  : to create a new request\n");
                    response.getWriter().write("- (GET)  /" + id + "/pending  : to view all pending requests\n");
                    response.getWriter().write("- (GET)  /" + id + "/resolved : to view all resolved requests\n");
                    response.getWriter().write("- (GET)  /" + id + "/logout   : to logout\n");
                    response.setStatus(200);
                    return;
                } else if (employeeController.get(id) == null) {
                    logger.warn("Employee does not exist: " + id);
                    response.getWriter().write("{" + "\"message\":\"Employee not found\"" + "}");
                    response.setStatus(404);
                    return;
                }
            } else if (parameters.length == 3 && parameters[2].equals("profile")) {
                try {
                    id = Integer.parseInt(parameters[1]);
                    logger.info("Employee is trying to get profile info: " + id);
                } catch (NumberFormatException e) {
                    response.getWriter().write("{" + "\"message\":\"Employee id must be an integer\"" + "}");
                    response.setStatus(400);
                    return;
                }

                if (isLoggedIn(request, response, id)) {
                    employeeNotLoggedInJSON(response);
                    return;
                }

                Employee employee = employeeController.get(id);

                if (employee != null) {
                    logger.info("Employee profile info retrieved: " + id);
                    response.getWriter().write(objectMapper.writeValueAsString(employee));
                    response.setStatus(200);
                    return;
                }

                logger.warn("Employee does not exist: " + id);
                response.getWriter().write("{" + "\"message\":\"Employee not found\"" + "}");
                response.setStatus(404);
                return;
            } else if (parameters.length == 3 && parameters[2].equals("pending")) {
                try {
                    id = Integer.parseInt(parameters[1]);
                    logger.info("Employee is trying to view pending requests: " + id);
                } catch (NumberFormatException e) {
                    logger.warn("Invalid employee id: " + parameters[1]);
                    response.getWriter().write("{" + "\"message\":\"Employee id must be an integer\"" + "}");
                    response.setStatus(400);
                    return;
                }

                if (isLoggedIn(request, response, id)) {
                    employeeNotLoggedInJSON(response);
                    return;
                }

                List<Reimbursement> pendingReimbursements = reimbursementController.getAllPendingForEmployee(id);

                if (pendingReimbursements != null) {
                    logger.info("Employee pending requests retrieved: " + id);
                    response.getWriter()
                            .write("{" + "\"pending_reimbursements\": "
                                    + objectMapper.writeValueAsString(pendingReimbursements)
                                    + "}");
                    response.setStatus(200);
                    return;
                }

                logger.warn("Employee does not exist: " + id);
                response.getWriter().write("{" + "\"message\":\"No pending reimbursements\"" + "}");
                response.setStatus(404);
                return;
            } else if (parameters.length == 3 && parameters[2].equals("resolved")) {
                try {
                    id = Integer.parseInt(parameters[1]);
                    logger.info("Employee is trying to view resolved requests: " + id);
                } catch (NumberFormatException e) {
                    logger.warn("Invalid employee id: " + parameters[1]);
                    response.getWriter().write("{" + "\"message\":\"Employee id must be an integer\"" + "}");
                    response.setStatus(400);
                    return;
                }

                if (isLoggedIn(request, response, id)) {
                    employeeNotLoggedInJSON(response);
                    return;
                }

                List<Reimbursement> resolvedReimbursements = reimbursementController.getAllResolvedForEmployee(id);

                if (resolvedReimbursements != null) {
                    logger.info("Employee resolved requests retrieved: " + id);
                    response.getWriter()
                            .write("{" + "\"resolved_reimbursements\": "
                                    + objectMapper.writeValueAsString(resolvedReimbursements)
                                    + "}");
                    response.setStatus(200);
                    return;
                }

                logger.warn("Employee does not exist: " + id);
                response.getWriter().write("{" + "\"message\":\"No resolved reimbursements\"" + "}");
                response.setStatus(404);
                return;
            } else if (parameters.length == 3 && parameters[2].equals("logout")) {
                try {
                    id = Integer.parseInt(parameters[1]);
                    logger.info("Employee is trying to logout: " + id);
                } catch (NumberFormatException e) {
                    logger.warn("Invalid employee id: " + parameters[1]);
                    response.getWriter().write("{" + "\"message\":\"Employee id must be an integer\"" + "}");
                    response.setStatus(400);
                    return;
                }

                if (isLoggedIn(request, response, id)) {
                    response.getWriter().write("{" + "\"message\":\"You are not logged in\"" + "}");
                    response.setStatus(401);
                    return;
                }

                request.getSession().invalidate();
                logger.info("Employee logged out: " + id);
                response.getWriter().write("{" + "\"message\":\"You have been logged out\"" + "}");
                response.setStatus(200);
                return;
            }
        }

        logger.warn("Invalid endpoint: " + request.getRequestURI());
        response.getWriter().write("{" + "\"message\":\"Endpoint not found\"" + "}");
        response.setStatus(400);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String[] parameters = req.getPathInfo().split("/");

        if (parameters.length == 2 && !parameters[1].equals("new") && !parameters[1].equals("login")) {
            logger.warn("Invalid endpoint: " + req.getRequestURI());
            resp.getWriter().write("{" + "\"message\":\"Endpoint not found\"" + "}");
            resp.setStatus(400);
            return;
        }

        if (parameters.length == 2 && parameters[1].equals("new")) {
            Employee employee = objectMapper.readValue(req.getReader(), Employee.class);
            int id = employeeController.register(employee);

            if (id > 0) {
                logger.info("Employee registered: " + id);
                resp.getWriter().write(objectMapper.writeValueAsString(employeeController.get(id)));
                resp.setStatus(201);
                return;
            }

            logger.warn("Employee registration failed: " + id);
            resp.getWriter().write("{" + "\"message\":\"Employee registration failed\"" + "}");
            resp.setStatus(404);
            return;
        } else if (parameters.length == 2) {
            logger.info("Employee is trying to login");
            Employee employee = objectMapper.readValue(req.getReader(), Employee.class);
            int id = employeeController.login(employee.getUsername(), employee.getPassword());

            if (id > 0) {
                logger.info("Employee logged in: " + id);

                if (isLoggedIn(req, resp, id)) {
                    req.getSession().setAttribute("employee", id);
                    req.getSession().setMaxInactiveInterval(15 * 60);
                }

                String path = req.getContextPath() + "/employees/" + id;
                logger.info("Redirecting to: " + path);
                resp.sendRedirect(path);
                return;
            }

            resp.getWriter().write("{" + "\"message\":\"Employee login failed\"" + "}");
            resp.setStatus(404);
            return;
        } else if (parameters.length == 3) {
            int id;

            try {
                id = Integer.parseInt(parameters[1]);
            } catch (NumberFormatException e) {
                logger.warn("Invalid employee id: " + parameters[1]);
                resp.getWriter().write("{" + "\"message\":\"Employee id must be an integer\"" + "}");
                resp.setStatus(400);
                return;
            }

            if (isLoggedIn(req, resp, id)) {
                employeeNotLoggedInJSON(resp);
                return;
            }

            if (parameters[2].equals("request")) {
                logger.info("Employee is trying to request reimbursement");
                Reimbursement reimbursement = objectMapper.readValue(req.getReader(), Reimbursement.class);
                reimbursement.setEmployeeId(id);
                int reimbursementId = reimbursementController.create(reimbursement);

                if (reimbursementId > 0) {
                    logger.info("Employee requested reimbursement: " + reimbursementId);
                    resp.getWriter()
                            .write(objectMapper.writeValueAsString(reimbursementController.get(reimbursementId)));
                    resp.setStatus(201);
                    return;
                }

                logger.warn("Employee request reimbursement failed: " + reimbursement.getId());
                resp.getWriter().write("{" + "\"message\":\"Employee request reimbursement failed\"" + "}");
                resp.setStatus(404);
                return;
            }
        }

        logger.warn("Invalid endpoint: " + req.getRequestURI());
        resp.getWriter().write("{" + "\"message\":\"Endpoint not found\"" + "}");
        resp.setStatus(400);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String idParameter = req.getRequestURI().split("/")[2];
        String operationParameter = req.getRequestURI().split("/")[3];

        int id;

        try {
            id = Integer.parseInt(idParameter);
            logger.info("Employee is trying to update profile: " + id);
        } catch (NumberFormatException e) {
            resp.getWriter().write("{" + "\"message\":\"Employee id must be an integer\"" + "}");
            resp.setStatus(400);
            return;
        }

        if (isLoggedIn(req, resp, id)) {
            employeeNotLoggedInJSON(resp);
            return;
        }

        if (!operationParameter.equals("update")) {
            logger.warn("Invalid endpoint: " + req.getRequestURI());
            resp.getWriter().write("{" + "\"message\":\"Endpoint not found\"" + "}");
            resp.setStatus(400);
            return;
        }

        if (employeeController.get(id) == null) {
            logger.warn("Employee not found: " + id);
            resp.getWriter().write("{" + "\"message\":\"Employee not found\"" + "}");
            resp.setStatus(404);
            return;
        }

        Employee employee = objectMapper.readValue(req.getReader(), Employee.class);
        String message = employeeController.updateProfile(id, employee.getUsername(),
                employee.getPassword(), employee.getFullName(), employee.getEmail());

        if (message.contains("success")) {
            logger.info("Employee updated profile: " + id);
            resp.getWriter().write(objectMapper.writeValueAsString(employeeController.get(id)));
            resp.setStatus(200);
            return;
        }

        logger.warn("Employee update profile failed: " + id);
        resp.getWriter().write(
                "{" + "\"message\":\"Something went wrong, try again. Make sure your username and email are unique.\""
                        + "}");
        resp.setStatus(400);
    }
}
