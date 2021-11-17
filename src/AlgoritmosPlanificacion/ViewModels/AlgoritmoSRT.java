/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoritmosPlanificacion.ViewModels;

import AlgoritmosPlanificacion.ViewModels.Interfaces.AlgoritmoExpropiativoInterface;
import Procesos.Proceso;
import java.util.Collections;

/**
 *
 * @author LAGV
 */
public class AlgoritmoSRT extends AlgoritmosSimuladorPlanificacion implements AlgoritmoExpropiativoInterface {

    @Override
    public void OrdenarCola(ProcesoServido proceso) {
        this.listaListos.add(proceso);
        Collections.sort(this.listaListos);
        if ( this.cpuEnUso && this.listaListos.get(0).getRafagasRestantesCPU() < this.procesoEjecutandose.getRafagasRestantesCPU()) {
            this.Expropiar();
        }

    }

    @Override
    public void Expropiar() {

        if (this.procesoEjecutandose != null) {
            // libero la cpu
            this.cpuEnUso = false;
            //agregar el proceso a la cola de listos
            this.OrdenarCola(this.procesoEjecutandose);
            // quitar el proceso de la cpu
            this.procesoEjecutandose = null;

        }
    }

}
