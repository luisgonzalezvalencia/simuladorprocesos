/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoritmosPlanificacion.Controllers;

import AlgoritmosPlanificacion.Models.ProcesosModel;
import AlgoritmosPlanificacion.ViewModels.AlgoritmoFCFS;
import AlgoritmosPlanificacion.ViewModels.Proceso;
import AlgoritmosPlanificacion.ViewModels.ProcesoServido;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 *
 * @author LAGV
 */
public class SimuladorController implements ActionListener {

    ProcesosModel _procesosModel = new ProcesosModel();

    //si queremos ver una ejecucion detenida paso a paso cuando cambia de proceso
    boolean detenerPasoPaso = false;

    @Override
    public void actionPerformed(ActionEvent ae) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void SimularFCFS() {
        AlgoritmoFCFS simuladorFCFS = new AlgoritmoFCFS();
        //inicializamos los componentes del algoritmo a simular
        simuladorFCFS.InicializarComponentes();

        //seteamos la lista de procesos por arribar obteniendolos del file        
        List<Proceso> listaArribo = _procesosModel.ObtenerListadoProcesos();
        simuladorFCFS.setListaArribo(listaArribo);

        //mientras no hayan terminado los procesos, sigo voy a seguir la simulacion
        while (!simuladorFCFS.isTerminaronProcesos()) {

            //un for o foreach para lista de procesos cargarlos en la cola de listos
            for (Proceso procesoArribar : simuladorFCFS.getListaArribo()) {
                if (procesoArribar.getTiempoArribo() == simuladorFCFS.getTiempoCPU()) {
                    //creo una instancia de proceso servido y seteo los parametros del proceso por arribar                    
                    ProcesoServido procesoServido = new ProcesoServido(procesoArribar.getId(), procesoArribar.getNombreProceso(), procesoArribar.getTiempoArribo(), procesoArribar.getRafagaCPU(), procesoArribar.getPrioridad(), procesoArribar.getRafagaCPU());
                    //indico al simulador que ordene la cola con el proceso servido
                    simuladorFCFS.OrdenarCola(procesoServido);
                }
            }

            if (!simuladorFCFS.isCpuEnUso()) {
                //llamo al metodo de ejecutar procesos que se encarga de verificar la cola, etc
                simuladorFCFS.EjecutarProceso();
            } else {
                //si la cpu esta en uso resto una ejecucion al proceso que se está ejecutando
                ProcesoServido procesoEjecutandose = simuladorFCFS.getProcesoEjecutandose();
                procesoEjecutandose.setRafagasRestantesCPU(procesoEjecutandose.getRafagasRestantesCPU() - 1);

                //si el proceso que estaba ejecutandose ya termino sus rafagas de cpu, libero
                if (procesoEjecutandose.getRafagasRestantesCPU() <= 0) {
                    procesoEjecutandose.setTiempoFinalizacion(simuladorFCFS.getTiempoCPU());
                    //asegurar que meto una copia en la lista de procesos terminados
                    //y no una direccion de memoria ya que luego borro la variable que indica el proceso ejecutandose
                    ProcesoServido procesoTerminado = new ProcesoServido(procesoEjecutandose.getId(), procesoEjecutandose.getNombreProceso(), procesoEjecutandose.getTiempoArribo(), procesoEjecutandose.getRafagaCPU(),procesoEjecutandose.getPrioridad(), 0);
                    procesoTerminado.setTiempoFinalizacion(procesoEjecutandose.getTiempoFinalizacion());
                    //el resto de tiempos se van a calcular dentro del proceso de mostrar los resultados
                    
                    //quitamos el proceso de la cpu
                    simuladorFCFS.setProcesoEjecutandose(null);
                    simuladorFCFS.setCpuEnUso(false);
                }
            }
            //incremento el tiempo de CPU
            simuladorFCFS.setTiempoCPU(simuladorFCFS.getTiempoCPU()+1);

        }
        
        simuladorFCFS.MostrarResultado();
        
        //aqui mostramos los resultados en la tabla de ejecucion

    }

    public void SimularRR() {

    }

    public void SimularSRT() {

    }

    public void SimularSPN() {

    }

}
