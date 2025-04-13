import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <p>
 * Materialien zu den zentralen NRW-Abiturpruefungen im Fach Informatik ab 2018
 * </p>
 * <p>
 * Klasse Server
 * </p>
 * <p>
 * Objekte von Unterklassen der abstrakten Klasse Server ermoeglichen das
 * Anbieten von Serverdiensten, so dass Clients Verbindungen zum Server mittels
 * TCP/IP-Protokoll aufbauen koennen. Zur Vereinfachung finden Nachrichtenversand
 * und -empfang zeilenweise statt, d. h., beim Senden einer Zeichenkette wird ein
 * Zeilentrenner ergaenzt und beim Empfang wird dieser entfernt.
 * Verbindungsannahme, Nachrichtenempfang und Verbindungsende geschehen
 * nebenlaeufig. Auf diese Ereignisse muss durch Ueberschreiben der entsprechenden
 * Ereignisbehandlungsmethoden reagiert werden. Es findet nur eine rudimentaere
 * Fehlerbehandlung statt, so dass z.B. Verbindungsabbrueche nicht zu einem
 * Programmabbruch fuehren. Einmal unterbrochene oder getrennte Verbindungen
 * koennen nicht reaktiviert werden.
 * </p>
 *
 * @author Qualitaets- und UnterstuetzungsAgentur - Landesinstitut fuer Schule (Bearbeitet)
 * @version 13.04.2025
 */


public abstract class Server {

    private List<ClientMessageHandler> messageHandlers;
    private NewConnectionHandler connectionHandler;

    public Server(int pPort) {
        connectionHandler = new NewConnectionHandler(pPort);
        messageHandlers = new List<>();
    }

    public void send(String pClientIP, int pClientPort, String pMessage) {
        ClientMessageHandler clientMessageHandler = this.findClientMessageHandler(pClientIP, pClientPort);
        if (clientMessageHandler != null) clientMessageHandler.send(pMessage);
    }

    public void sendToAll(String pMessage) {
        synchronized (messageHandlers) {
            messageHandlers.toFirst();
            while (messageHandlers.hasAccess()) {
                messageHandlers.getContent().send(pMessage);
                messageHandlers.next();
            }
        }
    }

    public void closeConnection(String pClientIP, int pClientPort) {
        ClientMessageHandler clientMessageHandler = findClientMessageHandler(pClientIP, pClientPort);
        if (clientMessageHandler != null) {
            synchronized (messageHandlers) {
                processClosingConnection(pClientIP, pClientPort);
            }
            clientMessageHandler.close();
            removeClientMessageHandler(clientMessageHandler);
        }
    }

    public void close() {
        connectionHandler.close();
        synchronized (messageHandlers) {
            ClientMessageHandler clientMessageHandler;
            messageHandlers.toFirst();
            while (messageHandlers.hasAccess()) {
                clientMessageHandler = messageHandlers.getContent();
                processClosingConnection(clientMessageHandler.getClientIP(), clientMessageHandler.getClientPort());
                clientMessageHandler.close();
                messageHandlers.remove();
            }
        }
    }

    public boolean isConnectedTo(String pClientIP, int pClientPort) {
        ClientMessageHandler clientMessageHandler = findClientMessageHandler(pClientIP, pClientPort);
        return clientMessageHandler != null && clientMessageHandler.active;
    }

    public boolean isOpen() {
        return connectionHandler.active;
    }

    public abstract void processNewConnection(String pClientIP, int pClientPort);

    public abstract void processMessage(String pClientIP, int pClientPort, String pMessage);

    public abstract void processClosingConnection(String pClientIP, int pClientPort);

    private void addNewClientMessageHandler(Socket pClientSocket) {
        synchronized (messageHandlers) {
            messageHandlers.append(new Server.ClientMessageHandler(pClientSocket));
        }
    }

    private void removeClientMessageHandler(ClientMessageHandler pClientMessageHandler) {
        synchronized (messageHandlers) {
            messageHandlers.toFirst();
            while (messageHandlers.hasAccess()) {
                if (pClientMessageHandler == messageHandlers.getContent()) {
                    messageHandlers.remove();
                    return;
                } else messageHandlers.next();
            }
        }
    }

    private ClientMessageHandler findClientMessageHandler(String pClientIP, int pClientPort) {
        synchronized (messageHandlers) {
            ClientMessageHandler clientMessageHandler;
            messageHandlers.toFirst();
            while (messageHandlers.hasAccess()) {
                clientMessageHandler = messageHandlers.getContent();
                if (clientMessageHandler.getClientIP().equals(pClientIP) && clientMessageHandler.getClientPort() == pClientPort)
                    return clientMessageHandler;
                messageHandlers.next();
            }
            return null;
        }
    }

    private class NewConnectionHandler extends Thread {

        private ServerSocket serverSocket;
        private boolean active;

        public NewConnectionHandler(int pPort) {
            try {
                serverSocket = new ServerSocket(pPort);
                start();
                active = true;
            } catch (Exception e) {
                serverSocket = null;
                active = false;
            }
        }

        public void run() {
            while (active) {
                try {
                    //Warten auf Verbdinungsversuch durch Client:
                    Socket clientSocket = serverSocket.accept();
                    // Eingehende Nachrichten vom neu verbundenen Client werden
                    // in einem eigenen Thread empfangen:
                    addNewClientMessageHandler(clientSocket);
                    synchronized (messageHandlers) {
                        processNewConnection(clientSocket.getInetAddress().getHostAddress(), clientSocket.getPort());
                    }
                } catch (IOException e) {
                    /*
                     * Kann keine Verbindung zum anfragenden Client aufgebaut werden,
                     * geschieht nichts.
                     */
                }
            }
        }

        public void close() {
            active = false;
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    /*
                     * Befindet sich der ServerSocket im accept()-Wartezustand oder wurde
                     * er bereits geschlossen, geschieht nichts.
                     */
                }
            }
        }
    }

    private class ClientMessageHandler extends Thread {

        private ClientSocketWrapper socketWrapper;
        private boolean active;

        private ClientMessageHandler(Socket pClientSocket) {
            socketWrapper = new ClientSocketWrapper(pClientSocket);
            if (pClientSocket != null) {
                start();
                active = true;
            } else {
                active = false;
            }
        }

        public void run() {
            String message;
            while (active) {
                message = socketWrapper.receive();
                if (message != null) {
                    synchronized (messageHandlers) {
                        processMessage(socketWrapper.getClientIP(), socketWrapper.getClientPort(), message);
                    }
                } else {
                    ClientMessageHandler clientMessageHandler = findClientMessageHandler(socketWrapper.getClientIP(), socketWrapper.getClientPort());
                    if (clientMessageHandler != null) {
                        clientMessageHandler.close();
                        removeClientMessageHandler(clientMessageHandler);
                        synchronized (messageHandlers) {
                            processClosingConnection(socketWrapper.getClientIP(), socketWrapper.getClientPort());
                        }
                    }
                }
            }
        }

        public void send(String pMessage) {
            if (active) socketWrapper.send(pMessage);
        }

        public void close() {
            if (active) {
                active = false;
                socketWrapper.close();
            }
        }

        public String getClientIP() {
            return (socketWrapper.getClientIP());
        }

        public int getClientPort() {
            return (socketWrapper.getClientPort());
        }

        private class ClientSocketWrapper {

            private Socket clientSocket;
            private BufferedReader fromClient;
            private PrintWriter toClient;

            public ClientSocketWrapper(Socket pSocket) {
                try {
                    clientSocket = pSocket;
                    toClient = new PrintWriter(clientSocket.getOutputStream(), true);
                    fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                } catch (IOException e) {
                    clientSocket = null;
                    toClient = null;
                    fromClient = null;
                }
            }

            public String receive() {
                if (fromClient != null)
                    try {
                        return fromClient.readLine();
                    } catch (IOException e) {
                    }
                return (null);
            }

            public void send(String pMessage) {
                if (toClient != null) {
                    toClient.println(pMessage);
                }
            }

            public String getClientIP() {
                return clientSocket != null ? clientSocket.getInetAddress().getHostAddress() : null; //Gemaess Java-API Rueckgabe bei nicht-verbundenen Sockets
            }

            public int getClientPort() {
                return clientSocket != null ? clientSocket.getPort() : 0; //Gemaess Java-API Rueckgabe bei nicht-verbundenen Sockets
            }

            public void close() {
                if (clientSocket != null) {
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        /*
                         * Falls eine Verbindung getrennt werden soll, deren Endpunkt
                         * nicht mehr existiert bzw. ihrerseits bereits beendet worden ist,
                         * geschieht nichts.
                         */
                    }
                }
            }

        }

    }

}
