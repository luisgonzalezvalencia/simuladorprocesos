/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoritmosPlanificacion.Controllers;

import AlgoritmosPlanificacion.Models.ProcesosModel;
import AlgoritmosPlanificacion.ViewModels.AlgoritmosPlanificacion.AlgoritmoExpropiativo;
import AlgoritmosPlanificacion.ViewModels.AlgoritmosPlanificacion.AlgoritmoFCFS;
import AlgoritmosPlanificacion.ViewModels.AlgoritmosPlanificacion.AlgoritmoRR;
import AlgoritmosPlanificacion.ViewModels.AlgoritmosPlanificacion.AlgoritmoSPN;
import AlgoritmosPlanificacion.ViewModels.AlgoritmosPlanificacion.AlgoritmoSRT;
import AlgoritmosPlanificacion.ViewModels.AlgoritmosPlanificacion.AlgoritmosPlanificacion;
import AlgoritmosPlanificacion.ViewModels.Procesos.Proceso;
import AlgoritmosPlanificacion.ViewModels.Procesos.ProcesoServido;
import AlgoritmosPlanificacion.Views.SimuladorView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author LAGV y JMG
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
    Integer quantum;
    Integer quantumCont;

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
            MostrarSimulacionEnVista(this.procesosUtilizar, null, null, null, -1, false, 0, this.algoritmoSeleccionado);
        }

        //agregamos los listeners de las acciones a la vista
        this.simuladorVista.btnIniciarSimulacion.addActionListener(this);
        this.simuladorVista.txtQuantumElegido.addActionListener(this);

    }

    public void Simular(AlgoritmosPlanificacion algoritmoSimular) {

//        AlgoritmoRR simuladorRR = new AlgoritmoRR();
        //inicializamos los componentes del algoritmo a simular
        algoritmoSimular.InicializarComponentes();

        //seteamos la lista de procesos por arribar obteniendolos del file        
        List<Proceso> listaArribo = this.procesosUtilizar;
        algoritmoSimular.setListaArribo(listaArribo);

        //mientras no hayan terminado los procesos,  voy a seguir la simulacion
        while (!algoritmoSimular.isTerminaronProcesos()) {

            //un for o foreach para lista de procesos cargarlos en la cola de listos
            for (Proceso procesoArribar : algoritmoSimular.getListaArribo()) {
                if (procesoArribar.getTiempoArribo() == algoritmoSimular.getTiempoCPU()) {
                    //creo una instancia de proceso servido y seteo los parametros del proceso por arribar                    
                    ProcesoServido procesoServido = new ProcesoServido(procesoArribar.getId(), procesoArribar.getNombreProceso(), procesoArribar.getTiempoArribo(), procesoArribar.getRafagaCPU(), procesoArribar.getPrioridad(), procesoArribar.getRafagaCPU());
                    //indico al simulador que ordene la cola con el proceso servido
                    algoritmoSimular.OrdenarCola(procesoServido);
                }
            }

            //llamamos al ejecutador de procesos
            algoritmoSimular.EjecutarProceso();

            //MOSTRAMOS LOS RESULTADOS DE CADA EJECUCION EN LA VISTA
            MostrarSimulacionEnVista(algoritmoSimular.getListaArribo(), algoritmoSimular.getListaListos(), algoritmoSimular.getProcesoEjecutandose(), algoritmoSimular.getListaProcesosTerminados(), algoritmoSimular.getTiempoCPU(), algoritmoSimular.isCpuEnUso(), 0, "FCFS");

            if ("RR".equals(this.algoritmoSeleccionado)) {
                //descontamos el quantum y verificamos si ees necesario expropiar
                quantumCont -= 1;
                
                //si luego de una ejecucion de RR el proceso terminó, reiniciamos el quantum
                if (algoritmoSimular.getProcesoEjecutandose() == null) {
                    quantumCont = quantum;
                }

                if (quantumCont == 0) {
                    //casteo a algoritmo expropiativo para poder usar el expropiar
                    ((AlgoritmoExpropiativo) algoritmoSimular).Expropiar();
                    quantumCont = quantum;
                }
            }

            //simulamos que el reloj de la CPU funcione mas lento para poder ver en detalle los movimientos de cada algoritmo
            this.SleepTime();
            //incremento el tiempo de CPU
            algoritmoSimular.setTiempoCPU(algoritmoSimular.getTiempoCPU() + 1);

        }

        //siempre tengo un tiempo  mas, se lo quito
        algoritmoSimular.setTiempoCPU(algoritmoSimular.getTiempoCPU() - 1);

        //calculamos los resultados
        algoritmoSimular.MostrarResultado();
        //aqui mostramos los resultados en la tabla de ejecucion
        //MOSTRAMOS LOS RESULTADOS DE CADA EJECUCION EN LA VISTA
        MostrarSimulacionEnVista(algoritmoSimular.getListaArribo(), algoritmoSimular.getListaListos(), algoritmoSimular.getProcesoEjecutandose(), algoritmoSimular.getListaProcesosTerminados(), algoritmoSimular.getTiempoCPU(), algoritmoSimular.isCpuEnUso(), algoritmoSimular.getTiempoPromedioTotal(), "FCFS");

    }

    private void SleepTime() {
        try {
            Thread.sleep(tiempoSleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void MostrarSimulacionEnVista(List<Proceso> listaProcesos, List<ProcesoServido> colaListos, ProcesoServido procesoEnCPU, List<ProcesoServido> procesosTerminados, int tiempoCPU, boolean hayProcesosEnEjecucion, float tiempoPromedioTotal, String algoritmoPlanificacion) {

        modeloTablaArribar = (DefaultTableModel) this.simuladorVista.tablaProcesosArribar.getModel();
        this.LimpiarTabla(modeloTablaArribar);
        Object[] objetoTabla = new Object[5];
        for (Proceso procesoDatos : listaProcesos) {
            objetoTabla[0] = procesoDatos.getId();
            objetoTabla[1] = procesoDatos.getNombreProceso();
            objetoTabla[2] = procesoDatos.getTiempoArribo();
            objetoTabla[3] = procesoDatos.getRafagaCPU();
            objetoTabla[4] = "NO ARRIBADO";
            //si el proceso està en la cola de listo lo marco como listo aqui
            if (colaListos != null) {
                for (ProcesoServido procesoListoDatos : colaListos) {
                    if (procesoListoDatos.getId() == procesoDatos.getId()) {
                        objetoTabla[4] = "LISTO";
                    }
                }

            }

            if (procesosTerminados != null) {
                for (ProcesoServido procesoTerminadoDatos : procesosTerminados) {
                    if (procesoTerminadoDatos.getId() == procesoDatos.getId()) {
                        objetoTabla[4] = "TERMINADO";
                    }
                }

            }

            //objetoTabla[4] = procesoDatos.getPrioridad();
            if (procesoEnCPU != null && procesoDatos.getId() == procesoEnCPU.getId()) {
                objetoTabla[4] = "PROCESANDOSE";
            }

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
                objetoTablaListos[4] = procesoListoDatos.getRafagasRestantesCPU();
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
            Object[] objetoTablaTerminados = new Object[9];
            for (ProcesoServido procesoTerminadoDatos : procesosTerminados) {
                objetoTablaTerminados[0] = procesoTerminadoDatos.getId();
                objetoTablaTerminados[1] = procesoTerminadoDatos.getNombreProceso();
                objetoTablaTerminados[2] = procesoTerminadoDatos.getTiempoArribo();
                objetoTablaTerminados[3] = procesoTerminadoDatos.getRafagaCPU();
                objetoTablaTerminados[4] = procesoTerminadoDatos.getTiempoInicio();
                objetoTablaTerminados[5] = procesoTerminadoDatos.getTiempoFinalizacion();
                objetoTablaTerminados[6] = procesoTerminadoDatos.getTiempoEstanciaTr();
                objetoTablaTerminados[7] = procesoTerminadoDatos.getTiempoPromedioTrTs();
                modeloTablaTerminados.addRow(objetoTablaTerminados);
            }
            //tabla procesos iniciales
            this.simuladorVista.tablaProcesosTerminados.setModel(modeloTablaTerminados);
        }

        //procesos en CPU
        if (procesoEnCPU != null) {
            this.simuladorVista.txtProcesoEnEjecucion.setText(procesoEnCPU.getNombreProceso());
            this.simuladorVista.txtRafagaRestanteProcesoCPU.setText(Integer.toString(procesoEnCPU.getRafagasRestantesCPU()));
        } else {
            //si no hay ninguno, limpio los campos de la vista
            this.simuladorVista.txtProcesoEnEjecucion.setText("");
            this.simuladorVista.txtRafagaRestanteProcesoCPU.setText("");
        }

        //seteo los campos
        this.simuladorVista.txtQuantumRafaga.setText(String.valueOf(this.quantumCont));
        this.simuladorVista.txtTiempoCPU.setText(String.valueOf(tiempoCPU));
        this.simuladorVista.txtNombreAlgoritmo.setText(this.algoritmoSeleccionado);
        this.simuladorVista.txtTiempoPromedioTotal.setText(Float.toString(tiempoPromedioTotal));

    }

    @Override
    public void run() {
        //dependiendo el algoritmo, llamo al metodo correspondiente        
        switch (this.algoritmoSeleccionado) {
            case "FCFS":
                this.Simular(new AlgoritmoFCFS());
                break;
            case "RR":
                this.quantum = Integer.parseInt(this.simuladorVista.txtQuantumElegido.getText());
                this.quantumCont = this.quantum;
                this.Simular(new AlgoritmoRR());
                break;
            case "SPN":
                this.Simular(new AlgoritmoSPN());
                break;
            case "SRT":
                this.Simular(new AlgoritmoSRT());
                break;
            default:
                this.Simular(new AlgoritmoFCFS());
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
