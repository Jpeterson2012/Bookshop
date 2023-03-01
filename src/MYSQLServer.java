import java.net.*;
import java.io.*;
import java.util.*;
import java.sql.*;
public class MYSQLServer extends Thread {

    public static int portNumber = 6066;
    private final Socket newServer;
    public MYSQLServer(Socket socket)
    {
        newServer = socket;
    }
    public static void main(String[] args)
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            while(true)
            {
                Socket server = serverSocket.accept( );
                Thread serverThread = new MYSQLServer(server);
                serverThread.start( );
            }
        }
        catch( BindException s )
        {
            System.out.println("Port Number:\t" + portNumber + " already in use");
        }
        catch(IOException e)
        {
            e.printStackTrace( );
            }
    }
    public void run( )
    {
        String[] args = {};
        System.out.println("Received request from:\t" +
                newServer.getInetAddress( ) + ":" + newServer.getPort( ));
        try
        {
            String treadName = (Thread.currentThread( )).getName( );
            System.out.println("Thread " + treadName + " has started!");

        }
        catch( Exception e )
        {
            System.err.println("Exception occurred " + e.getMessage());
        }
        try {
            newServer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
