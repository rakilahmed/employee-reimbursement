package com.rakilahmed.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rakilahmed.controllers.reimbursement.ReimbursementController;
import com.rakilahmed.models.reimbursement.Reimbursement;
import com.rakilahmed.services.reimbursement.ReimbursementDAO;

@WebServlet("/reimbursements")
public class ReimbursementsServlet extends HttpServlet {
    private ObjectMapper objectMapper;
    private ReimbursementController reimbursementController;

    @Override
    public void init() {
        objectMapper = new ObjectMapper();
        reimbursementController = new ReimbursementController(new ReimbursementDAO());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        List<Reimbursement> reimbursements = reimbursementController.getAll();

        if (reimbursements != null) {
            resp.getWriter().write(objectMapper.writeValueAsString(reimbursements));
            resp.setStatus(200);
        } else {
            resp.getWriter().write("{" + "\"message\":\"No reimbursements found\"" + "}");
            resp.setStatus(404);
        }
    }
}
