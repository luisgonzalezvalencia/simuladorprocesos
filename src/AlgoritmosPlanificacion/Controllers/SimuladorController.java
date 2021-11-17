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
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author LAGV
 */
public class SimuladorController implements ActionListener, Runnable {
    
    ProcesosModel _procesosModel = new ProcesosModel();
    SimuladorView simuladorVista;
    //si queremos ver una ejecucion detenida paso a paso cuando cambia de proceso
    boolean detenerPasoPaso = false;
    List<Proceso> procesosUtilizar;
    String algoritmoSeleccionado;
    DefaultTableModel modeloTablaArribar = new DefaultTableModel();
    DefaultTableModel modeloTablaListos = new DefaultTableModel();
    DefaultTableModel modeloTablaTerminados = new DefaultTableModel();
    Integer tiempoSleep = 1000;
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        //seteamos las acciones sobre la vista
        if (ae.getSource() == simuladorVista.btnIniciarSimulacion) {
            //dependiendo el algoritmo llamamos al metodo correspondiente
            //
            //ejecutamos el hilo de la clase actua
            new Thread(this).start();
            
        }
        
        if (ae.getSource() == simuladorVista.txtQuantumElegido) {
            if (!"".equals(simuladorVista.txtQuantumElegido.getText())) {
                if (Integer.parseInt(this.simuladorVista.txtQuantumElegido.getText()) > 0) {
                    this.simuladorVista.btnIniciarSimulacion.setEnabled(true);
                    ////seteo en true el enabled y salgo
                    return;
                }                
            }
            this.simuladorVista.btnIniciarSimulacion.setEnabled(false);
        }
    }
    
    public SimuladorController(SimuladorView simuladorVista, String algoritmoSeleccionado) {
        this.algoritmoSeleccionado = algoritmoSeleccionado;
        this.simuladorVista = simuladorVista;
        this.procesosUtilizar = _procesosModel.ObtenerListadoProcesos();
        
        switch (this.algoritmoSeleccionado) {
            case "RR":
                this.simuladorVista.txtQuantumElegido.setVisible(true);
                this.simuladorVista.lblQuantum.setVisible(true);
                this.simuladorVista.lblQuantumRafaga.setVisible(true);
                this.simuladorVista.txtQuantumRafaga.setVisible(true);
                this.simuladorVista.btnIniciarSimulacion.setEnabled(false);
                break;
            default:
                this.simuladorVista.txtQuantumElegido.setVisible(false);
                this.simuladorVista.lblQuantum.setVisible(false);
                this.simuladorVista.lblQuantumRafaga.setVisible(false);
                this.simuladorVista.txtQuantumRafaga.setVisible(false);
        }
        
        if (this.simuladorVista.isVisible()) {
            MostrarSimulacionEnVista(this.procesosUtilizar, null, null, null, -1, false, this.algoritmoSeleccionado);
        }

        //agregamos los listeners de las acciones a la vista
        this.simuladorVista.btnIniciarSimulacion.addActionListener(this);
        this.simuladorVista.txtQuantumElegido.addActionListener(this);
        
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
            this.SleepTime();

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
            this.SleepTime();
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
    
    private void SleepTime() {
        try {
            Thread.sleep(tiempoSleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
            this.SleepTime();
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
            this.SleepTime();

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
        
        modeloTablaArribar = (DefaultTableModel) this.simuladorVista.tablaProcesosArribar.getModel();
        this.LimpiarTabla(modeloTablaArribar);
        Object[] objetoTabla = new Object[5];
        for (Proceso procesoDatos : listaProcesos) {
            objetoTabla[0] = procesoDatos.getId();
            objetoTabla[1] = procesoDatos.getNombreProceso();
            objetoTabla[2] = procesoDatos.getTiempoArribo();
            objetoTabla[3] = procesoDatos.getRafagaCPU();
            objetoTabla[4] = procesoDatos.getPrioridad();
            modeloTablaArribar.addRow(objetoTabla);
        }
        //tabla procesos iniciales
        this.simuladorVista.tablaProcesosArribar.setModel(modeloTablaArribar);

        //tabla procesos en cola de listos
        //validamos que no este vacia la cola
        if (colaListos != null) {
            modeloTablaListos = (DefaultTableModel) this.simuladorVista.tablaColaListos.getModel();
            this.LimpiarTabla(modeloTablaListos);
            Object[] objetoTablaListos = new Object[5];
            for (ProcesoServido procesoListoDatos : colaListos) {
                objetoTablaListos[0] = procesoListoDatos.getId();
                objetoTablaListos[1] = procesoListoDatos.getNombreProceso();
                objetoTablaListos[2] = procesoListoDatos.getTiempoArribo();
                objetoTablaListos[3] = procesoListoDatos.getRafagaCPU();
                objetoTablaListos[4] = procesoListoDatos.getPrioridad();
                modeloTablaListos.addRow(objetoTablaListos);
            }
            //tabla procesos iniciales
            this.simuladorVista.tablaColaListos.setModel(modeloTablaListos);
        }

        //tabla procesos terminados
        //validamos que no este vacia la cola
        if (procesosTerminados != null) {
            modeloTablaTerminados = (DefaultTableModel) this.simuladorVista.tablaProcesosTerminados.getModel();
            this.LimpiarTabla(modeloTablaTerminados);
            Object[] objetoTablaTerminados = new Object[5];
            for (ProcesoServido procesoTerminadoDatos : procesosTerminados) {
                objetoTablaTerminados[0] = procesoTerminadoDatos.getId();
                objetoTablaTerminados[1] = procesoTerminadoDatos.getNombreProceso();
                objetoTablaTerminados[2] = procesoTerminadoDatos.getTiempoArribo();
                objetoTablaTerminados[3] = procesoTerminadoDatos.getRafagaCPU();
                objetoTablaTerminados[4] = procesoTerminadoDatos.getPrioridad();
                modeloTablaTerminados.addRow(objetoTablaTerminados);
            }
            //tabla procesos iniciales
            this.simuladorVista.tablaProcesosTerminados.setModel(modeloTablaTerminados);
        }

        //proceso en CPU
        if (procesoEnCPU != null) {
            this.simuladorVista.txtProcesoEnEjecucion.setText(procesoEnCPU.getNombreProceso());
        }

        //seteo los campos
        this.simuladorVista.txtTiempoCPU.setText(String.valueOf(tiempoCPU));
        this.simuladorVista.txtNombreAlgoritmo.setText(algoritmoPlanificacion);
        
    }
    
    @Override
    public void run() {
        //dependiendo el algoritmo, llamo al metodo correspondiente
        
        switch (this.algoritmoSeleccionado) {
            case "FCFS":
                SimularFCFS();
                break;
            case "RR":
                SimularRR(Integer.parseInt(this.simuladorVista.txtQuantumElegido.getText()));
                break;
            case "SPN":
                SimularSPN();
                break;
            case "SRT":
                SimularSRT();
                break;
            default:
                break;
        }
    }
    
    private void LimpiarTabla(final DefaultTableModel model) {
        //debemos limpiar la tabla antes de cargar los datos nuevamente para que no se repitan las filas
        for (int i = model.getRowCount() - 1; i >= 0; i--) {
            model.removeRow(i);
        }
        
    }
    
}
