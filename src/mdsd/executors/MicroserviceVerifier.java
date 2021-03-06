package mdsd.executors;

import mdsd.executors.util.HttpUtil;
import mdsd.model.Endpoint;
import mdsd.model.HttpMethod;
import mdsd.model.Microservice;
import rawhttp.core.RawHttpRequest;

import java.util.Map;

/**
 * Specializes the HttpSocketMicroserviceExecutor by defining how to verify
 * a request in context of the populated meta model.
 * (Notes for self below:)
 * Could also have been implemented as a Decorator, however the method of
 * interest (handleIncomingRequest) is only called from within the class itself.
 * Could also have been implemented as a Strategy, however there would only be
 * a single strategy (this one), and since every HttpSocketMicroserviceExecutor
 * would need the same strategy, that looks more like a Template.
 *
 * @author Niels
 *
 */
public class MicroserviceVerifier extends HttpSocketMicroserviceExecutor {

    public MicroserviceVerifier(Microservice model) {
        super(model);
    }
    
    @Override
    protected boolean verify(RawHttpRequest request) {
    	String path = request.getStartLine().getUri().getPath();
        if (verifyPath(path)) {
            Endpoint endpoint = getModel().getEndpoint(path);
            System.out.println("Found endpoint " + endpoint.getPath());
            if (verifyEndpoint(endpoint, request)) {
                return true;
            }
        }
        else {
            System.out.println("Microservice " + getModel().getName() + " does not contain endpoint " + path);
        }
        return false;
    }

    private boolean verifyEndpoint(Endpoint endpoint, RawHttpRequest request) {
        HttpMethod requestMethod = HttpMethod.valueOf(request.getStartLine().getMethod());
        if (verifyMethod(requestMethod, endpoint)) {
            System.out.println("Verified method " + requestMethod);
            HttpUtil httpUtil = new HttpUtil();
            if (verifyParameters(httpUtil.toMap(httpUtil.getBody(request)), endpoint)) {
                System.out.println("Verified parameters");
                return true;
            }
            else {
                System.out.println("Did not verify parameters");
            }
        }
        else {
            System.out.println("Endpoint " + endpoint.getPath() + " supports http method " + endpoint.getHttpMethod() + " but received " + requestMethod);
        }
        return false;
    }

    private boolean verifyPath(String path) {
        return getModel().getEndpoint(path) != null;
    }

    private boolean verifyMethod(HttpMethod method, Endpoint endpoint) {
        return endpoint.getHttpMethod().equals(method);
    }

    private boolean verifyParameters(Map<String, Object> parameters, Endpoint endpoint) {
        for (Map.Entry<String, Class<?>> entry : endpoint.getParameters().entrySet()) {
            String expectedName = entry.getKey();
            Class<?> expectedType = entry.getValue();
            if (parameters.containsKey(expectedName)) {
                try {
                    expectedType.cast(parameters.get(expectedName)); // attempt to cast received parameter value to expected type (since everything is a string after network communication)
                } catch (ClassCastException e) {
                    System.out.println("Expected parameter " + expectedName + " to be of type " + expectedType + " but it was not.");
                    return false;
                }
            }
            else {
                System.out.println("Expected parameter " + expectedName + " but was not received.");
                return false;
            }
        }
        return true;
    }

}
