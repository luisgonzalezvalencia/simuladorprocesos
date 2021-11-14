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
public class AlgoritmoPeterson  {
boolean quieroSC[] = {false, false};
int turno = 1;
public void accederSC(int i) {
int j = 1 - i;
quieroSC[i] = true; // manifiesto mi intencion de entrar
turno = j;
while (quieroSC[j] && (turno == j)) ; // Esta ocupado
}
public void liberarSC(int i) {
quieroSC[i] = false;
}
}
