import java.io.*;
import java.net.*;

public class S {

    static int serverPort = 10322; // default port

    public static void main(String[] args) {

        if( args.length == 1 ) serverPort = Integer.parseInt(args[0]);
        try {
            // instantiates a stream socket for accepting
            // connections
            ServerSocket myConnectionSocket = new ServerSocket(serverPort);
            String message;
            InputStreamReader is = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(is);
            File file = new File("");
            try {
                System.out.println("Enter path to your file.");
                String fileName = br.readLine();
                file = new File(fileName);
            } catch(IOException e) {
                System.out.println(e);
            }
            while( true ) { // forever loop
                // wait to accept a connection
                System.out.println("Waiting for a connection.");
                MyStreamSocket myDataSocket = new MyStreamSocket(myConnectionSocket.accept());
                System.out.println("Connection accepted");
                boolean done = false;
                boolean messageSent = false;
                while( !done ) {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    message = myDataSocket.receiveMessage();
                    System.out.println("message received: " + message);
                    int n = 1;
                    if(message.equalsIgnoreCase("quit")) {
                       myDataSocket.sendMessage("Client disconnected.");
                       messageSent = true;
                       myDataSocket.close();
                       myDataSocket = new MyStreamSocket(myConnectionSocket.accept());
                  } else if(message.equalsIgnoreCase("stop")) {
                        //shutdown server
                        myDataSocket.sendMessage("Server shutting down...");
                        messageSent = true;
                        myDataSocket.close();
                        myConnectionSocket.close();
                        return;
                    } else {
                        int i = 0;
                        try {
                            i = Integer.parseInt(message);
                        } catch(Exception e) {
                            myDataSocket.sendMessage("ERR");
                            messageSent = true;
                        }
                        if ( i > 0) {
                            String line;
                            while (reader.readLine() != null) {
                                if (n+1 == i) {
                                    line = reader.readLine();
                                    if(line == null) {
                                        myDataSocket.sendMessage("ERR");
                                    } else {
                                        myDataSocket.sendMessage(line);
                                        messageSent = true;
                                        break;
                                    }
                                } else {
                                    n++;
                                }
                            }
                        } else {
                            myDataSocket.sendMessage("ERR");
                            messageSent = true;
                        }
                    }
                }// end while !done
                if(!messageSent) {
                    myDataSocket.sendMessage("ERR");
                }
            } // end while forever
        } // end try
        catch (Exception ex) {
        }
    } // end main
} // end class