/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.bean.MailMessage;
import com.bean.User;
import com.business.MyMail;
import com.database.DBContext;
import com.database.DBMail;
import com.database.DBUser;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author tiny
 */
public class Controller extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {

    HttpSession session = request.getSession();

    String action = request.getParameter("action");
    if (action.equals("") || action == null) {
      response.sendRedirect("home");
    } else if (action.equals("logout")) {
      session.invalidate();
      response.sendRedirect("home");
    } else if(action.equals("getEmail")) {
      session.setAttribute("allEmail", new DBMail().getSQL());
      request.getRequestDispatcher("sent").forward(request, response);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {

    String action = request.getParameter("action");

    if (action.equals("") || action == null) {
      response.sendRedirect("home");
    }

    HttpSession session = request.getSession();
    Connection conn = null;

    try {
      conn = new DBContext().getConnection();
    } catch (Exception ex) {
      Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
      return;
    }
    DBUser dbUser = new DBUser(conn);

    String email = request.getParameter("email");
    String username = request.getParameter("username");
    String password = request.getParameter("password");

    /**
     * Login Process
     *
     * Validate, query in database and login
     */
    if (action.equals("signin")) {
      User user = new User(email, username, password);

      try {
        if (dbUser.isExistUsername(username)) {
          if (dbUser.checkPassword(username, password)) {
            int userId = dbUser.getUserID(username);
            session.setAttribute("userId", userId);
            session.setAttribute("user", user);
            request.getRequestDispatcher("home").forward(request, response);
          } else {
            request.setAttribute("invalidPassword", "Password is incorrect");
            request.getRequestDispatcher("sign-in").forward(request, response);
          }
        } else {
          request.setAttribute("invalidUsername", "Username is not exists");
          request.getRequestDispatcher("sign-in").forward(request, response);
        }
      } catch (SQLException ex) {
        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        response.sendRedirect("error");
      }
    } /**
     * Sign Up Process
     *
     * Validate, create new user and insert into database
     */
    else if (action.equals("signup")) {
      User user = new User(email, username, password);

      try {
        if (!user.validate(user)) {
          List<String> errors = user.getErrors();
          for (String e : errors) {
            if (e.equals("Invalid email!")) {
              request.setAttribute("invalidEmail", e);
            } else if (e.equals("Invalid username!")) {
              request.setAttribute("invalidUsername", e);
            } else {
              request.setAttribute("invalidPassword", e);
            }
          }
          request.getRequestDispatcher("sign-up").forward(request, response);
        } else {
          if (dbUser.isExistUsername(username)) {
            request.setAttribute("errors", "Email or Username is exists");
            request.getRequestDispatcher("sign-up").forward(request, response);
          } else {
            dbUser.createUser(user);
            session.invalidate();
            request.getRequestDispatcher("sign-in").forward(request, response);
          }
        }
      } catch (SQLException ex) {
        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        response.sendRedirect("error");
      }
    }/**
     * Compose Process
     *
     * Create new email and insert into database
     */
    else if (action.equals("sendEmail")) {
      User user = (User) session.getAttribute("user");
      DBMail dbMail = new DBMail(conn);
      MyMail mMail = new MyMail();
      boolean sent = false;

      String toAddress = request.getParameter("toAddress");
      String ccAddress = request.getParameter("ccAddress");
      String subject = request.getParameter("subject");
      String content = request.getParameter("content");

      MailMessage mm = new MailMessage(content, subject, toAddress, ccAddress);

      try {
        if (!mm.validate(mm)) {
          List<String> errors = mm.getErrors();
          for (String e : errors) {
            if (e.equals("Invalid email!")) {
              request.setAttribute("invalidEmail", e);
            } else if (e.equals("Subject is required!")) {
              request.setAttribute("invalidSubject", e);
            }
          }
          request.getRequestDispatcher("compose").forward(request, response);
        } else {
          int userId = dbUser.getUserID(user.getUsername());
          sent = mMail.sendMail(mm, mMail.getMailSession());
          if (sent) {
            dbMail.createMail(userId, toAddress, ccAddress, subject, content);
            request.setAttribute("sendSuccess", "Email sent success, thank you!");
            request.getRequestDispatcher("compose").forward(request, response);
          } else {
            request.setAttribute("sendFail", "Email sent fail, please try again!");
            request.getRequestDispatcher("compose").forward(request, response);
          }
        }
      } catch (SQLException ex) {
        response.sendRedirect("error");
        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
      } catch (Exception ex) {
        request.setAttribute("sendFail", "Email sent fail, please try again!");
        request.getRequestDispatcher("compose").forward(request, response);
        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
      }

    }

    try {
      conn.close();
    } catch (SQLException ex) {
      response.sendRedirect("error");
      Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
      return;
    }

  }

}
