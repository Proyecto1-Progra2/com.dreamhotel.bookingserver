package data;

import domain.Hotel;
import domain.Room;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

class RoomDataTest {

//    @Test
    public void crear_archivo(){
        //arrange
        RoomData roomData = null;
        ArrayList<Room> rooms = new ArrayList<>();
        Room room1 = new Room("A17", "Disponible", "Lujo", 10000.0,null, "100");
        Room room2 = new Room("B17", "Ocupado", "Familiar", 20000.0,null, "200");
        Room room3 = new Room("C17", "Mantenimiento", "Lujo", 5000.0,null, "300");

        //act
        try{
            roomData = new RoomData();
            roomData.insert(room1);
            roomData.insert(room2);
            roomData.insert(room3);
            System.out.println("----------------------------------------");
            System.out.println(roomData.findAll());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }




    }

//    @Test
    public void prueba_excepcion(){

        //arrange
        RoomData roomData = null;
        Room room1 = new Room("A17", "Disponible", "Lujo", 10000.0,null, "100");
        Room room2 = new Room("B17", "Ocupado", "Familiar", 20000.0,null, "200");
        Room room3 = new Room("A17", "Mantenimiento", "Lujo", 5000.0,null, "300");

        //act
        try{
            roomData = new RoomData();
            roomData.insert(room1);
            roomData.insert(room2);
            roomData.insert(room3);
            System.out.println("----------------------------------------");
            System.out.println(roomData.findAll());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

//    @Test
    public void rooms_diferentes_en_hoteles() throws IOException {
        //arrange
        RoomData roomData = null;

        Room room1 = new Room("A17", "Disponible", "Lujo", 10000.0,null, "100");
        Room room2 = new Room("B17", "Ocupado", "Familiar", 20000.0,null, "200");
        Room room3 = new Room("C17", "Mantenimiento", "Lujo", 5000.0,null, "300");
        Room room4 = new Room("D17", "Disponible", "Simple", 1000.0,null, "400");
        Room room5 = new Room("E17", "Ocupado", "Lujo", 50000.0,null, "500");
        Room room6 = new Room("F17", "Disponible", "Lujo", 55000.0,null, "600");

        //act
        try{
            roomData = new RoomData();
            roomData.insert(room1);
            roomData.insert(room2);
            roomData.insert(room3);
            roomData.insert(room4);
            roomData.insert(room5);
            roomData.insert(room6);
            System.out.println("----------------------------------------");
            System.out.println(roomData.findAll());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

//    @Test
    public void rooms_sin_excepcion_en_hoteles() {
        //arrange
        RoomData roomData = null;

        Room room1 = new Room("A17", "Disponible", "Lujo", 10000.0,null, "100");
        Room room2 = new Room("B17", "Ocupado", "Familiar", 20000.0,null, "100");
        Room room3 = new Room("D17", "Mantenimiento", "Lujo", 5000.0,null, "100");
        Room room4 = new Room("A17", "Disponible", "Simple", 1000.0,null, "200");
        Room room5 = new Room("B17", "Ocupado", "Lujo", 50000.0,null, "200");
        Room room6 = new Room("D17", "Disponible", "Lujo", 55000.0,null, "200");

        //act
        try{
            roomData = new RoomData();
            roomData.insert(room1);
            roomData.insert(room2);
            roomData.insert(room3);
            roomData.insert(room4);
            roomData.insert(room5);
            roomData.insert(room6);
            System.out.println("----------------------------------------");
            System.out.println(roomData.findAll());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

//    @Test
    public void rooms_con_excepcion_en_hoteles() {
        //arrange
        RoomData roomData = null;

        Room room1 = new Room("A17", "Disponible", "Lujo", 10000.0,null, "100");
        Room room2 = new Room("B17", "Ocupado", "Familiar", 20000.0,null, "100");
        Room room3 = new Room("D17", "Mantenimiento", "Lujo", 5000.0,null, "100");
        Room room4 = new Room("B17", "Disponible", "Simple", 1000.0,null, "200");
        Room room5 = new Room("C17", "Ocupado", "Lujo", 50000.0,null, "200");
        Room room6 = new Room("B17", "Disponible", "Lujo", 55000.0,null, "200");

        //act
        try{
            roomData = new RoomData();
            roomData.insert(room1);
            roomData.insert(room2);
            roomData.insert(room3);
            roomData.insert(room4);
            roomData.insert(room5);
            roomData.insert(room6);
            System.out.println("----------------------------------------");
            System.out.println(roomData.findAll());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void rooms_con_hotel() throws IOException {
        //arrange
        HotelData hotelData = null;
        ArrayList<Room> rooms1 = new ArrayList<>();
        ArrayList<Room> rooms2 = new ArrayList<>();
        ArrayList<Room> rooms3 = new ArrayList<>();

        Room room1 = new Room("A17", "Disponible", "Lujo", 10000.0,null, "100");
        Room room2 = new Room("B17", "Ocupado", "Familiar", 20000.0,null, "100");
        Room room3 = new Room("C17", "Mantenimiento", "Lujo", 5000.0,null, "200");
        Room room4 = new Room("D17", "Disponible", "Simple", 1000.0,null, "200");
        Room room5 = new Room("E17", "Ocupado", "Lujo", 50000.0,null, "300");
        Room room6 = new Room("F17", "Disponible", "Lujo", 55000.0,null, "300");


        rooms1.add(room1);
        rooms1.add(room2);
        rooms2.add(room3);
        rooms2.add(room4);
        rooms3.add(room5);
        rooms3.add(room6);


        Hotel hotel1 = new Hotel("100","RIO", "Guanacaste", rooms1);
        Hotel hotel2 = new Hotel("200","PUNTA CANA", "Cartago", rooms2);
        Hotel hotel3 = new Hotel("300","BARSEL", "San Jose", rooms3);
        //act
        try{
            hotelData = new HotelData();
            hotelData.insert(hotel1);
            hotelData.insert(hotel2);
            hotelData.insert(hotel3);
            System.out.println("----------------------------------------");
            System.out.println(hotelData.findAll());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}