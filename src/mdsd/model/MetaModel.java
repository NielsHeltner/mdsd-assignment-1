package mdsd.model;

import java.util.ArrayList;
import java.util.Collections;
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
	 * Lookup table for the microservices currently in the system. // THIS SHOULD BE MOVED TO THE FLUENT INTERFACE FOR BINDING
	 */
	//private Map<String, Microservice> microservices;
	
	/**
	 * Collection of the microservices in the system.
	 */
	private List<Microservice> microservices;
	
	/**
	 * Construct
	 * @param microservices the collection of microservices the system consists of
	 */
	public MetaModel(List<Microservice> microservices) {
		this.microservices = new ArrayList<Microservice>(microservices); 
	}
	
	/**
	 * Gets a read-only collection of the microservices this system consists of.
	 * @return
	 */
	public List<Microservice> getMicroservices() {
		return Collections.unmodifiableList(microservices);
	}

}
