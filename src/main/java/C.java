import java.io.*;
import java.net.InetAddress;

public class C {

    public static void main(String[] args) {
        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(is);


        try {
            String hostName = "localhost";
            final int portNum = 10322;
            MyStreamSocket socket = new MyStreamSocket(InetAddress.getByName(hostName), portNum);
            boolean done = false;
            String echo;
            while(!done) {
                String message = br.readLine();
                if(message.equalsIgnoreCase("quit")) {
                    socket.sendMessage(message);
                    socket.close();
                    done = true;
                } else if(message.equalsIgnoreCase("stop")) {
                    socket.sendMessage(message);
                    socket.close();
                    done = true;
                } else if (message.toUpperCase().startsWith("GET")) {
                    message = message.substring(message.indexOf(" ")+1);
                    if(message.matches("-?\\d+(\\.\\d+)?")) { //check if there is a line number
                        socket.sendMessage(message);
                        // get reply from server
                        echo = socket.receiveMessage(); 
                        System.out.println(echo);
                    } else {
                        System.out.println("ERR");
                    }
                } else {
                    System.out.println("ERR");
                }
            } // end while
        } // end try
        catch (Exception ex) {
            ex.printStackTrace();
        } // end catch
    } // end main
} // end class