package mdsd.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import mdsd.executors.util.HttpUtil;
import rawhttp.core.RawHttpRequest;

/**
 * Represents an endpoint for a microservice. Consists of a path, a valid
 * HttpMethod, a collection of expected parameter names and their types, and a
 * response type.
 *
 * @author Niels
 *
 */
public class Endpoint {

    /**
     * The path of this endpoint, which corresponds to the path in the URL.
     */
    private String path;

    /**
     * The valid HttpMethod for this endpoint.
     */
    private HttpMethod method;

    /**
     * The parameters this endpoint expects to receive. Consists of key-value
     * pairs of the parameter's name and the parameter's type.
     */
    private Map<String, Class<?>> parameters;

    /**
     * The type of this endpoint's response. Defaults to Void.class if none is
     * given.
     */
    private Class<?> responseType;

    /**
     * The method that should be invoked when this endpoint is invoked.
     */
    private Invocable<Map<String, Object>, Object> invocation;

    /**
     * Construct
     *
     * @param path the path of this endpoint
     */
    public Endpoint(String path) {
        if (!path.substring(0, 1).equals("/")) {
            path = "/" + path;
        }
        this.path = path;
        parameters = new HashMap<>();
        responseType = Void.class;
    }

    public boolean verify(RawHttpRequest request) {
        HttpMethod requestMethod = HttpMethod.valueOf(request.getStartLine().getMethod());
        if (verifyMethod(requestMethod)) {
            System.out.println("Verified method " + requestMethod);
            HttpUtil httpUtil = new HttpUtil();
            if (verifyParameters(httpUtil.toMap(httpUtil.getBody(request)))) {
                System.out.println("Verified parameters");
                return true;
            }
            else {
                System.out.println("Did not verify parameters");
            }
        }
        else {
            System.out.println("Endpoint " + getPath() + " supports http method " + getHttpMethod() + " but received " + requestMethod);
        }
        return false;
    }

    private boolean verifyParameters(Map<String, Object> parameters) {
        //loop through the two maps and compare

        return true;
    }

    private boolean verifyMethod(HttpMethod method) {
        return this.method.equals(method);
    }

    public void setInvocation(Invocable<Map<String, Object>, Object> invocation) {
        this.invocation = invocation;
    }

    public Object invoke(Map<String, Object> parameters) {
        return invocation.accept(parameters);
    }

    public void setHttpMethod(HttpMethod method) {
        this.method = method;
    }

    public void setResponseType(Class<?> responseType) {
        if (responseType == null) {
            responseType = Void.class;
        }
        this.responseType = responseType;
    }

    public void addParameter(String name, Class<?> type) { // throw new ArgumentException if either is null
        parameters.put(name, type);
    }

    public String getPath() {
        return path;
    }

    public HttpMethod getHttpMethod() {
        return method;
    }

    public Class<?> getResponseType() {
        return responseType;
    }

    /**
     * Gets a read-only collection of key-value pairs that describe the expected
     * parameters for this endpoint.
     *
     * @return
     */
    public Map<String, Class<?>> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    @Override
    public String toString() {
        return "Endpoint{path = " + path + ", http method = " + method + ", parameters = " + parameters + ", response type = " + responseType + "}";
    }

}
