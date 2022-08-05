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
import com.rakilahmed.controllers.user.ManagerController;
import com.rakilahmed.models.reimbursement.Reimbursement;
import com.rakilahmed.models.user.Employee;
import com.rakilahmed.models.user.Manager;
import com.rakilahmed.services.reimbursement.ReimbursementDAO;
import com.rakilahmed.services.user.EmployeeDAO;
import com.rakilahmed.services.user.ManagerDAO;

@WebServlet("/managers/*")
public class ManagerServlet extends HttpServlet {
    private ObjectMapper objectMapper;
    private ManagerController managerController;
    private EmployeeController employeeController;
    private ReimbursementController reimbursementController;
    private final Logger logger = LogManager.getLogger(ManagerServlet.class);

    /**
     * Private helper method to validate user's login status.
     * 
     * @param request  The request object.
     * @param response The response object.
     * @param id       The user's id.
     * @return True if user is logged in, false otherwise.
     */
    private boolean isLoggedIn(HttpServletRequest request, HttpServletResponse response, int id) {
        logger.info("Validating login status for manager: " + id);

        if (id <= 0) {
            logger.warn("Invalid manager id: " + id);
            response.setStatus(401);
            return true;
        }

        if (request.getSession().getAttribute("manager") == null
                || (int) request.getSession().getAttribute("manager") != id) {
            logger.warn("Manager is not logged in: " + id);
            response.setStatus(401);
            return true;
        }

        logger.info("Manager is logged in: " + id);
        return false;
    }

    /**
     * Private helper to write a JSON response if manager is not logged in.
     * 
     * @param response The response object.
     * @throws IOException If an error occurs while writing to the response.
     */
    private void managerNotLoggedInJSON(HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.getWriter().write(
                "You are not logged in, but you are trying to access an manager's profile.\n\n");
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
        managerController = new ManagerController(new ManagerDAO());
        employeeController = new EmployeeController(new EmployeeDAO());
        reimbursementController = new ReimbursementController(new ReimbursementDAO());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json");
        String[] parameters = request.getPathInfo().split("/");

        if (parameters.length == 2 && parameters[1].equals("login")) {
            logger.info("Manager is trying to login");
            response.getWriter().write("{"
                    + "\"message\":\"To login, make a POST request to this endpoint with your username and password in the body of the request.\""
                    + "}");
            response.setStatus(400);
        } else if (parameters.length == 2 && parameters[1].equals("new")) {
            logger.info("Manager is trying to create a new account");
            response.getWriter().write("{"
                    + "\"message\":\"To create a new manager, make a POST request to this endpoint with your username, password, full name, and email in the body of the request.\""
                    + "}");
            response.setStatus(400);
        } else if (parameters.length >= 2) {
            int id;

            if (parameters.length == 2) {
                try {
                    id = Integer.parseInt(parameters[1]);
                    logger.info("Manager is trying to go to dashboard: " + id);
                } catch (NumberFormatException e) {
                    logger.warn("Manager id must be an integer: " + parameters[1]);
                    response.getWriter().write("{" + "\"message\":\"Manager id must be an integer\"" + "}");
                    response.setStatus(400);
                    return;
                }

                if (isLoggedIn(request, response, id)) {
                    managerNotLoggedInJSON(response);
                    return;
                }

                if (managerController.get(id) != null) {
                    logger.info("Manager reached dashboard: " + id);
                    response.setContentType("text/plain");
                    response.getWriter().write("You are successfully logged in, your id is " + id + ".\n\n");
                    response.getWriter().write("Allowed endpoints & method you need to use:\n");
                    response.getWriter().write("- (GET) /" + id + "/profile   : to view your profile\n");
                    response.getWriter().write("- (PUT) /" + id + "/update    : to update your profile\n");
                    response.getWriter().write("- (GET) /" + id + "/employees : to view all employees\n");
                    response.getWriter().write("- (GET) /" + id + "/pending   : to view all pending requests\n");
                    response.getWriter().write("- (PUT) /" + id + "/resolve   : to resolve a pending request\n");
                    response.getWriter().write("- (GET) /" + id + "/resolved  : to view all resolved requests\n");
                    response.getWriter().write("- (GET) /" + id + "/logout    : to logout\n");
                    response.setStatus(200);
                    return;
                } else if (managerController.get(id) == null) {
                    logger.warn("Manager does not exist: " + id);
                    response.getWriter().write("{" + "\"message\":\"Manager not found\"" + "}");
                    response.setStatus(404);
                    return;
                }
            } else if (parameters.length == 3 && parameters[2].equals("profile")) {
                try {
                    id = Integer.parseInt(parameters[1]);
                    logger.info("Manager is trying to get profile info: " + id);
                } catch (NumberFormatException e) {
                    logger.warn("Manager id must be an integer: " + parameters[1]);
                    response.getWriter().write("{" + "\"message\":\"Manager id must be an integer\"" + "}");
                    response.setStatus(400);
                    return;
                }

                if (isLoggedIn(request, response, id)) {
                    managerNotLoggedInJSON(response);
                    return;
                }

                Manager manager = managerController.get(id);

                if (manager != null) {
                    logger.info("Manager reached profile: " + id);
                    response.getWriter().write(objectMapper.writeValueAsString(manager));
                    response.setStatus(200);
                    return;
                }

                logger.warn("Manager does not exist: " + id);
                response.getWriter().write("{" + "\"message\":\"Manager not found\"" + "}");
                response.setStatus(404);
                return;
            } else if (parameters.length == 3 && parameters[2].equals("employees")) {
                try {
                    id = Integer.parseInt(parameters[1]);
                    logger.info("Manager is trying to get all employees: " + id);
                } catch (NumberFormatException e) {
                    logger.warn("Manager id must be an integer: " + parameters[1]);
                    response.getWriter().write("{" + "\"message\":\"Manager id must be an integer\"" + "}");
                    response.setStatus(400);
                    return;
                }

                if (isLoggedIn(request, response, id)) {
                    managerNotLoggedInJSON(response);
                    return;
                }

                List<Employee> employees = employeeController.getAll();

                if (employees != null) {
                    logger.info("Manager found all employees: " + id);
                    response.getWriter()
                            .write("{" + "\"employees\": " + objectMapper.writeValueAsString(employees) + "}");
                    response.setStatus(200);
                    return;
                }

                logger.warn("No employees found: " + id);
                response.getWriter().write("{" + "\"message\":\"No employees found\"" + "}");
                response.setStatus(404);
                return;
            } else if (parameters.length == 4 && parameters[2].equals("employees")) {
                int employeeId;

                try {
                    id = Integer.parseInt(parameters[1]);
                    employeeId = Integer.parseInt(parameters[3]);
                    logger.info("Manager " + id + " is trying to get employee's pending reimbursements: " + employeeId);
                } catch (NumberFormatException e) {
                    logger.warn("Manager id and employee id must be integers: " + parameters[1] + " " + parameters[3]);
                    response.getWriter().write("{" + "\"message\":\"Manager id and employee id must be integers\""
                            + "}");
                    response.setStatus(400);
                    return;
                }

                if (isLoggedIn(request, response, id)) {
                    managerNotLoggedInJSON(response);
                    return;
                }

                if (employeeController.get(employeeId) == null) {
                    logger.warn("Employee does not exist: " + employeeId);
                    response.getWriter().write("{" + "\"message\":\"Employee not found\"" + "}");
                    response.setStatus(404);
                    return;
                }

                List<Reimbursement> pendingReimbursements = reimbursementController
                        .getAllPendingForEmployee(employeeId);

                if (pendingReimbursements != null) {
                    logger.info("Manager found all pending reimbursements for employee: " + employeeId);
                    response.getWriter()
                            .write("{" + "\"pending_reimbursements\": "
                                    + objectMapper.writeValueAsString(pendingReimbursements) + "}");
                    response.setStatus(200);
                    return;
                }

                logger.warn("No pending reimbursements found for employee: " + employeeId);
                response.getWriter().write(
                        "{" + "\"message\":\"No pending reimbursements of employee " + employeeId + "\"" + "}");
                response.setStatus(404);
                return;
            } else if (parameters.length == 3 && parameters[2].equals("pending")) {
                try {
                    id = Integer.parseInt(parameters[1]);
                    logger.info("Manager is trying to get all pending reimbursements: " + id);
                } catch (NumberFormatException e) {
                    logger.warn("Manager id must be an integer: " + parameters[1]);
                    response.getWriter().write("{" + "\"message\":\"Manager id must be an integer\"" + "}");
                    response.setStatus(400);
                    return;
                }

                if (isLoggedIn(request, response, id)) {
                    managerNotLoggedInJSON(response);
                    return;
                }

                List<Reimbursement> pendingReimbursements = reimbursementController.getAllPending();

                if (pendingReimbursements != null) {
                    logger.info("Manager found all pending reimbursements: " + id);
                    response.getWriter()
                            .write("{" + "\"pending_reimbursements\": "
                                    + objectMapper.writeValueAsString(pendingReimbursements) + "}");
                    response.setStatus(200);
                    return;
                }

                logger.warn("No pending reimbursements found: " + id);
                response.getWriter().write("{" + "\"message\":\"No pending reimbursements found\"" + "}");
                response.setStatus(404);
                return;
            } else if (parameters.length == 3 && parameters[2].equals("resolved")) {
                try {
                    id = Integer.parseInt(parameters[1]);
                    logger.info("Manager is trying to get all resolved reimbursements: " + id);
                } catch (NumberFormatException e) {
                    logger.warn("Manager id must be an integer: " + parameters[1]);
                    response.getWriter().write("{" + "\"message\":\"Manager id must be an integer\"" + "}");
                    response.setStatus(400);
                    return;
                }

                if (isLoggedIn(request, response, id)) {
                    managerNotLoggedInJSON(response);
                    return;
                }

                List<Reimbursement> resolvedReimbursements = reimbursementController.getAllResolved();

                if (resolvedReimbursements != null) {
                    logger.info("Manager found all resolved reimbursements: " + id);
                    response.getWriter()
                            .write("{" + "\"resolved_reimbursements\": "
                                    + objectMapper.writeValueAsString(resolvedReimbursements) + "}");
                    response.setStatus(200);
                    return;
                }

                logger.warn("No resolved reimbursements found: " + id);
                response.getWriter().write("{" + "\"message\":\"No resolved reimbursements found\"" + "}");
                response.setStatus(404);
                return;
            } else if (parameters.length == 3 && parameters[2].equals("logout")) {
                try {
                    id = Integer.parseInt(parameters[1]);
                    logger.info("Manager is trying to logout: " + id);
                } catch (NumberFormatException e) {
                    logger.warn("Manager id must be an integer: " + parameters[1]);
                    response.getWriter().write("{" + "\"message\":\"Manager id must be an integer\"" + "}");
                    response.setStatus(400);
                    return;
                }

                if (isLoggedIn(request, response, id)) {
                    response.getWriter().write("{" + "\"message\":\"You are not logged in\"" + "}");
                    response.setStatus(401);
                    return;
                }

                logger.info("Manager logged out: " + id);
                request.getSession().invalidate();
                response.getWriter().write("{" + "\"message\":\"You have been logged out\"" + "}");
                response.setStatus(200);
                return;
            }

            logger.warn("Invalid endpoint: " + request.getRequestURI());
            response.getWriter().write("{" + "\"message\":\"Endpoint not found\"" + "}");
            response.setStatus(400);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("application/json");
        String[] parameters = req.getPathInfo().split("/");

        if (parameters.length == 2 && !parameters[1].equals("new") && !parameters[1].equals("login")) {
            logger.warn("Invalid request: " + req.getRequestURI());
            resp.getWriter().write("{" + "\"message\":\"Endpoint not found\"" + "}");
            resp.setStatus(400);
            return;
        }

        if (parameters.length == 2 && parameters[1].equals("new")) {
            logger.info("User is trying to create a new manager account");
            Manager manager = objectMapper.readValue(req.getReader(), Manager.class);
            int id = managerController.register(manager);

            if (id > 0) {
                logger.info("Manager registered: " + id);
                resp.getWriter().write(objectMapper.writeValueAsString(managerController.get(id)));
                resp.setStatus(201);
                return;
            }

            logger.warn("Manager creation failed");
            resp.getWriter().write("{" + "\"message\":\"Manager registration failed\"" + "}");
            resp.setStatus(404);
            return;
        } else if (parameters.length == 2) {
            logger.info("User is trying to login");

            Manager manager = objectMapper.readValue(req.getReader(), Manager.class);
            int id = managerController.login(manager.getUsername(), manager.getPassword());

            if (id > 0) {
                logger.info("Manager logged in: " + id);

                if (isLoggedIn(req, resp, id)) {
                    req.getSession().setAttribute("manager", id);
                    req.getSession().setMaxInactiveInterval(15 * 60);
                }

                String path = req.getContextPath() + "/managers/" + id;
                logger.info("Redirecting to " + path);
                resp.sendRedirect(path);
                return;
            }

            logger.warn("Manager login failed");
            resp.getWriter().write("{" + "\"message\":\"Manager login failed\"" + "}");
            resp.setStatus(404);
            return;
        }

        logger.warn("Invalid request: " + req.getRequestURI());
        resp.getWriter().write("{" + "\"message\":\"Endpoint not found\"" + "}");
        resp.setStatus(400);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("application/json");
        String idParameter = req.getRequestURI().split("/")[2];
        String operationParameter = req.getRequestURI().split("/")[3];

        int id;

        try {
            id = Integer.parseInt(idParameter);
            logger.info("Manager is trying to update profile or resolve a request: " + id);
        } catch (NumberFormatException e) {
            logger.warn("Manager id must be an integer: " + idParameter);
            resp.getWriter().write("{" + "\"message\":\"Manager id must be an integer\"" + "}");
            resp.setStatus(400);
            return;
        }

        if (isLoggedIn(req, resp, id)) {
            managerNotLoggedInJSON(resp);
            return;
        }

        if (!operationParameter.equals("update") && !operationParameter.equals("resolve")) {
            logger.warn("Invalid request: " + req.getRequestURI());
            resp.getWriter().write("{" + "\"message\":\"Endpoint not found\"" + "}");
            resp.setStatus(400);
            return;
        }

        if (operationParameter.equals("update")) {
            logger.info("Manager is trying to update profile: " + id);

            Manager manager = objectMapper.readValue(req.getReader(), Manager.class);
            String message = managerController.updateProfile(id, manager.getUsername(), manager.getPassword(),
                    manager.getFullName(),
                    manager.getEmail());

            if (message.contains("success")) {
                logger.info("Manager profile updated: " + id);
                resp.getWriter().write(objectMapper.writeValueAsString(managerController.get(id)));
                resp.setStatus(200);
                return;
            }

            logger.warn("Manager profile update failed: " + id);
            resp.getWriter().write(
                    "{" + "\"message\":\"Something went wrong, try again. Make sure your username and email are unique.\""
                            + "}");
            resp.setStatus(400);
            return;
        }

        Reimbursement reimbursement = objectMapper.readValue(req.getReader(), Reimbursement.class);
        logger.info("Manager is trying to resolve a request: " + reimbursement.getId());
        String message = reimbursementController.update(reimbursement.getId(), id, reimbursement.getStatus());

        if (message.contains("success")) {
            logger.info("Manager resolved a request: " + reimbursement.getId());
            resp.getWriter()
                    .write(objectMapper.writeValueAsString(reimbursementController.get(reimbursement.getId())));
            resp.setStatus(200);
            return;
        }

        logger.warn("Manager request resolution failed: " + reimbursement.getId());
        resp.getWriter().write(
                "{" + "\"message\":\"Something went wrong, try again. Make sure the reimbursement with the provided id actually exists.\""
                        + "}");
        resp.setStatus(400);
    }
}
