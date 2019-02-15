package mdsd.executors;

import java.util.ArrayList;
import java.util.List;

import mdsd.executors.util.HttpUtil;
import mdsd.model.MetaModel;
import mdsd.model.Microservice;

/**
 * Class for creating new instances of subclasses of MicroserviceExecutor
 * to execute the meta model.
 * 
 * @author Niels
 */
public class MetaModelExecutor {
	
	private MetaModel metaModel;
	private List<HttpSocketMicroserviceExecutor> executors;
	private HttpUtil httpUtil;
	
	public MetaModelExecutor(MetaModel metaModel) {
		this.metaModel = metaModel;
		executors = new ArrayList<HttpSocketMicroserviceExecutor>();
		for (Microservice microservice : metaModel.getMicroservices()) {
			HttpSocketMicroserviceExecutor executor = new HttpSocketMicroserviceExecutor(microservice);
			executor.startService();
			executors.add(executor);
		}
		httpUtil = new HttpUtil();
		
		System.out.println("Meta model contains: ");
		System.out.println(metaModel);
	}

}
