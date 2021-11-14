/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoritmosPlanificacion.ViewModels;

/**
 *
 * @author LAGV clase para abm de procesos Ejemplo funcionamiento con
 * prioridades: https://www.youtube.com/watch?v=BxjI8jM94Ss
 */
public class ProcesoServido extends Proceso {

    //atributos del proceso servido (adicionales a Proceso por herencia)
    int rafagasRestantesCPU;
    int tiempoFinalizacion;
    int tiempoEstanciaTr;
    //tr/ts
    int tiempoPromedioTrTs;

    public ProcesoServido() {

    }

    //set de datos de proceso Servido en el procesador
    public ProcesoServido(int id, String nombreProceso, int tiempoArribo, int rafagaCPU, int prioridad,
            int rafagasRestantesCPU) {
        this.id = id;
        this.nombreProceso = nombreProceso;
        this.tiempoArribo = tiempoArribo;
        this.rafagaCPU = rafagaCPU;
        this.prioridad = prioridad;
        this.rafagasRestantesCPU = rafagasRestantesCPU;
    }

    public int getRafagasRestantesCPU() {
        return rafagasRestantesCPU;
    }

    public void setRafagasRestantesCPU(int rafagasRestantesCPU) {
        this.rafagasRestantesCPU = rafagasRestantesCPU;
    }

    public int getTiempoFinalizacion() {
        return tiempoFinalizacion;
    }

    public void setTiempoFinalizacion(int tiempoFinalizacion) {
        this.tiempoFinalizacion = tiempoFinalizacion;
    }

    public int getTiempoEstanciaTr() {
        return tiempoEstanciaTr;
    }

    public void setTiempoEstanciaTr(int tiempoEstanciaTr) {
        this.tiempoEstanciaTr = tiempoEstanciaTr;
    }

    public int getTiempoPromedioTrTs() {
        return tiempoPromedioTrTs;
    }

    public void setTiempoPromedioTrTs(int tiempoPromedioTrTs) {
        this.tiempoPromedioTrTs = tiempoPromedioTrTs;
    }

}
