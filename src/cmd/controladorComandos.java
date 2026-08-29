
package cmd;

import java.io.*;
import java.util.Calendar;

public class controladorComandos {
    private consolaGUI consola;
    private GestorArchivos archivos;
    private boolean modoEscritura;
    private boolean append;
    private File archivoEscritura;
    private FileWriter escritor;
    
    public GestorComandos(ConsolaGUI consola){
        this.consola = consola;
        archivos = new GestorArchivos(consola);
        modoEscritura = false;
    }
    
    public void ejecutarComando(String entrada){
        entrada = entrada.trim();
        if(entrada.isEmpty()){
            return;
        }
        String[] partes = entrada.split("\\s+");
        String comando = partes[0];
        switch(comando){
            case "Mkdir":
                mkdir(partes);
                break;
            case "Mfile":
                mfile(partes);
                break;
            case "Rm":
                rm(partes);
                break;
            case "Cd":
                cd(partes);
                break;
            case "..":
                regresrar();
                break;
            case "Dir":
                dir();
                break;
            case "Date":
                date();
                break;
            case "Time":
                time();
                break;
            case "Wr":
                iniciarEscritura(partes, false);
                break;
            case "Rd":
                rd(partes);
                break;
            case "Ap":
                iniciarEscritura(partes, true);
            case "Ren":
                ren(partes);
                break;
            case "Copy":
                copy(partes);
                break;
            case "Find":
                find(entrada);
                break;
            case "Info":
                info(partes);
                break;
            case "Tree":
                tree();
                break;
            case "Cls":
                consola.limpiar();
                break;
            case "Help":
                help();
                break;
            case "Exit":
                cerrar();
                break;
            case "Grep":
                grep(entrada);
                break;
            default:
                consola.imprimir("'" + comando + "' no se reconoce como un comando interno o externo;
        }
    }
    
    private void mkdir(String[] partes){
        if(partes.length < 2){
            consola.imprimir("Uso: Mkdir <nombre>");
            return;
        }
        File carpeta = new File(consola.getCarpetaActual(), partes[1]);
        if(carpeta.exists()){
            consola.imprimir("Ya existe un archivo o carpeta con ese nombre.");
            return;
        }
        if(carpeta.mkdir()){
            consola.imprimir("Carpeta creada correctamente.");
        }
        else{
            consola.imprimir("No se pudo crear la carpeta.");
        }
    }
    
    
}
