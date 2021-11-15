/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoritmosPlanificacion.ViewModels;

import AlgoritmosPlanificacion.ViewModels.Interfaces.AlgoritmosInterface;

/**
 *
 * @author LAGV
 */
public class AlgoritmoFCFS extends AlgoritmosSimuladorPlanificacion implements AlgoritmosInterface {

    @Override
    public void EjecutarProceso() {
        //
//          if(arrayColaListos.length > 0){
//            //si hay procesos en la cola de listos, agarro el primero y lo ejecuto
//            //lo asigno a la cpu y lo quito de la cola de listos
//            procesoEjecutandose =arrayColaListos[0];
//            //quito de la cola de listos
//            arrayColaListos.pop(procesoEjecutandose);
//            //guardo el tiempo en el que inicia a hacer uso de la cpu
//            procesoEjecutandose.tiempoInicio = tiempoCPU; 
//            //marco la cpu en uso
//            cpuEnUso = true;
//
//        }

    }

   

    @Override
    public void InicializarComponentes() {
        //para saber si terminaron todas las rafagas
        this.terminaronProcesos = false;
        //iniciamos el tiempo de CPU
        this.tiempoCPU = 0;
        //lista vacia
        this.listaListos = null;
        //para almacenar los procesos que terminaron su ejecucion
        this.listaProcesosTerminados = null;
        //proceso usando cpu
        this.cpuEnUso = false;
        //proceso usando la cpu
        this.procesoEjecutandose = null;

        this.terminaronProcesos = false;

    }

    @Override
    public void OrdenarCola(ProcesoServido p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void MostrarResultado() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
