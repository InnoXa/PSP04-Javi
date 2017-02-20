package Servidor;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import Partida.Jugar;

public class ThreadRecibe implements Runnable {

    private final Jugar main;
    int[] dato;
    private ObjectInputStream entrada;
    private Socket cliente;

    private boolean bandera;

    //Inicializar chatServer y configurar GUI
    public ThreadRecibe(Socket cliente, Jugar main) {
        this.cliente = cliente;
        this.main = main;
        bandera = true;
    }

    public void run() {
        try {
            entrada = new ObjectInputStream(cliente.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(ThreadRecibe.class.getName()).log(Level.SEVERE, null, ex);
        }
        do { //procesa los mensajes enviados dsd el servidor
            try {//leer el mensaje y mostrarlo 

                main.actualizarBarcosEne((int[][]) entrada.readObject());

                if (!bandera) {
                    dato = (int[]) entrada.readObject();

                    main.recibirDisparo(dato[0], dato[1], dato[2]);

                    main.turnoServidor();
                } else {
                    main.jButtonDisparar.setEnabled(true);
                    bandera = false;
                }
                main.jButtonDisparar.setEnabled(true);
                main.jLabelMiTurno.setVisible(true);
                main.jLabelTurnoEnemigo.setVisible(false);
            } //fin try
            catch (SocketException ex) {
            } catch (EOFException eofException) {
                break;
            } //fin catch
            catch (IOException ex) {
                Logger.getLogger(ThreadRecibe.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException classNotFoundException) {
            } //fin catch               

        } while (true); //Ejecuta hasta que el server escriba TERMINATE

        try {
            entrada.close(); //cierra input Stream
            cliente.close(); //cieraa Socket
        } //Fin try
        catch (IOException ioException) {
            ioException.printStackTrace();
        } //fin catch
        System.exit(0);
    }
}
