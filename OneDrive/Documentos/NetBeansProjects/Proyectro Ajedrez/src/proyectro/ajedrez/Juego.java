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
        entrada = new Scanner(System.in);
        capturadas = new ArrayList<>();
        ejecutando = true;
        inicializarTablero();
        turno = 'w';
    }

    private void inicializarTablero() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tablero[i][j] = null;
            }
        }

        for (int j = 0; j < 8; j++) {
            tablero[6][j] = new Pieza('P', 'w');
            tablero[1][j] = new Pieza('P', 'b');
        }

        tablero[7][0] = new Pieza('R', 'w');
        tablero[7][7] = new Pieza('R', 'w');
        tablero[0][0] = new Pieza('R', 'b');
        tablero[0][7] = new Pieza('R', 'b');

        tablero[7][1] = new Pieza('N', 'w');
        tablero[7][6] = new Pieza('N', 'w');
        tablero[0][1] = new Pieza('N', 'b');
        tablero[0][6] = new Pieza('N', 'b');

        tablero[7][2] = new Pieza('B', 'w');
        tablero[7][5] = new Pieza('B', 'w');
        tablero[0][2] = new Pieza('B', 'b');
        tablero[0][5] = new Pieza('B', 'b');

        tablero[7][3] = new Pieza('Q', 'w');
        tablero[0][3] = new Pieza('Q', 'b');

        tablero[7][4] = new Pieza('K', 'w');
        tablero[0][4] = new Pieza('K', 'b');
    }

    public void iniciar() {
        System.out.println("=== Juego de Ajedrez ===");
        imprimirTablero();

        while (ejecutando) {
            System.out.println("\nTurno: " + (turno == 'w' ? "Blancas" : "Negras"));
            System.out.print("Ingrese jugada (ej: e2 e4) o comando (capturadas, reiniciar, salir): ");

            String linea = entrada.nextLine().trim();

            if (linea.equalsIgnoreCase("salir")) {
                ejecutando = false;
                System.out.println("Saliendo...");
                break;
            } else if (linea.equalsIgnoreCase("reiniciar")) {
                inicializarTablero();
                capturadas.clear();
                turno = 'w';
                imprimirTablero();
                continue;
            } else if (linea.equalsIgnoreCase("capturadas")) {
                mostrarCapturadasOrdenadas();
                continue;
            }

            String[] partes = linea.split("\\s+");
            if (partes.length != 2) {
                System.out.println("Entrada inválida. Use formato: origen destino");
                continue;
            }

            int[] origen = convertirAlgebraico(partes[0]);
            int[] destino = convertirAlgebraico(partes[1]);

            if (origen == null || destino == null) {
                System.out.println("Coordenadas inválidas.");
                continue;
            }

            if (!intentarMovimiento(origen[0], origen[1], destino[0], destino[1])) {
                continue;
            } else {
                imprimirTablero();
                turno = (turno == 'w') ? 'b' : 'w';
            }
        }
    }

    private boolean intentarMovimiento(int r1, int c1, int r2, int c2) {
        Pieza p = tablero[r1][c1];

        if (p == null) {
            System.out.println("No hay pieza en esa casilla.");
            return false;
        }
        if (p.color != turno) {
            System.out.println("Turno incorrecto.");
            return false;
        }
        if (!movimientoValido(p, r1, c1, r2, c2)) {
            System.out.println("Movimiento inválido.");
            return false;
        }

        Pieza destinoGuardado = tablero[r2][c2];
        tablero[r2][c2] = p;
        tablero[r1][c1] = null;

        if (reyEnJaque(turno)) {
            tablero[r1][c1] = p;
            tablero[r2][c2] = destinoGuardado;
            System.out.println("Tu rey quedaría en jaque. Movimiento no permitido.");
            return false;
        }

        if (destinoGuardado != null) {
            capturadas.add(destinoGuardado.toString());
            System.out.println("Capturaste: " + destinoGuardado.toString());
        }

        if (p.tipo == 'P') {
            if (p.color == 'w' && r2 == 0) {
                tablero[r2][c2] = new Pieza('Q', 'w');
            }
            if (p.color == 'b' && r2 == 7) {
                tablero[r2][c2] = new Pieza('Q', 'b');
            }
        }

        return true;
    }

    private int[] convertirAlgebraico(String s) {
        s = s.trim().toLowerCase();
        if (s.length() != 2) {
            return null;
        }

        char archivo = s.charAt(0);
        char fila = s.charAt(1);

        if (archivo < 'a' || archivo > 'h' || fila < '1' || fila > '8') {
            return null;
        }

        int col = archivo - 'a';
        int row = 8 - (fila - '0');

        return new int[]{row, col};
    }

    private void imprimirTablero() {
        System.out.println();
        for (int i = 0; i < 8; i++) {
            System.out.print((8 - i) + "  ");
            for (int j = 0; j < 8; j++) {
                if (tablero[i][j] == null) {
                    System.out.print(". ");
                } else {
                    System.out.print(tablero[i][j].toString() + " ");
                }
            }
            System.out.println();
        }
        System.out.println("\n   a b c d e f g h");
    }

    private boolean movimientoValido(Pieza p, int r1, int c1, int r2, int c2) {
        if (r2 < 0 || r2 > 7 || c2 < 0 || c2 > 7) {
            return false;
        }
        if (tablero[r2][c2] != null && tablero[r2][c2].color == p.color) {
            return false;
        }

        int dr = r2 - r1;
        int dc = c2 - c1;
        int adr = Math.abs(dr);
        int adc = Math.abs(dc);

        switch (p.tipo) {
            case 'P':
                if (p.color == 'w') {
                    if (dc == 0 && dr == -1 && tablero[r2][c2] == null) {
                        return true;
                    }
                    if (dc == 0 && r1 == 6 && dr == -2 && tablero[r1 - 1][c1] == null && tablero[r2][c2] == null) {
                        return true;
                    }
                    if (adr == 1 && adc == 1 && dr == -1 && tablero[r2][c2] != null && tablero[r2][c2].color != p.color) {
                        return true;
                    }
                } else {
                    if (dc == 0 && dr == 1 && tablero[r2][c2] == null) {
                        return true;
                    }
                    if (dc == 0 && r1 == 1 && dr == 2 && tablero[r1 + 1][c1] == null && tablero[r2][c2] == null) {
                        return true;
                    }
                    if (adr == 1 && adc == 1 && dr == 1 && tablero[r2][c2] != null && tablero[r2][c2].color != p.color) {
                        return true;
                    }
                }
                return false;

            case 'R':
                if (dr == 0 && dc != 0) {
                    return caminoHorizontal(r1, c1, c2);
                }
                if (dc == 0 && dr != 0) {
                    return caminoVertical(c1, r1, r2);
                }
                return false;

            case 'N':
                return (adr == 2 && adc == 1) || (adr == 1 && adc == 2);

            case 'B':
                if (adr == adc && adr != 0) {
                    return caminoDiagonal(r1, c1, r2, c2);
                }
                return false;

            case 'Q':
                if (dr == 0 && dc != 0) {
                    return caminoHorizontal(r1, c1, c2);
                }
                if (dc == 0 && dr != 0) {
                    return caminoVertical(c1, r1, r2);
                }
                if (adr == adc && adr != 0) {
                    return caminoDiagonal(r1, c1, r2, c2);
                }
                return false;

            case 'K':
                return adr <= 1 && adc <= 1 && (adr + adc) != 0;

            default:
                return false;
        }
    }

    private boolean caminoHorizontal(int r, int c1, int c2) {
        int ini = Math.min(c1, c2) + 1;
        int fin = Math.max(c1, c2) - 1;
        for (int c = ini; c <= fin; c++) {
            if (tablero[r][c] != null) {
                return false;
            }
        }
        return true;
    }

    private boolean caminoVertical(int c, int r1, int r2) {
        int ini = Math.min(r1, r2) + 1;
        int fin = Math.max(r1, r2) - 1;
        for (int r = ini; r <= fin; r++) {
            if (tablero[r][c] != null) {
                return false;
            }
        }
        return true;
    }

    private boolean caminoDiagonal(int r1, int c1, int r2, int c2) {
        int dr = (r2 > r1) ? 1 : -1;
        int dc = (c2 > c1) ? 1 : -1;
        int r = r1 + dr, c = c1 + dc;
        while (r != r2 && c != c2) {
            if (tablero[r][c] != null) {
                return false;
            }
            r += dr;
            c += dc;
        }
        return true;
    }

    private int[] encontrarRey(char color) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (tablero[i][j] != null && tablero[i][j].tipo == 'K' && tablero[i][j].color == color) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    private boolean reyEnJaque(char color) {
        int[] pos = encontrarRey(color);
        if (pos == null) {
            return false;
        }
        int kr = pos[0], kc = pos[1];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Pieza p = tablero[i][j];
                if (p != null && p.color != color) {
                    if (piezaAmenazaCasilla(p, i, j, kr, kc)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean piezaAmenazaCasilla(Pieza p, int r1, int c1, int r2, int c2) {
        int dr = r2 - r1;
        int dc = c2 - c1;
        int adr = Math.abs(dr);
        int adc = Math.abs(dc);

        switch (p.tipo) {
            case 'P':
                return p.color == 'w' ? (dr == -1 && adc == 1) : (dr == 1 && adc == 1);
            case 'R':
                if (dr == 0 && dc != 0) {
                    return caminoHorizontal(r1, c1, c2);
                }
                if (dc == 0 && dr != 0) {
                    return caminoVertical(c1, r1, r2);
                }
                return false;
            case 'N':
                return (adr == 2 && adc == 1) || (adr == 1 && adc == 2);
            case 'B':
                if (adr == adc && adr != 0) {
                    return caminoDiagonal(r1, c1, r2, c2);
                }
                return false;
            case 'Q':
                if (dr == 0 && dc != 0) {
                    return caminoHorizontal(r1, c1, c2);
                }
                if (dc == 0 && dr != 0) {
                    return caminoVertical(c1, r1, r2);
                }
                if (adr == adc && adr != 0) {
                    return caminoDiagonal(r1, c1, r2, c2);
                }
                return false;
            case 'K':
                return adr <= 1 && adc <= 1 && (adr + adc) != 0;
            default:
                return false;
        }
    }

    private void mostrarCapturadasOrdenadas() {
        if (capturadas.isEmpty()) {
            System.out.println("No hay piezas capturadas.");
            return;
        }
        String[] arr = capturadas.toArray(new String[0]);
        burbuja(arr);
        System.out.println("Piezas capturadas:");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + (i == arr.length - 1 ? "" : ", "));
        }
        System.out.println();
    }

    private void burbuja(String[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            for (int j = 0; j < a.length - 1 - i; j++) {
                if (a[j].compareTo(a[j + 1]) > 0) {
                    String t = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = t;
                }
            }
        }
    }
}
