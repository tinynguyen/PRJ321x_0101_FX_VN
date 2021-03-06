/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.bean.Post;
import com.bean.Users;
import com.context.DBContext;
import com.database.Account;
import com.database.DBPosts;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
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
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String action = request.getParameter("action");
    HttpSession session = request.getSession();
    if (action.equals("dologout")) {
      session.invalidate();
      response.sendRedirect("blogs");
    } else if (action.equals("getposts")) {
      Connection conn = null;
      // Connect to database
      try {
        conn = new DBContext().getConnection();
      } catch (Exception ex) {
        response.getWriter().println(ex.toString());
        return;
      }
      
      DBPosts post = new DBPosts(conn);
      try {
        List<Post> posts = post.showAllPosts();
        ServletContext context = this.getServletContext();
        context.setAttribute("posts", posts);
        request.getRequestDispatcher("blogs").forward(request, response);
      } catch (SQLException ex) {
        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
      }
    }

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {

    String action = request.getParameter("action");

    if (action == null) {
      request.getRequestDispatcher("index").forward(request, response);
    }

    HttpSession session = request.getSession();

    Connection conn = null;
    // Connect to database
    try {
      conn = new DBContext().getConnection();
    } catch (Exception ex) {
      response.getWriter().println(ex.toString());
      return;
    }
    Account account = new Account(conn);

    String username = request.getParameter("username");
    String password = request.getParameter("password");

    /**
     * Login Process
     *
     * Validate, query in database and login
     */
    if (action.equals("dologin")) {
      Users user = new Users(username, password);

      try {
        // Query user in database        
        if (account.login(username, password)) {
          int countPost = new DBPosts(conn).countPost(username);
          session.setAttribute("user", user);
          session.setAttribute("countPost", countPost);
          request.getRequestDispatcher("blogs").forward(request, response);
        } else {
          request.setAttribute("error", "Username or password is incorrect");
          request.getRequestDispatcher("login").forward(request, response);
        }
      } catch (SQLException ex) {
        response.getWriter().println("Error query data! Please check again!");
      }
    } /**
     * Sign Up Process
     *
     * Validate, create new user and insert into database
     */
    else if (action.equals("dosignup")) {
      Users user = new Users(username, password);
      // Validate user info from sign up form        
      if (!user.validate(username, password)) {
        request.setAttribute("user", user);
        request.getRequestDispatcher("signup").forward(request, response);
      } else {
        try {
          // Check user is exist or not?        
          if (account.exist(username)) {
            user.setError("Username is exist");
            request.setAttribute("user", user);
            request.getRequestDispatcher("signup").forward(request, response);
          } else {
            account.create(username, password);
            session.invalidate();
            request.getRequestDispatcher("login").forward(request, response);
          }
        } catch (SQLException ex) {
          Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
      }

    } /**
     * Create Post Process
     *
     * Create new post and insert into database
     */
    else if (action.equals("dopost")) {
      Users user = (Users) session.getAttribute("user");
      String title = request.getParameter("title");
      String desc = request.getParameter("description");
      Post p = new Post(title, desc);
      if (!p.validate(title, desc)) {
        request.setAttribute("error", p.getError());
        request.getRequestDispatcher("createpost").forward(request, response);
      } else {
        String category = request.getParameter("category");
        String content = request.getParameter("content");

        DBPosts post = new DBPosts(conn);
        try {
          post.createPost(user, title, desc, category, content);
          response.sendRedirect("blogs");
        } catch (SQLException ex) {
          Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    } /**
     * Edit Post Process
     *
     * Edit a post and update database
     */
    else if (action.equals("editpost")) {
      Users user = (Users) session.getAttribute("user");
      int postID = Integer.parseInt(request.getParameter("postID"));
      String title = request.getParameter("title");
      String desc = request.getParameter("description");
      String category = request.getParameter("category");
      String content = request.getParameter("content");

      DBPosts post = new DBPosts(conn);
      try {
        post.editPost(user, postID, title, desc, category, content);
        response.sendRedirect("blogs");
      } catch (SQLException ex) {
        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
      }
    } /**
     * Search Post Process
     *
     * Search post in database
     */
    else if (action.equals("search")) {
      String search = request.getParameter("search");
      if (search.trim().equals("") || search == null) {
        request.getRequestDispatcher("home").forward(request, response);
      } else {
        DBPosts post = new DBPosts(conn);
        try {
          List<Post> posts = post.searchPost(search);
          request.setAttribute("post", posts);
          request.getRequestDispatcher("search").forward(request, response);
        } catch (SQLException ex) {
          Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }

      }
    }

    try {
      conn.close();
    } catch (SQLException ex) {
      response.getWriter().println(ex.toString());
      return;
    }

  }

}
