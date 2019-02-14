package mdsd.model;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import rawhttp.core.HttpMessage;
import rawhttp.core.RawHttpRequest;

public class HttpUtil {
	
	private final String keyValueSeparator = "=";
	private final String parameterSeparator = "&";
	
	public String toString(Map<String, Object> parameters) {
		StringBuilder result = new StringBuilder();
		for (Map.Entry<String, Object> entry : parameters.entrySet()) {
			result.append(entry.getKey()).append(keyValueSeparator).append(entry.getValue()).append(parameterSeparator);
		}
		result.replace(result.length() - 1, result.length(), ""); // remove last '&'
		return result.toString();
	}
	
	public Map<String, Object> toMap(String parameters) {
		Map<String, Object> result = new HashMap<String, Object>();
		String[] parameterList = parameters.split(parameterSeparator);
		for (String parameter : parameterList) {
			String[] keyAndValue = parameter.split(keyValueSeparator);
			result.put(keyAndValue[0], keyAndValue[1]);
		}
		return result;
	}
	
	public String getBody(HttpMessage message) { // works for both request and response
		StringBuilder result = new StringBuilder();
		message.getBody().ifPresent((bodyReader) -> {
        	try {
				result.append(bodyReader.asRawString(Charset.defaultCharset()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
		return result.toString();
	}

}
