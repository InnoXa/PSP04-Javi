/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Partida;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author alumno
 */
//Implementamos actionlistener para botones y runnable para el desarrollo de los hilos
public class Jugar extends javax.swing.JFrame implements ActionListener, Runnable {

    int[][] miTablero, tableroEnemigo;
    static final int FILAS = 3;
    static final int COL = 3;
    static final int BARCOS = 3;
    
    private Thread principal;
    
    //Matrices de botones del tablero
    JButton[][] botonesMiArea;
    JButton[][] botonesAreaEnemiga;
    
    //Coordenadas de los disparos hecho por el jugador.
    int x, y;
    //Contador de barcos que quedan en mi tablero
    int misBarcos;
    //Contador de barcos enemigos que quedan en el tablero
    int barcosEnemigo;
    //Contador que usaremos para la generacion de barcos.
    int contadorDeBarcos;
    
    //Controladores de turno, por defecto empieza el jugador que es el servidor.
    boolean turnoJugServidor = true;
    boolean turnoJugCliente = false;
    
    public Jugar() {
        initComponents();
        
        this.setVisible(true);
        pack();
        
        //Inicializamos los tableros
        miTablero = new int[FILAS][COL];
        tableroEnemigo = new int[FILAS][COL];
        
        misBarcos = BARCOS;
        barcosEnemigo = BARCOS;
        contadorDeBarcos = BARCOS;
        
        //Controlamos que los jlabel muestren la cantidad de barco correspondiente
        jLabelBarcosEnemigo.setText(String.valueOf(barcosEnemigo));
        jLabelMisBarcos.setText(String.valueOf(misBarcos));
        
        //Inicializamos ambas matrices de botones
        botonesMiArea = new JButton[][]{{BmiTableroA1, BmiTableroB1, BmiTableroC1},
                         {BmiTableroA2, BmiTableroB2, BmiTableroC2},
                         {BmiTableroA3, BmiTableroB3, BmiTableroC3}};
        
        botonesAreaEnemiga = new JButton[][]{{BenemigoA1, BenemigoB1, BenemigoC1},
                         {BenemigoA2, BenemigoB2, BenemigoC2},
                         {BenemigoA3, BenemigoB3, BenemigoC3}};
        
        //Añadimos los eventos a los botones del enemigo, seran los unicos con los
        //que podremos interactuar
        this.BenemigoA1.addActionListener(this);
        this.BenemigoB1.addActionListener(this);
        this.BenemigoC1.addActionListener(this);
        this.BenemigoA2.addActionListener(this);
        this.BenemigoB2.addActionListener(this);
        this.BenemigoC2.addActionListener(this);
        this.BenemigoA3.addActionListener(this);
        this.BenemigoB3.addActionListener(this);
        this.BenemigoC3.addActionListener(this);
        
        //Inicializamos las variables de las coordenadas de los disparos
        x=0;
        y=0;
        
        inicializarAgua();
        generarBarcos();
        
        //Deshabilitamos el boton disparar y los label indicadores de los turnos.
        jButtonDisparar.setEnabled(false);
        jLabelMiTurno.setVisible(false);
        jLabelTurnoEnemigo.setVisible(false);
        //Instanciamos el hilo
        principal = new Thread(this);
        //Iniciamos el hilo
        principal.start();
    }
    
    //Metodo que usaremos en el hilo envia para mandarle el tablero al enemigo
    public int[][] getMatrizTablero(){
        return miTablero;
    }
    
    
    void inicializarAgua(){
        for(int i=0; i<FILAS; i++){
            for(int j=0; j<COL; j++){
                miTablero[i][j] = 0;
                tableroEnemigo[i][j] = 0;
            }
        }
    }
    
    void generarBarcos(){
        //Usaremos un aleatorio que nos de valores permitidos dentro de la matriz
        Random rand = new Random();
        int i, j;
        //Mientras no se hayan colocado todos los barcos, seguiremos colocandolos
        while(contadorDeBarcos > 0){
            //Mientras haya barco en la posicion, volveremos a buscar otra.
            do{
                i = rand.nextInt(FILAS);
                j = rand.nextInt(COL);
            }while(miTablero[i][j]==1);
            //Le asignamos un numero 1, indicativo de que existe un barco en 
            //esa posicion
            miTablero[i][j] = 1;
            //PINTAMOS EL BARCO (LO DEJAMOS INDICADO EN LA MATRIZ)
            pintarBarcos(i, j);
            contadorDeBarcos--;
        }
    }
    
    
    void pintarBarcos(int i, int j){
        try{
            botonesMiArea[i][j].setIcon(new ImageIcon(ImageIO.read(new File("src/Imagenes/barco30.png"))));
        }catch(IOException ex){
            System.err.println("Error al pintar los barcos: "+ ex.getMessage());
            System.exit(0);
        }
    }

    //Metodo para actualizar el area
    public void actualizarMiArea(){
        int i, j;
        
        for(i = 0; i<FILAS; i++){
            for(j=0; j<COL; j++){
                switch(miTablero[i][j]){
                    case 1:
                        try{
                            botonesMiArea[i][j].setIcon(new ImageIcon(ImageIO.read(new File("src/Imagenes/barco30.png"))));
                        }catch(IOException ex){
                            System.err.println(ex.getMessage());
                            System.exit(0);
                        }
                        break;
                    case 2:
                        try{
                            botonesMiArea[i][j].setIcon(new ImageIcon(ImageIO.read(new File("src/Imagenes/agua30.png"))));
                        }catch(IOException ex){
                            System.err.println(ex.getMessage());
                            System.exit(0);
                        }
                        break;
                    case 3:
                        try{
                            botonesMiArea[i][j].setIcon(new ImageIcon(ImageIO.read(new File("src/Imagenes/explosion30.png"))));
                        }catch(IOException ex){
                            System.err.println(ex.getMessage());
                            System.exit(0);
                        }
                        break;
                    default:
                        try{
                            botonesMiArea[i][j].setIcon(new ImageIcon(ImageIO.read(new File("src/Imagenes/agua30.png"))));
                        }catch(IOException ex){
                            System.err.println(ex.getMessage());
                            System.exit(0);
                        }
                        break;  
                }
            }
        }  
    }
    
    
    //Metodo para actualizar el area
    public void actualizarAreaEnemiga(){
        int i, j;
        
        for(i = 0; i<FILAS; i++){
            for(j=0; j<COL; j++){
                switch(tableroEnemigo[i][j]){
                    case 2:
                        try{
                            botonesAreaEnemiga[i][j].setIcon(new ImageIcon(ImageIO.read(new File("src/Imagenes/agua30.png"))));
                        }catch(IOException ex){
                            System.err.println(ex.getMessage());
                            System.exit(0);
                        }
                        break;
                    case 3:
                        try{
                            botonesAreaEnemiga[i][j].setIcon(new ImageIcon(ImageIO.read(new File("src/Imagenes/explosion30.png"))));
                        }catch(IOException ex){
                            System.err.println(ex.getMessage());
                            System.exit(0);
                        }
                        break;
                    default:
                        try{
                            botonesAreaEnemiga[i][j].setIcon(new ImageIcon(ImageIO.read(new File("src/Imagenes/inicial30.png"))));
                        }catch(IOException ex){
                            System.err.println(ex.getMessage());
                            System.exit(0);
                        }
                        break;  
                }
            }
        }  
    }
    
    
    public void actualizarMisBarcos(int [][] misBarcos){
        miTablero = misBarcos;
    }
    
    
    public void actualizarBarcosEne(int [][] barcosEne){
        tableroEnemigo = barcosEne;
    }
    
    public int getXBoton(){
        return x;
    }
    
    public int getYBoton(){
        return y;
    }
    
    public void turnoServidor() {
        turnoJugServidor = true;
        turnoJugCliente = false;
    }

    public void turnoCliente() {
        turnoJugServidor = false;
        turnoJugCliente = true;
    }
    
    
    public int disparar(int x, int y) {
        switch (tableroEnemigo[x][y]) {
            //Si hay agua, indicamos zona dispara sin impacto (agua)
            case 0:
                tableroEnemigo[x][y] = 2;
                break;
            //Si hay un barco, indicamos impacto (3)
            case 1:
                tableroEnemigo[x][y] = 3;
                //Bajamos el numero de barcos del enemigo
                jLabelBarcosEnemigo.setText(String.valueOf(--barcosEnemigo));
        }
        //Modificamos los iconos (si tuviese que hacerlo)
        actualizarAreaEnemiga();
        //Devolvemos el nuevo tablero
        return tableroEnemigo[x][y];
    }

    public void recibirDisparo(int x, int y, int zona) {
        miTablero[x][y] = zona;
        
        if(zona == 3){
            jLabelMisBarcos.setText(String.valueOf(--misBarcos));
        } 
        actualizarMiArea();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == BenemigoA1) {
            x = 0;
            y = 0;
        } else if (e.getSource() == BenemigoB1) {
            x = 0;
            y = 1;
        } else if (e.getSource() == BenemigoC1) {
            x = 0;
            y = 2;
        } else if (e.getSource() == BenemigoA2) {
            x = 1;
            y = 0;
        } else if (e.getSource() == BenemigoB2) {
            x = 1;
            y = 1;
        } else if (e.getSource() == BenemigoC2) {
            x = 1;
            y = 2;
        } else if (e.getSource() == BenemigoA3) {
            x = 2;
            y = 0;
        } else if (e.getSource() == BenemigoB3) {
            x = 2;
            y = 1;
        } else if (e.getSource() == BenemigoC3) {
            x = 2;
            y = 2;
        }
    }

    @Override
    public void run() {
        String mensaje;

        while (misBarcos > 0 && barcosEnemigo > 0) {
            System.out.println("\nMis barcos: "+misBarcos);
            System.out.println("Barcos rivales: "+barcosEnemigo);
        }

        if (misBarcos == 0) {
            mensaje = "Has perdido.";
        } else {
            mensaje = "Has ganado.";
        }

        JOptionPane.showMessageDialog(null, mensaje);
        System.exit(0);
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        BenemigoA1 = new javax.swing.JButton();
        BenemigoB1 = new javax.swing.JButton();
        BenemigoC1 = new javax.swing.JButton();
        BenemigoA2 = new javax.swing.JButton();
        BenemigoB2 = new javax.swing.JButton();
        BenemigoC2 = new javax.swing.JButton();
        BenemigoA3 = new javax.swing.JButton();
        BenemigoB3 = new javax.swing.JButton();
        BenemigoC3 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabelBarcosEnemigo = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabelMisBarcos = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        BmiTableroB1 = new javax.swing.JButton();
        BmiTableroC1 = new javax.swing.JButton();
        BmiTableroA1 = new javax.swing.JButton();
        BmiTableroA2 = new javax.swing.JButton();
        BmiTableroB2 = new javax.swing.JButton();
        BmiTableroC2 = new javax.swing.JButton();
        BmiTableroA3 = new javax.swing.JButton();
        BmiTableroB3 = new javax.swing.JButton();
        BmiTableroC3 = new javax.swing.JButton();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jButtonPreparado = new javax.swing.JButton();
        jButtonDisparar = new javax.swing.JButton();
        jLabelTurnoEnemigo = new javax.swing.JLabel();
        jLabelMiTurno = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 0, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("TABLERO ENEMIGO");

        BenemigoA1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/inicial30.png"))); // NOI18N
        BenemigoA1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51)));

        BenemigoB1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/inicial30.png"))); // NOI18N
        BenemigoB1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51)));

        BenemigoC1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/inicial30.png"))); // NOI18N
        BenemigoC1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51)));

        BenemigoA2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/inicial30.png"))); // NOI18N
        BenemigoA2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51)));

        BenemigoB2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/inicial30.png"))); // NOI18N
        BenemigoB2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51)));

        BenemigoC2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/inicial30.png"))); // NOI18N
        BenemigoC2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51)));

        BenemigoA3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/inicial30.png"))); // NOI18N
        BenemigoA3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51)));

        BenemigoB3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/inicial30.png"))); // NOI18N
        BenemigoB3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51)));

        BenemigoC3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/inicial30.png"))); // NOI18N
        BenemigoC3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51)));

        jLabel2.setText("A");

        jLabel3.setText("B");

        jLabel4.setText("C");

        jLabel12.setText("1");

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("BARCOS ENEMIGOS");

        jLabelBarcosEnemigo.setText("3");

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("TUS BARCOS");

        jLabelMisBarcos.setText("3");

        jLabel26.setText("B");

        jLabel27.setText("C");

        jLabel37.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(0, 255, 0));
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("TU TABLERO");

        jLabel46.setText("A");

        BmiTableroB1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/agua30.png"))); // NOI18N
        BmiTableroB1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 255)));
        BmiTableroB1.setEnabled(false);

        BmiTableroC1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/agua30.png"))); // NOI18N
        BmiTableroC1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 255)));
        BmiTableroC1.setEnabled(false);

        BmiTableroA1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/agua30.png"))); // NOI18N
        BmiTableroA1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 255)));
        BmiTableroA1.setEnabled(false);

        BmiTableroA2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/agua30.png"))); // NOI18N
        BmiTableroA2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 255)));
        BmiTableroA2.setEnabled(false);

        BmiTableroB2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/agua30.png"))); // NOI18N
        BmiTableroB2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 255)));
        BmiTableroB2.setEnabled(false);

        BmiTableroC2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/agua30.png"))); // NOI18N
        BmiTableroC2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 255)));
        BmiTableroC2.setEnabled(false);

        BmiTableroA3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/agua30.png"))); // NOI18N
        BmiTableroA3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 255)));
        BmiTableroA3.setEnabled(false);

        BmiTableroB3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/agua30.png"))); // NOI18N
        BmiTableroB3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 255)));
        BmiTableroB3.setEnabled(false);

        BmiTableroC3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/agua30.png"))); // NOI18N
        BmiTableroC3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 255)));
        BmiTableroC3.setEnabled(false);

        jLabel35.setText("2");

        jLabel36.setText("3");

        jLabel38.setText("1");

        jLabel39.setText("2");

        jLabel40.setText("3");

        jButtonPreparado.setText("¿Preparado?");
        jButtonPreparado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPreparadoActionPerformed(evt);
            }
        });

        jButtonDisparar.setText("DISPARAR");

        jLabelTurnoEnemigo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelTurnoEnemigo.setForeground(new java.awt.Color(255, 0, 0));
        jLabelTurnoEnemigo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTurnoEnemigo.setText("TURNO ENEMIGO");

        jLabelMiTurno.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelMiTurno.setForeground(new java.awt.Color(0, 255, 0));
        jLabelMiTurno.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelMiTurno.setText("TU TURNO");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonDisparar)
                .addGap(127, 127, 127))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addComponent(jLabelBarcosEnemigo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(jLabelMisBarcos)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(BenemigoA1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(BenemigoB1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(BenemigoC1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(jLabel2)
                                .addGap(29, 29, 29)
                                .addComponent(jLabel3)
                                .addGap(34, 34, 34)
                                .addComponent(jLabel4))
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(BenemigoA2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(BenemigoB2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(BenemigoC2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(BenemigoA3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(BenemigoB3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(BenemigoC3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabelTurnoEnemigo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(57, 57, 57)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(56, 56, 56)
                                .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(BmiTableroA2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(BmiTableroB2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(BmiTableroC2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(BmiTableroA3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(BmiTableroB3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(BmiTableroC3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(BmiTableroA1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(BmiTableroB1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(BmiTableroC1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel46)
                                        .addGap(29, 29, 29)
                                        .addComponent(jLabel26)
                                        .addGap(24, 24, 24)
                                        .addComponent(jLabel27)))
                                .addGap(18, 18, 18)))
                        .addGap(0, 28, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButtonPreparado, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(jLabelMiTurno, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(33, 33, 33))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel37)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel46)
                            .addComponent(jLabel26)
                            .addComponent(jLabel27))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(BmiTableroC1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(BmiTableroB1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(BmiTableroA1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(jLabel38, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(BmiTableroA2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BmiTableroB2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BmiTableroC2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(BmiTableroA3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BmiTableroB3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BmiTableroC3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                            .addComponent(BenemigoA1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(BenemigoB1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(BenemigoC1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(BenemigoA2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BenemigoB2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BenemigoC2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(BenemigoA3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BenemigoB3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BenemigoC3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelBarcosEnemigo))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelMisBarcos)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonPreparado)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTurnoEnemigo)
                    .addComponent(jLabelMiTurno))
                .addGap(16, 16, 16)
                .addComponent(jButtonDisparar)
                .addGap(31, 31, 31))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonPreparadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPreparadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonPreparadoActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BenemigoA1;
    private javax.swing.JButton BenemigoA2;
    private javax.swing.JButton BenemigoA3;
    private javax.swing.JButton BenemigoB1;
    private javax.swing.JButton BenemigoB2;
    private javax.swing.JButton BenemigoB3;
    private javax.swing.JButton BenemigoC1;
    private javax.swing.JButton BenemigoC2;
    private javax.swing.JButton BenemigoC3;
    private javax.swing.JButton BmiTableroA1;
    private javax.swing.JButton BmiTableroA2;
    private javax.swing.JButton BmiTableroA3;
    private javax.swing.JButton BmiTableroB1;
    private javax.swing.JButton BmiTableroB2;
    private javax.swing.JButton BmiTableroB3;
    private javax.swing.JButton BmiTableroC1;
    private javax.swing.JButton BmiTableroC2;
    private javax.swing.JButton BmiTableroC3;
    public javax.swing.JButton jButtonDisparar;
    public javax.swing.JButton jButtonPreparado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel46;
    public javax.swing.JLabel jLabelBarcosEnemigo;
    public javax.swing.JLabel jLabelMiTurno;
    public javax.swing.JLabel jLabelMisBarcos;
    public javax.swing.JLabel jLabelTurnoEnemigo;
    // End of variables declaration//GEN-END:variables
}
