import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientMain {


    static private Registry reg = null;
    static private FTBillboard currentServer = null;
    static private String serverID = null;

    static public void main(String [] args) throws NotBoundException {

        if(args.length !=2) {
            System.out.println("Server port");
            System.exit(0);
        }

        String address = args[0];
        int port = Integer.parseInt(args[1]);
        serverID = address+":"+port;

        try {
            reg = LocateRegistry.getRegistry(address,port);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        try {
            currentServer = (FTBillboard) reg.lookup(FTBillboard.LOOKUP_NAME);
        } catch (RemoteException e) {
            e.printStackTrace();
        }



        System.out.println("Starting Lucas client");

        for(int i = 0; i<1000 ; i++) {
            String message = "Hello boys " + i, received="";

            System.out.println("Message :" + message);


            try {
                currentServer.setMessage(message);
                Thread.sleep(2500);
                received = currentServer.getMessage();
            } catch (RemoteException e) {


            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(received.equals(message)) {
                System.out.println("No problem, it's good !");
            }
            else {
                System.out.println("OOppps, problem: " + received + " instead of " + message );
            }
        }
    }


}
