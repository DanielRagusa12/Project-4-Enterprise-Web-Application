/* Name: Daniel Ragusa
Course: CNT 4714 – Spring 2023 – Project Four
Assignment title: A Three-Tier Distributed Web-Based Application
Date: April 23, 2023
*/











import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.text.html.StyleSheet;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.*;



public class AddJobsServlet extends HttpServlet
{

    private String message;
    private Connection connect;
    private PreparedStatement preparedStatement;
    private PreparedStatement bLStatement;
    private int mysqlReturnValue;
    private int updateReturnValue;


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        String jnum = request.getParameter("Jobs_param1");
        String jname = request.getParameter("Jobs_param2");
        String numWorkersString = request.getParameter("Jobs_param3");
        if (numWorkersString == null || numWorkersString == "") {
            numWorkersString = "0";
        }
        int numWorkersInt = Integer.parseInt(numWorkersString);
        String city = request.getParameter("Jobs_param4");
        message = null;

    


        try {

            Properties props = new Properties();
            FileInputStream in = null;
            MysqlDataSource dataSource = null;

            in = new FileInputStream("C:\\tomcat10\\webapps\\Project4\\WEB-INF\\lib\\data-entry.properties");
            props.load(in);

            dataSource = new MysqlDataSource();
            dataSource.setURL(props.getProperty("MYSQL_DB_URL"));
            dataSource.setUser(props.getProperty("MYSQL_DB_USERNAME"));
    	    dataSource.setPassword(props.getProperty("MYSQL_DB_PASSWORD"));


            connect = dataSource.getConnection();



            String sqlStatement = "INSERT INTO jobs VALUES (?, ?, ?, ?)";
            preparedStatement = connect.prepareStatement(sqlStatement);
            preparedStatement.setString(1, jnum);
            preparedStatement.setString(2, jname);
            preparedStatement.setInt(3, numWorkersInt);
            preparedStatement.setString(4, city);
            mysqlReturnValue = preparedStatement.executeUpdate();

            // message += "Rows affected: " + mysqlReturnValue + "\n";



            // message = "<table id=\"result\">";
            // message += "<tr>";
            // message += "<th>" + "Rows affected: " + mysqlReturnValue + "</th>";
            // message += "</tr>";
            // message += "</table>";
            String returnRecord = "New Jobs record: (" + jnum + ", " + jname + ", " + numWorkersInt + ", " + city + ") - successfully entered into the database.";


            message = "<div style=\"background-color:green\" id=\"result\">";
            message += "<h2>" + returnRecord + "</h2>";
            message += "</div>";


            










        }

        catch (SQLException e) {
            // message = "<table id=\"result\">";
            // message += "<tr>";
            // message += "<th>" + "Error: " + e + "</th>";
            // message += "</tr>";
            // message += "</table>";

            message = "<div style=\"background-color:red\" id=\"result\">";
            message += "<h2>" + "Error Executing the SQL statement:</h2>";
            
            message += "<div style=\"background-color:white\">";
            message += "<h2>" + e + "</h2>";

           
            message += "</div>";

        }


        System.out.println(message);


        HttpSession session = request.getSession();
        session.setAttribute("message", message);
        RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher("/dataEntryHome.jsp");
        dispatcher.forward(request, response);


        





    }








    
}
