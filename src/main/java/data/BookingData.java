package data;

import domain.Booking;
import domain.Hotel;
import domain.Person;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDate;
import java.util.ArrayList;

public class BookingData {

    private RandomAccessFile raf;

    private final int TAMANO_BOOKING_NUMBER = 10;
    private final int TAMANO_HOST = 50;
    private final int TAMANO_STARTDATE = 15;
    private final int TAMANO_DEPARTUREDATE = 15;
    private final int TAMANO_RECEPTIONIST = 50;
    private final int TAMANO_ROOMNUMBER = 20;
    private final int TAMANO_HOTELNUMBER = 20;
    private final int TAMANO_REGISTRO = 180;

    public BookingData() throws FileNotFoundException {
        this.raf = new RandomAccessFile(new File("bookings.dat"), "rw");
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

    public void insert(Booking booking) throws IOException {
        raf.setLength(raf.length() + TAMANO_REGISTRO);
        raf.seek(raf.length() - TAMANO_REGISTRO);

        raf.write(toBytes(booking.getBookingNumber(), TAMANO_BOOKING_NUMBER));
        raf.write(toBytes(booking.getHost().toString(), TAMANO_HOST));
        raf.write(toBytes(booking.getStartDate().toString(), TAMANO_STARTDATE));
        raf.write(toBytes(booking.getDepartureDate().toString(), TAMANO_DEPARTUREDATE));
        raf.write(toBytes(booking.getReceptionist().toString(), TAMANO_RECEPTIONIST));
        raf.write(toBytes(booking.getRoomNumber(), TAMANO_ROOMNUMBER));
        raf.write(toBytes(booking.getHotelNumber(), TAMANO_HOTELNUMBER));
    }

    public ArrayList<Booking> findAll() throws IOException {
        ArrayList<Booking> bookings = new ArrayList<>();
        int totalRegistros = (int)(raf.length() / TAMANO_REGISTRO);

        for (int i = 0; i < totalRegistros; i++) {
            raf.seek(i * TAMANO_REGISTRO);

            String bookingNumber = readString(TAMANO_BOOKING_NUMBER, raf.getFilePointer());
            if (bookingNumber.trim().equals("***")) {
                raf.skipBytes(TAMANO_REGISTRO - TAMANO_BOOKING_NUMBER);
                continue;
            }

            String hostStr = readString(TAMANO_HOST, raf.getFilePointer());
            String startDateStr = readString(TAMANO_STARTDATE, raf.getFilePointer());
            String departureDateStr = readString(TAMANO_DEPARTUREDATE, raf.getFilePointer());
            String receptionistStr = readString(TAMANO_RECEPTIONIST, raf.getFilePointer());
            String roomNumber = readString(TAMANO_ROOMNUMBER, raf.getFilePointer());
            String hotelNumber = readString(TAMANO_HOTELNUMBER, raf.getFilePointer());

            Person host = Person.fromString(hostStr);
            Person receptionist = Person.fromString(receptionistStr);
            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate departureDate = LocalDate.parse(departureDateStr);

            Booking booking = new Booking(
                    bookingNumber,
                    host,
                    startDate,
                    departureDate,
                    receptionist,
                    roomNumber,
                    hotelNumber
            );
            bookings.add(booking);
        }
        return bookings;
    }

    public ArrayList<Booking> findBookingHotelNumber(String hotelNumber) throws IOException {
        ArrayList<Booking> bookings = new ArrayList<>();

        for (int i = 0; i < this.findAll().size(); i++) {
            if (this.findAll().get(i).getHotelNumber().equalsIgnoreCase(hotelNumber)) {
                bookings.add(this.findAll().get(i));
            }
        }

        return bookings;
    }

    public boolean bookingNumberExists(String bookingNumber) throws IOException {
        int totalRegistros = (int)(raf.length() / TAMANO_REGISTRO);

        for (int i = 0; i < totalRegistros; i++) {
            raf.seek(i * TAMANO_REGISTRO);
            String currentBookingNumber = readString(TAMANO_BOOKING_NUMBER, raf.getFilePointer());

            if (currentBookingNumber.trim().equals(bookingNumber)) {
                return true;
            }
        }

        return false;
    }

    public void delete(String bookingNumber) throws IOException {
        int totalRegistros = (int)(raf.length() / TAMANO_REGISTRO);
        for (int i = 0; i < totalRegistros; i++) {
            raf.seek(i * TAMANO_REGISTRO);
            String numeroActual = readString(TAMANO_BOOKING_NUMBER, raf.getFilePointer());
            if (numeroActual.trim().equalsIgnoreCase(bookingNumber)) {
                raf.seek(i * TAMANO_REGISTRO);
                byte[] marcador = toBytes("***", TAMANO_BOOKING_NUMBER);
                raf.write(marcador);
                return;
            }
        }
    }
}
