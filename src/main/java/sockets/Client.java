package sockets;

import data.HotelData;
import data.ImageData;
import data.RoomData;
import domain.Hotel;
import domain.Image;
import domain.Room;
import utils.Action;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client extends Thread {

    private PrintStream send;
    private BufferedReader receive;
    private Socket socket;
    private String lectura;

    private HotelData hotelData;
    private RoomData roomData;
    private ImageData imageData;

    public Client(Socket socket) throws IOException {
        this.socket = socket;
        this.send = new PrintStream(this.socket.getOutputStream());
        this.receive = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

        this.hotelData = new HotelData();
        this.roomData = new RoomData();
        this.imageData = new ImageData();
    } // constructor

    @Override
    public void run() {
        try {
            while (true) {
                this.lectura = this.receive.readLine();
                System.out.println(this.lectura);
                String[] datos = this.lectura.split("-");
                String accion = datos[0];
                switch (accion) {
                    case Action.HOTEL_REGISTER:
                        this.hotelData.insert(new Hotel(datos[1], datos[2], datos[3]));
                        this.send.println(Action.HOTEL_REGISTERED);
                        break;
                    case Action.HOTEL_NOT_REGISTER:
                        this.send.println(Action.HOTEL_NOT_REGISTERED);
                        break;
                    case Action.HOTEL_LIST:
                        String envioHoteles = Action.HOTEL_LIST;
                        ArrayList<Hotel> hoteles = this.hotelData.findAll();
                        for (Hotel hotel : hoteles) {
                            envioHoteles += hotel.toString();
                        }
                        this.send.println(envioHoteles);
                        break;
                    case Action.HOTEL_SEARCH:
                        Hotel hotelSolicitado = this.hotelData.buscarHotel(datos[1]);
                        this.send.println(Action.HOTEL_SEARCH+hotelSolicitado.toString());
                        break;
                    case Action.HOTEL_UPDATE:
                        Hotel hotel = new Hotel(datos[1], datos[2], datos[3]);
                        int posHotel = this.hotelData.buscarPosicion(hotel.getNumber());
                        this.hotelData.insertPos(hotel, posHotel);
                        this.send.println(Action.HOTEL_UPDATED);
                        break;
                    case Action.HOTEL_DELETE:
                        this.hotelData.eliminar(datos[1]);
                        this.send.println(Action.HOTEL_DELETED);
                        break;
                    case Action.ROOM_REGISTER: //el dato 5 es de imagenes, tal tal en el insert de RoomData mandarle un arreglo de bytes con la imagen
                        this.roomData.insert(new Room(datos[1], datos[2], datos[3], Double.parseDouble(datos[4]), null));//revisar para meter los datos
                        this.imageData.insert(new Image(datos[5], new File(datos[6])));
                        this.send.println(Action.ROOM_REGISTERED);
                        break;
                    case Action.ROOM_LIST: //revisar
                        String envioRooms = Action.ROOM_LIST;
                        ArrayList<Room> rooms = this.roomData.findAll();
                        for (Room room : rooms) {
                            envioRooms += room.toString();
                        }
                        this.send.println(envioRooms);
                        break;
                    case Action.ROOM_UPDATE:
                        //Room room = new Room(datos[1], datos[2], datos[3], Double.parseDouble(datos[4]));
                        //int posRoom = this.roomData.buscarPosicion(room.getRoomNumber());
                        //this.roomData.insertPos(room, posRoom);
                        //this.send.println(Action.ROOM_UPDATED);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + accion);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
