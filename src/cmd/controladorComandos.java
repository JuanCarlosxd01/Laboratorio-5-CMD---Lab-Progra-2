
package cmd;

import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;

public class controladorComandos {
    private consolaGUI consola;
    private GestorArchivos gestor;
    private boolean modoEscritura;
    private boolean append;
    private File archivoEscritura;
    private FileWriter escritor;
    
    public controladorComandos(consolaGUI consola, GestorArchivos gestor){
        this.consola = consola;
        this.gestor = gestor;
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
                mFile(partes);
                break;
            case "Rm":
                rm(partes);
                break;
            case "Cd":
                cd(partes);
                break;
            case "..":
                regresar();
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
                consola.imprimir("'" + comando + "' no se reconoce como un comando interno o externo.");
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
    
    private void mFile(String[] partes){
        if(partes.length < 2){
            consola.imprimir("Uso: Mfile <nombre.ext>");
            return;
        }
        File archivo = new File(consola.getCarpetaActual(), partes[1]);
        if(archivo.exists){
            consola.imprimir("Ya existe un archivo o carpeta con ese nombre.");
            return;
        }
        try{
            FileWriter fw = new FileWriter(archivo);
            fw.close();
            consola.imprimir("Archivo creado correctamente");
        }catch(IOException e){
            consola.imprimir("Error creando el archivo: " + e.getMessage());
        }
    }
    
    private void rm(String[] partes){
        if(partes.length < 2){
            consola.imprimir("Uso: Rm <nombre>");
            return;
        }
        File archivo = new File(consola.getCarpetaActual(), partes[1]);
        if(!archivo.exists()){
            consola.imprimir("El archivo o carpeta no existe.");
            return;
        }
        if(archivos.eliminarRecursivo(archivo)){
            consola.imprimir("Eliminado correctamente.");
        }
        else{
            consola.imprimir("No se pudo eliminar.");
        }
    }
    
    private void cd(String[] partes){
        if(partes.length < 2){
            consola.imprimir("Uso: Cd <nombre carpete>");
            return;
        }
        File destino = new File(consola.getCarpetaActual(), partes[1]);
        if(!destino.exists()){
            consola.imprimir("La carpeta indicada no existe.");
            return;
        }
        if(!destino.isDirectory()){
            consola.imprimir("El elemento indicado no es una carpeta");
            return;
        }
        try{
            if(!archivos.estaDentroRaiz(destino)){
                consola.imprimir("No puede salir de la carpeta raíz.");
                return;
            }
        }catch(IOException e){
            consola.imprimir("Error accediendo a la ruta.");
            return;
        }
        consola.setCarpetaActual(destino);
    }
    
    private void regresar(){
        File actual = consola.getCarpetaActual();
        if(actual.equals(consola.getCarpetaRaiz())){
            consola.imprimir("Ya se encuentra en la carpeta raíz.");
            return;
        }
        File anterior = actual.getParentFile();
        if(anterior != null){
            consola.setCarpetaActual(anterior);
        }
    }
    
    private void dir(){
        File[] contenido = consola.getCarpetaActual().listFiles();
        if(contenido == null || contenido.length == 0){
            consola.imprimir("La carpeta esta vacía.");
            return;
        }
        for(File archivo : contenido){
            if(archivo.isDirectory()){
                consola.imprimir("<DIR>     " + archivo.getName());
            }
            else{
                consola.imprimir("          " + archivo.getName());
            }
        }
    }
    
    private void date(){
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        consola.imprimir("Fecha actual: " + formato.format(new Date()));
    }
    
    private void time(){
        SimpleDateFormat formato = new SimpleDateFormat("HH:mm:ss");
        consola.imprimir("Hora actual: " + formato.format(new Date()));
    }
    
    private String wr(String nombreArchivo, String texto){
        File archivo = new File(carpetaActual, nombreArchivo);
        if(!archivo.exists()){
            return "El archivo no existe.";
        }
        if(!archivo.isFile()){
            return "El nombre indicado no corresponde a un archivo.";
        }
        try{
            FileWriter escritor = new FileWriter(archivo);
            escritor.write(texto);
            escritor.write(System.lineSeparator());
            escritor.close();
            return "Texto escrito correctamente.";
        }catch(IOException e){
            return "Error al escribir en el archivo.";
        }
    }
        
    
     
    
    
    
}
