package Servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import Partida.Jugar;

public class InicioServidor{
    private static ServerSocket servidor; //
    private static Socket conexion; //Socket para conectarse con el cliente
    private static String ip = "127.0.0.1"; //ip a la cual se conecta
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Jugar pantalla = new Jugar();
        pantalla.setLayout(null);
        pantalla.setVisible(true);
        
        //pantalla.decidirTurno();
        
        ExecutorService executor = Executors.newCachedThreadPool(); //Para correr los threads
        
        try {
            servidor = new ServerSocket(11111, 100); 
            pantalla.setTitle("Esperando Cliente ...");

            while (true){
                try {
                    conexion = servidor.accept(); //Permite al servidor aceptar conexiones
                    
                    pantalla.setTitle("Conectado a : " + conexion.getInetAddress().getHostName() + " | Anfitrion: Servidor");
                    
                    executor.execute(new Servidor.ThreadEnvia(conexion, pantalla));
                    executor.execute(new Servidor.ThreadRecibe(conexion, pantalla));
                } catch (IOException ex) {
                
                }
            }
        } catch (IOException ex) {
        
        }
        finally {
        }
        executor.shutdown();
    }
}
