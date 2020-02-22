package info.rohanmorris;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * @author rohan
 */
public class Mysql_Connection {
    // init database constants
    private static final boolean DEV = false;//True for local, False for Live server
    private static final String DATABASE_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/bank";
    private static final String MAX_POOL = "250";

    // init connection object
    private Connection connection;
    // init properties object
    private Properties properties;

    // create properties
    private Properties getProperties() {
        if (properties == null) {
            String USERNAME = (DEV)? "root" : "web_db_user";
            String PASSWORD = (DEV)? "" : "Qe854rI98t9Ac06x";
            properties = new Properties();
            properties.setProperty("user", USERNAME);
            properties.setProperty("password", PASSWORD);
            properties.setProperty("MaxPooledStatements", MAX_POOL);
        }
        return properties;
    }

    // connect database
    public Connection connect() {
        if (connection == null) {
            try {
                Class.forName(DATABASE_DRIVER);
                connection = DriverManager.getConnection(DATABASE_URL, getProperties());
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
    
    //Exec Qyuery
    public ResultSet ExecQuery(String sql){
        ResultSet rs = null;
        try {
            Statement state = connect().createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            rs = state.executeQuery(sql);
            rs.beforeFirst();
        } catch (SQLException ex) {
            Logger.getLogger(Mysql_Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    // disconnect database
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
