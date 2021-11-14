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
public class Lamport implements Lock {
int cantidadProcesos ;
boolean [] elegir; // inside doorway
int [] number;
public Lamport (int numProc) {
cantidadProcesos=numProc;
elegir = new boolean[cantidadProcesos];
number = new int[cantidadProcesos];
for (int j = 0; j <cantidadProcesos; j++) {
elegir[j] = false;
number[j]=0;
}
}

@Override
public void accederSC(int i) {
// paso 1: elegir un numero de atencion
// tomamos el ultimo numero disponible
elegir[i] = true;
for (int j=0; j<cantidadProcesos; j++)   
if (number[j] > number[i])
number[i] = number[j];
number[i]++;
elegir[i] = false;
// paso 2: verificar si mi numero es el menor de todos
for(int j=0; j<cantidadProcesos; j++) {
while (elegir[j]) ; // proceso j intentando tomar un numero
//Esta ocupado esperar..
while ( (number[j] !=0) && ( (number[j] < number[i]) || ((number[j] == number[i]) && j<i) ) ) ;
}
}
@Override
public void liberarSC (int i) {
number[i] = 0;
}

    
}