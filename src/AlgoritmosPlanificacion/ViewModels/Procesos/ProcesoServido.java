/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoritmosPlanificacion.ViewModels.Procesos;

/**
 *
 * @author LAGV clase para abm de procesos Ejemplo funcionamiento con
 * prioridades: https://www.youtube.com/watch?v=BxjI8jM94Ss
 */
public class ProcesoServido extends Proceso implements Comparable<ProcesoServido> {

    //atributos del proceso servido (adicionales a Proceso por herencia)
    Integer rafagasRestantesCPU;
    int tiempoFinalizacion;
    int tiempoEstanciaTr;
    //tr/ts
    float tiempoPromedioTrTs;
    
    int tiempoInicio;

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

    public Integer getRafagasRestantesCPU() {
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

   

    public int getTiempoInicio() {
        return tiempoInicio;
    }

    public void setTiempoInicio(int tiempoInicio) {
        this.tiempoInicio = tiempoInicio;
    }

    public float getTiempoPromedioTrTs() {
        return tiempoPromedioTrTs;
    }

    public void setTiempoPromedioTrTs(float tiempoPromedioTrTs) {
        this.tiempoPromedioTrTs = tiempoPromedioTrTs;
    }

    @Override
    public int compareTo(ProcesoServido t) {
        return this.getRafagasRestantesCPU().compareTo(t.getRafagasRestantesCPU());
    }

}
