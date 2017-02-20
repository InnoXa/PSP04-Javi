package Cliente;

import Partida.Jugar;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.*;

public class InicioCliente extends JFrame{
    private static Socket cliente; //Socket para conectarse con el cliente
    private static String ip = "127.0.0.1"; //ip a la cual se conecta

    public static void main(String[] args) {

        Jugar tablero = new Jugar();
        tablero.setLayout(null);
        tablero.setVisible(true);

        ExecutorService executor = Executors.newCachedThreadPool();
        
        tablero.jButtonPreparado.setEnabled(false);

        try {
            tablero.setTitle("Buscando Servidor ...");
            cliente = new Socket(InetAddress.getByName(ip), 11111);
            tablero.setTitle("Conectado a :" + cliente.getInetAddress().getHostName() + " | Cliente: Cliente");

            executor.execute(new Cliente.ThreadRecibe(cliente, tablero));
            executor.execute(new Cliente.ThreadEnvia(cliente, tablero));

        } catch (IOException ex) {

        } finally {

        }
        executor.shutdown();
    }
}
