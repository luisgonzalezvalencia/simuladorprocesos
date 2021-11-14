/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algoritmos;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alejandro
 */
import java.util.ArrayList;

public class Disco {
    private int posicionInicial;

    public Disco(int d_posicionInicial){
        this.posicionInicial = d_posicionInicial;
    }
    
    public void SSF (DiscoView discoView, ArrayList<Integer> peticiones)
    {
     
       int distancia = 0,numPistas= peticiones.size();  //la distancia almacenara el movimiento total recorrido segun el numero de peticiones 
        int cabezal = this.posicionInicial;
        System.out.print(cabezal);
        String resultado = Integer.toString(cabezal);
        
        while (peticiones.size() > 0)  // mientras halla peticiones en la lista
        {
            int posicion = 0;
            for (int j=1; j<peticiones.size(); j++){
                //Se van comparando las diferencias entre el cabezal y las pistas, se va guardando la 
                //posición de la pista más cercana, es decir, donde la resta salió menor
                if (Math.abs(cabezal - peticiones.get(j).intValue()) < Math.abs(cabezal - peticiones.get(posicion).intValue())){
                    posicion = j;
                }
            }
                //Se guarda la resta entre el cabezal y la pista más cercana actual, se va acumulando.
                distancia += Math.abs(cabezal - peticiones.get(posicion).intValue());
                cabezal = peticiones.get(posicion).intValue();
                System.out.print(" -> "+ cabezal);
                resultado += " -> "+ cabezal;
                peticiones.remove(posicion);//como el ciclo while depende del tamaño de la lista, entonces una vez
                //que se "atendió la petición" se borra de la lista para que pueda finalizar el ciclo
        }
 
        /**
         * estos son los datos que saldran por pantalla y deben aparecer en nuestra interfaz
         */
        discoView.getjLabelOrdenRecorrido().setText(resultado);
        int promedio=distancia/numPistas; // promedio calculado por el numero de distancias dividido el numero de pistas 
        System.out.println("\n"+distancia + " pistas recorridas");
        discoView.getjLabelPistasRecorridas().setText(Integer.toString(distancia));
        System.out.println("Longitud promedio de busqueda: "+promedio);
        discoView.getjLabelLongitudPromedio().setText(Integer.toString(promedio));
  }

   
}
