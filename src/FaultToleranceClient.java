import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class FaultToleranceClient {
    static private Registry reg = null;
    static private FTBillboard currentServer = null;
    static List<String> neighbors = new ArrayList<>();
    static private String serverID = null;

    static public void main(String[] args) {

        if (args.length != 2) {
            System.out.println("@params needed : [adresse] [port].");
            System.out.println("Example: localhost 1250");
            System.exit(0);
        }

        FaultToleranceClient.registerServer(args[0], Integer.parseInt(args[1]));
        FaultToleranceClient.connectServer();

        System.out.println("---- Starting FaultTolerency Client ----");
        while(true) {
            try {
                FaultToleranceClient.updateNeighbors();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                System.out.println("Oopsss, impossible to listen the server : " + serverID);
                FaultToleranceClient.findAndConnectToReplicas();
            }
        }
    }

    private static void registerServer(String address, int port) {
        try {
            reg = LocateRegistry.getRegistry(address, port);
            serverID = address + ":" + port;
            System.out.println("Register the server : " + serverID);
        } catch (RemoteException e) {
            System.out.println("Oopsss, impossible to register the server : " + address + ":" + port);
            e.printStackTrace();
        }
    }

    private static void connectServer() {
        try {
            currentServer = (FTBillboard) reg.lookup(FTBillboard.LOOKUP_NAME);
            System.out.println("Server connected to : " + serverID);
        } catch (RemoteException e) {
            System.out.println("Oops, impossible to connect the server : " + serverID);
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

    private static void updateNeighbors() throws RemoteException, InterruptedException {
        /* Get all neighbours registered */
        neighbors = currentServer.getNeighbors();

        /* Print all neighbours */
        System.out.println("Neighbours availables are : ");
        neighbors.forEach(System.out::println);
        System.out.println("\n");

        /* Simulate asynchronous connection of 2000ms */
        Thread.sleep(2000);
    }

    private static void findAndConnectToReplicas() {
        if (neighbors.size() < 2) {
            System.out.println("Error 501 : No server is unavailable.");
            return;
        }

        String replicaServer = neighbors.get(1);
        String replicaAdress = replicaServer.split(":")[0];
        int replicaPort = Integer.parseInt(replicaServer.split(":")[1]);
        FaultToleranceClient.registerServer(replicaAdress, replicaPort);
        FaultToleranceClient.connectServer();
    }
}
