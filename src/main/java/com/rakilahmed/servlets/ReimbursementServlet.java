package com.rakilahmed.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rakilahmed.controllers.reimbursement.ReimbursementController;
import com.rakilahmed.models.reimbursement.Reimbursement;
import com.rakilahmed.services.reimbursement.ReimbursementDAO;

@WebServlet("/reimbursements/*")
public class ReimbursementServlet extends HttpServlet {
    private ObjectMapper objectMapper;
    private ReimbursementController reimbursementController;

    @Override
    public void init() throws ServletException {
        objectMapper = new ObjectMapper();
        reimbursementController = new ReimbursementController(new ReimbursementDAO());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String parameter = req.getRequestURI().split("/")[2];

        try {
            int id = Integer.parseInt(parameter);

            if (reimbursementController.get(id) != null) {
                resp.getWriter().write(objectMapper.writeValueAsString(reimbursementController.get(id)));
                resp.setStatus(200);
            } else {
                resp.getWriter().write("{" + "\"message\":\"No reimbursement found\"" + "}");
                resp.setStatus(404);
            }
        } catch (NumberFormatException e) {
            resp.getWriter().write(
                    "{" + "\"message\":\"Invalid request, to get a reimbursement, use /reimbursements/{id} endpoint\""
                            + "}");
            resp.setStatus(400);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String parameter = req.getRequestURI().split("/")[2];

        try {
            if (!parameter.equals("new")) {
                resp.getWriter().write(
                        "{" + "\"message\":\"Invalid request, to create a new reimbursement, use the /reimbursements/new endpoint\""
                                + "}");
                resp.setStatus(400);
                return;
            }
            Reimbursement reimbursement = objectMapper.readValue(req.getReader(), Reimbursement.class);
            String message = reimbursementController.create(reimbursement);

            if (message.contains("success")) {
                resp.getWriter().write(objectMapper.writeValueAsString(reimbursement));
                resp.setStatus(201);
            } else {
                resp.getWriter().write("{" + "\"message\":\"" + message + "\"" + "}");
                resp.setStatus(404);
            }
        } catch (NumberFormatException e) {
            resp.getWriter().write(
                    "{" + "\"message\":\"Invalid request, to create a new reimbursement, use the /reimbursements/new endpoint\""
                            + "}");
            resp.setStatus(400);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String idParameter = req.getRequestURI().split("/")[2];
        String operationParameter = req.getRequestURI().split("/")[3];

        try {
            int id = Integer.parseInt(idParameter);

            if (!operationParameter.equals("update")) {
                resp.getWriter().write(
                        "{" + "\"message\":\"Invalid request, to update a reimbursement, use the /reimbursements/{id}/update endpoint\""
                                + "}");
                resp.setStatus(400);
                return;
            }

            if (reimbursementController.get(id) == null) {
                resp.getWriter().write("{" + "\"message\":\"No reimbursement found\"" + "}");
                resp.setStatus(404);
                return;
            }

            Reimbursement reimbursement = objectMapper.readValue(req.getReader(), Reimbursement.class);
            String message = reimbursementController.update(id, reimbursement.getManagerId(),
                    reimbursement.getStatus());

            if (message.contains("success")) {
                resp.getWriter().write(objectMapper.writeValueAsString(reimbursementController.get(id)));
                resp.setStatus(200);
            } else {
                resp.getWriter().write("{" + "\"message\":\"" + message + "\"" + "}");
                resp.setStatus(404);
            }
        } catch (NumberFormatException e) {
            resp.getWriter().write(
                    "{" + "\"message\":\"Invalid request, to update a reimbursement, use the /reimbursements/{id}/update endpoint\""
                            + "}");
            resp.setStatus(400);
        }
    }
}
