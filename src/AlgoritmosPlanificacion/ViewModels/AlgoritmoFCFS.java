/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoritmosPlanificacion.ViewModels;

import AlgoritmosPlanificacion.ViewModels.Interfaces.AlgoritmosInterface;
import java.util.ArrayList;

/**
 *
 * @author LAGV
 */
public class AlgoritmoFCFS extends AlgoritmosSimuladorPlanificacion implements AlgoritmosInterface {


    @Override
    public void OrdenarCola(ProcesoServido proceso) {
        //en FCFS solo añadimos el proceso al final de la cola
        this.listaListos.add(proceso);
        
    }

}