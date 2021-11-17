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
import AlgoritmosPlanificacion.Views.SimuladorView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 *
 * @author LAGV
 */
public class SimuladorController implements ActionListener {

    ProcesosModel _procesosModel = new ProcesosModel();
    SimuladorView simuladorVista;
    //si queremos ver una ejecucion detenida paso a paso cuando cambia de proceso
    boolean detenerPasoPaso = false;
    List<Proceso> procesosUtilizar;
    String algoritmoSeleccionado;

    @Override
    public void actionPerformed(ActionEvent ae) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public SimuladorController(SimuladorView simuladorVista, String algoritmoSeleccionado) {
        this.algoritmoSeleccionado = algoritmoSeleccionado;
        this.simuladorVista = simuladorVista;
        this.procesosUtilizar = _procesosModel.ObtenerListadoProcesos();
        if (this.simuladorVista.isVisible()) {
            MostrarSimulacionEnVista(this.procesosUtilizar, null, null, null, -1, false, this.algoritmoSeleccionado);
        }

        //test de fcfs
        this.SimularFCFS();
        //agregamos los listeners de las acciones a la vista
//        this.procesosVista.btnRecargarListado.addActionListener(this);
//        this.procesosVista.btnGuardar.addActionListener(this);
//        this.procesosVista.btnEditar.addActionListener(this);
//        this.procesosVista.btnLimpiarDatos.addActionListener(this);
//        this.procesosVista.btnEliminarProceso.addActionListener(this);
    }

    public void SimularFCFS() {
        AlgoritmoFCFS simuladorFCFS = new AlgoritmoFCFS();
        //inicializamos los componentes del algoritmo a simular
        simuladorFCFS.InicializarComponentes();

        //seteamos la lista de procesos por arribar obteniendolos del file        
        List<Proceso> listaArribo = this.procesosUtilizar;
        simuladorFCFS.setListaArribo(listaArribo);

        //mientras no hayan terminado los procesos,  voy a seguir la simulacion
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

            //llamamos al ejecutador de procesos
            simuladorFCFS.EjecutarProceso();
            //si la cpu no está en uso, llamo a la funcion ejecutar del algoritmo correcpondiente
//            if (!simuladorFCFS.isCpuEnUso()) {
//                //llamo al metodo de ejecutar procesos que se encarga de verificar la cola, etc
//                simuladorFCFS.EjecutarProceso();
//            } else {
//                //si la cpu esta en uso resto una ejecucion al proceso que se está ejecutando
//                ProcesoServido procesoEjecutandose = simuladorFCFS.getProcesoEjecutandose();
//                procesoEjecutandose.setRafagasRestantesCPU(procesoEjecutandose.getRafagasRestantesCPU() - 1);
//
//                //si el proceso que estaba ejecutandose ya termino sus rafagas de cpu, libero
//                if (procesoEjecutandose.getRafagasRestantesCPU() <= 0) {
//                    procesoEjecutandose.setTiempoFinalizacion(simuladorFCFS.getTiempoCPU());
//                    //asegurar que meto una copia en la lista de procesos terminados
//                    //y no una direccion de memoria ya que luego borro la variable que indica el proceso ejecutandose
//                    ProcesoServido procesoTerminado = new ProcesoServido(procesoEjecutandose.getId(), procesoEjecutandose.getNombreProceso(), procesoEjecutandose.getTiempoArribo(), procesoEjecutandose.getRafagaCPU(), procesoEjecutandose.getPrioridad(), 0);
//                    procesoTerminado.setTiempoFinalizacion(procesoEjecutandose.getTiempoFinalizacion());
//                    //el resto de tiempos se van a calcular dentro del proceso de mostrar los resultados
//
//                    //quitamos el proceso de la cpu
//                    simuladorFCFS.setProcesoEjecutandose(null);
//                    simuladorFCFS.setCpuEnUso(false);
//                }
//            }

            //MOSTRAMOS LOS RESULTADOS DE CADA EJECUCION EN LA VISTA
            MostrarSimulacionEnVista(simuladorFCFS.getListaArribo(), simuladorFCFS.getListaListos(), simuladorFCFS.getProcesoEjecutandose(), simuladorFCFS.getListaProcesosTerminados(), simuladorFCFS.getTiempoCPU(), simuladorFCFS.isCpuEnUso(), "FCFS");

            //simulamos que el reloj de la CPU funcione mas lento para poder ver en detalle los movimientos de cada algoritmo
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            //incremento el tiempo de CPU
            simuladorFCFS.setTiempoCPU(simuladorFCFS.getTiempoCPU() + 1);

        }

        simuladorFCFS.MostrarResultado();
        //aqui mostramos los resultados en la tabla de ejecucion
        //MOSTRAMOS LOS RESULTADOS DE CADA EJECUCION EN LA VISTA
        MostrarSimulacionEnVista(simuladorFCFS.getListaArribo(), simuladorFCFS.getListaListos(), simuladorFCFS.getProcesoEjecutandose(), simuladorFCFS.getListaProcesosTerminados(), simuladorFCFS.getTiempoCPU(), simuladorFCFS.isCpuEnUso(), "FCFS");

    }

    public void SimularRR() {

    }

    public void SimularSRT() {

    }

    public void SimularSPN() {

    }

    private void MostrarSimulacionEnVista(List<Proceso> listaProcesos, List<ProcesoServido> colaListos, ProcesoServido procesoEnCPU, List<ProcesoServido> procesosTerminados, int tiempoCPU, boolean hayProcesosEnEjecucion, String algoritmoPlanificacion) {

    }

}
