package mdsd.model;

import java.net.URL;
import java.util.ArrayList;
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
	 */
	private Map<URL, Microservice> microservices;
	
	/**
	 * Construct
	 * @param microservices the collection of microservices the system consists of
	 */
	public MetaModel(List<Microservice> microservices) {
		this.microservices = new HashMap<URL, Microservice>();
		for (Microservice microservice : microservices) {
			this.microservices.put(microservice.getUrl(), microservice);
		}
	}
	
	/**
	 * Gets a read-only collection of the microservices this system consists of.
	 * @return
	 */
	public Collection<Microservice> getMicroservices() {
		return Collections.unmodifiableCollection(microservices.values());
	}
	
	/**
	 * Get a specific microservice
	 * @param url the url corresponding to the microservice to rectrieve
	 * @return
	 */
	public Microservice getMicroservice(URL url) {
		return microservices.get(url);
	}

}
