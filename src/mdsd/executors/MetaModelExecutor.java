package mdsd.executors;

import java.util.ArrayList;
import java.util.List;

import mdsd.model.MetaModel;
import mdsd.model.Microservice;

/**
 * Class for executing a meta model, by passing each microservice it contains
 * into a microservice executor.
 *
 * @author Niels
 */
public class MetaModelExecutor {

    private final MetaModel metaModel;
    private List<HttpSocketMicroserviceExecutor> executors;

    public MetaModelExecutor(MetaModel metaModel) {
        this.metaModel = metaModel;
        executors = new ArrayList<>();
        for (Microservice microservice : metaModel.getMicroservices()) {
            HttpSocketMicroserviceExecutor executor = new MicroserviceVerifier(microservice);
            executors.add(executor);
        }

        System.out.println("Meta model contains: ");
        System.out.println(metaModel);
    }

    public void startExecution() {
        for (HttpSocketMicroserviceExecutor executor : executors) {
            executor.startService();
        }
    }

    public void stopExecution() {
        for (HttpSocketMicroserviceExecutor executor : executors) {
            executor.stopService();
        }
    }

}
