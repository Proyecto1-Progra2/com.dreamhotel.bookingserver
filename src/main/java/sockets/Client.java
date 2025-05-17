package sockets;

import data.HotelData;
import data.HuespedData;
import domain.Hotel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

public class Client extends Thread {

    private PrintStream send;
    private BufferedReader receive;
    private Socket socket;
    private String lectura;

    private HuespedData huespedData;
    private HotelData hotelData;

    public Client(Socket socket) throws IOException {
        this.socket = socket;
        this.send = new PrintStream(this.socket.getOutputStream());
        this.receive = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

        this.huespedData = new HuespedData();
        this.hotelData = new HotelData();
    } // constructor

    @Override
    public void run() {
        try {
            while (true) {
                this.lectura = this.receive.readLine();
                //System.out.println(this.lectura);
                String[] datos = this.lectura.split("-");
                String accion = datos[0];
                switch (accion) {
                    case "registrarHotel":
                        this.hotelData.insert(new Hotel(datos[1], datos[2], datos[3]));
                        break;
                    case "mostrarHoteles":
                        String envioHoteles = "mostrarHoteles";
                        ArrayList<Hotel> hoteles = this.hotelData.findAll();
                        for (Hotel hotel : hoteles) {
                            envioHoteles += hotel.toString();
                        }
                        this.send.println(envioHoteles);
                        break;
                    case "solicitarHotel":
                        Hotel hotelSolicitado = this.hotelData.buscarHotel(datos[1]);
                        this.send.println("hotelSolicitado"+hotelSolicitado.toString());
                        break;
                    case "modificarHotel":
                        Hotel hotel = new Hotel(datos[1], datos[2], datos[3]);
                        int posHotel = this.hotelData.buscarPosicion(hotel.getNumber());
                        this.hotelData.insertPos(hotel, posHotel);
                        break;
                    case "eliminarHotel":
                        this.hotelData.eliminar(datos[1]);
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
