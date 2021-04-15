package tools;

import java.sql.Connection;
import java.sql.DriverManager;

public class MyConnection {
    String url = "jdbc:mysql://localhost:3306/knowme";
    String user = "root";
    String pwd = "";
    public static MyConnection instance;

    Connection cnx;

    private MyConnection() {

        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
            cnx = DriverManager.getConnection(url, user, pwd);
            System.out.println("Connected!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public Connection getCnx() {
        return cnx;
    }

    public static MyConnection getInstance() {
        if(instance == null){
            instance = new MyConnection();
        }
        return instance;
    }
}
