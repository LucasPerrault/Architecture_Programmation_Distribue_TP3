import interfaces.Billboard;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class BillboardServer extends UnicastRemoteObject implements Billboard {
    private final Object msgLock = new Object();
    private String message;

    protected BillboardServer() throws RemoteException {
        super();
    }

    @Override
    public String getMessage() {
        String out;
        synchronized (msgLock) {
            out = message;
        }
        return out;
    }

    @Override
    public void setMessage(String message) {
        synchronized (msgLock) {
            this.message = message;
        }
    }
}
