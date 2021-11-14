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
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author LAGV
 */

//final previene herencia. en este caso no se necesita por lo que la clase es final
public final class ProcesosController implements ActionListener{
    ProcesosModel _procesosModel = new ProcesosModel();
    DefaultTableModel modeloTabla = new DefaultTableModel();
    ProcesosView procesosVista = new ProcesosView();
    
    //constructor de la clase
    public ProcesosController(ProcesosView procesosVista){
        this.procesosVista = procesosVista;
        if(this.procesosVista.isVisible()){
            MostrarListadoProcesos(this.procesosVista.tablaProcesosPlanficados);
        }
    }
    
    //el controlador se comunica con el modelo y setea en la vista la lista de procesos
    public void MostrarListadoProcesos(JTable tabla) {
        modeloTabla = (DefaultTableModel)tabla.getModel();
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

    //al implementar la clase ActionListener, debemos implementar sus metodos
    @Override
    public void actionPerformed(ActionEvent ae) {
        
    }
}
