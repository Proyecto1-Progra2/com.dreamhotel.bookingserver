package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class HuespedData {

    private RandomAccessFile raf;
    private final int TAMANO_REGISTRO = 60;
    private final int TAMANO_NOMBRE = 30;
    private final int TAMANO_APELLIDOS = 30;

    public HuespedData() throws FileNotFoundException {
        this.raf = new RandomAccessFile(new File("huespedes.dat"), "rw");
    }

    private String readString(int tamanoString, long posicion) throws IOException {
        raf.seek(posicion);
        byte[] datos = new byte[tamanoString];
        raf.readFully(datos);
        String dato = new String(datos).trim();
        return dato;

    }//readString

    private byte[] toBytes(String dato, int tamanoString){
        byte[] datos = new byte[tamanoString];
        byte[] temp = dato.getBytes();
        for (int i = 0; i < tamanoString; i++) {
            if (i<temp.length)
                datos[i] =temp[i];
        }
        return datos;
    }
/*
    public void insert(Resident resident) throws IOException {
        //aumentar el tamaño del archivo en 64 bytes (TAMAÑO_REGISTRO)
        raf.setLength(raf.length() + TAMANO_REGISTRO);
        raf.seek(raf.length() - TAMANO_REGISTRO);
        // Se debe transformar el string en un arreglo de bytes para
        // poder escribirlo en el archivo
        byte nombre[] = toBytes(resident.getName(),TAMANO_NOMBRE);
        raf.write(nombre);
        byte apellidos[]= toBytes(resident.getApellido(),TAMANO_APELLIDOS);
        raf.write(apellidos);
    }

    public ArrayList<Resident> findAll() throws IOException {
        int totalRegistros = (int)(raf.length()/TAMANO_REGISTRO);
        ArrayList<Resident> huespedes = new ArrayList<>();
        for(int i=0; i<totalRegistros; i++){
            raf.seek(i*TAMANO_REGISTRO);
            String nombre = this.readString(TAMANO_NOMBRE,
                    raf.getFilePointer());
            String apellidos = this.readString(TAMANO_APELLIDOS,
                    raf.getFilePointer());
            huespedes.add(new Resident(nombre, apellidos));
        }//for
        return huespedes;
    }*/
}
