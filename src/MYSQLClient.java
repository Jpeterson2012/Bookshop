import java.net.*;
import java.io.*;
import java.sql.* ;
public class MYSQLClient {

    public static int portNumber = 6066;
    public static String serverHostName = "localhost";
    public static void main(String[] args)
    {
        try
        {
            /*----------------Define an instance of the Socket class ----------------------------------*/
            System.out.println("Connecting to " + serverHostName + " on port " + portNumber);
            Socket client = new Socket(serverHostName, portNumber);
            System.out.println("Just connected to " + client.getRemoteSocketAddress( ));
            MYSQL.main(args);
            client.close( );
        }
        catch(IOException e)
        {
            e.printStackTrace( );
        }
    }
}
