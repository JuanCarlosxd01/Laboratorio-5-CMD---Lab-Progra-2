
package cmd;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
public class ComandoCMD2 {
    private File carpetaActual;
    private File carpetaRaiz;

    public ComandoCMD2(File carpetaRaiz) {
        this.carpetaRaiz = carpetaRaiz;
        this.carpetaActual = carpetaRaiz;
    }

    public File getCarpetaActual() {
        return carpetaActual;
    }

    public String ap(String nombreArchivo, String texto) {

        File archivo = new File(carpetaActual, nombreArchivo);

        if (!archivo.exists()) {
            return "El archivo no existe.";
        }

        if (!archivo.isFile()) {
            return "El nombre indicado no corresponde a un archivo.";
        }

        try {
            FileWriter escritor = new FileWriter(archivo, true);

            escritor.write(texto);
            escritor.write(System.lineSeparator());

            escritor.close();

            return "Texto agregado correctamente.";

        } catch (IOException e) {
            return "Error al agregar texto al archivo.";
        }
    }

    public String ren(String actual, String nuevo) {

        File archivoActual = new File(carpetaActual, actual);
        File archivoNuevo = new File(carpetaActual, nuevo);

        if (!archivoActual.exists()) {
            return "El archivo o carpeta no existe.";
        }

        if (archivoNuevo.exists()) {
            return "Ya existe un archivo o carpeta con ese nombre.";
        }

        boolean renombrado = archivoActual.renameTo(archivoNuevo);

        if (renombrado) {
            return "Renombrado correctamente.";
        }

        return "No se pudo renombrar.";
    }

    public String copy(String origen, String destino) {

        File archivoOrigen = new File(carpetaActual, origen);
        File archivoDestino = new File(carpetaActual, destino);

        if (!archivoOrigen.exists()) {
            return "El archivo de origen no existe.";
        }

        if (!archivoOrigen.isFile()) {
            return "Solo se pueden copiar archivos.";
        }

        if (archivoDestino.exists()) {
            return "Ya existe un archivo con ese nombre.";
        }

        try {
            FileReader lector = new FileReader(archivoOrigen);
            FileWriter escritor = new FileWriter(archivoDestino);

            int caracter = lector.read();

            while (caracter != -1) {
                escritor.write(caracter);
                caracter = lector.read();
            }

            lector.close();
            escritor.close();

            return "Archivo copiado correctamente.";

        } catch (IOException e) {
            return "Error al copiar el archivo.";
        }
    }

    private String buscar(File carpeta, String nombre) {

        String resultado = "";

        File[] archivos = carpeta.listFiles();

        if (archivos == null) {
            return resultado;
        }

        for (int i = 0; i < archivos.length; i++) {

            File archivo = archivos[i];

            if (archivo.getName().toLowerCase().contains(nombre.toLowerCase())) {
                resultado += archivo.getAbsolutePath() + "\n";
            }

            if (archivo.isDirectory()) {
                resultado += buscar(archivo, nombre);
            }
        }

        return resultado;
    }

    public String find(String nombre) {

        String resultado = buscar(carpetaActual, nombre);

        if (resultado.isEmpty()) {
            return "No se encontraron archivos o carpetas.";
        }

        return resultado;
    }

    public String info(String nombre) {

        File archivo = new File(carpetaActual, nombre);

        if (!archivo.exists()) {
            return "El archivo o carpeta no existe.";
        }

        String tipo;

        if (archivo.isDirectory()) {
            tipo = "Carpeta";
        } else {
            tipo = "Archivo";
        }

        String resultado = "";

        resultado += "Tipo: " + tipo + "\n";
        resultado += "Ruta: " + archivo.getAbsolutePath() + "\n";
        resultado += "Tamaño: " + archivo.length() + " bytes\n";
        resultado += "Última modificación: " + new Date(archivo.lastModified());

        return resultado;
    }

    public String tree() {

        return carpetaActual.getName() + "\n" + mostrarArbol(carpetaActual, "");
    }

    private String mostrarArbol(File carpeta, String espacio) {

        String resultado = "";

        File[] archivos = carpeta.listFiles();

        if (archivos == null) {
            return resultado;
        }

        for (int i = 0; i < archivos.length; i++) {

            File archivo = archivos[i];

            resultado += espacio + "|-- " + archivo.getName() + "\n";

            if (archivo.isDirectory()) {
                resultado += mostrarArbol(archivo, espacio + "    ");
            }
        }

        return resultado;
    }

    public String cls() {
        return "";
    }
}
