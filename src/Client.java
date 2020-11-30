import interfaces.FTBillboard;
import java.rmi.Naming;

public class Client {
    public final static String SERVER_NAME = "server_fault_tolerancy";

    public static void main(String args[])
    {
        try
        {
            FTBillboard server = (FTBillboard)Naming.lookup("rmi://localhost:1099/" + SERVER_NAME);
            for (String n : server.getNeighbors()) {
                System.out.println("Neighbor : " + n);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
