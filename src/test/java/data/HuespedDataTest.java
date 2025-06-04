package data;

import domain.Host;
//import org.junit.jupiter.api.Test;

import java.io.IOException;

//import static org.junit.jupiter.api.Assertions.*;

class HuespedDataTest {

    //@Test
    public void crear_archivo() throws IOException {
        //arrange
        HuespedData huespedData = null;
        Host person1 = new Host("A17","Luis", "Sibaja", 87056350,  "Cartago", "luis@gmail.com", "Costa Rica");
        Host person2 = new Host("B17","Jime", "Salas", 89631785,  "Cartago", "jime@gmail.com", "Costa Rica");
        Host person3 = new Host("C17","Jefrey", "Quiros", 89632587, "Turrialba", "jefrey@gmail.com", "Costa Rica");

        //act
        try{
            huespedData = new HuespedData();
            huespedData.insert(person1);
            huespedData.insert(person2);
            huespedData.insert(person3);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}