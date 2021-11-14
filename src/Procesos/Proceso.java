/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Procesos;

import Algoritmos.LamportView;
import Algoritmos.Lock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Augusto
 */
public class Proceso implements Comparable<Proceso>, Runnable {
    
    private int id;
    private String nombre;
    private int tiempoLlegada;
    private int rafagaCpu;
    private boolean bloqueado;
    private boolean seleccionado;
    private LamportView lamportView;
    
    int myId;
    Lock lock;
    static int Total=0; //  Seccion Crítica!!!
    public Proceso(int id, Lock lock, LamportView lv) {
        myId = id;
        this.lock = lock;
        lamportView = lv;
    }
    
    void nonCriticalSection() {
        System.out.println("Proceso " + myId + " SALIO de la seccion critica\n");
        lamportView.getResultadoLamport().setText(lamportView.getResultadoLamport().getText() + " \n Proceso " + myId + " SALIO de la seccion critica\n");
       
    }
    void CriticalSection() {
        System.out.println("Proceso " + myId + " ENTRO en la seccion critica");
        lamportView.getResultadoLamport().setText(lamportView.getResultadoLamport().getText() + " \n Proceso " + myId + " ENTRO en la seccion critica");
    }
    

    public Proceso() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTiempoLlegada() {
        return tiempoLlegada;
    }

    public void setTiempoLlegada(int tiempoLlegada) {
        this.tiempoLlegada = tiempoLlegada;
    }

    public int getRafagaCpu() {
        return rafagaCpu;
    }

    public void setRafagaCpu(int rafagaCpu) {
        this.rafagaCpu = rafagaCpu;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }
    
    public String mostrar(){ // defino mi método mostrar para poder visualizar mi objeto de la manera en que necesito en SwappingView
        //System.out.println(this.nom + " " + this.bloqueado);
        String aux ;
        if(this.isBloqueado() == true ){ //verifico si  esta bloqueado retorno el String "Bloqueado", caso contrario retorno "Listo"
            aux = "Bloqueado";
        }else{ aux = "Listo";}
        return (this.getNombre()+ " - " + aux); //retorno una cadena de caracteres con el nombre y el estado bbloqueado
    }
    
    @Override
        public int compareTo(Proceso p) {
            if (this.getTiempoLlegada() < p.getTiempoLlegada()) {
                return -1;
            }
            if (this.getTiempoLlegada() > p.getTiempoLlegada()) {
                return 1;
            }
            return 0;
        }

    @Override
    public void run() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(Proceso.class.getName()).log(Level.SEVERE, null, ex);
        }
        lock.accederSC(myId);       
        System.out.println("Proceso " + myId + " DESEA entrar en la seccion Critica");
        lamportView.getResultadoLamport().setText(lamportView.getResultadoLamport().getText() + " \n Proceso " + myId + " DESEA entrar en la seccion Critica");
        CriticalSection();
        lock.liberarSC(myId);
        nonCriticalSection();
    }
    
    
}
