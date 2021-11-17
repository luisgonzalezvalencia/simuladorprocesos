/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoritmosPlanificacion.ViewModels;

import AlgoritmosPlanificacion.ViewModels.Interfaces.AlgoritmoExpropiativoInterface;

import java.util.ArrayList;

/**
 *
 * @author LAGV
 */
public class AlgoritmoRR extends AlgoritmosSimuladorPlanificacion implements AlgoritmoExpropiativoInterface {

    @Override
    public void Expropiar() {
        
        if (this.procesoEjecutandose != null) {
            //agregar el proceso a la cola de listos
            this.OrdenarCola(this.procesoEjecutandose);
            // quitar el proceso de la cpu
            this.procesoEjecutandose = null;
            // libero la cpu
            this.cpuEnUso = false;
        }
    }


    @Override
    public void OrdenarCola(ProcesoServido proceso) {
        //en RR solo añadimos el proceso al final de la cola
        this.listaListos.add(proceso);

    }

}
