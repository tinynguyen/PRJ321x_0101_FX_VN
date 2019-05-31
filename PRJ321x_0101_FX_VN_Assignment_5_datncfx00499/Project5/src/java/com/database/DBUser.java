/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.database;

import com.bean.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author tiny
 */
public class DBUser {

  private Connection conn;

  public DBUser(Connection conn) {
    this.conn = conn;
  }

  public boolean login(String username, String password) throws SQLException {

    int count = 0;
    String query = "SELECT count(*) FROM Users WHERE username = ?";
    PreparedStatement ps = conn.prepareStatement(query);
    ps.setString(1, username);
    ResultSet rs = ps.executeQuery();

    if (rs.next()) {
      count = rs.getInt("count(*)");
    }

    if (count == 0) {
      return false;
    } else {
      return true;
    }

  }
  
  public boolean isExists(String username) throws SQLException {

    int count = 0;
    String query = "SELECT count(*) FROM Users WHERE username = ?";
    PreparedStatement ps = conn.prepareStatement(query);
    ps.setString(1, username);
    ResultSet rs = ps.executeQuery();

    if (rs.next()) {
      count = 1;
    }

    if (count == 1) {
      return true;
    } else {
      return false;
    }

  }
  
  public void createUser(User user) throws SQLException {
    String insert = "INSERT INTO Users (email, username, password) VALUES (?, ?, ?)";
    PreparedStatement ps = conn.prepareStatement(insert);
    ps.setString(1, user.getEmail());
    ps.setString(2, user.getUsername());
    ps.setString(3, user.getPassword());
    ps.executeUpdate();
    ps.close();
  }

  public int getUserID(String username) throws SQLException {

    int userId = 0;
    String query = "SELECT userId FROM Users WHERE username = ?";
    PreparedStatement ps = conn.prepareStatement(query);
    ps.setString(1, username);
    ResultSet rs = ps.executeQuery();

    if (rs.next()) {
      userId = rs.getInt("userId");
    }

    return userId;
  }

}
