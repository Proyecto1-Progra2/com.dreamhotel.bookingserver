package data;

import domain.Receptionist;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ReceptionistDataTest {

    @Test
    public void crear_archivo() throws IOException {
        //arrange
        ReceptionistData receptionistData = null;
        Receptionist person1 = new Receptionist("A17","Luis", "Sibaja", 87056350,  "Luka", "Sorpresa");
        Receptionist person2 = new Receptionist("B17","Jime", "Salas", 89631785,  "JimeEst", "Lindi");
        Receptionist person3 = new Receptionist("C17","Jefrey", "Quiros", 89632587, "Jefreyss", "ProgrA2");

        //act
        try{
            receptionistData = new ReceptionistData();
            receptionistData.insert(person1);
            receptionistData.insert(person2);
            receptionistData.insert(person3);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Jefrey se encuentra en la posición: " + receptionistData.buscarEmployedNumber("C17"));
        System.out.println("El recepcionista que esta en la primera posición es: " + receptionistData.buscarRecepcionista("C17"));
        System.out.println("----------------------------------------");
        System.out.println("Jime se encuentra en la posición: " + receptionistData.buscarEmployedNumber("B17"));
        System.out.println("El recepcionista que esta en la primera posición es: " + receptionistData.buscarRecepcionista("B17"));
        System.out.println("----------------------------------------");
        System.out.println("Luis se encuentra en la posición: " + receptionistData.buscarEmployedNumber("A17"));
        System.out.println("El recepcionista que esta en la primera posición es: " + receptionistData.buscarRecepcionista("A17"));
        System.out.println("----------------------------------------");
        System.out.println(receptionistData.findAll());

        int one = receptionistData.buscarEmployedNumber("C17");//3
        int two = receptionistData.buscarEmployedNumber("B17");//2
        int three = receptionistData.buscarEmployedNumber("A17");//1
        //assert
        assertEquals(3, one);
        assertEquals(2, two);
        assertEquals(1, three);
        System.out.println("------------------------------");

    }

    @Test
    void login_usuario() throws IOException {
        //arrange
        ReceptionistData receptionistData = null;
        Receptionist person1 = new Receptionist("A17","Luis", "Sibaja", 87056350,  "Luka", "Sorpresa");
        Receptionist person2 = new Receptionist("B17","Jime", "Salas", 89631785,  "JimeEst", "Lindi");
        Receptionist person3 = new Receptionist("C17","Jefrey", "Quiros", 89632587, "Jefreyss", "ProgrA2");

        //act
        try{
            receptionistData = new ReceptionistData();
            receptionistData.insert(person1);
            receptionistData.insert(person2);
            receptionistData.insert(person3);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("----------------------------------------");
//        boolean login1 = receptionistData.receptionistLogin("Luka", "Sorpresa");
//        boolean login2 = receptionistData.receptionistLogin("JimeEst", "Lindi");
//        boolean login3 = receptionistData.receptionistLogin("Jefreyss", "ProgrA2");
//        boolean login4 = receptionistData.receptionistLogin("Rosela", "Wiki");


//        String username1 = "Luka";
//        String username2 = "JimeEst";
//        String username3 = "Jefreyss";
//        String username4 = "Rosela";
//
//        System.out.println("El usuario " + username1 + " esta logueado? " + login1);
//        System.out.println("El usuario " + username2 + " esta logueado? " + login2);
//        System.out.println("El usuario " + username3 + " esta logueado? " + login3);
//        System.out.println("El usuario " + username4 + " esta logueado? " + login4);

//
//        assertTrue(login1);
//        assertTrue(login2);
//        assertTrue(login3);
//
//        assertFalse(login4);

    }

//    @Test
//    void login_one_usuario() throws IOException {
//        //arrange
//        ReceptionistData receptionistData = null;
//        Receptionist person1 = new Receptionist("A17","Luis", "Sibaja", 87056350,  "Luka", "Sorpresa");
//
//        //act
//        try{
//            receptionistData = new ReceptionistData();
//            receptionistData.insert(person1);
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        System.out.println("----------------------------------------");
//        boolean login1 = receptionistData.receptionistLogin("Luka", "Sorpresa");
//
//        String username = "Luka";
//        String password = "Sorpresa";
//
//        System.out.println("El usuario " + username + " esta logueado? " + login1);
//
//
//    }
}