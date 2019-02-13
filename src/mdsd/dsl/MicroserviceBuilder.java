package mdsd.dsl;

import mdsd.model.HttpMethod;
import mdsd.model.MetaModel;

/**
 * Fluent interface, representing an internal DSL, for populating the MetaModel.
 * Follows the Expression Builder pattern [Fowler].
 * 
 * @author Niels
 *
 */
public abstract class MicroserviceBuilder {
	
	private MetaModel metaModel;
	
	public MetaModel getMetaModel() {
		buildAndVerifyModel();
		return metaModel;
	}
	
	private void buildAndVerifyModel() {
		build();
		// verify and handle illegal states and errors
		//metaModel = new MetaModel();
	}

	/**
	 * Override in subclasses, must define populate meta model 
	 * using fluent interface
	 */
	protected abstract void build();
	
	public MicroserviceBuilder microservice(String name, String location, int port) {
		return this;
	}
	
	public MicroserviceBuilder endpoint(String name) {
		return this;
	}
	
	public MicroserviceBuilder method(HttpMethod method) {
		return this;
	}
	
	public MicroserviceBuilder parameter(String name, Class<?> type) {
		return this;
	}
	
	public MicroserviceBuilder response(Class<?> type) {
		return this;
	}

}
