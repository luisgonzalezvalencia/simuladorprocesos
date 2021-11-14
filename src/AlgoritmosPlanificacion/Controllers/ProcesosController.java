/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoritmosPlanificacion.Controllers;

import java.util.List;
import AlgoritmosPlanificacion.Models.Proceso;
import AlgoritmosPlanificacion.Models.ProcesosModel;
import AlgoritmosPlanificacion.Views.ProcesosView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author LAGV
 */
//final previene herencia. en este caso no se necesita por lo que la clase es final
public final class ProcesosController implements ActionListener {

    ProcesosModel _procesosModel = new ProcesosModel();
    DefaultTableModel modeloTabla = new DefaultTableModel();
    ProcesosView procesosVista = new ProcesosView();

    //constructor de la clase
    public ProcesosController(ProcesosView procesosVista) {
        this.procesosVista = procesosVista;
        if (this.procesosVista.isVisible()) {
            MostrarListadoProcesos(this.procesosVista.tablaProcesosPlanficados);
        }

        //agregamos los listeners de las acciones a la vista
        this.procesosVista.btnRecargarListado.addActionListener(this);
        this.procesosVista.btnGuardar.addActionListener(this);
        this.procesosVista.btnEditar.addActionListener(this);
        this.procesosVista.btnLimpiarDatos.addActionListener(this);
        this.procesosVista.btnEliminarProceso.addActionListener(this);
    }

    //el controlador se comunica con el modelo y setea en la vista la lista de procesos
    public void MostrarListadoProcesos(JTable tabla) {
        modeloTabla = (DefaultTableModel) tabla.getModel();
        //llamamos al proceso que limpia la tabla de procesos
        this.LimpiarTablaProcesos();
        List<Proceso> listaProcesos = _procesosModel.ObtenerListadoProcesos();
        //defino un objeto de 5 elementos que son las columnas de la tabla de la vista de procesos
        Object[] objetoTabla = new Object[5];
        for (Proceso procesoDatos : listaProcesos) {
            objetoTabla[0] = procesoDatos.getId();
            objetoTabla[1] = procesoDatos.getNombreProceso();
            objetoTabla[2] = procesoDatos.getTiempoArribo();
            objetoTabla[3] = procesoDatos.getRafagaCPU();
            objetoTabla[4] = procesoDatos.getPrioridad();
            modeloTabla.addRow(objetoTabla);
        }
        //seteo los datos en la tabla
        procesosVista.tablaProcesosPlanficados.setModel(modeloTabla);
    }

    public void AgregarProceso() {
        //creo una nueva instancia de proceso0
        Proceso procesoAgregar = new Proceso();
        //seteo los valores al objeto
        procesoAgregar.setId(Integer.parseInt(procesosVista.txtIdProceso.getText()));
        procesoAgregar.setNombreProceso(procesosVista.txtNombreProceso.getText());
        procesoAgregar.setTiempoArribo(Integer.parseInt(procesosVista.txtTiempoArribo.getText()));
        procesoAgregar.setRafagaCPU(Integer.parseInt(procesosVista.txtRafagaCPU.getText()));
        procesoAgregar.setPrioridad(Integer.parseInt(procesosVista.txtPrioridad.getText()));

        boolean resultadoAgregar = _procesosModel.AgregarProceso(procesoAgregar);

        //si se agregó correctamente el proceso, actualizo la tabla
        if (resultadoAgregar) {
            this.MostrarListadoProcesos(procesosVista.tablaProcesosPlanficados);
            JOptionPane.showMessageDialog(procesosVista, "Proceso Guardado Correctamente");
        } else {
            JOptionPane.showConfirmDialog(procesosVista, "Error: no se pudo guardar el proceso, intente nuevamente");
        }

    }

    public void EditarFila() {
        int filaSeleccionada = procesosVista.tablaProcesosPlanficados.getSelectedRow();
        //si no hay ninguna fila seleccionada, mostramos mensaje
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(procesosVista, "Debe seleccionar una fila de la tabla de Procesos.");
        } else {
            //todos string ya que son para los input box
            String idProceso = (String) procesosVista.tablaProcesosPlanficados.getValueAt(filaSeleccionada, 0).toString();
            String nombreProceso = (String) procesosVista.tablaProcesosPlanficados.getValueAt(filaSeleccionada, 1).toString();
            String tiempoArriboProceso = (String) procesosVista.tablaProcesosPlanficados.getValueAt(filaSeleccionada, 2).toString();
            String rafagaCPUProceso = (String) procesosVista.tablaProcesosPlanficados.getValueAt(filaSeleccionada, 3).toString();
            String prioridadProceso = (String) procesosVista.tablaProcesosPlanficados.getValueAt(filaSeleccionada, 4).toString();

            procesosVista.txtIdProceso.setText(idProceso);
            procesosVista.txtNombreProceso.setText(nombreProceso);
            procesosVista.txtTiempoArribo.setText(tiempoArriboProceso);
            procesosVista.txtRafagaCPU.setText(rafagaCPUProceso);
            procesosVista.txtPrioridad.setText(prioridadProceso);

            //el id de proceso no se puede modificar de un proceso existente para evitar errores
            procesosVista.txtIdProceso.setEditable(false);
        }

    }

    public void EliminarProceso() {
        int filaSeleccionada = procesosVista.tablaProcesosPlanficados.getSelectedRow();
        //si no hay ninguna fila seleccionada, mostramos mensaje
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(procesosVista, "Debe seleccionar una fila de la tabla de Procesos.");
        } else {
            int confirmacionEliminar = JOptionPane.showConfirmDialog(procesosVista, "¿Está seguro que desea eliminar el proceso seleccionado?");

            if (confirmacionEliminar == 0) {
                //todos string ya que son para los input box
                int idProceso = Integer.parseInt((String) procesosVista.tablaProcesosPlanficados.getValueAt(filaSeleccionada, 0).toString());
                String nombreProceso = (String) procesosVista.tablaProcesosPlanficados.getValueAt(filaSeleccionada, 1).toString();
                int tiempoArriboProceso = Integer.parseInt((String) procesosVista.tablaProcesosPlanficados.getValueAt(filaSeleccionada, 2).toString());
                int rafagaCPUProceso = Integer.parseInt((String) procesosVista.tablaProcesosPlanficados.getValueAt(filaSeleccionada, 3).toString());
                int prioridadProceso = Integer.parseInt((String) procesosVista.tablaProcesosPlanficados.getValueAt(filaSeleccionada, 4).toString());
                //creamos una nueva instancia y la mandamos al modelo a eliminar del archivo
                Proceso procesoEliminar = new Proceso(idProceso, nombreProceso, tiempoArriboProceso, rafagaCPUProceso, prioridadProceso);
                boolean eliminacionProceso = _procesosModel.EliminarProceso(procesoEliminar);

                //si se agregó correctamente el proceso, actualizo la tabla
                if (eliminacionProceso) {
                    this.MostrarListadoProcesos(procesosVista.tablaProcesosPlanficados);
                    JOptionPane.showMessageDialog(procesosVista, "Proceso Eliminado Correctamente");
                } else {
                    JOptionPane.showConfirmDialog(procesosVista, "Error: no se pudo eliminar el proceso, intente nuevamente");
                }
            }

        }
    }

    public void LimpiarTablaProcesos() {
        //debemos limpiar la tabla antes de cargar los datos nuevamente para que no se repitan las filas
        for (int i = 0; i < procesosVista.tablaProcesosPlanficados.getRowCount(); i++) {
            modeloTabla.removeRow(i);
            //al remover siempre la primera fila es la 0 por lo que debemos volver el valor de i a -1
            i = i - 1;
        }
    }

    public void LimpiarDatosProceso() {
        //limpiamos los datos y habilitamos el id para poder crear procesos desde 0

        procesosVista.txtIdProceso.setText("");
        procesosVista.txtNombreProceso.setText("");
        procesosVista.txtTiempoArribo.setText("");
        procesosVista.txtRafagaCPU.setText("");
        procesosVista.txtPrioridad.setText("");

        procesosVista.txtIdProceso.setEditable(true);
    }

    //al implementar la clase ActionListener, debemos implementar sus metodos
    @Override
    public void actionPerformed(ActionEvent e) {

        //seteamos las acciones sobre la vista
        if (e.getSource() == procesosVista.btnRecargarListado) {
            MostrarListadoProcesos(procesosVista.tablaProcesosPlanficados);
        }

        //seteamos las acciones sobre la vista
        if (e.getSource() == procesosVista.btnGuardar) {
            AgregarProceso();
        }

        //si apretamos en editar, debemos habilitar la edicion en la vista
        if (e.getSource() == procesosVista.btnEditar) {
            EditarFila();
        }

        if (e.getSource() == procesosVista.btnLimpiarDatos) {
            LimpiarDatosProceso();
        }

        if (e.getSource() == procesosVista.btnEliminarProceso) {
            EliminarProceso();
        }
    }
}
