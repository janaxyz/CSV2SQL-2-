package org.example.database;


import com.mysql.cj.x.protobuf.MysqlxDatatypes;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class MySQLConnection {

    private String host = "127.0.0.1";
    private String username = "root";
    private String password = "";
    private int port = 3306;
    private String dbname = "csv2sql";
    private Connection connection = null;


    private String tableName = "cities";
    private String columnID = "ID";
    private String  columnCountry = "country";
    private String columnCity = "city";
    private String columnFunction = "function";


    public String getTableName() {
        return tableName;
    }

    public String getColumnID() {
        return columnID;
    }

    public String getColumnCountry() {
        return columnCountry;
    }

    public String getColumnCity() {
        return columnCity;
    }

    public String getColumnFunction() {
        return columnFunction;
    }



    public MySQLConnection(Properties dbProperties) {
        if (dbProperties != null) {
            System.out.println("INFO: reading database config...");

            host = dbProperties.getProperty("db.host");
            username = dbProperties.getProperty("db.username");
            password = dbProperties.getProperty("db.password");
            port = Integer.valueOf(dbProperties.getProperty("db.port"));
            dbname = dbProperties.getProperty("db.dbname");
        }
    }

    public Connection getConnectionIfDBExistsNOT() {
        if (this.connection == null) {

            String url = "jdbc:mysql://" + host + ":" + port + "/";
            try {
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException ex) {
                System.out.println("ERROR: cannot connect to the database. " + ex.getMessage());
            }
        }

        return connection;
    }
    public Connection getConnection() {
        if (this.connection == null) {

            String url = "jdbc:mysql://" + host + ":" + port + "/" + dbname;
            try {
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException ex) {
                System.out.println("ERROR: cannot connect to the database. " + ex.getMessage());
            }
        }

        return connection;
    }


    //TODO: wenn Tabelle schon existiert diese löschen, und grundsätzlich neue Tabelle erstellen
    //TODO: besser, wenn auch nach Datenbank geschaut wird und wenn die nicht existiert, neu angelegt wird

    public void createDB(){
        try {
            Statement statement = getConnectionIfDBExistsNOT().createStatement();

            String createDatabase = "CREATE DATABASE IF NOT EXISTS " + dbname;
            statement.executeUpdate(createDatabase);

        }catch (SQLException ex) {
            System.out.println("ERROR: Cannot create database. " + ex.getMessage());
        }
    }


    //TODO: IST ES IN ORDNUNG DROP TABLE UND CREATE TABLE IN EINS ZU NEHMEN ODER DOCH GETRENNT?


    public void dropTableIfExists(){
        try {
            Statement statement = getConnection().createStatement();

            String dropTable = "DROP TABLE IF EXISTS " + tableName;

        }catch (SQLException ex) {
            System.out.println("ERROR: Cannot delete table. " + ex.getMessage());
        }

    }


    public void createTable() {

        try {
            Statement statement = getConnection().createStatement();

            String createTable = "CREATE TABLE " + tableName + " ("
                    + columnID + "INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                    + columnCountry + " VARCHAR(255), NOT NULL,"
                    + columnCity + " VARCHAR(255), NOT NULL,"
                    + columnFunction + " VARCHAR(255), NOT NULL,";

            statement.executeUpdate(createTable);
            System.out.println("Tabelle wurde erstellt.");

        } catch (SQLException ex) {
            System.out.println("ERROR: Cannot create table. " + ex.getMessage());
        }
    }




    public boolean addCity(String country, String city, String function) {
        try {
            String sql = "INSERT INTO cities"
                    + "(country, city, function)"
                    + "VALUES(?, ?, ?)";

            PreparedStatement ps = this.getConnection().prepareStatement(sql);
            ps.setString(1, country);
            ps.setString(2, city);
            ps.setString(3, function);


            int result = ps.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("ERROR: cannot execute statement. " + ex.getMessage());
            return false;
        }

        return true;
    }


}