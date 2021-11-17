/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoritmosPlanificacion.Controllers;

import AlgoritmosPlanificacion.Models.ProcesosModel;
import AlgoritmosPlanificacion.ViewModels.AlgoritmoFCFS;
import AlgoritmosPlanificacion.ViewModels.AlgoritmoRR;
import AlgoritmosPlanificacion.ViewModels.AlgoritmoSPN;
import AlgoritmosPlanificacion.ViewModels.AlgoritmoSRT;
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
        this.SimularSRT();
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
        
        //siempre tengo un tiempo  mas, se lo quito
        simuladorFCFS.setTiempoCPU(simuladorFCFS.getTiempoCPU() - 1);
        simuladorFCFS.MostrarResultado();
        //aqui mostramos los resultados en la tabla de ejecucion
        //MOSTRAMOS LOS RESULTADOS DE CADA EJECUCION EN LA VISTA
        MostrarSimulacionEnVista(simuladorFCFS.getListaArribo(), simuladorFCFS.getListaListos(), simuladorFCFS.getProcesoEjecutandose(), simuladorFCFS.getListaProcesosTerminados(), simuladorFCFS.getTiempoCPU(), simuladorFCFS.isCpuEnUso(), "FCFS");

    }

    public void SimularRR(int quantum) {
        int quantumCont = quantum;

        AlgoritmoRR simuladorRR = new AlgoritmoRR();
        //inicializamos los componentes del algoritmo a simular
        simuladorRR.InicializarComponentes();

        //seteamos la lista de procesos por arribar obteniendolos del file        
        List<Proceso> listaArribo = this.procesosUtilizar;
        simuladorRR.setListaArribo(listaArribo);

        //mientras no hayan terminado los procesos,  voy a seguir la simulacion
        while (!simuladorRR.isTerminaronProcesos()) {

            //un for o foreach para lista de procesos cargarlos en la cola de listos
            for (Proceso procesoArribar : simuladorRR.getListaArribo()) {
                if (procesoArribar.getTiempoArribo() == simuladorRR.getTiempoCPU()) {
                    //creo una instancia de proceso servido y seteo los parametros del proceso por arribar                    
                    ProcesoServido procesoServido = new ProcesoServido(procesoArribar.getId(), procesoArribar.getNombreProceso(), procesoArribar.getTiempoArribo(), procesoArribar.getRafagaCPU(), procesoArribar.getPrioridad(), procesoArribar.getRafagaCPU());
                    //indico al simulador que ordene la cola con el proceso servido
                    simuladorRR.OrdenarCola(procesoServido);
                }
            }

            //llamamos al ejecutador de procesos
            simuladorRR.EjecutarProceso();

            //MOSTRAMOS LOS RESULTADOS DE CADA EJECUCION EN LA VISTA
            MostrarSimulacionEnVista(simuladorRR.getListaArribo(), simuladorRR.getListaListos(), simuladorRR.getProcesoEjecutandose(), simuladorRR.getListaProcesosTerminados(), simuladorRR.getTiempoCPU(), simuladorRR.isCpuEnUso(), "FCFS");

            //descontamos el quantum y verificamos si ees necesario expropiar
            quantumCont -= 1;
            if (quantumCont == 0) {
                simuladorRR.Expropiar();
                quantumCont = quantum;
            }

            //simulamos que el reloj de la CPU funcione mas lento para poder ver en detalle los movimientos de cada algoritmo
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            //incremento el tiempo de CPU
            simuladorRR.setTiempoCPU(simuladorRR.getTiempoCPU() + 1);

        }

        //siempre tengo un tiempo  mas, se lo quito
        simuladorRR.setTiempoCPU(simuladorRR.getTiempoCPU() - 1);
        //calculamos los resultados
        simuladorRR.MostrarResultado();
        //aqui mostramos los resultados en la tabla de ejecucion
        //MOSTRAMOS LOS RESULTADOS DE CADA EJECUCION EN LA VISTA
        MostrarSimulacionEnVista(simuladorRR.getListaArribo(), simuladorRR.getListaListos(), simuladorRR.getProcesoEjecutandose(), simuladorRR.getListaProcesosTerminados(), simuladorRR.getTiempoCPU(), simuladorRR.isCpuEnUso(), "FCFS");

    }

    public void SimularSRT() {
        
        AlgoritmoSRT simuladorSRT = new AlgoritmoSRT();
        //inicializamos los componentes del algoritmo a simular
        simuladorSRT.InicializarComponentes();

        //seteamos la lista de procesos por arribar obteniendolos del file        
        List<Proceso> listaArribo = this.procesosUtilizar;
        simuladorSRT.setListaArribo(listaArribo);

        //mientras no hayan terminado los procesos,  voy a seguir la simulacion
        while (!simuladorSRT.isTerminaronProcesos()) {

            //un for o foreach para lista de procesos cargarlos en la cola de listos
            for (Proceso procesoArribar : simuladorSRT.getListaArribo()) {
                if (procesoArribar.getTiempoArribo() == simuladorSRT.getTiempoCPU()) {
                    //creo una instancia de proceso servido y seteo los parametros del proceso por arribar                    
                    ProcesoServido procesoServido = new ProcesoServido(procesoArribar.getId(), procesoArribar.getNombreProceso(), procesoArribar.getTiempoArribo(), procesoArribar.getRafagaCPU(), procesoArribar.getPrioridad(), procesoArribar.getRafagaCPU());
                    //indico al simulador que ordene la cola con el proceso servido
                    simuladorSRT.OrdenarCola(procesoServido);
                }
            }

            //llamamos al ejecutador de procesos
            simuladorSRT.EjecutarProceso();

            //MOSTRAMOS LOS RESULTADOS DE CADA EJECUCION EN LA VISTA
            MostrarSimulacionEnVista(simuladorSRT.getListaArribo(), simuladorSRT.getListaListos(), simuladorSRT.getProcesoEjecutandose(), simuladorSRT.getListaProcesosTerminados(), simuladorSRT.getTiempoCPU(), simuladorSRT.isCpuEnUso(), "FCFS");

            //simulamos que el reloj de la CPU funcione mas lento para poder ver en detalle los movimientos de cada algoritmo
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            //incremento el tiempo de CPU
            simuladorSRT.setTiempoCPU(simuladorSRT.getTiempoCPU() + 1);

        }
        
        //siempre tengo un tiempo  mas, se lo quito
        simuladorSRT.setTiempoCPU(simuladorSRT.getTiempoCPU() - 1);
        simuladorSRT.MostrarResultado();
        //aqui mostramos los resultados en la tabla de ejecucion
        //MOSTRAMOS LOS RESULTADOS DE CADA EJECUCION EN LA VISTA
        MostrarSimulacionEnVista(simuladorSRT.getListaArribo(), simuladorSRT.getListaListos(), simuladorSRT.getProcesoEjecutandose(), simuladorSRT.getListaProcesosTerminados(), simuladorSRT.getTiempoCPU(), simuladorSRT.isCpuEnUso(), "FCFS");



    }

    public void SimularSPN() {
         AlgoritmoSPN simuladorSPN = new AlgoritmoSPN();
        //inicializamos los componentes del algoritmo a simular
        simuladorSPN.InicializarComponentes();

        //seteamos la lista de procesos por arribar obteniendolos del file        
        List<Proceso> listaArribo = this.procesosUtilizar;
        simuladorSPN.setListaArribo(listaArribo);

        //mientras no hayan terminado los procesos,  voy a seguir la simulacion
        while (!simuladorSPN.isTerminaronProcesos()) {

            //un for o foreach para lista de procesos cargarlos en la cola de listos
            for (Proceso procesoArribar : simuladorSPN.getListaArribo()) {
                if (procesoArribar.getTiempoArribo() == simuladorSPN.getTiempoCPU()) {
                    //creo una instancia de proceso servido y seteo los parametros del proceso por arribar                    
                    ProcesoServido procesoServido = new ProcesoServido(procesoArribar.getId(), procesoArribar.getNombreProceso(), procesoArribar.getTiempoArribo(), procesoArribar.getRafagaCPU(), procesoArribar.getPrioridad(), procesoArribar.getRafagaCPU());
                    //indico al simulador que ordene la cola con el proceso servido
                    simuladorSPN.OrdenarCola(procesoServido);
                }
            }

            //llamamos al ejecutador de procesos
            simuladorSPN.EjecutarProceso();

            //MOSTRAMOS LOS RESULTADOS DE CADA EJECUCION EN LA VISTA
            MostrarSimulacionEnVista(simuladorSPN.getListaArribo(), simuladorSPN.getListaListos(), simuladorSPN.getProcesoEjecutandose(), simuladorSPN.getListaProcesosTerminados(), simuladorSPN.getTiempoCPU(), simuladorSPN.isCpuEnUso(), "FCFS");

            //simulamos que el reloj de la CPU funcione mas lento para poder ver en detalle los movimientos de cada algoritmo
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            //incremento el tiempo de CPU
            simuladorSPN.setTiempoCPU(simuladorSPN.getTiempoCPU() + 1);

        }
        
        //siempre tengo un tiempo  mas, se lo quito
        simuladorSPN.setTiempoCPU(simuladorSPN.getTiempoCPU() - 1);
        simuladorSPN.MostrarResultado();
        //aqui mostramos los resultados en la tabla de ejecucion
        //MOSTRAMOS LOS RESULTADOS DE CADA EJECUCION EN LA VISTA
        MostrarSimulacionEnVista(simuladorSPN.getListaArribo(), simuladorSPN.getListaListos(), simuladorSPN.getProcesoEjecutandose(), simuladorSPN.getListaProcesosTerminados(), simuladorSPN.getTiempoCPU(), simuladorSPN.isCpuEnUso(), "FCFS");


    }

    private void MostrarSimulacionEnVista(List<Proceso> listaProcesos, List<ProcesoServido> colaListos, ProcesoServido procesoEnCPU, List<ProcesoServido> procesosTerminados, int tiempoCPU, boolean hayProcesosEnEjecucion, String algoritmoPlanificacion) {

    }

}
