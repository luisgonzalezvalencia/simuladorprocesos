/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoritmosPlanificacion.ViewModels.Interfaces;

import AlgoritmosPlanificacion.ViewModels.ProcesoServido;

/**
 *
 * @author LAGV
 */
public interface AlgoritmosInterface {
    public void InicializarComponentes();
    public void OrdenarCola(ProcesoServido p);
    public void EjecutarProceso();
    public void MostrarResultado();
}
