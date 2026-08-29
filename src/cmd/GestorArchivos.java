/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cmd;

/**
 *
 * @author David Suazo Palao
 */
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class GestorArchivos {
    
    private final File directorioRaiz;
    private File directorioActual;
    
    public GestorArchivos(){
        this.directorioRaiz=new File(System.getProperty("user.dir"),"ConsolaSim");
        if(!directorioRaiz.exists()){
            directorioRaiz.mkdirs();
        }
        this.directorioActual=directorioRaiz;
    }
    
    public File getDirectorioActual() {
        return directorioActual;
    }

    public String obtenerPrompt() {
        String relativa = directorioActual.getAbsolutePath().substring(directorioRaiz.getAbsolutePath().length());
        if (relativa.startsWith(File.separator)) {
            relativa = relativa.substring(1);
        }
        return "C:\\ConsolaSimulada" + (relativa.isEmpty() ? "" : "\\" + relativa) + ">";
    }

    public String crearDirectorio(String nombre) {
        File f = new File(directorioActual, nombre);
        if (f.exists()) {
            return "Ya existe un archivo o carpeta con ese nombre.\n";
        }
        if (f.mkdir()) {
            return "Carpeta creada correctamente.\n";
        }
        return "No se pudo crear la carpeta.\n";
    }

    public String crearArchivo(String nombre) {
        File f = new File(directorioActual, nombre);
        if (f.exists()) {
            return "El archivo ya existe.\n";
        }
        try {
            if (f.createNewFile()) {
                return "Archivo creado correctamente.\n";
            }
            return "No se pudo crear el archivo.\n";
        } catch (IOException e) {
            return "Error al crear archivo: " + e.getMessage() + "\n";
        }
    }

    public String eliminar(String nombre) {
        File f = new File(directorioActual, nombre);
        if (!f.exists()) {
            return "El archivo o carpeta no existe.\n";
        }
        if (eliminarRecursivo(f)) {
            return "Eliminado correctamente.\n";
        }
        return "Error al intentar eliminar el elemento.\n";
    }

    private boolean eliminarRecursivo(File f) {
        if (f.isDirectory()) {
            File[] hijos = f.listFiles();
            if (hijos != null) {
                for (File hijo : hijos) {
                    eliminarRecursivo(hijo);
                }
            }
        }
        return f.delete();
    }

    public String cambiarDirectorio(String nombre) {
        File destino = new File(directorioActual, nombre);
        if (!destino.exists()) {
            return "El sistema no puede encontrar la ruta especificada.\n";
        }
        if (!destino.isDirectory()) {
            return "El nombre de directorio no es válido.\n";
        }
        directorioActual = destino;
        return "";
    }

    public String subirDirectorio() {
        if (directorioActual.equals(directorioRaiz)) {
            return "Ya se encuentra en el directorio raíz simulado.\n";
        }
        File padre = directorioActual.getParentFile();
        if (padre != null && padre.getAbsolutePath().startsWith(directorioRaiz.getAbsolutePath())) {
            directorioActual = padre;
        } else {
            directorioActual = directorioRaiz;
        }
        return "";
    }

}
