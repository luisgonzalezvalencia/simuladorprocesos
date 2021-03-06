/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algoritmos;

import Procesos.Proceso;
import Procesos.ProcesoTableModel;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JTable;
import javax.swing.JTextArea;

/**
 *
 * @author Juan Carlos
 */
public class LamportView extends javax.swing.JFrame {

    public static ArrayList<Proceso> listProcess;
    public static ArrayList<Proceso> listProcessTerminados = new ArrayList<Proceso>();
    public static boolean seccionCriticaOcupada;
    
    public static ProcesoTableModel procesoTableModelEspera;
    public static ProcesoTableModel procesoTableModelEnCurso;
    public static ProcesoTableModel procesoTableModelSalientes;
    
    
    /**
     * Creates new form LamportView
     */
    public LamportView(ArrayList<Proceso> l_listProcess) {
        initComponents();
        Collections.sort(l_listProcess);
        this.setListProcess(l_listProcess);
        this.setProcesoTableModelEspera(new ProcesoTableModel(l_listProcess));
        this.setProcesoTableModelEnCurso(new ProcesoTableModel(new ArrayList<Proceso>()));
        this.setProcesoTableModelSalientes(new ProcesoTableModel(new ArrayList<Proceso>()));
        Thread t[];

        t=new Thread[l_listProcess.size()];
        Lock lock = new Lamport(l_listProcess.size());
        // o algun otro algoritmo de mutex
        // Lock lock = new Bakery(N);
        for (int i=0; i<l_listProcess.size(); i++) {
            t[i] = new Thread(new Proceso(i, lock, this));
            t[i].start();
            //System.out.println("Proceso " + i + " DESEA entrar en la seccion Critica");
        }
       
    }

    public JTextArea getResultadoLamport() {
        return resultadoLamport;
    }

    public void setResultadoLamport(JTextArea resultadoLamport) {
        this.resultadoLamport = resultadoLamport;
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        resultadoLamport = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Muestras de Resultados");

        resultadoLamport.setColumns(20);
        resultadoLamport.setRows(5);
        resultadoLamport.setAlignmentX(400.0F);
        resultadoLamport.setAlignmentY(400.0F);
        jScrollPane1.setViewportView(resultadoLamport);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 601, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(365, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(594, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public ProcesoTableModel getProcesoTableModelEspera() {
        return procesoTableModelEspera;
    }

    public void setProcesoTableModelEspera(ProcesoTableModel procesoTableModelEspera) {
        this.procesoTableModelEspera = procesoTableModelEspera;
    }

    public ProcesoTableModel getProcesoTableModelEnCurso() {
        return procesoTableModelEnCurso;
    }

    public void setProcesoTableModelEnCurso(ProcesoTableModel procesoTableModelEnCurso) {
        this.procesoTableModelEnCurso = procesoTableModelEnCurso;
    }

    public ProcesoTableModel getProcesoTableModelSalientes() {
        return procesoTableModelSalientes;
    }

    public void setProcesoTableModelSalientes(ProcesoTableModel procesoTableModelSalientes) {
        this.procesoTableModelSalientes = procesoTableModelSalientes;
    }
    
    
    
    public ArrayList<Proceso> getListProcess() {
        return listProcess;
    }

    private void setListProcess(ArrayList<Proceso> listProcess) {
        this.listProcess = listProcess;
    }

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea resultadoLamport;
    // End of variables declaration//GEN-END:variables
}
