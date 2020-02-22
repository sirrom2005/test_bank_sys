package info.rohanmorris;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 * @author rohan
 */
@javax.ws.rs.ApplicationPath("bank")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(info.rohanmorris.AccountResource.class);
        resources.add(info.rohanmorris.BillerResource.class);
        resources.add(info.rohanmorris.ChequeResource.class);
        resources.add(info.rohanmorris.FxRatesResource.class);
    }
    
}
