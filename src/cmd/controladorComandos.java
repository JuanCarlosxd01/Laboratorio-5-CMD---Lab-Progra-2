
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
    private ComandoCMD2 cmd2;
    
    public controladorComandos(consolaGUI consola, GestorArchivos gestor){
        this.consola = consola;
        this.gestor = gestor;
        modoEscritura = false;
        cmd2 = new ComandoCMD2(consola.getCarpetaRaiz());
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
                wr(partes);
                break;
            case "Rd":
                rd(partes);
                break;
            case "Ap":
                  if(partes.length < 3){
                    consola.imprimir("Uso: Ap <archivo.ext> <texto>");
                } else {
                    String texto = "";
                    for(int i = 2; i < partes.length; i++){
                        texto += partes[i];
                        if(i < partes.length - 1){
                            texto += " ";
                        }
                    }
                    consola.imprimir(
                        cmd2.ap(partes[1], texto)
                    );   
                  }
                  break;
            case "Ren":
                if(partes.length < 3){
                    consola.imprimir("Uso: Ren <actual> <nuevo>");
                } else {
                    consola.imprimir(cmd2.ren(partes[1], partes[2]));
                }
                break;
            case "Copy":
                if(partes.length < 3){
                    consola.imprimir("Uso: Copy <origen> <destino>");
                } else {
                    consola.imprimir(cmd2.copy(partes[1], partes[2]));
                }
                break;
            case "Find":
                if(partes.length < 2){
                    consola.imprimir("Uso: Find <nombre>");
                } else {
                    consola.imprimir(cmd2.find(partes[1]));
                }
                break;
            case "Info":
                if(partes.length < 2){
                    consola.imprimir("Uso: Info <nombre>");
                } else {
                    consola.imprimir(cmd2.info(partes[1]));
                }
                break;
            case "Tree":
                consola.imprimir(cmd2.tree());
                break;
            case "Cls":
                consola.limpiar();
                break;
            case "Help":
                consola.imprimir(cmd2.help());
                break;      
            case "Exit":
                cmd2.exit();
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
        if(archivo.exists()){
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
        if(gestor.eliminarRecursivo(archivo)){
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
    
    private void wr(String[] partes){
        if(partes.length < 3){
            consola.imprimir("Uso: Wr <archivo.ext> <texto>");
            return;
        }
        String nombreArchivo = partes[1];
        String texto = "";
        for(int i = 2; i < partes.length; i++){
            texto += partes[i];
            if(i < partes.length - 1){
                texto += " ";
            }
        }
        File archivo = new File(
                consola.getCarpetaActual(),
                nombreArchivo
        );
        if(!archivo.exists()){
            consola.imprimir("El archivo no existe.");
            return;
        }
        if(!archivo.isFile()){
            consola.imprimir(
                    "El nombre indicado no corresponde a un archivo."
            );
            return;
        }
        try{

            FileWriter escritor = new FileWriter(archivo);

            escritor.write(texto);
            escritor.write(System.lineSeparator());

            escritor.close();

            consola.imprimir(
                    "Texto escrito correctamente."
            );
        }catch(IOException e){
            consola.imprimir(
                    "Error al escribir en el archivo."
            );
        }
    }
    
    public void rd(String[] partes){
        if(partes.length < 2){
            consola.imprimir("Uso: Rd <archivo.ext>");
            return;
        }
        String nombreArchivo = partes[1];
        File archivo = new File(
                consola.getCarpetaActual(),
                nombreArchivo
        );
        if(!archivo.exists()){
            consola.imprimir("El archivo no existe.");
            return;
        }
        if(!archivo.isFile()){
            consola.imprimir(
                    "El nombre indicado no corresponde a un archivo."
            );
            return;
        }
        try{
            FileReader lector = new FileReader(archivo);
            String contenido = "";
            int caracter = lector.read();
            while(caracter != -1){
                contenido += (char) caracter;
                caracter = lector.read();
            }
            lector.close();
            if(contenido.isEmpty()){
                consola.imprimir(
                        "El archivo esta vacío."
                );
            }else{
                consola.imprimir(contenido);
            }
        }catch(IOException e){
            consola.imprimir(
                    "Error al leer el archivo."
            );
        } 
    }
       
}
