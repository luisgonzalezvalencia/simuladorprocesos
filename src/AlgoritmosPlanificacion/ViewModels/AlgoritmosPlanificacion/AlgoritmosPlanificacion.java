/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoritmosPlanificacion.ViewModels.AlgoritmosPlanificacion;

import AlgoritmosPlanificacion.ViewModels.Procesos.Proceso;
import AlgoritmosPlanificacion.ViewModels.Procesos.ProcesoServido;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author LAGV
 */
public abstract class AlgoritmosPlanificacion {

    List<Proceso> listaArribo;
    List<ProcesoServido> listaListos;
    int tiempoCPU;
    List<ProcesoServido> listaProcesosTerminados;
    boolean cpuEnUso;
    ProcesoServido procesoEjecutandose;
    boolean terminaronProcesos;
    float tiempoPromedioTotal;

    public AlgoritmosPlanificacion() {

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

    public void verificarTiempoRestante() {
        //si la cpu esta en uso resto una ejecucion al proceso que se está ejecutando
        this.procesoEjecutandose.setRafagasRestantesCPU(this.procesoEjecutandose.getRafagasRestantesCPU() - 1);

        //si el proceso que estaba ejecutandose ya termino sus rafagas de cpu, libero
        if (this.procesoEjecutandose.getRafagasRestantesCPU() <= 0) {
            //sumo uno porque no cambio toddavia el tiempo de cpu
            this.procesoEjecutandose.setTiempoFinalizacion(this.tiempoCPU + 1);

                //asegurar que meto una copia en la lista de procesos terminados
            //y no una direccion de memoria ya que luego borro la variable que indica el proceso ejecutandose
            ProcesoServido procesoTerminado = new ProcesoServido(this.procesoEjecutandose.getId(), this.procesoEjecutandose.getNombreProceso(), this.procesoEjecutandose.getTiempoArribo(), procesoEjecutandose.getRafagaCPU(), procesoEjecutandose.getPrioridad(), 0);
            procesoTerminado.setTiempoFinalizacion(this.procesoEjecutandose.getTiempoFinalizacion());
            procesoTerminado.setTiempoInicio(this.procesoEjecutandose.getTiempoInicio());
            //el resto de tiempos se van a calcular dentro del proceso de mostrar los resultados
            this.listaProcesosTerminados.add(procesoTerminado);
            //quitamos el proceso de la cpu
            this.procesoEjecutandose = null;
            this.cpuEnUso = false;
        }
    }

    public void MostrarResultado() {
        //calculamos los resultados de cada proceso
        for (ProcesoServido proceso : this.listaProcesosTerminados) {

            //tiempo de estancia es tiempo de finalizacion - tiempo de arribo
            proceso.setTiempoEstanciaTr(proceso.getTiempoFinalizacion() - proceso.getTiempoArribo());

            //tiempoPromedioTrTs = tiempo de estancia / tiempo de servicio (cantidad de rafagas de cpu)
            proceso.setTiempoPromedioTrTs((float) proceso.getTiempoEstanciaTr() / proceso.getRafagaCPU());

            //sumamos el tiempo promedio total;
            this.tiempoPromedioTotal = this.tiempoPromedioTotal + proceso.getTiempoPromedioTrTs();
        }
        this.tiempoPromedioTotal = this.tiempoPromedioTotal / this.listaProcesosTerminados.size();
    }

    public void InicializarComponentes() {
        //para saber si terminaron todas las rafagas
        this.terminaronProcesos = false;
        //iniciamos el tiempo de CPU
        this.tiempoCPU = 0;
        //lista vacia
        this.listaListos = new ArrayList<>();
        //para almacenar los procesos que terminaron su ejecucion
        this.listaProcesosTerminados = new ArrayList<>();
        //proceso usando cpu
        this.cpuEnUso = false;
        //proceso usando la cpu
        this.procesoEjecutandose = null;

        this.terminaronProcesos = false;

    }

    public void EjecutarProceso() {
        //si la cpu no está en uso, llamo a la funcion ejecutar del algoritmo correcpondiente
        if (!this.isCpuEnUso()) {
            if (this.listaListos.size() > 0) {
                //si hay procesos en la cola de listos, agarro el primero y lo ejecuto
                //lo asigno a la cpu y lo quito de la cola de listos
                this.procesoEjecutandose = this.listaListos.get(0);
                //quito el proceso de la cola de listos
                this.listaListos.remove(0);
                //guardo el tiempo en el que inicia a hacer uso de la cpu
                //solo la primera vez que entra a la cola de listo se guarda el tiempo de inicio
                if (this.procesoEjecutandose.getRafagasRestantesCPU() == this.procesoEjecutandose.getRafagaCPU()) {
                    this.procesoEjecutandose.setTiempoInicio(this.tiempoCPU);
                }
                this.cpuEnUso = true;
                //inicializamos el tiempo promedio en 0
                this.tiempoPromedioTotal = 0;
                this.verificarTiempoRestante();

            } else {
                //creo una bandera local para verificar
                boolean procesoPorArribar = false;
                //si no hay procesos en la cola de listo, verifico si hay procesos por arribar
                for (Proceso procesoArribar : this.listaArribo) {
                    if (procesoArribar.getTiempoArribo() > this.tiempoCPU) {
                        //si hay algun proceso por arribar, la bandera se pone en true
                        procesoPorArribar = true;
                    }
                }

                //si no quedan procesos por arribar, significan que se ejecutaron todos
                if (!procesoPorArribar) {
                    this.terminaronProcesos = true;
                }

            }
        } else {
            this.verificarTiempoRestante();
        }

    }

    public abstract void OrdenarCola(ProcesoServido proceso);
    
   

}
