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
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
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
@Path("biller")
public class BillerResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of BillerResource
     */
    public BillerResource() {
    }

    /**
     * Get biller list
     * @return an instance of String
     */
    @GET
    @Path("/get-biller")
    @Produces("application/json")
    public String getBillerList() {
        Mysql_Connection mysql = new Mysql_Connection();  
        JSONArray obj = new JSONArray();
        ResultSet rs = null;
        try {           
            rs = mysql.ExecQuery("SELECT * FROM view_get_biller");      
            while(rs.next()) {
                Map m = new HashMap();
                m.put("Id",   rs.getInt("id"));
                m.put("Name", rs.getString("name"));
                obj.add(m);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FxRatesResource.class.getName()).log(Level.SEVERE, null, ex);
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
     * Add Payment
     * @param account
     * @param biller_id
     * @param amount
     * @param date
     * @param user
     * @param pin
     * @return an instance of String
     */
    @GET
    @Path("/{account}/{biller_id}/{amount}/{date}/{user}/{pin}")
    @Produces("application/json")
    public String addPayment(
            @PathParam("account")int account, 
            @PathParam("biller_id")int biller_id, 
            @PathParam("amount")double amount,
            @PathParam("date")String date,
            @PathParam("user")int user,
            @PathParam("pin")int pin)
    {
        Mysql_Connection mysql = new Mysql_Connection();  
        Connection con = mysql.connect();
        ResultSet rs = null;
        String code,result;
        try {
            PreparedStatement pStat = con.prepareStatement("{CALL sp_add_biller_payment(?,?,?,?,?,?)}");
            pStat.setInt(1, account);
            pStat.setInt(2, biller_id);
            pStat.setDouble(3, amount);
            pStat.setString(4, date);
            pStat.setInt(5, user);
            pStat.setInt(6, pin);
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
     * Buy phone credit
     * @param account
     * @param amount
     * @param network
     * @param phone
     * @param user
     * @param pin
     * @return
     */
    @GET
    @Path("buy-credit/{account}/{amount}/{network}/{phone}/{user}/{pin}")
    @Produces("application/json")
    public String buyCredit(
            @PathParam("account")int account, 
            @PathParam("amount")double amount,
            @PathParam("network")String network,
            @PathParam("phone")String phone,
            @PathParam("user")int user,
            @PathParam("pin")int pin)
    {
        Mysql_Connection mysql = new Mysql_Connection();  
        Connection con = mysql.connect();
        ResultSet rs = null;
        String code,result;
        try {
            PreparedStatement pStat = con.prepareStatement("{CALL sp_buy_credit(?,?,?,?,?,?)}");
            pStat.setInt(1, account);
            pStat.setDouble(2, amount);
            pStat.setString(3, network);
            pStat.setString(4, phone);
            pStat.setInt(5, user);
            pStat.setInt(6, pin);
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
     * PUT method for updating or creating an instance of BillerResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    public void putXml(String content) {
    }
}
