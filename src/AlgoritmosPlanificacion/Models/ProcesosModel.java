/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoritmosPlanificacion.Models;

import AlgoritmosPlanificacion.ViewModels.Proceso;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author LAGV
 *
 * vamos a escribir en el archivo los datos de los procesos
 */
public class ProcesosModel {

    private final String rutaFile = System.getProperties().getProperty("user.dir");
    //variables de procesosModel
    File procesosFile = null;
    FileReader procesosFileReader = null;
    FileWriter procesosFileWriter = null;
    PrintWriter procesosFileWriterLinea = null;
    BufferedReader procesosBuffer = null;

    //Obtenenmos el listado de procesos del archivo de texto usado como base de datos de los procesos.
    public List ObtenerListadoProcesos() {
        //obtenemos del archivo todos los procesos
        List<Proceso> procesos = new ArrayList<>();
        //el try catch es para manejar los errores.. en este caso pueden ser de lectura-escritura del archivo
        //para facilitar, vamos a usar un solo archivo para guardar todos los procesos.
        //En la carpeta AlmacenamientoProcesos van a estar todos.
        try {
            //apertura del archivo y creacion de buffer para poder hacer una lectura comodo con el metodo readLine()
            //cada fila es un proceso con el id - nombreProceso - tiempoArribo - rafagaCPU - prioridad
            procesosFile = new File(rutaFile + "//src//AlgoritmosPlanificacion//Models//AlmacenamientoProcesos//procesos.txt");
            procesosFileReader = new FileReader(procesosFile);
            procesosBuffer = new BufferedReader(procesosFileReader);

            //si no existe el archivo, lo creamos
            if (!procesosFile.exists()) {
                procesosFile.createNewFile();
            }
            String linea;
            //mientras el readline devuelva una fila, significa que hay procesos
            while ((linea = procesosBuffer.readLine()) != null) {
                System.out.println(linea);
                Proceso lecturaProceso = new Proceso();
                //separamos los datos con - por cada dato del proceso
                String[] datosProceso = linea.split("-");

                //suponemos que siempre el orden se guarda correctamente
                lecturaProceso.setId(Integer.parseInt(datosProceso[0]));
                lecturaProceso.setNombreProceso(datosProceso[1]);
                lecturaProceso.setTiempoArribo(Integer.parseInt(datosProceso[2]));
                lecturaProceso.setRafagaCPU(Integer.parseInt(datosProceso[3]));
                lecturaProceso.setPrioridad(Integer.parseInt(datosProceso[4]));

                //agregamos el proceso a la lista de procesos
                procesos.add(lecturaProceso);
            }

        } catch (IOException | NumberFormatException e) {
            //hubo un error en la lectura de los datos
            System.out.println(e.getMessage());
        } finally {
            try {
                //intentamos cerrar la conexxion
                if (procesosFileReader != null) {
                    procesosFileReader.close();
                }
            } catch (IOException e2) {
                //no se pudo cerrar la conexion
                System.out.println(e2.getMessage());
            }
        }

        return procesos;
    }

    //devolvemos true o false si se agrega correctamente
    //luego debemos recargar el listado
    public boolean AgregarProceso(Proceso proceso) {
        //variable a devolver en true o false si todo fue correcto o no
        boolean agregarProcesoEstado;
        //Debemos Verificar si el proceso ya existe con el Id enviado, lo sobreescribimos.
        //Sino creamos uno nuevo
        //creo una bandera para saber si es edicion (default false)
        boolean edicion = false;
        List<Proceso> procesos = this.ObtenerListadoProcesos();
        //si es edicion, al salir del for voy a tener en procesoEditar un valor distinto de null
        Proceso procesoEditar = null;
        //si entra en alguno, edicion va a igualar a true;
        for (Proceso procesoDatos : procesos) {
            if (procesoDatos.getId() == proceso.getId()) {
                edicion = true;
                procesoEditar = procesoDatos;
            }
        }

        //cuando es edicion, debemos borrar todos los datos del archivo y poner la lista nueva con los datos nuevos.
        if (edicion && procesoEditar != null) {
            procesoEditar.setNombreProceso(proceso.getNombreProceso());
            procesoEditar.setTiempoArribo(proceso.getTiempoArribo());
            procesoEditar.setRafagaCPU(proceso.getRafagaCPU());
            procesoEditar.setPrioridad(proceso.getPrioridad());
        } else {
            //si no es edicion, solo agregamos al final de la lista el nuevo proceso y luego al archivo
            procesos.add(proceso);
        }

          //haciendo refactoring, vemos que toda esta parte se utiliza en edicion y agregacion de procesos
        //creamos nuevo método con try catch para EscribirProcesosFile
        //vamos a abrir el archivo, borrarlo y cargar todos los procesos nuevamente
        agregarProcesoEstado = EscribirProcesosFile(procesos);

        return agregarProcesoEstado;
    }

    public boolean EliminarProceso(Proceso proceso) {
        //variable a devolver en true o false si todo fue correcto o no
        boolean eliminarProcesoEstado;
        //Debemos Verificar si el proceso ya existe con el Id enviado, lo sobreescribimos.
        //Sino creamos uno nuevo
        //creo una bandera para saber si es edicion (default false)
        boolean existeProceso = false;
        List<Proceso> procesos = this.ObtenerListadoProcesos();
        //si es edicion, al salir del for voy a tener en procesoEditar un valor distinto de null
        Proceso procesoEliminarArchivo = null;
        //si entra en alguno, edicion va a igualar a true;
        for (Proceso procesoDatos : procesos) {
            if (procesoDatos.getId() == proceso.getId()) {
                existeProceso = true;
                procesoEliminarArchivo = procesoDatos;
            }
        }

        //si existe el proceso, lo quitamos de la lista
        if (existeProceso && procesoEliminarArchivo != null) {
            procesos.remove(procesoEliminarArchivo);
        } else {
            //si no existe, directamente devolvemos true
            return true;
        }

        //haciendo refactoring, vemos que toda esta parte se utiliza en edicion y agregacion de procesos
        //creamos nuevo método con try catch para EscribirProcesosFile
        //vamos a abrir el archivo, borrarlo y cargar todos los procesos nuevamente
        eliminarProcesoEstado = EscribirProcesosFile(procesos);
        
        //retornamos el resultado
        return eliminarProcesoEstado;
    }

    private boolean EscribirProcesosFile(List<Proceso> procesos) {
        //bandera actualizacion file
        boolean resultadoEscritura = false;

        try {
            //apertura del archivo y creacion de buffer para poder hacer una lectura comodo con el metodo readLine()
            //cada fila es un proceso con el id - nombreProceso - tiempoArribo - rafagaCPU - prioridad
            procesosFile = new File(rutaFile + "//src//AlgoritmosPlanificacion//Models//AlmacenamientoProcesos//procesos.txt");
            procesosFileWriter = new FileWriter(procesosFile);
            procesosBuffer = new BufferedReader(procesosFileReader);
            //si no existe el archivo, lo creamos
            if (!procesosFile.exists()) {
                procesosFile.createNewFile();
            }

            //escribimos en el archivo desde 0
            procesosFileWriter = new FileWriter(procesosFile);
            procesosFileWriterLinea = new PrintWriter(procesosFileWriter);

            //si no hay procesos, debo borrar todas las lineas
            if (procesos.isEmpty()) {
                procesosFileWriterLinea.println("");
            } else {
                //por cada proceso en el listado, voy a escribir la linea en el  archivo
                for (Proceso procesoDatos : procesos) {
                    String lineaProceso = procesoDatos.getId() + "-" + procesoDatos.getNombreProceso() + "-" + procesoDatos.getTiempoArribo() + "-" + procesoDatos.getRafagaCPU() + "-" + procesoDatos.getPrioridad();
                    procesosFileWriterLinea.println(lineaProceso);
                }
            }

            //seteo todo como correcto
            resultadoEscritura = true;

        } catch (Exception e) {
            //hubo un error en la lectura de los datos
            System.out.println(e.getMessage());
        } finally {
            try {
                //intentamos cerrar la conexxion
                if (procesosFileWriter != null) {
                    procesosFileWriterLinea.close();
                    procesosFileWriter.close();
                }
            } catch (IOException e2) {
                //no se pudo cerrar la conexion
                System.out.println(e2.getMessage());
            }
        }
        
        return resultadoEscritura;
    }
}
