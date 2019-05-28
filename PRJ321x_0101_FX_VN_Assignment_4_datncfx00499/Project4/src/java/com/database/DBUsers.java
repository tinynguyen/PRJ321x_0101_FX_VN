/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author tiny
 */
public class DBUsers {

  private Connection conn;

  public DBUsers(Connection conn) {
    this.conn = conn;
  }

  public String queryUser(int userID) throws SQLException {
    String username = "";
    String sql = "select username from Users where userID = ?";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setInt(1, userID);

    ResultSet rs = ps.executeQuery();

    if (rs.next()) {
      username = rs.getString("username");
    }

    return username;
  }

}
