package test;

import java.io.InputStreamReader;
import java.io.BufferedReader;

public class SimpleClient extends Client {

    /**
     * SimpleClient Konstruktor erstellt einen einfachen Client,
     * mit dem man Befehle und die darauffolgenden Reaktionen des Servers testen kann.
     * 
     * Die Befehle schreibt man dafür in die Konsole, welche wiederum an den Server weiterleitet.
     *
     * @param port der Port des Servers
     */
    public SimpleClient(int port) {
        super("localhost", port);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String line;
        try {
            while ((line = reader.readLine()) != null && isConnected()) {
                send(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processMessage(String pMessage) {
        System.out.println(pMessage);
    }

}
