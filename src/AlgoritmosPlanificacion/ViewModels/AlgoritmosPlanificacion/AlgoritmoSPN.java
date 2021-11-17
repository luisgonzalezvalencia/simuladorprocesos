/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoritmosPlanificacion.ViewModels.AlgoritmosPlanificacion;

import AlgoritmosPlanificacion.ViewModels.Procesos.ProcesoServido;
import java.util.Collections;

/**
 *
 * @author LAGV
 */
public class AlgoritmoSPN extends AlgoritmosPlanificacion {

        @Override
    public void OrdenarCola(ProcesoServido proceso) {
        this.listaListos.add(proceso);
        Collections.sort(this.listaListos);
       
    }

    
}
