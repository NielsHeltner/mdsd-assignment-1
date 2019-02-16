package mdsd.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Meta model for the system of microservices.
 *
 * @author Niels
 *
 */
public class MetaModel {

    /**
     * Lookup table for the microservices currently in the system.
     * Implemented as Map instead of List to be able to look up
     * in time complexity O(1), given the name of the service.
     */
    private Map<String, Microservice> microservices;

    /**
     * Construct
     *
     * @param microservices the collection of microservices the system consists
     * of
     */
    public MetaModel(List<Microservice> microservices) {
        this.microservices = new HashMap<>();
        for (Microservice microservice : microservices) {
            this.microservices.put(microservice.getUrl().getAuthority(), microservice);
        }
    }

    /**
     * Gets a read-only collection of the microservices this system consists of.
     *
     * @return
     */
    public Collection<Microservice> getMicroservices() {
        return Collections.unmodifiableCollection(microservices.values());
    }

    /**
     * Get a specific microservice
     *
     * @param url the url authority corresponding to the microservice to
     * retrieve
     * @return
     */
    public Microservice getMicroservice(String url) {
        return microservices.get(url);
    }

    @Override
    public String toString() {
        return "MetaModel{microservices = " + microservices.values() + "}";
    }

}
