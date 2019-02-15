package mdsd.instances.cinema;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import mdsd.executors.MetaModelExecutor;
import mdsd.executors.MetaModelTester;

import static mdsd.model.HttpMethod.*;

public class Cinema {
	
	public static void main(String[] args) {
		new MetaModelExecutor(new CinemaScript().getMetaModel());
		MetaModelTester tester = new MetaModelTester();
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("username", "niels");
			parameters.put("password", "123");
			tester.request(new URL("http://localhost:5000/login"), POST, parameters);
			tester.request(new URL("http://localhost:5000/login"), GET);
			tester.request(new URL("http://localhost:5001/movies1"), GET);
			
			
			
			
			
			/*executor.handleRequest(new URL("http://localhost:5001/movies")); //legal microservice, legal endpoint, expects no parameters
			executor.handleRequest(new URL("http://localhost:5000/login"));//legal microservice, legal endpoint, expects two parameters
			executor.handleRequest(new URL("http://localhost:5002/qwerty"));//legal microservice, illegal endpoint
			executor.handleRequest(new URL("http://localhost:11/qwerty"));//illegal microservice
			*/
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

}
