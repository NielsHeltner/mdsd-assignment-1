package mdsd.instances.cinema;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import mdsd.executors.MetaModelExecutor;
import mdsd.executors.MetaModelTester;

import static mdsd.model.HttpMethod.*;

/**
 * Main class for running, executing and testing the cinema instance of the meta
 * model.
 *
 * @author Niels
 *
 */
public class Cinema {

    public static void main(String[] args) {
        new MetaModelExecutor(new CinemaScript().getMetaModel());
        MetaModelTester tester = new MetaModelTester();
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("username", "niels");
            parameters.put("password", "123");
            tester.request(new URL("http://localhost:5000/login"), POST, parameters); // legal microservice, legal endpoint, legal method, legal parameters
            tester.request(new URL("http://localhost:5000/login"), POST); // legal microservice, legal endpoint, legal method, illegal parameters
            tester.request(new URL("http://localhost:5000/login"), GET); // legal microservice, legal endpoint, illegal method, illegal parameters
            tester.request(new URL("http://localhost:5000/qwerty"), GET); // legal microservice, illegal endpoint, illegal method, illegal parameters
            tester.request(new URL("http://localhost:1/qwerty"), GET); // illegal microservice, illegal endpoint, illegal method, illegal parameters

            tester.request(new URL("http://localhost:5001/movies"), GET); // legal microservice, legal endpoint, legal method, legal parameters
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}
