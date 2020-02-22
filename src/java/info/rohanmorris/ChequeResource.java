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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import org.json.simple.JSONObject;

/**
 * REST Web Service
 *
 * @author rohan
 */
@Path("cheque")
public class ChequeResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ChequeResource
     */
    public ChequeResource() {
    }

    /**
     * Upload cheque information
     * @param account
     * @param amount
     * @param cheque_back
     * @param cheque_front
     * @return String
     */
    @POST
    @Path("/{account}/{amount}/{cheque_front}/{cheque_back}")
    @Produces("application/json")
    public String getJson(
            @PathParam("account")int  account, 
            @PathParam("amount")double amount, 
            @PathParam("cheque_front")String cheque_front,
            @PathParam("cheque_back")String cheque_back
    ) {
        Mysql_Connection mysql = new Mysql_Connection();  
        Connection con = mysql.connect();
        ResultSet rs = null;
        String code,result;
        try {
            PreparedStatement pStat = con.prepareStatement("{CALL sp_deposit_cheque(?,?,?,?)}");
            pStat.setInt(1, account);
            pStat.setDouble(2, amount);
            pStat.setString(3, cheque_front);
            pStat.setString(4, cheque_back);
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
     * PUT method for updating or creating an instance of ChequeResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}
