package mdsd.dsl.internal;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mdsd.model.Endpoint;
import mdsd.model.HttpMethod;
import mdsd.model.Invocable;
import mdsd.model.MetaModel;
import mdsd.model.Microservice;

/**
 * Fluent interface, representing an internal DSL, for populating the MetaModel.
 * Based on the Expression Builder pattern [Fowler].
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

    private List<Microservice> microservices = new ArrayList<>();

    private Microservice currentMicroservice;

    private String currentPath;

    private void buildAndVerifyModel() {
        build();
        // verify and handle illegal states and errors
        metaModel = new MetaModel(microservices);
    }

    /**
     * Override in subclasses, must populate meta model using the fluent
     * interface
     */
    protected abstract void build();

    public MicroserviceBuilder microservice(String name, String location, int port) {
        try {
            currentMicroservice = new Microservice(name, location, port);
            microservices.add(currentMicroservice);
        }
        catch (MalformedURLException e) {
            throw new IllegalArgumentException("Cannot create microservice due to malformed URL");
        }
        return this;
    }

    public MicroserviceBuilder endpoint(String path) {
        if (currentMicroservice == null) {
            throw new IllegalStateException("Cannot create endpoint " + path + " before creating microservice.");
        }
        currentPath = path;
        currentMicroservice.addEndpoint(new Endpoint(path));
        return this;
    }

    public MicroserviceBuilder method(HttpMethod method) {
        if (currentMicroservice == null) {
            throw new IllegalStateException("Cannot configure endpoint method before creating microservice.");
        }
        if (currentPath == null) {
            throw new IllegalStateException("Cannot configure endpoint method before creating endpoint.");
        }
        currentMicroservice.getEndpoint(currentPath).setHttpMethod(method);
        return this;
    }

    public MicroserviceBuilder parameter(String name, Class<?> type) {
        if (currentMicroservice == null) {
            throw new IllegalStateException("Cannot configure endpoint parameter before creating microservice.");
        }
        if (currentPath == null) {
            throw new IllegalStateException("Cannot configure endpoint parameter before creating endpoint.");
        }
        currentMicroservice.getEndpoint(currentPath).addParameter(name, type);
        return this;
    }

    public MicroserviceBuilder response(Class<?> responseType) {
        if (currentMicroservice == null) {
            throw new IllegalStateException("Cannot configure endpoint response type before creating microservice.");
        }
        if (currentPath == null) {
            throw new IllegalStateException("Cannot configure endpoint response type before creating endpoint.");
        }
        currentMicroservice.getEndpoint(currentPath).setResponseType(responseType);
        return this;
    }

    public MicroserviceBuilder function(Invocable<Map<String, Object>, Object> invocation) {
        if (currentMicroservice == null) {
            throw new IllegalStateException("Cannot configure endpoint response type before creating microservice.");
        }
        if (currentPath == null) {
            throw new IllegalStateException("Cannot configure endpoint response type before creating endpoint.");
        }
        currentMicroservice.getEndpoint(currentPath).setInvocation(invocation);
        return this;
    }

}
