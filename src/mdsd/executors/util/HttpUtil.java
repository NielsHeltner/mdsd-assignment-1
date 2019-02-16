package mdsd.executors.util;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import rawhttp.core.HttpMessage;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.body.StringBody;

/**
 * Utility methods for working with http in Java, and 'serializing' /
 * 'de-serializing' a map of parameters.
 *
 * @author Niels
 *
 */
public class HttpUtil {

    private final String keyValueSeparator = "=";
    private final String parameterSeparator = "&";

    /**
     * Converts a map of parameters (pairing parameter names with parameter
     * values) to a String of the format 'parameterName=parameterValue', where
     * each pair is separated with an '&'
     *
     * @param parameters
     * @return
     */
    public String toString(Map<String, Object> parameters) {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            result.append(entry.getKey())
                    .append(keyValueSeparator)
                    .append(entry.getValue())
                    .append(parameterSeparator);
        }
        result.replace(result.length() - 1, result.length(), ""); // remove last '&'
        return result.toString();
    }

    /**
     * Converts a String of parameters of the format
     * 'parameterName=parameterValue', where each pair is separated with an '&',
     * to a map where the key is parameter name and the value is the parameter
     * value.
     *
     * @param parameters
     * @return
     */
    public Map<String, Object> toMap(String parameters) {
        Map<String, Object> result = new HashMap<>();
        if (!parameters.isEmpty()) {
            String[] parameterList = parameters.split(parameterSeparator);
            for (String parameter : parameterList) {
                String[] keyAndValue = parameter.split(keyValueSeparator);
                result.put(keyAndValue[0], keyAndValue[1]);
            }
        }
        return result;
    }

    /**
     * Parses and returns the body of an HttpMessage (either request or
     * response) as a String. If no body is present, returns an empty String ""
     *
     * @param message
     * @return
     */
    public String getBody(HttpMessage message) {
        StringBuilder result = new StringBuilder();
        message.getBody().ifPresent((bodyReader) -> {
            try {
                result.append(bodyReader.asRawString(Charset.defaultCharset()));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });
        return result.toString();
    }

    public RawHttpRequest parseRequest(Socket socket) throws IOException {
        return new RawHttp().parseRequest(socket.getInputStream()).eagerly();
    }

    public void sendResponse(Socket clientSocket, Object content) throws IOException {
        new RawHttp()
                .parseResponse("HTTP/1.1 200 OK\r\n"
                        + "Content-Type: text/plain\r\n")
                .withBody(new StringBody(content.toString()))
                .writeTo(clientSocket.getOutputStream());
    }

}
