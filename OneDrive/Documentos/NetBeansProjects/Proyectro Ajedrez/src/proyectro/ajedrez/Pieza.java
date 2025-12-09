/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectro.ajedrez;

/**
 *
 * @author zunig
 */
public class Pieza {
    char tipo;
    char color; 
    
    public Pieza(char tipo, char color){
        this.tipo=tipo;
        this.color=color;
    }
    
    @Override
    public String toString(){
        return ""+(color=='W' ? Character.toUpperCase(tipo):Character.toLowerCase(tipo));
    }
    
    
}
