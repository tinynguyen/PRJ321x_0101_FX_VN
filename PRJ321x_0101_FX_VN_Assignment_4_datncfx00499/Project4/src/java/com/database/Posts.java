/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.database;

import com.bean.Users;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author tiny
 */
public class Posts {
  
  private Connection conn;

  public Posts(Connection conn) {
    this.conn = conn;
  }
  
  public void post(Users user, String title, String desc, String category, String content) throws SQLException {
    int userID = 0;
    int categoryID = 0;
    
    String getUser = "select userID from Users where username = ?";
    PreparedStatement ps1 = conn.prepareStatement(getUser);
    
    ps1.setString(1, user.getUsername());
    ResultSet rs1 = ps1.executeQuery();
    if(rs1.next()) {
      userID = rs1.getInt("userID");
    }
  
    
    String getCategory = "select categoryID from Categories where title = ?";
    PreparedStatement ps2 = conn.prepareStatement(getCategory);
    ps2.setString(1, category);
    ResultSet rs2 = ps2.executeQuery();
    if(rs2.next()) {
      categoryID = rs2.getInt("categoryID");
    }
    
    String insert = "insert into Posts(title, description, content, category, user, dateCreate) values (?, ?, ?, ?, ?, now())";
    PreparedStatement ps3 = conn.prepareStatement(insert);
    
    ps3.setString(1, title);
    ps3.setString(2, desc);
    ps3.setString(3, content);
    ps3.setInt(4, categoryID);
    ps3.setInt(5, userID);
    
    ps3.executeUpdate();
    ps3.close();
  }
  
}
