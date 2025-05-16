package data;

import domain.Hotel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class HotelData {

    private RandomAccessFile raf;
    private final int TAMANO_REGISTRO = 100;
    private final int TAMANO_NUMERO = 20;
    private final int TAMANO_NOMBRE = 30;
    private final int TAMANO_DIRECCION = 50;

    public HotelData() throws FileNotFoundException {
        this.raf = new RandomAccessFile(new File("hoteles.dat"), "rw");
    }

    private String readString(int tamanoString, long posicion) throws IOException {
        raf.seek(posicion);
        byte[] datos = new byte[tamanoString];
        raf.readFully(datos);
        String dato = new String(datos).trim();
        return dato;

    }//readString

    private byte[] toBytes(String dato, int tamanoString) {
        byte[] datos = new byte[tamanoString];
        byte[] temp = dato.getBytes();
        for (int i = 0; i < tamanoString; i++) {
            if (i < temp.length)
                datos[i] = temp[i];
        }
        return datos;
    }

    public void insert(Hotel hotel) throws IOException {
        //aumentar el tamaño del archivo en 64 bytes (TAMAÑO_REGISTRO)
        raf.setLength(raf.length() + TAMANO_REGISTRO);
        raf.seek(raf.length() - TAMANO_REGISTRO);
        // Se debe transformar el string en un arreglo de bytes para
        // poder escribirlo en el archivo
        byte numero[] = toBytes(hotel.getNumber(), TAMANO_NUMERO);
        raf.write(numero);
        byte nombre[] = toBytes(hotel.getName(), TAMANO_NOMBRE);
        raf.write(nombre);
        byte direccion[] = toBytes(hotel.getAddress(), TAMANO_DIRECCION);
        raf.write(direccion);
    }

    public void insertPos(Hotel hotel, int posicion) throws IOException {
        raf.seek(posicion * TAMANO_REGISTRO);

        byte[] numero = toBytes(hotel.getNumber(), TAMANO_NUMERO);
        raf.write(numero);

        byte[] nombre = toBytes(hotel.getName(), TAMANO_NOMBRE);
        raf.write(nombre);

        byte[] direccion = toBytes(hotel.getAddress(), TAMANO_DIRECCION);
        raf.write(direccion);
    }

    public ArrayList<Hotel> findAll() throws IOException {
        int totalRegistros = (int)(raf.length() / TAMANO_REGISTRO);
        ArrayList<Hotel> hoteles = new ArrayList<>();
        for (int i = 0; i < totalRegistros; i++) {
            raf.seek(i * TAMANO_REGISTRO);
            String numero = readString(TAMANO_NUMERO, raf.getFilePointer());
            if (numero.equals("***")) {
                // Registro borrado lógicamente
                raf.skipBytes(TAMANO_NOMBRE + TAMANO_DIRECCION);
                continue;
            }
            String nombre = readString(TAMANO_NOMBRE, raf.getFilePointer());
            String direccion = readString(TAMANO_DIRECCION, raf.getFilePointer());
            hoteles.add(new Hotel(numero, nombre, direccion));
        }
        return hoteles;
    }

    public int buscarPosicion(String numeroHotel) throws IOException {
        boolean encontrado = false;
        this.raf.length();
        int totalRegistros = (int) (this.raf.length() / TAMANO_REGISTRO);
        int numReg = 0;
        while (numReg < totalRegistros && !encontrado) {
            this.raf.seek(numReg * TAMANO_REGISTRO);
            String numeroHotelActual = this.readString(TAMANO_NUMERO, this.raf.getFilePointer());
            if (numeroHotel.equalsIgnoreCase(numeroHotelActual))
                encontrado = true;
            else numReg++;

        }
        return numReg;
    }

    public Hotel buscarHotel(String numeroHotel) throws IOException {
        Hotel hotel = null;
        this.raf.length();
        int totalRegistros = (int) (this.raf.length() / TAMANO_REGISTRO);
        int numReg = 0;
        while (numReg < totalRegistros && hotel == null) {
            this.raf.seek(numReg * TAMANO_REGISTRO);
            String numeroHotelActual = this.readString(TAMANO_NUMERO, this.raf.getFilePointer());
            if (numeroHotel.equalsIgnoreCase(numeroHotelActual))
                hotel = new Hotel(numeroHotelActual, this.readString(TAMANO_NOMBRE, this.raf.getFilePointer()),
                        this.readString(TAMANO_DIRECCION, this.raf.getFilePointer()));
            else numReg++;

        }
        return hotel;
    }

    public void eliminar(String numeroHotel) throws IOException {
        int totalRegistros = (int)(raf.length() / TAMANO_REGISTRO);
        for (int i = 0; i < totalRegistros; i++) {
            raf.seek(i * TAMANO_REGISTRO);
            String numeroActual = readString(TAMANO_NUMERO, raf.getFilePointer());
            if (numeroActual.equalsIgnoreCase(numeroHotel)) {
                raf.seek(i * TAMANO_REGISTRO);
                byte[] marcador = toBytes("***", TAMANO_NUMERO);
                raf.write(marcador); // Marcar como borrado
                return;
            }
        }
    }

}
