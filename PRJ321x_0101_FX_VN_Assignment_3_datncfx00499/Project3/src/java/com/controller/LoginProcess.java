/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.model.ValidateUser;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author tiny
 */
public class LoginProcess extends HttpServlet {

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    
    String username = request.getParameter("username");
    String password = request.getParameter("password");

    List<String> errors = ValidateUser.ValidateUserLogin(username, password);

    if (errors.size() != 0) {
      request.setAttribute("errors", errors);
      request.getRequestDispatcher("login.jsp").forward(request, response);
    }
    
    response.getWriter().print("<h2>OK</h2>");
  }

  @Override
  public String getServletInfo() {
    return "Process user login to system";
  }// </editor-fold>

}
