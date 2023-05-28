package server.database;

import server.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class DbHandler extends Configs {
    Connection dbConnection;

    public Connection getConnection(){
        String connectionString="jdbc:mysql://" +dbHost+":" +dbPort+"/"+dbName;
        try {
            //Class.forName("com.mysql.jdbc.Driver");
            dbConnection=DriverManager.getConnection(connectionString, dbUser, dbPass);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dbConnection;
    }

    public void WriteUser(User user){
        String sqlInsert="INSERT INTO "+Consts.USER_TABLE +"("+ Consts.USER_NAME+","+Consts.USER_PASSWORD+")"+"VALUES(?,?)";
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(sqlInsert);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public ResultSet checkUnique(User user){
        ResultSet rs= null;
        String sqlSelect = "SELECT * FROM "+ Consts.USER_TABLE +" WHERE "+Consts.USER_NAME+" = ?";
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(sqlSelect);
            preparedStatement.setString(1, user.getName());
            rs = preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rs;
    }
    public ResultSet getUser(User user){
        ResultSet rs= null;
        String sqlSelect = "SELECT * FROM "+ Consts.USER_TABLE+" WHERE "+Consts.USER_NAME+" = ? AND "+Consts.USER_PASSWORD+" =?";
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(sqlSelect);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());
            rs = preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rs;
    }
}
