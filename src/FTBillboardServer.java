import interfaces.FTBillboard;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.List;

public class FTBillboardServer extends BillboardServer implements FTBillboard {
    private final static String RMI = "rmi://";
    private final static String HOST = "localhost";
    private final static String SERVER_NAME = "/server_fault_tolerancy";

    private java.rmi.registry.Registry currentLeader;

    protected FTBillboardServer(String host) throws RemoteException {
        super();
        currentLeader =  java.rmi.registry.LocateRegistry.getRegistry(host);
    }

    /**
     * Returns the name of the current leader for this instance
     * @return String containing address:port of the leader's registry
     * @throws RemoteException
     */
    public String getLeader() throws RemoteException {
        return currentLeader.list()[0];
    }
    /**
     * List of address:port of neighbors of this node.
     * If this node is leader, it will return the list of replicas.
     * @return
     * @throws RemoteException
     */
    public List<String> getNeighbors() throws RemoteException {
        return Arrays.asList(currentLeader.list());
    }
    /**
     * Registers a replica to a leader.
     * If the leader does not answer this request in a timely fashion,
     * a new leader is chosen as the first element of the replica list after
     * removal of the leader.
     * @param server address:port of the caller
     * @param replica callback
     * @throws RemoteException
     */
    public void registerReplica(String server, FTBillboard replica) throws RemoteException, MalformedURLException, NotBoundException, AlreadyBoundException {
        Naming.bind(server, replica);
    }

    public static void main(String args[])
    {
        try
        {
            /* Basic server */

            /* 1 - Configuration */
            final int port = 1099;
            String serverUrl = RMI + HOST + ":" + port + SERVER_NAME;

            /* 2 - Init */
            FTBillboardServer server = new FTBillboardServer(HOST);
            java.rmi.registry.LocateRegistry.createRegistry(port);
            Naming.rebind(serverUrl, server);


            /* Replica server */
            final int replicaPort = 1250;
            String replicaServerUrl = RMI + HOST + ":" + replicaPort + SERVER_NAME;

            FTBillboard replicaServer = new FTBillboardServer(HOST);
            java.rmi.registry.LocateRegistry.createRegistry(replicaPort);
            server.registerReplica(replicaServerUrl, replicaServer);

            UnicastRemoteObject.exportObject(replicaServer);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
