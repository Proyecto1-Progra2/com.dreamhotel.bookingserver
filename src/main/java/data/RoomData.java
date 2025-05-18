package data;

import domain.Hotel;
import domain.Room;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class RoomData {

    private RandomAccessFile raf;
    private final int TAMANO_REGISTRO = 98; //si se usara el tamaño de imagen el tamaño seria de 100_098
    private final int TAMANO_STATUS = 30;
    private final int TAMANO_STYLE = 30;
    private final int TAMANO_ROOMNUMBER = 30;
    private final int TAMANO_PRICE = 8;
    //private final int TAMANO_IMAGE = 100_000; // 100 KB por imagen;


    public RoomData() throws FileNotFoundException {
        this.raf = new RandomAccessFile(new File("rooms.dat"), "rw");
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

    public void insert(Room room) throws IOException {
        //aumentar el tamaño del archivo en 64 bytes (TAMAÑO_REGISTRO)
        raf.setLength(raf.length() + TAMANO_REGISTRO);
        raf.seek(raf.length() - TAMANO_REGISTRO);
        // Se debe transformar el string en un arreglo de bytes para
        // poder escribirlo en el archivo
        byte roomNumber[] = toBytes(room.getRoomNumber(), TAMANO_ROOMNUMBER);
        raf.write(roomNumber);
        byte status[] = toBytes(room.getStatus(), TAMANO_STATUS);
        raf.write(status);
        byte style[] = toBytes(room.getStyle(), TAMANO_STYLE);
        raf.write(style);
        raf.writeDouble(room.getPrice());
    }

    public void insertPos(Room room, int posicion) throws IOException {
        raf.seek(posicion * TAMANO_REGISTRO);

        byte[] roomNumber = toBytes(room.getRoomNumber(), TAMANO_ROOMNUMBER);
        raf.write(roomNumber);

        byte[] status = toBytes(room.getStatus(), TAMANO_STATUS);
        raf.write(status);

        byte[] style = toBytes(room.getStyle(), TAMANO_STYLE);
        raf.write(style);
        
        raf.writeDouble(room.getPrice());
    }

    public ArrayList<Room> findAll() throws IOException {
        int totalRegistros = (int)(raf.length() / TAMANO_REGISTRO);
        ArrayList<Room> rooms = new ArrayList<>();
        for (int i = 0; i < totalRegistros; i++) {
            raf.seek(i * TAMANO_REGISTRO);
            String roomNumber = readString(TAMANO_ROOMNUMBER, raf.getFilePointer());
            if (roomNumber.equals("***")) {
                // Registro borrado lógicamente
                raf.skipBytes(TAMANO_STATUS + TAMANO_STYLE + TAMANO_PRICE);
                continue;
            }
            String status = readString(TAMANO_STATUS, raf.getFilePointer());
            String style = readString(TAMANO_STYLE, raf.getFilePointer());
            double price = raf.readDouble();
            rooms.add(new Room(status, style, roomNumber, price));
        }
        return rooms;
    }

    public int buscarPosicion(String roomNumber) throws IOException {
        boolean encontrado = false;
        this.raf.length();
        int totalRegistros = (int) (this.raf.length() / TAMANO_REGISTRO);
        int numReg = 0;
        while (numReg < totalRegistros && !encontrado) {
            this.raf.seek(numReg * TAMANO_REGISTRO);
            String roomNumberActually = this.readString(TAMANO_ROOMNUMBER, this.raf.getFilePointer());
            if (roomNumber.equalsIgnoreCase(roomNumberActually))
                encontrado = true;
            else numReg++;
        }
        return numReg;
    }

    public Room roomSearh(String roomNumber) throws IOException {
        Room room = null;
        this.raf.length();
        int totalRegistros = (int) (this.raf.length() / TAMANO_REGISTRO);
        int numReg = 0;
        while (numReg < totalRegistros && room == null) {
            this.raf.seek(numReg * TAMANO_REGISTRO);
            String roomNumberActually = this.readString(TAMANO_ROOMNUMBER, this.raf.getFilePointer());
            if (roomNumber.equalsIgnoreCase(roomNumberActually))
                room = new Room(roomNumberActually, this.readString(TAMANO_STATUS, this.raf.getFilePointer()),
                        this.readString(TAMANO_STYLE, this.raf.getFilePointer()), this.raf.readDouble());
            else numReg++;

        }
        return room;
    }

    public void eliminar(String roomNumber) throws IOException {
        int totalRegistros = (int)(raf.length() / TAMANO_REGISTRO);
        for (int i = 0; i < totalRegistros; i++) {
            raf.seek(i * TAMANO_REGISTRO);
            String roomNumberActually = readString(TAMANO_ROOMNUMBER, raf.getFilePointer());
            if (roomNumberActually.equalsIgnoreCase(roomNumber)) {
                raf.seek(i * TAMANO_REGISTRO);
                byte[] marcador = toBytes("***", TAMANO_ROOMNUMBER);
                raf.write(marcador); // Marcar como borrado
                return;
            }
        }
    }

}
