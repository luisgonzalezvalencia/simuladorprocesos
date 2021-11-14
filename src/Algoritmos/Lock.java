/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algoritmos;

/**
 *
 * @author Juan Carlos
 */
public interface Lock {
public void accederSC(int pid);
public void liberarSC(int pid);
}
