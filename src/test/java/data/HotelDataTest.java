package data;

import domain.Hotel;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class HotelDataTest {

//    @Test
    public void crear_archivo() throws IOException {
        //arrange
        HotelData hotelData = null;
        Hotel hotel1 = new Hotel("100","RIO", "Guanacaste", null);
        Hotel hotel2 = new Hotel("200","PUNTA CANA", "Salas", null);
        Hotel hotel3 = new Hotel("300","BARSEL", "Quiros", null);

        //act
        try{
            hotelData = new HotelData();
            hotelData.insert(hotel1);
            hotelData.insert(hotel2);
            hotelData.insert(hotel3);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Jefrey se encuentra en la posición: " + hotelData.buscarPosicion("100"));
        System.out.println("El recepcionista que esta en la primera posición es: " + hotelData.buscarHotel("100"));
        System.out.println("----------------------------------------");
        System.out.println("Jime se encuentra en la posición: " + hotelData.buscarPosicion("200"));
        System.out.println("El recepcionista que esta en la primera posición es: " + hotelData.buscarHotel("200"));
        System.out.println("----------------------------------------");
        System.out.println("Luis se encuentra en la posición: " + hotelData.buscarPosicion("300"));
        System.out.println("El recepcionista que esta en la primera posición es: " + hotelData.buscarHotel("300"));
        System.out.println("----------------------------------------");
        System.out.println(hotelData.findAll());

    }

//    @Test
    public void prueba_excepcion() throws IOException {
        //arrange
        HotelData hotelData = null;
        Hotel hotel1 = new Hotel("100","RIO", "Guanacaste", null);
        Hotel hotel2 = new Hotel("200","PUNTA CANA", "Salas", null);
        Hotel hotel3 = new Hotel("100","BARSEL", "Quiros", null);

        //act
        try{
            hotelData = new HotelData();
            hotelData.insert(hotel1);
            hotelData.insert(hotel2);
            hotelData.insert(hotel3);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Jefrey se encuentra en la posición: " + hotelData.buscarPosicion("100"));
        System.out.println("El recepcionista que esta en la primera posición es: " + hotelData.buscarHotel("100"));
        System.out.println("----------------------------------------");
        System.out.println("Jime se encuentra en la posición: " + hotelData.buscarPosicion("200"));
        System.out.println("El recepcionista que esta en la primera posición es: " + hotelData.buscarHotel("200"));
        System.out.println("----------------------------------------");
        System.out.println("Luis se encuentra en la posición: " + hotelData.buscarPosicion("300"));
        System.out.println("El recepcionista que esta en la primera posición es: " + hotelData.buscarHotel("300"));
        System.out.println("----------------------------------------");
        System.out.println(hotelData.findAll());

    }
}