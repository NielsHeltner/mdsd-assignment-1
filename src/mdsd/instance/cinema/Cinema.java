package mdsd.instance.cinema;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import mdsd.model.HttpMethod;
import static mdsd.model.HttpMethod.*;
import mdsd.model.MetaModelExecutor;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;
import rawhttp.core.body.StringBody;

public class Cinema {
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		MetaModelExecutor executor = new MetaModelExecutor(new CinemaScript().getMetaModel());
		try {
			executor.handleRequest(new URL("http://localhost:5001/movies")); //legal microservice, legal endpoint, expects no parameters
			/*executor.handleRequest(new URL("http://localhost:5000/login"));//legal microservice, legal endpoint, expects two parameters
			executor.handleRequest(new URL("http://localhost:5002/qwerty"));//legal microservice, illegal endpoint
			executor.handleRequest(new URL("http://localhost:11/qwerty"));//illegal microservice
			*/
			
			request(new URL("http://localhost:5000/login"), POST);
			request(new URL("http://localhost:5000/login"), POST);
			request(new URL("http://localhost:5001/movies1"), GET);
			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public static void request(URL url, HttpMethod method) throws UnknownHostException, IOException {
		String location = url.getHost();
		int port = url.getPort();
		String path = url.getPath();
		
		Socket socket = new Socket(location, port);

		RawHttp http = new RawHttp();
		RawHttpRequest request = http.parseRequest(method + " " + path + " HTTP/1.1\r\n" + 
						"Host: " + location + ":"+ port + "\r\n").withBody(new StringBody("username=3"));
		request.writeTo(socket.getOutputStream());
		
		RawHttpResponse<?> response = http.parseResponse(socket.getInputStream());
		
		System.out.println(response.getBody().get().asRawString(Charset.defaultCharset()));
	}

}
