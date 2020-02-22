package info.rohanmorris;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;

import org.json.simple.JSONArray;

/**
 * REST Web Service
 * @author rohan
 */
@Path("get-rates")
public class FxRatesResource {
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GenericResource
     */
    public FxRatesResource() {}

    @GET
    @Produces("application/json")
    public String getFxrates() {
        Mysql_Connection mysql = new Mysql_Connection();  
        JSONArray obj = new JSONArray();
        ResultSet rs = null;
        try {           
            rs = mysql.ExecQuery("SELECT * FROM view_get_major_fx_rates");  
            DecimalFormat df = new DecimalFormat("####0.00");    
            while(rs.next()) {
                Map m = new HashMap();
                m.put("Currency",   rs.getString("currency"));
                m.put("Sell",       "$" + df.format(rs.getDouble("rate")));
                m.put("Buy Cash",   "$" + df.format(rs.getDouble("rate")-2));
                m.put("Buy Chq",    "$" + df.format(rs.getDouble("rate")-3));
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
     * PUT method for updating or creating an instance of FxRatesResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}
