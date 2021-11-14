/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoritmosPlanificacion.Models;

/**
 *
 * @author LAGV
 * clase para abm de procesos
 * Ejemplo funcionamiento con prioridades: https://www.youtube.com/watch?v=BxjI8jM94Ss
 */
public class Proceso {

    //atributos del proceso
    int id;
    String nombreProceso;
    int tiempoArribo;
    int rafagaCPU;
    int prioridad;

    public Proceso() {

    }

    //set de datos de proceso
    public Proceso(int id, String nombreProceso, int tiempoArribo, int rafagaCPU, int prioridad) {
        this.id = id;
        this.nombreProceso = nombreProceso;
        this.tiempoArribo = tiempoArribo;
        this.rafagaCPU = rafagaCPU;
        this.prioridad = prioridad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreProceso() {
        return nombreProceso;
    }

    public void setNombreProceso(String nombreProceso) {
        this.nombreProceso = nombreProceso;
    }

    public int getTiempoArribo() {
        return tiempoArribo;
    }

    public void setTiempoArribo(int tiempoArribo) {
        this.tiempoArribo = tiempoArribo;
    }

    public int getRafagaCPU() {
        return rafagaCPU;
    }

    public void setRafagaCPU(int rafagaCPU) {
        this.rafagaCPU = rafagaCPU;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

  
    
    
    
}
