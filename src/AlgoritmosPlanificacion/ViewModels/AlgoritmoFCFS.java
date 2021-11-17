/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoritmosPlanificacion.ViewModels;

import AlgoritmosPlanificacion.ViewModels.Interfaces.AlgoritmosInterface;
import java.util.ArrayList;

/**
 *
 * @author LAGV
 */
public class AlgoritmoFCFS extends AlgoritmosSimuladorPlanificacion implements AlgoritmosInterface {

    @Override
    public void EjecutarProceso() {
        //si la cpu no está en uso, llamo a la funcion ejecutar del algoritmo correcpondiente
        if (!this.isCpuEnUso()) {
            if (this.listaListos.size() > 0) {
                //si hay procesos en la cola de listos, agarro el primero y lo ejecuto
                //lo asigno a la cpu y lo quito de la cola de listos
                this.procesoEjecutandose = this.listaListos.get(0);
                //quito el proceso de la cola de listos
                this.listaListos.remove(this.procesoEjecutandose);
                //guardo el tiempo en el que inicia a hacer uso de la cpu
                this.procesoEjecutandose.setTiempoInicio(this.tiempoCPU);
                this.cpuEnUso = true;
                //inicializamos el tiempo promedio en 0
                this.tiempoPromedioTotal = 0;
            } else {
                //creo una bandera local para verificar
                boolean procesoPorArribar = false;
                //si no hay procesos en la cola de listo, verifico si hay procesos por arribar
                for (Proceso procesoArribar : this.listaArribo) {
                    if (procesoArribar.tiempoArribo > this.tiempoCPU) {
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
            //si la cpu esta en uso resto una ejecucion al proceso que se está ejecutando
            this.procesoEjecutandose.rafagasRestantesCPU = this.procesoEjecutandose.rafagasRestantesCPU - 1;

            //si el proceso que estaba ejecutandose ya termino sus rafagas de cpu, libero
            if (this.procesoEjecutandose.rafagasRestantesCPU <= 0) {
                this.procesoEjecutandose.tiempoFinalizacion = this.tiempoCPU;

                //asegurar que meto una copia en la lista de procesos terminados
                //y no una direccion de memoria ya que luego borro la variable que indica el proceso ejecutandose
                ProcesoServido procesoTerminado = new ProcesoServido(this.procesoEjecutandose.getId(), this.procesoEjecutandose.getNombreProceso(), this.procesoEjecutandose.getTiempoArribo(), procesoEjecutandose.getRafagaCPU(), procesoEjecutandose.getPrioridad(), 0);
                procesoTerminado.setTiempoFinalizacion(this.procesoEjecutandose.tiempoFinalizacion);
                procesoTerminado.setTiempoInicio(this.procesoEjecutandose.tiempoInicio);
                    //el resto de tiempos se van a calcular dentro del proceso de mostrar los resultados
                this.listaProcesosTerminados.add(procesoTerminado);
                //quitamos el proceso de la cpu
                this.procesoEjecutandose = null;
                this.cpuEnUso = false;
            }
        }

    }

    @Override
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

    @Override
    public void OrdenarCola(ProcesoServido proceso) {
        //en FCFS solo añadimos el proceso al final de la cola
        this.listaListos.add(proceso);
        
    }

    @Override
    public void MostrarResultado() {
        //calculamos los resultados de cada proceso
        for (ProcesoServido proceso : this.listaProcesosTerminados) {

            //tiempo de estancia es tiempo de finalizacion - tiempo de arribo
            proceso.tiempoEstanciaTr = proceso.tiempoFinalizacion - proceso.tiempoArribo;

            //tiempoPromedioTrTs = tiempo de estancia / tiempo de servicio (cantidad de rafagas de cpu)
            proceso.tiempoPromedioTrTs =(float) proceso.tiempoEstanciaTr / proceso.rafagaCPU;

            //sumamos el tiempo promedio total;
            this.tiempoPromedioTotal = this.tiempoPromedioTotal + proceso.tiempoPromedioTrTs;
        }
        this.tiempoPromedioTotal = this.tiempoPromedioTotal / this.listaProcesosTerminados.size();
    }

}
