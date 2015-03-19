package ai.smarthome.util.xmlrpc;

import java.net.URL;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**
 * 
 * @author vito
 * 
 *         Client per il servizio ServiceOrganization .
 */
public class ReasoningServiceClientOrganization implements IReasoningServiceClientOrganization {

	private XmlRpcClient client;

	public ReasoningServiceClientOrganization(URL url, String username, String password) {
		this(url, username, password, 60 * 100000, 60 * 100000);
	}

	public ReasoningServiceClientOrganization(URL url, String username, String password, int connectionTimeout, int replyTimeout) {
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();

		config.setServerURL(url);

		config.setBasicUserName(username);
		config.setBasicPassword(password);

		config.setEnabledForExtensions(true);
		config.setEnabledForExceptions(true);

		config.setConnectionTimeout(connectionTimeout);
		config.setReplyTimeout(replyTimeout);

		config.setBasicEncoding("UTF-8");

		this.client = new XmlRpcClient();
		this.client.setConfig(config);
	}

	private Object _invokeMethod(String methodName, Object[] params) throws XmlRpcException {
		return client.execute(methodName, params);
	}

	// metodi per chiamare funzionalità SmartKitchen su ReasoningXMLRPCServiceOrganization. Autore: Labianca Mario
	
	@Override
	public boolean sendPosition(String user, String latitude, String longitude) throws Exception {
		Object[] params = new Object[] {user, latitude, longitude};
		return (Boolean) _invokeMethod("SMARTKITCHEN.sendPosition", params);
	}

	@Override
	public boolean startServer() throws Exception {
		Object[] params = new Object[] {};
		return (Boolean) _invokeMethod("SMARTKITCHEN.startServer", params);
	}

	@Override
	public boolean startSimulazione() throws Exception {
		Object[] params = new Object[] {};
		return (Boolean) _invokeMethod("SMARTKITCHEN.startSimulazione", params);
	}

	@Override
	public boolean retract(Object[] listaRetract) throws Exception {
		Object[] params = new Object[] {listaRetract};
		return (Boolean) _invokeMethod("SMARTKITCHEN.retract", params);
	}

	@Override
	public Object[] inferisci(Object[] listaProlog) throws Exception {
		Object[] params = new Object[] {listaProlog};
		return (Object[]) _invokeMethod("SMARTKITCHEN.inferisci", params);
	}
	
}
