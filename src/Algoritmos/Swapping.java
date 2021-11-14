/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algoritmos;

/**
 *
 * @author Agustin
 */
import Procesos.Proceso; 
import java.lang.reflect.Array; // importo la librería para poder utilizar ArrayList
import java.util.*; 
public class Swapping{
    private ArrayList<Proceso> procesosPrincipales; // atributo procesosPrincipales. Aquí se irán agregando los procesos "de la memoria  principal"
    private ArrayList<Proceso> procesosSecundarios; // atributo procesosSecundarios. Aquí se irán agregando los procesos "de la memoria  secundaria"
    private int tamanioPrincipal = 5; //defino un atributo tamanioPrincipal con valor 5
    String cambios=""; // declaro una variable interna de tipo String llamada cambios. Aqui se sumarán cadenas  
    String cadePrin=""; // declaro una variable interna de tipo String llamada cadeSecu. Aqui se sumarán cadenas 
    String cadeSecu = ""; // declaro una variable interna de tipo String llamada cadeSecu. Aqui se sumarán cadenas 
    public Swapping(ArrayList<Proceso> p_procesoLista){ //constructor de la clase Swapping que recibe un ArrayList con procesos
        
        this.setProcPrincipales(new ArrayList()); 
        this.setProcSecundarios(new ArrayList());
        
        this.agregarProceso(p_procesoLista);
    }
    
    private void setProcPrincipales(ArrayList pArray){ //set del ArrayList setProcPrincipales
        this.procesosPrincipales = pArray;
    }
    public ArrayList<Proceso> getProcPrincipales(){  //get del ArrayList setProcPrincipales
        return this.procesosPrincipales;
    }
    private void setProcSecundarios(ArrayList pArray){  //set del ArrayList setProcSecundarios
        this.procesosSecundarios = pArray;
    }
    public ArrayList<Proceso> getProcSecundarios(){ //get del ArrayList setProcSecundarios
        return this.procesosSecundarios;
    }
    public void agregarProceso(ArrayList<Proceso> p_procesoLista){ // el método agregarProceso() recibe un ArrayList de procesos y los va agregando según vayan entrando a la condición 
        for (Proceso proceso : p_procesoLista) {
            
            if (this.getProcPrincipales().size() < this.tamanioPrincipal){ // si el tamaño del arreglo principal es menor a 5 se agregará el array procPrincipales
                this.getProcPrincipales().add(proceso);
            } else {// caso contrario se agregará en el array procSecundarios
                this.getProcSecundarios().add(proceso);
            }
        }
        
    }
    public String mostrarProcPrinc(){
      
        int i = 0; // variable i que utilizaré para mostrar el número de partición
        String cadena= ""; // vairable cadena para retornarla luego
        for(Proceso p: this.getProcPrincipales()){ // recorro mi arreglo principal
            cadena = cadena +  ("Partición " + i + ":"+"\n"+p.mostrar()+"\n-------------------------------------------------------\n"); //sumo a la cadena que retornaré la partición y los datos del proceso
            i++; // sumo 1 a i para mostrar correctamente las particiones
        }
        
       return cadena; // retorno toda la cadena
    }
    
    public String mostrarProcSecond(){ // se realiza el mismo procedimiento que en mostrarProcPrinc() pero con el segundo arreglo
       
        int i = 0;
       
        String cadena = "";
        for(Proceso p: this.getProcSecundarios()){
            
            cadena = cadena +  ("Partición " + i + ":"+"\n"+p.mostrar()+"\n-------------------------------------------------------\n");
            i++;
        }
        
        return cadena;
       
    }
    
    public String mostrarPrin(){
        return cadePrin;  //retorno cadePrin
    }
    public String mostrarSecu(){
        return cadeSecu;
    }
    public String cambiosLugar(){
        
        return cambios;
    }
    public void swappear(){
 
        int j=0,i=0;
        Proceso proAux;
        int cant2 = this.getProcSecundarios().size();
        for(Proceso p: this.getProcPrincipales()){ // el swpping consiste en recorrer mi cola de procesos y sumar dependiendo de la condición a la cadena princiapl "cadePrin" o a la cadena secundaria "cadeSecu"
              
            if(p.isBloqueado() == true && j < cant2){ //si el proceso está bloqueado y la cantidad de veces que se ingresó a la condición es menor a la cantidad de procesos que posee el segundo arreglo entrará
          
                cambios = cambios + "Swapping en partición " + i + "\n"; //sumo a la cadena cambios donde se realizó swapping
                
                cadePrin = cadePrin +"Particion "+ i + ":\n"+(this.getProcSecundarios().get(j).mostrar() +"\n-------------------------------------------------------\n"); //sumo a cadePrin el proceso que está en posición j del arreglo secundario
                
                cadeSecu = cadeSecu + "Particion "+ j + ":\n"+(this.getProcPrincipales().get(i).mostrar() +"\n-------------------------------------------------------\n"); //sumo a cadeSec el proceso que se encuentra en la posición i del arreglo principal
                
                
                j++;
                
            }else{ //si no ingresa en la condición anterior entrará por aquí
                cadePrin = cadePrin +"Particion "+ i + ":\n"+(this.getProcPrincipales().get(i).mostrar() +"\n-------------------------------------------------------\n"); // sumo a cadePrin el proceso que se encuentra en la posición i del arreglo principal
            
            }
            i++; //sumo 1 en i
            
           
        }
        
        for (i=j ; i < cant2; i++){ //desde i igual a j hasta i menor a la cantidad de procesos den el segundo arreglo
            cadeSecu = cadeSecu + "Particion"+ i + ":\n"+(this.getProcSecundarios().get(i).mostrar() +"\n-------------------------------------------------------\n"); // sumo a cadeSecu el proceso que se encuentra en la posición i del arreglo secundario
       }
    }
}
