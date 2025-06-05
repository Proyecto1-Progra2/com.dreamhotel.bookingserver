package sockets;

import data.*;
import domain.*;
import utils.Action;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;

public class Client extends Thread {

    private PrintStream send;
    private BufferedReader receive;
    private Socket socket;
    private String lectura;

    private HotelData hotelData;
    private RoomData roomData;
    private ImageData imageData;
    private ReceptionistData receptionistData;
    private BookingData bookingData;
    private HuespedData huespedData;

    private boolean inserted; //es la hora de registrar hotel y room, valida que no se repitan sus numeros

    private Receptionist receptionist;
    private Host host;

    public Client(Socket socket) throws IOException {
        this.socket = socket;
        this.send = new PrintStream(this.socket.getOutputStream());
        this.receive = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    } // constructor

    @Override
    public void run() {
        try {
            this.hotelData = new HotelData();
            this.roomData = new RoomData();
            this.imageData = new ImageData();
            this.receptionistData = new ReceptionistData();
            this.bookingData = new BookingData();
            this.huespedData = new HuespedData();

            while (true) {
                this.lectura = this.receive.readLine();
                //System.out.println(this.lectura);
                String[] datos = this.lectura.split("\\|\\|\\|");
                String accion = datos[0];
                switch (accion) {
                    case Action.HOTEL_REGISTER:
                        inserted = this.hotelData.insert(new Hotel(datos[1], datos[2], datos[3], new ArrayList<>()));
                        if (inserted){
                            this.send.println(Action.HOTEL_REGISTERED);

                        }else{
                            this.send.println(Action.HOTEL_NOT_REGISTERED);
                        }
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
                        Hotel hotel = new Hotel(datos[1], datos[2], datos[3], new ArrayList<>());
                        int posHotel = this.hotelData.buscarPosicion(datos[4]);
                        this.hotelData.insertPos(hotel, posHotel);
                        this.send.println(Action.HOTEL_UPDATED);
                        break;
                    case Action.HOTEL_DELETE:
                        this.hotelData.eliminar(datos[1]);
                        this.send.println(Action.HOTEL_DELETED);
                        break;
                    case Action.ROOM_REGISTER: //el dato 5 es de imagenes, tal tal en el insert de RoomData mandarle un arreglo de bytes con la imagen
                        inserted = this.roomData.insert(new Room(datos[1], datos[2], datos[3], Double.parseDouble(datos[4]), null, datos[7]));//revisar para meter los datos
                        byte[] imageBytes = Base64.getDecoder().decode(datos[6]);
                        this.imageData.insert(new Image(datos[5], datos[7], imageBytes));

                        if (inserted){
                            this.send.println(Action.ROOM_REGISTERED);

                        }else{
                            this.send.println(Action.ROOM_NOT_REGISTERED);
                        }
                        break;
                    case Action.ROOM_NOT_REGISTER:
                        this.send.println(Action.ROOM_NOT_REGISTERED);
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
                        Room room = new Room(datos[1], datos[2], datos[3], Double.parseDouble(datos[4]), null, datos[7]);
                        int posRoom = this.roomData.buscarPosicion(room.getRoomNumber(), room.getHotelNumber());
                        System.out.println(posRoom);
                        this.roomData.insertPos(room, posRoom);
                        this.send.println(Action.ROOM_UPDATED);
                        break;
                    case Action.ROOM_SEARCH://ese roomSolicitado dice que es null
                        Room roomSolicitado = this.roomData.findHotelRoom(datos[1], datos[2]);
                        this.send.println(Action.ROOM_SEARCH+roomSolicitado.toString());//no se que debe colocarse
                        break;
                    case Action.ROOM_DELETE:
                        this.roomData.deleteHotelRoom(datos[1], datos[2]);
                        this.send.println(Action.ROOM_DELETED);
                        break;
                    case Action.HOTEL_ROOMS:
                        String envioRoomsHotel = Action.HOTEL_ROOMS;
                        ArrayList<Room> roomsHotel = this.roomData.findAllRoomsByHotel(datos[1]);
                        for (Room roomHotel : roomsHotel) {
                            envioRoomsHotel += roomHotel.toString();
                        }
                        this.send.println(envioRoomsHotel);
                        break;
                    case Action.IMAGE_REQUEST:
                        StringBuilder envioImageBuilder = new StringBuilder(Action.IMAGE_REQUEST);
                        ArrayList<Image> images = this.imageData.findByRoomNumber(datos[1], datos[2]);

                        for (Image image : images) {
                            byte[] imageBytes2 = image.getImage();
                            String encoded = Base64.getEncoder().encodeToString(imageBytes2);
                            envioImageBuilder.append("|||").append(encoded);
                        }

                        this.send.println(envioImageBuilder.toString());
                        break;
                    case Action.RECEPTIONIST_REGISTER:
                        this.receptionistData.insert(new Receptionist(datos[1], datos[2], datos[3], Integer.parseInt(datos[4]), datos[5], datos[6]));
                        this.send.println(Action.RECEPTIONIST_REGISTERED);
                        break;
                    case Action.RECEPTIONIST_SEARCH:
                        if ((this.receptionist = this.receptionistData.receptionistLogin(datos[1],datos[2])) != null) {
                            this.send.println(Action.RECEPTIONIST_LOGIN + this.receptionist);
                        } else {
                            this.send.println(Action.RECEPTIONIST_NOT_LOGIN);
                        }
                        break;
                    case Action.REQUEST_BOOKING_NUMBER:
                        if (this.bookingData.bookingNumberExists(datos[1])) {
                            this.send.println(Action.BOOKING_NUMBER_EXIST);
                        } else {
                            this.send.println(Action.BOOKING_NUMBER_NO_EXIST);
                        }
                        break;
                    case Action.BOOKING_REGISTER:
                        Person host = new Person(datos[2], datos[3], 0);
                        LocalDate starDate = LocalDate.parse(datos[4]);
                        LocalDate departureDate = LocalDate.parse(datos[5]);
                        Person receptionist = new Person(datos[6], datos[7], 0);
                        this.bookingData.insert(new Booking(datos[1], host, starDate, departureDate, receptionist, datos[8], datos[9]));
                        break;
                    case Action.BOOKING_LIST:
                        String envioBookings = Action.BOOKING_LIST;
                        ArrayList<Booking> bookings = this.bookingData.findBookingHotelNumber(datos[1]);
                        for (Booking booking : bookings) {
                            envioBookings += booking.toString();
                        }
                        this.send.println(envioBookings);
                        break;
                    case Action.HOST_REGISTER:
                        this.huespedData.insert(new Host(datos[1], datos[2], datos[3], Integer.parseInt(datos[4]), datos[5], datos[6], datos[7]));
                        this.send.println(Action.HOST_REGISTERED);
                        break;
                    case Action.HOST_REQUEST:
                        if ((this.host = this.huespedData.hostSearch(datos[1])) != null) {
                            this.send.println(Action.HOST_EXIST+this.host);
                        } else {
                            this.send.println(Action.HOST_NO_EXIST);
                        }
                        break;
                    case Action.BOOKING_DELETE:
                        this.bookingData.delete(datos[1]);
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
