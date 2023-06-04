package server.database;

import server.User;
import server.Message;

import java.sql.*;
import java.util.List;

public class DbHandler extends Configs {
    Connection dbConnection;

    public Connection getConnection(){
        String connectionString="jdbc:mysql://" +dbHost+":" +dbPort+"/"+dbName+"?useUnicode=true&characterEncoding=UTF-8";
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

    public void WriteMessage(Message message){
        String sqlInsert="INSERT INTO "+Consts.MESSAGE_TABLE +"("+ Consts.SENDER+","+ Consts.RECIPIENT+","+Consts.TIMESTAMP+","+Consts.TEXT+")"+"VALUES(?,?,?,?)";
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(sqlInsert);
            preparedStatement.setString(1, message.getSender());
            preparedStatement.setString(2, message.getRecipient());
            preparedStatement.setTimestamp(3, message.getTimeMark());
            preparedStatement.setString(4, message.getText());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public ResultSet getChat(List<String> names){
        ResultSet rs= null;
        String sqlSelect = "SELECT * FROM "+ Consts.MESSAGE_TABLE+" WHERE "+Consts.SENDER+" = ? AND "+Consts.RECIPIENT+" =? OR "+Consts.SENDER+" =? AND "+Consts.RECIPIENT+" =? ORDER BY "+ Consts.TIMESTAMP;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(sqlSelect);
            preparedStatement.setString(1, names.get(0));
            preparedStatement.setString(2, names.get(1));
            preparedStatement.setString(3, names.get(1));
            preparedStatement.setString(4, names.get(0));
            rs = preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rs;
    }
    public ResultSet getNewChat(List<String> names, Timestamp timemark){
        ResultSet rs= null;
        String sqlSelect = "SELECT * FROM "+ Consts.MESSAGE_TABLE+" WHERE ("+Consts.SENDER+" = ? AND "+Consts.RECIPIENT+" =? OR "+ Consts.SENDER+" =? AND "+Consts.RECIPIENT+" =?) AND "+Consts.TIMESTAMP +" >?"+ " ORDER BY "+ Consts.TIMESTAMP;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(sqlSelect);
            preparedStatement.setString(1, names.get(0));
            preparedStatement.setString(2, names.get(1));
            preparedStatement.setString(3, names.get(1));
            preparedStatement.setString(4, names.get(0));
            preparedStatement.setTimestamp(5, timemark);
            rs = preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rs;
    }
    public ResultSet getRegisteredUsers(){
        ResultSet rs= null;
        String sqlSelect = "SELECT "+Consts.USER_NAME +" FROM "+ Consts.USER_TABLE;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(sqlSelect);
            rs = preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rs;
    }
    public ResultSet getUserChat(String userName){
        ResultSet rs= null;
        String sqlSelect = "SELECT "+Consts.SENDER +", " + Consts.TEXT + " FROM "+ Consts.MESSAGE_TABLE+" WHERE ( "+Consts.SENDER+ "=?" +" OR "+ Consts.RECIPIENT+" =? ) ORDER BY " +Consts.TIMESTAMP;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(sqlSelect);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, userName);
            rs = preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rs;
    }
}
