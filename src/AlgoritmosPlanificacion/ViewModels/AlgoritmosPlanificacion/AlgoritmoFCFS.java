/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoritmosPlanificacion.ViewModels.AlgoritmosPlanificacion;

import AlgoritmosPlanificacion.ViewModels.Procesos.ProcesoServido;

/**
 *
 * @author LAGV
 */
public class AlgoritmoFCFS extends AlgoritmosPlanificacion {


    @Override
    public void OrdenarCola(ProcesoServido proceso) {
        //en FCFS solo añadimos el proceso al final de la cola
        this.listaListos.add(proceso);
        
    }


}