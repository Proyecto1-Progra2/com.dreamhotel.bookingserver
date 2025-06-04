package data;

import domain.Hotel;
import domain.Room; // Importamos la clase Room para poder trabajar con ella

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

    // Instancia de RoomData para manejar las habitaciones
    private RoomData roomData;

    public HotelData() throws FileNotFoundException {
        this.raf = new RandomAccessFile(new File("hoteles.dat"), "rw");
        try {
            this.roomData = new RoomData(); // Inicializamos RoomData al crear HotelData
        } catch (FileNotFoundException e) {
            // Manejar la excepción si 'rooms.dat' no se puede crear/encontrar.
            // Es crucial que 'rooms.dat' también sea accesible.
            System.err.println("Error al inicializar RoomData: " + e.getMessage());
            throw e; // Relanzar la excepción para que el problema sea evidente en la aplicación principal
        }
    }

    private String readString(int tamanoString, long posicion) throws IOException {
        raf.seek(posicion);
        byte[] datos = new byte[tamanoString];
        raf.readFully(datos);
        String dato = new String(datos).trim(); // Usar trim() para eliminar espacios en blanco
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

//    public void insert(Hotel hotel) throws IOException {
//        // Aumentar el tamaño del archivo para el nuevo registro del hotel
//        raf.setLength(raf.length() + TAMANO_REGISTRO);
//        raf.seek(raf.length() - TAMANO_REGISTRO);
//
//        // Escribir la información del hotel
//        byte numero[] = toBytes(hotel.getNumber(), TAMANO_NUMERO);
//        raf.write(numero);
//        byte nombre[] = toBytes(hotel.getName(), TAMANO_NOMBRE);
//        raf.write(nombre);
//        byte direccion[] = toBytes(hotel.getAddress(), TAMANO_DIRECCION);
//        raf.write(direccion);
//
//        // Si el hotel tiene habitaciones, las insertamos usando RoomData
//        if (hotel.getRooms() != null) {
//            for (Room room : hotel.getRooms()) {
//                // Asegurarnos de que la habitación está vinculada al hotel correcto
//                room.setHotelNumber(hotel.getNumber());
//                roomData.insert(room); // Delegar la inserción de la habitación a RoomData
//            }
//        }
//    }

    public void insertPos(Hotel hotel, int posicion) throws IOException {
        raf.seek(posicion * TAMANO_REGISTRO);

        byte[] numero = toBytes(hotel.getNumber(), TAMANO_NUMERO);
        raf.write(numero);

        byte[] nombre = toBytes(hotel.getName(), TAMANO_NOMBRE);
        raf.write(nombre);

        byte[] direccion = toBytes(hotel.getAddress(), TAMANO_DIRECCION);
        raf.write(direccion);

        // Al insertar/actualizar un hotel en una posición específica,
        // podrías querer manejar también la actualización de sus habitaciones.
        // Una estrategia común es eliminar las habitaciones antiguas asociadas
        // y luego insertar las nuevas.
//        if (hotel.getRooms() != null) {
//            // Esto elimina todas las habitaciones de este hotel antes de insertar las nuevas.
//            // Ten cuidado con esto si solo quieres actualizar algunas habitaciones.
//            roomData.deleteAllRoomsByHotel(hotel.getNumber());
//            for (Room room : hotel.getRooms()) {
//                room.setHotelNumber(hotel.getNumber());
//                roomData.insert(room);
//            }
//        }
    }

    public ArrayList<Hotel> findAll() throws IOException {
        int totalRegistros = (int)(raf.length() / TAMANO_REGISTRO);
        ArrayList<Hotel> hoteles = new ArrayList<>();
        for (int i = 0; i < totalRegistros; i++) {
            raf.seek(i * TAMANO_REGISTRO);
            String numero = readString(TAMANO_NUMERO, raf.getFilePointer());
            if (numero.trim().equals("***")) { // Usar trim() para una comparación correcta
                // Registro borrado lógicamente, lo saltamos
                raf.skipBytes(TAMANO_NOMBRE + TAMANO_DIRECCION);
                continue;
            }
            String nombre = readString(TAMANO_NOMBRE, raf.getFilePointer());
            String direccion = readString(TAMANO_DIRECCION, raf.getFilePointer());

            // Obtener las habitaciones asociadas a este hotel usando RoomData
            //ArrayList<Room> rooms = roomData.findAllRoomsByHotel(numero.trim());

            hoteles.add(new Hotel(numero.trim(), nombre.trim(), direccion.trim(), new ArrayList<>()));
        }
        return hoteles;
    }

    public int buscarPosicion(String numeroHotel) throws IOException {
        boolean encontrado = false;
        int totalRegistros = (int) (this.raf.length() / TAMANO_REGISTRO);
        int numReg = 0;
        while (numReg < totalRegistros && !encontrado) {
            this.raf.seek(numReg * TAMANO_REGISTRO);
            String numeroHotelActual = this.readString(TAMANO_NUMERO, this.raf.getFilePointer());
            if (numeroHotel.equalsIgnoreCase(numeroHotelActual.trim())) // Usar trim() para comparar
                encontrado = true;
            else numReg++;
        }
        return encontrado ? numReg : -1; // Devolvemos -1 si no se encuentra el hotel
    }

    public Hotel buscarHotel(String numeroHotel) throws IOException {
        Hotel hotel = null;
        int totalRegistros = (int) (this.raf.length() / TAMANO_REGISTRO);
        int numReg = 0;
        while (numReg < totalRegistros && hotel == null) {
            this.raf.seek(numReg * TAMANO_REGISTRO);
            String numeroHotelActual = this.readString(TAMANO_NUMERO, this.raf.getFilePointer());
            if (numeroHotel.equalsIgnoreCase(numeroHotelActual.trim())) { // Usar trim() para comparar
                String nombre = this.readString(TAMANO_NOMBRE, this.raf.getFilePointer());
                String direccion = this.readString(TAMANO_DIRECCION, this.raf.getFilePointer());

                // Obtener las habitaciones asociadas a este hotel
                ArrayList<Room> rooms = roomData.findAllRoomsByHotel(numeroHotelActual.trim());

                hotel = new Hotel(numeroHotelActual.trim(), nombre.trim(), direccion.trim(), rooms);
            } else {
                numReg++;
            }
        }
        return hotel;
    }

    public void eliminar(String numeroHotel) throws IOException {
        int totalRegistros = (int)(raf.length() / TAMANO_REGISTRO);
        for (int i = 0; i < totalRegistros; i++) {
            raf.seek(i * TAMANO_REGISTRO);
            String numeroActual = readString(TAMANO_NUMERO, raf.getFilePointer());
            if (numeroActual.trim().equalsIgnoreCase(numeroHotel)) { // Usar trim() para comparar
                raf.seek(i * TAMANO_REGISTRO);
                byte[] marcador = toBytes("***", TAMANO_NUMERO);
                raf.write(marcador); // Marcar el hotel como borrado lógicamente

                // IMPORTANTE: También eliminar todas las habitaciones asociadas a este hotel
                roomData.deleteAllRoomsByHotel(numeroHotel.trim());
                return;
            }
        }
    }

    public boolean insert(Hotel hotel) throws IOException {
        //Verificar si ya existe un hotel con el mismo número
        ArrayList<Hotel> hotelesExistentes = findAll(); // Usamos el metodo findAll()
        for (Hotel h : hotelesExistentes) {
            if (h.getNumber().equals(hotel.getNumber())) {
                // Si encontramos un hotel con el mismo número, lanzamos una false
                return false;
            }
        }

        // Aumentar el tamaño del archivo para el nuevo registro del hotel
        raf.setLength(raf.length() + TAMANO_REGISTRO);
        raf.seek(raf.length() - TAMANO_REGISTRO);

        // Escribir la información del hotel
        byte numero[] = toBytes(hotel.getNumber(), TAMANO_NUMERO);
        raf.write(numero);
        byte nombre[] = toBytes(hotel.getName(), TAMANO_NOMBRE);
        raf.write(nombre);
        byte direccion[] = toBytes(hotel.getAddress(), TAMANO_DIRECCION);
        raf.write(direccion);

        // Si el hotel tiene habitaciones, las insertamos usando RoomData
        if (hotel.getRooms() != null) {
            for (Room room : hotel.getRooms()) {
                // Asegurarnos de que la habitación está vinculada al hotel correcto
                room.setHotelNumber(hotel.getNumber());
                roomData.insert(room); // Delegar la inserción de la habitación a RoomData
            }
        }

        return true; //se inserto con exito
    }
}