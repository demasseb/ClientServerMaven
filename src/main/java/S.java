import java.io.*;
import java.net.*;

public class S {


    public static void main(String[] args) {
        int serverPort = 10322; // default port
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
                System.out.println(file.getName());
            } catch(IOException e) {
                System.out.println(e);
            }
            while( true ) { // forever loop
                // wait to accept a connection
                System.out.println("Waiting for a connection.");
                MyStreamSocket myDataSocket = new MyStreamSocket(myConnectionSocket.accept());
                System.out.println("connection accepted");
                boolean done = false;
                while( !done ) {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    message = myDataSocket.receiveMessage();
                    /**/System.out.println("message received: " + message);
                    int n = 1;
                    if(message.equalsIgnoreCase("quit")) {
                       myDataSocket.sendMessage("Client disconnected.");
                       myDataSocket.close();
                       myDataSocket = new MyStreamSocket(myConnectionSocket.accept());
                  } else
                        if(message.equalsIgnoreCase("stop")) {
                        //shutdown server
                        myDataSocket.sendMessage("Server shutting down...");
                        myDataSocket.close();
                        myConnectionSocket.close();
                        return;
                    } else {
                        int i = 0;
                        try {
                            i = Integer.parseInt(message);
                        } catch(Exception e) {
                            myDataSocket.sendMessage("ERR");
                        }
                        System.out.println("i = " + i);
                        if ( i > 0) {
                            String line;
                            while (reader.readLine() != null) {
                                line = reader.readLine();
                                if (n == i) {
                                    myDataSocket.sendMessage(line);
                                    break;
                                } else {
                                    n++;
                                }
                            }
                        } else {
                            myDataSocket.sendMessage("ERR");
                        }
                    }
                } // end while !done
            } // end while forever
        } // end try
        catch (Exception ex) {
            ex.printStackTrace();
        }
    } // end main

    public static int countLinesNew(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];

            int readChars = is.read(c);
            if (readChars == -1) {
                // bail out if nothing to read
                return 0;
            }

            // make it easy for the optimizer to tune this loop
            int count = 0;
            while (readChars == 1024) {
                for (int i=0; i<1024;) {
                    if (c[i++] == '\n') {
                        ++count;
                    }
                }
                readChars = is.read(c);
            }

            // count remaining characters
            while (readChars != -1) {
                System.out.println(readChars);
                for (int i=0; i<readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
                readChars = is.read(c);
            }

            return count == 0 ? 1 : count;
        } finally {
            is.close();
        }
    }
} // end class