package it.ingsoftw.progetto.server;

import java.io.*;
import java.net.*;
import java.rmi.server.*;


public class ReverseClientSocket {

    static RMIClientSocketFactory clientSocketFactory;
    static RMIServerSocketFactory serverSocketFactory;


    static ServerSocket reverseSocketListener;

    public static void initializeServer() {
        try {
            reverseSocketListener = new ServerSocket(ServerConfig.reversePort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static RMIServerSocketFactory getServerSocketFactory() {
        if (serverSocketFactory == null)
            serverSocketFactory = new ReverseServerSocketFactory();
        return serverSocketFactory;
    }

    public static RMIClientSocketFactory getClientSocketFactory() {
        if (clientSocketFactory == null)
            clientSocketFactory = new ReverseClientSocketFactory();
        return clientSocketFactory;
    }

    public static class ReverseClientSocketFactory
            implements RMIClientSocketFactory, Serializable {

        public ReverseClientSocketFactory() {
        }

        public Socket createSocket(String host, int port)
                throws IOException
        {
            return reverseSocketListener.accept();
        }

        public int hashCode() {
            return 0;
        }

        public boolean equals(Object obj) {
            return (getClass() == obj.getClass());
        }
    }


    public static class ReverseServerSocket extends ServerSocket {

        private String hostname;
        private int port;

        public ReverseServerSocket(String hostname, int port) throws IOException {

            this.hostname = hostname;
            this.port = port;
        }

        public Socket accept() throws IOException {
            return new Socket(hostname, port);
        }
    }

    public static class ReverseServerSocketFactory
            implements RMIServerSocketFactory {

        private static ReverseServerSocket reverseServerSocket;

        public ReverseServerSocketFactory() {
        }

        public ServerSocket createServerSocket(int port)
                throws IOException
        {
            if (reverseServerSocket == null)
                reverseServerSocket = new ReverseServerSocket(ServerConfig.hostname, ServerConfig.reversePort);

            return reverseServerSocket;
        }

        public int hashCode() {
            return 0;
        }

        public boolean equals(Object obj) {
            return (getClass() == obj.getClass());
        }
    }
}
