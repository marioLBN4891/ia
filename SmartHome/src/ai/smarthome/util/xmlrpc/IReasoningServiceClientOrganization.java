package ai.smarthome.util.xmlrpc;

/**
 * @author pasquale
 * Interfaccia che definisce i metodi esposti da ReasoningXMLRPCService.
 */
public interface IReasoningServiceClientOrganization {
	
	public boolean sendPosition(String user, String latitude, String longitude) throws Exception;

	public boolean startServer() throws Exception;

	public boolean startSimulazione() throws Exception;

	public boolean retract(Object[] listaRetract) throws Exception;
	
	public Object[] inferisci(Object[] listaProlog) throws Exception;

	public Object[] loginUtente(String username, String pwd) throws Exception;

}
