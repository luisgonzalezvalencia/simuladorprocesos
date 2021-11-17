/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoritmosPlanificacion.ViewModels;

import java.util.List;

/**
 *
 * @author LAGV
 */


public class AlgoritmosSimuladorPlanificacion {
    List<Proceso> listaArribo;
    List<ProcesoServido> listaListos;
    int tiempoCPU;
    List<ProcesoServido> listaProcesosTerminados;
    boolean cpuEnUso;
    ProcesoServido procesoEjecutandose;
    boolean terminaronProcesos;
    float tiempoPromedioTotal;
    
    
    public AlgoritmosSimuladorPlanificacion() {

    }

    public List<Proceso> getListaArribo() {
        return listaArribo;
    }

    public void setListaArribo(List<Proceso> listaArribo) {
        this.listaArribo = listaArribo;
    }

    public List<ProcesoServido> getListaListos() {
        return listaListos;
    }

    public void setListaListos(List<ProcesoServido> listaListos) {
        this.listaListos = listaListos;
    }

    public int getTiempoCPU() {
        return tiempoCPU;
    }

    public void setTiempoCPU(int tiempoCPU) {
        this.tiempoCPU = tiempoCPU;
    }


    public List<ProcesoServido> getListaProcesosTerminados() {
        return listaProcesosTerminados;
    }

    public void setListaProcesosTerminados(List<ProcesoServido> listaProcesosTerminados) {
        this.listaProcesosTerminados = listaProcesosTerminados;
    }

    public boolean isCpuEnUso() {
        return cpuEnUso;
    }

    public void setCpuEnUso(boolean cpuEnUso) {
        this.cpuEnUso = cpuEnUso;
    }

    public ProcesoServido getProcesoEjecutandose() {
        return procesoEjecutandose;
    }

    public void setProcesoEjecutandose(ProcesoServido procesoEjecutandose) {
        this.procesoEjecutandose = procesoEjecutandose;
    }

    public boolean isTerminaronProcesos() {
        return terminaronProcesos;
    }

    public void setTerminaronProcesos(boolean terminaronProcesos) {
        this.terminaronProcesos = terminaronProcesos;
    }

    public float getTiempoPromedioTotal() {
        return tiempoPromedioTotal;
    }

    public void setTiempoPromedioTotal(float tiempoPromedioTotal) {
        this.tiempoPromedioTotal = tiempoPromedioTotal;
    }

   
    
    
     

    
}
