package Cliente;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketException;
import Partida.Jugar;

public class ThreadEnvia implements Runnable {

    private final Jugar main;
    private ObjectOutputStream salida;
    private Socket conexion;

    public ThreadEnvia(Socket conexion, final Jugar main) {
        this.conexion = conexion;
        this.main = main;

        main.jButtonDisparar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int x = main.getXBoton();
                int y = main.getYBoton();

                main.jLabelMiTurno.setVisible(false);
                main.jLabelTurnoEnemigo.setVisible(true);

                enviarDatos(x, y, main.disparar(x, y));

                main.jButtonDisparar.setEnabled(false);
            }
        });

        main.jButtonPreparado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                main.jLabelTurnoEnemigo.setVisible(true);
                enviarDatosComienzo();
            }
        });
    }

    private void enviarDatosComienzo() {
        main.jButtonPreparado.setEnabled(false);

        try {
            salida.writeObject(main.getMatrizTablero());
            salida.flush();
        } catch (IOException ioe) {
            System.out.println("Error escribiendo Mensaje");
        }
    }

    private void enviarDatos(int x, int y, int zona) {

        int[][] coordenadas1 = main.getMatrizTablero();

        int[] coordenadas2 = new int[3];

        coordenadas2[0] = x;
        coordenadas2[1] = y;
        coordenadas2[2] = zona;

        try {
            salida.writeObject(coordenadas1);
            salida.writeObject(coordenadas2);
            salida.flush();
        } catch (IOException ioe) {
            System.out.println("Error escribiendo Mensaje");
        }

    } //Fin methodo enviarDatos

    public void run() {
        try {
            salida = new ObjectOutputStream(conexion.getOutputStream());
            salida.flush();
        } catch (SocketException ex) {
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (NullPointerException ex) {
        }
    }

}
