package data;

import domain.Room;
import domain.Image; // Importa tu clase Image
import java.io.File; // Necesario para la clase File
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class RoomData {

    private RandomAccessFile raf;
    // Ajusta estos tamaños según tus necesidades y el tamaño máximo esperado de los datos.
    private final int TAMANO_ROOMNUMBER = 30;
    private final int TAMANO_STATUS = 30;
    private final int TAMANO_STYLE = 30;
    private final int TAMANO_PRICE = 8; // Double siempre ocupa 8 bytes
    // TAMANO_IMAGE_PATH: DEBE SER SUFICIENTE PARA LA RUTA ABSOLUTA DE UN ARCHIVO
    private final int TAMANO_IMAGE_PATH = 100; // Por ejemplo, 100 caracteres para la ruta del archivo
    private final int TAMANO_HOTEL_NUMBER = 20;

    // Calcula el tamaño total del registro sumando todos los tamaños de los campos
    private final int TAMANO_REGISTRO = TAMANO_ROOMNUMBER + TAMANO_STATUS + TAMANO_STYLE + TAMANO_PRICE + TAMANO_IMAGE_PATH + TAMANO_HOTEL_NUMBER;


    public RoomData() throws FileNotFoundException {
        this.raf = new RandomAccessFile(new File("rooms.dat"), "rw");
    }

    private String readString(int tamanoString, long posicion) throws IOException {
        raf.seek(posicion);
        byte[] datos = new byte[tamanoString];
        raf.readFully(datos);
        String dato = new String(datos).trim();
        return dato;
    }

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
        raf.setLength(raf.length() + TAMANO_REGISTRO);
        raf.seek(raf.length() - TAMANO_REGISTRO);

        raf.write(toBytes(room.getRoomNumber(), TAMANO_ROOMNUMBER));
        raf.write(toBytes(room.getStatus(), TAMANO_STATUS));
        raf.write(toBytes(room.getStyle(), TAMANO_STYLE));
        raf.writeDouble(room.getPrice());

        // Guardar la ruta absoluta del archivo de la imagen
        // Si no hay imagen, guarda una cadena vacía o nula.
        String imagePathToSave = (room.getImage() != null && room.getImage().getImage() != null) ?
                room.getImage().getImage().getAbsolutePath() : "";
        raf.write(toBytes(imagePathToSave, TAMANO_IMAGE_PATH));

        raf.write(toBytes(room.getHotelNumber(), TAMANO_HOTEL_NUMBER));
    }

    public void insertPos(Room room, int posicion) throws IOException {
        raf.seek(posicion * TAMANO_REGISTRO);

        raf.write(toBytes(room.getRoomNumber(), TAMANO_ROOMNUMBER));
        raf.write(toBytes(room.getStatus(), TAMANO_STATUS));
        raf.write(toBytes(room.getStyle(), TAMANO_STYLE));
        raf.writeDouble(room.getPrice());

        String imagePathToSave = (room.getImage() != null && room.getImage().getImage() != null) ?
                room.getImage().getImage().getAbsolutePath() : "";
        raf.write(toBytes(imagePathToSave, TAMANO_IMAGE_PATH));

        raf.write(toBytes(room.getHotelNumber(), TAMANO_HOTEL_NUMBER));
    }

    public ArrayList<Room> findAllRoomsByHotel(String hotelNumber) throws IOException {
        ArrayList<Room> rooms = new ArrayList<>();
        long totalRegistros = raf.length() / TAMANO_REGISTRO;

        for (int i = 0; i < totalRegistros; i++) {
            raf.seek(i * TAMANO_REGISTRO);

            String roomNumber = readString(TAMANO_ROOMNUMBER, raf.getFilePointer());
            if (roomNumber.trim().equals("***")) {
                raf.skipBytes(TAMANO_STATUS + TAMANO_STYLE + TAMANO_PRICE + TAMANO_IMAGE_PATH + TAMANO_HOTEL_NUMBER);
                continue;
            }

            String status = readString(TAMANO_STATUS, raf.getFilePointer());
            String style = readString(TAMANO_STYLE, raf.getFilePointer());
            double price = raf.readDouble();
            String imagePath = readString(TAMANO_IMAGE_PATH, raf.getFilePointer());
            String currentHotelNumber = readString(TAMANO_HOTEL_NUMBER, raf.getFilePointer());

            if (currentHotelNumber.trim().equalsIgnoreCase(hotelNumber)) {
                Image roomImage = null;
                if (!imagePath.isEmpty()) {
                    // Recrear el objeto File y luego el objeto Image
                    File imageFile = new File(imagePath.trim());
                    // Asegúrate de que el roomNumber de Image coincida con el roomNumber de la Room
                    roomImage = new Image(roomNumber.trim(), imageFile);
                }
                rooms.add(new Room(roomNumber.trim(), status.trim(), style.trim(), price, currentHotelNumber.trim()));
            }
        }
        return rooms;
    }

    public ArrayList<Room> findAll() throws IOException {
        ArrayList<Room> rooms = new ArrayList<>();
        long totalRegistros = raf.length() / TAMANO_REGISTRO;

        for (int i = 0; i < totalRegistros; i++) {
            raf.seek(i * TAMANO_REGISTRO);
            String roomNumber = readString(TAMANO_ROOMNUMBER, raf.getFilePointer());
            if (roomNumber.trim().equals("***")) {
                raf.skipBytes(TAMANO_STATUS + TAMANO_STYLE + TAMANO_PRICE + TAMANO_IMAGE_PATH + TAMANO_HOTEL_NUMBER);
                continue;
            }
            String status = readString(TAMANO_STATUS, raf.getFilePointer());
            String style = readString(TAMANO_STYLE, raf.getFilePointer());
            double price = raf.readDouble();
            String imagePath = readString(TAMANO_IMAGE_PATH, raf.getFilePointer());
            String hotelNumber = readString(TAMANO_HOTEL_NUMBER, raf.getFilePointer());

            Image roomImage = null;
            if (!imagePath.isEmpty()) {
                File imageFile = new File(imagePath.trim());
                roomImage = new Image(roomNumber.trim(), imageFile);
            }
            rooms.add(new Room(roomNumber.trim(), status.trim(), style.trim(), price, roomImage, hotelNumber.trim()));
        }
        return rooms;
    }

    public int buscarPosicion(String roomNumber) throws IOException {
        boolean encontrado = false;
        int totalRegistros = (int) (this.raf.length() / TAMANO_REGISTRO);
        int numReg = 0;
        while (numReg < totalRegistros && !encontrado) {
            this.raf.seek(numReg * TAMANO_REGISTRO);
            String roomNumberActual = this.readString(TAMANO_ROOMNUMBER, this.raf.getFilePointer());
            if (roomNumber.equalsIgnoreCase(roomNumberActual.trim()))
                encontrado = true;
            else numReg++;
        }
        return encontrado ? numReg : -1;
    }

    public Room roomSearh(String roomNumber) throws IOException {
        Room room = null;
        int totalRegistros = (int) (this.raf.length() / TAMANO_REGISTRO);
        int numReg = 0;
        while (numReg < totalRegistros && room == null) {
            this.raf.seek(numReg * TAMANO_REGISTRO);
            String roomNumberActual = this.readString(TAMANO_ROOMNUMBER, this.raf.getFilePointer());
            if (roomNumber.equalsIgnoreCase(roomNumberActual.trim())) {
                String status = this.readString(TAMANO_STATUS, this.raf.getFilePointer());
                String style = this.readString(TAMANO_STYLE, this.raf.getFilePointer());
                double price = this.raf.readDouble();
                String imagePath = this.readString(TAMANO_IMAGE_PATH, this.raf.getFilePointer());
                String hotelNumber = this.readString(TAMANO_HOTEL_NUMBER, this.raf.getFilePointer());

                Image roomImage = null;
                if (!imagePath.isEmpty()) {
                    File imageFile = new File(imagePath.trim());
                    roomImage = new Image(roomNumberActual.trim(), imageFile);
                }
                room = new Room(roomNumberActual.trim(), status.trim(), style.trim(), price, roomImage, hotelNumber.trim());
            } else {
                numReg++;
            }
        }
        return room;
    }

    public void eliminar(String roomNumberToDelete, String hotelNumberToDelete) throws IOException {
        int totalRegistros = (int)(raf.length() / TAMANO_REGISTRO);
        for (int i = 0; i < totalRegistros; i++) {
            raf.seek(i * TAMANO_REGISTRO);
            String roomNumberActual = readString(TAMANO_ROOMNUMBER, raf.getFilePointer());
            // Mover el puntero para leer el hotelNumber sin leer los campos intermedios nuevamente
            raf.seek(raf.getFilePointer() + TAMANO_STATUS + TAMANO_STYLE + TAMANO_PRICE + TAMANO_IMAGE_PATH);
            String hotelNumberActual = readString(TAMANO_HOTEL_NUMBER, raf.getFilePointer());

            if (roomNumberActual.trim().equalsIgnoreCase(roomNumberToDelete) && hotelNumberActual.trim().equalsIgnoreCase(hotelNumberToDelete)) {
                raf.seek(i * TAMANO_REGISTRO);
                byte[] marcador = toBytes("***", TAMANO_ROOMNUMBER);
                raf.write(marcador);
                return;
            }
        }
    }

    public void deleteAllRoomsByHotel(String hotelNumberToDelete) throws IOException {
        long totalRegistros = raf.length() / TAMANO_REGISTRO;
        for (int i = 0; i < totalRegistros; i++) {
            raf.seek(i * TAMANO_REGISTRO);
            raf.skipBytes(TAMANO_ROOMNUMBER + TAMANO_STATUS + TAMANO_STYLE + TAMANO_PRICE + TAMANO_IMAGE_PATH);
            String currentHotelNumber = readString(TAMANO_HOTEL_NUMBER, raf.getFilePointer());

            if (currentHotelNumber.trim().equalsIgnoreCase(hotelNumberToDelete)) {
                raf.seek(i * TAMANO_REGISTRO);
                byte[] marcador = toBytes("***", TAMANO_ROOMNUMBER);
                raf.write(marcador);
            }
        }
    }
}