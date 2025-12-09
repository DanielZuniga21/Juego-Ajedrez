/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectro.ajedrez;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author zunig
 */
public class Juego {

    Pieza[][] tablero;
    char turno;
    Scanner entrada;
    ArrayList<String> capturadas;
    boolean ejecutando;

    public Juego() {
        tablero = new Pieza[8][8];
        Scanner entrada = new Scanner(System.in);
        ArrayList<String> capturadas = new ArrayList<>();
        ejecutando = true;
        inicializarTablero();
        turno = 'W';

    }
    //vaciando tablero

    private void inicializarTablero() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tablero[i][j] = null;

            }
        }

        //Piezas peon
        for (int j = 0; j < 8; j++) {
            tablero[6][j] = new Pieza('P', 'W');
            tablero[1][j] = new Pieza('P', 'b');

        }

        //Piezas torres
        tablero[7][0] = new Pieza('R', 'W');
        tablero[0][0] = new Pieza('R', 'b');
        tablero[7][7] = new Pieza('R', 'W');
        tablero[0][7] = new Pieza('R', 'b');

        //Piezas caballos
        tablero[7][1] = new Pieza('N', 'W');
        tablero[0][1] = new Pieza('N', 'b');
        tablero[7][6] = new Pieza('N', 'W');
        tablero[0][6] = new Pieza('N', 'b');

        //Piezas alfiles
        tablero[7][2] = new Pieza('B', 'W');
        tablero[0][2] = new Pieza('B', 'b');
        tablero[7][5] = new Pieza('B', 'W');
        tablero[0][5] = new Pieza('B', 'b');

        //Piezas Reinas 
        tablero[7][3] = new Pieza('Q', 'W');
        tablero[0][3] = new Pieza('B', 'b');

        //Piezas Reyes
        tablero[7][4] = new Pieza('K', 'W');
        tablero[0][4] = new Pieza('K', 'b');

    }

    public void iniciar() {
        System.out.println("---Bienvenido al Juego de Ajedrez---");
        imprimirTablero();

        while (ejecutando) {
            System.out.println("Turno: " + (turno == 'W' ? "Blancas" : "Negras"));
            System.out.print("Ingrese jugada(Ejemplo: e2 e4) o comandos como(capturadas,reiniciar o salir):");

            String linea;
            linea = entrada.nextLine().trim();

            if (linea.equalsIgnoreCase("salir")) {
                ejecutando = false;
                System.out.println("Saliendo.......");
                break;

            } else if (linea.equalsIgnoreCase("reiniciar")) {
                inicializarTablero();
                capturadas.clear();
                turno = 'W';
                imprimirTablero();
                continue;
            } else if (linea.equalsIgnoreCase("capturadas")) {
                mostrarCapturadasOrdenadas();
                continue;

            }
            String[] partes = linea.split("///");
            if (partes.length != 2) {
                System.out.println("Entrada invalida. use formato: origen destino");
                continue;
            }

            int[] origen = convertirAlgebraico(partes[0]);
            int[] destino = convertirAlgebraico(partes[1]);

            if (origen == null || destino == null) {
                System.out.println("Coordenadas Invalidas");
                continue;
            }
            if (!intentarmovimiento(origen[0], origen[1], destino[0], destino[1])) {
                continue;
            } else {
                imprimirTablero();
                turno = (turno == 'W') ? 'b' : 'W';
            }

        }
    }
    
    

}
