/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.rohanmorris;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * REST Web Service
 *
 * @author rohan
 */
@Path("account")
public class AccountResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of AccountResource
     */
    public AccountResource() {
    }

    /**
     * Account login
     * @param user
     * @param pass
     * @return an instance of String
     */
    @GET
    @Path("/{user}/{pass}")
    @Produces("application/json")
    public String getAccountUser(@PathParam("user")String user, @PathParam("pass")String pass) {
        Mysql_Connection mysql = new Mysql_Connection();  
        Connection con = mysql.connect();
        JSONObject obj = new JSONObject();
        ResultSet rs = null;
        try {
            PreparedStatement pStat = con.prepareStatement("{CALL sp_login(?,?)}");
            pStat.setString(1, user);
            pStat.setString(2, pass);
            rs = pStat.executeQuery();
            if(rs.next()){
                obj.put("id", rs.getInt("id"));
                obj.put("fname", rs.getString("fname"));
                obj.put("lname", rs.getString("lname"));
                obj.put("photo", rs.getString("photo"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(AccountResource.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
        }finally{
            mysql.disconnect();
            if(rs!=null){
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(FxRatesResource.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return obj.toJSONString();
    }
    
    /**
     * Get user accounts
     * @param id
     * @return 
     */
    @GET
    @Path("/{id}")
    @Produces("application/json")
    public String getAccount(@PathParam("id")int id) {
        Mysql_Connection mysql = new Mysql_Connection();  
        Connection con = mysql.connect();
        JSONArray obj = new JSONArray();
        ResultSet rs = null;
        try {
            PreparedStatement pStat = con.prepareStatement("{CALL sp_get_accounts(?)}");
            pStat.setInt(1, id);
            rs = pStat.executeQuery();
            while(rs.next()){
                Map m = new HashMap();
                m.put("account",rs.getString("account"));
                m.put("name",   rs.getString("name"));
                m.put("bal",    rs.getString("bal"));
                m.put("avl_bal",rs.getString("avl_bal"));
                obj.add(m);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AccountResource.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
        }finally{
            mysql.disconnect();
            if(rs!=null){
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(FxRatesResource.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return obj.toJSONString();
    }
    
    /**
     * Transfer money to other accounts
     * @param faccount
     * @param famount
     * @param taccount
     * @param user
     * @param pin
     * @return string
     */
    @GET
    @Path("/{faccount}/{famount}/{taccount}/{user}/{pin}")
    @Produces("application/json")
    public String transferMoney(
            @PathParam("faccount")int faccount, 
            @PathParam("famount")int famount, 
            @PathParam("taccount")double taccount,
            @PathParam("user")int user,
            @PathParam("pin")int pin)
    {
        Mysql_Connection mysql = new Mysql_Connection();  
        Connection con = mysql.connect();
        ResultSet rs = null;
        String code,result;
        try {
            PreparedStatement pStat = con.prepareStatement("{CALL sp_transfer_money(?,?,?,?,?)}");
            pStat.setInt(1, faccount);
            pStat.setInt(2, famount);
            pStat.setDouble(3, taccount);
            pStat.setInt(4, user);
            pStat.setInt(5, pin);
            rs = pStat.executeQuery();
            rs.beforeFirst();
            rs.next();
            code = rs.getString("code");
            result = rs.getString("msg");
        } catch (SQLException ex) {
            Logger.getLogger(AccountResource.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
        }finally{
            mysql.disconnect();
            if(rs!=null){
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(FxRatesResource.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        JSONObject obj = new JSONObject();
        obj.put("code", code);
        obj.put("msg", result);
        
        return obj.toJSONString();
    }
    /**
     * PUT method for updating or creating an instance of AccountResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}
