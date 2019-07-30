import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class MyStreamSocket {
    private Socket socket;

    private DataInputStream input;

    private DataOutputStream output;


    MyStreamSocket(InetAddress acceptorHost, int acceptorPort)
            throws IOException {

        socket = new Socket(acceptorHost, acceptorPort);
        setStreams();
    }


    MyStreamSocket(Socket socket) throws IOException {
        this.socket = socket;
        setStreams();
    }



    private void setStreams() throws IOException {
        // get an input stream for reading from the data socket
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());
    }


    public void sendMessage(String message) throws IOException {
        output.writeUTF(message);
        output.flush();
    } // end sendMessage


    public String receiveMessage() throws IOException {
        String message = input.readUTF();
        return message;
    } // end receiveMessage

    public void close() throws IOException {
        socket.close();
    }

} // end class