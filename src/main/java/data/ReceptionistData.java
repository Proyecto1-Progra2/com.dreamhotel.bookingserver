package data;

import domain.Receptionist;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class ReceptionistData {

    private RandomAccessFile raf;
    private final int TAMANO_REGISTRO = 166; //total por cantidad de registros
    private final int TAMANO_EMPLOYEDNUMBER = 10;
    private final int TAMANO_NAME = 50;
    private final int TAMANO_LASTNAME = 50;
    private final int TAMANO_PHONENUMBER = 16;
    private final int TAMANO_USERNAME = 20;
    private final int TAMANO_PASSWORD = 20;

    public ReceptionistData() throws FileNotFoundException {
        this.raf = new RandomAccessFile(new File("receptionist.dat"), "rw");
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

    public void insert(Receptionist receptionist) throws IOException {
        // Aumentar el tamaño del archivo para el nuevo registro del hotel
        raf.setLength(raf.length() + TAMANO_REGISTRO);
        raf.seek(raf.length() - TAMANO_REGISTRO);

        // Escribir la información del recepcionista
        byte[]  employedNumber = toBytes(receptionist.getEmployedNumber(), TAMANO_EMPLOYEDNUMBER);
        raf.write(employedNumber);//numero del recepcionista
        byte[] name = toBytes(receptionist.getName(), TAMANO_NAME);
        raf.write(name);
        byte[]  lastName = toBytes(receptionist.getLastName(), TAMANO_LASTNAME);
        raf.write(lastName);
        raf.writeInt(receptionist.getPhoneNumber());
        //el name de usuario va en el formato name.lastName
        byte[]  userName = toBytes(receptionist.getUsername(), TAMANO_USERNAME);
        raf.write(userName);
        byte[]  passWord = toBytes(receptionist.getPassword(), TAMANO_PASSWORD);
        raf.write(passWord);

    }

    public boolean receptionistLogin(String username, String password) throws IOException {
        boolean encontrado = false;
        int totalRegistros = (int) (this.raf.length() / TAMANO_REGISTRO);
        int numReg = 0;

        while (numReg < totalRegistros && !encontrado) {
            int posInicioRegistro = numReg * TAMANO_REGISTRO;// el inicio del archivo

            int posUsername = posInicioRegistro + TAMANO_EMPLOYEDNUMBER + TAMANO_NAME + TAMANO_LASTNAME + 4; //la posición para apuntar al usuario de cada archivo
            raf.seek(posUsername);
            String usernameActual = readString(TAMANO_USERNAME, posUsername);

            int posPassword = posUsername + TAMANO_USERNAME; //la posición para apuntar la password de cada archivo
            raf.seek(posPassword);
            String passwordActual = readString(TAMANO_PASSWORD, posPassword);

            //valida si el usuario con su contraseña ya existen
            if (username.compareToIgnoreCase(usernameActual.trim())==0 && password.compareToIgnoreCase(passwordActual.trim())==0) // Usar trim() para comparar
                encontrado = true;
            else numReg++;
        }

        return encontrado;
    }

    public Receptionist receptionistLoginTwo(String username, String password) throws IOException {
        Receptionist receptionist = null;
        ArrayList<Receptionist> receptionists = findAll();
        for (Receptionist r : receptionists) {
            if (r.getUsername().trim().equalsIgnoreCase(username.trim()) && r.getPassword().trim().equalsIgnoreCase(password)) {
                receptionist = r;
                break;
            }
        }
        return receptionist;
    }


    public ArrayList<Receptionist> findAll() throws IOException {
        int totalRegistros = (int)(raf.length() / TAMANO_REGISTRO);
        ArrayList<Receptionist> receptionists = new ArrayList<>();
        for (int i = 0; i < totalRegistros; i++) {
            raf.seek(i * TAMANO_REGISTRO);

            String employedNumber = readString(TAMANO_EMPLOYEDNUMBER, raf.getFilePointer());
            String name = readString(TAMANO_NAME, raf.getFilePointer());
            String lastName = readString(TAMANO_LASTNAME, raf.getFilePointer());
            int phoneNumbers = raf.readInt();
            String userName = readString(TAMANO_USERNAME, raf.getFilePointer());
            String password = readString(TAMANO_PASSWORD, raf.getFilePointer());

            receptionists.add(new Receptionist(employedNumber,name,lastName, phoneNumbers,userName,password));
        }
        return receptionists;
    }

    public int buscarEmployedNumber(String employedNumber) throws IOException {
        boolean encontrado = false;
        int totalRegistros = (int) (this.raf.length() / TAMANO_REGISTRO);
        int numReg = 0;

        while (numReg < totalRegistros && !encontrado) {
            this.raf.seek(numReg * TAMANO_REGISTRO);
            String numeroRecepcionistaActual = this.readString(TAMANO_EMPLOYEDNUMBER, this.raf.getFilePointer());

            if (employedNumber.equalsIgnoreCase(numeroRecepcionistaActual.trim())) // Usar trim() para comparar
                encontrado = true;
            else numReg++;
        }
        return encontrado ? numReg : -1; // Devolvemos -1 si no se encuentra el recepcionista
    }

    public Receptionist buscarRecepcionista(String employedNumber) throws IOException {
        Receptionist receptionist = null;
        int totalRegistros = (int) (this.raf.length() / TAMANO_REGISTRO);
        int numReg = 0;

        while (numReg < totalRegistros && receptionist == null) {

            this.raf.seek(numReg * TAMANO_REGISTRO);
            String numeroRecepcionistaActual = this.readString(TAMANO_EMPLOYEDNUMBER, this.raf.getFilePointer());

            if (employedNumber.equalsIgnoreCase(numeroRecepcionistaActual.trim())) { // Usar trim() para comparar

                String name = readString(TAMANO_NAME, raf.getFilePointer());
                String lastName = readString(TAMANO_LASTNAME, raf.getFilePointer());
                int phoneNumbers = raf.readInt();
                String userName = readString(TAMANO_USERNAME, raf.getFilePointer());
                String password = readString(TAMANO_PASSWORD, raf.getFilePointer());

                receptionist = new Receptionist(numeroRecepcionistaActual,name,lastName, phoneNumbers,userName,password);
            } else {
                numReg++;
            }
        }
        return receptionist;
    }

//    public void eliminar(String employedNumber) throws IOException {
//        int totalRegistros = (int)(raf.length() / TAMANO_REGISTRO);
//        int offsetEmployedNumber = TAMANO_NAME// 50
//                + TAMANO_LASTNAME // 50
//                + TAMANO_PHONENUMBER; // 16
//        // total = 116 bytes antes de employedNumber
//
//        for (int i = 0; i < totalRegistros; i++) {
//            this.raf.seek(i * TAMANO_REGISTRO + offsetEmployedNumber);
//            String numeroRecepcionistaActual = this.readString(TAMANO_EMPLOYEDNUMBER, this.raf.getFilePointer());
//
//            if (employedNumber.trim().equalsIgnoreCase(numeroRecepcionistaActual)) { // Usar trim() para comparar
//                raf.seek(i * TAMANO_REGISTRO);
//                byte[] marcador = toBytes("***", TAMANO_NUMERO);
//                raf.write(marcador); // Marcar el hotel como borrado lógicamente
//
//
//                return;
//            }
//        }
//    }
//    public void insertPos(Receptionist receptionist, int posicion) throws IOException {
//        raf.seek(posicion * TAMANO_REGISTRO);
//
//        // Escribir la información del recepcionista
//        byte[]  employedNumber = toBytes(receptionist.getEmployedNumber(), TAMANO_EMPLOYEDNUMBER);
//        raf.write(employedNumber);//numero del recepcionista
//        byte[] name = toBytes(receptionist.getName(), TAMANO_NAME);
//        raf.write(name);
//        byte[]  lastName = toBytes(receptionist.getLastName(), TAMANO_LASTNAME);
//        raf.write(lastName);
//        raf.writeInt(receptionist.getPhoneNumber());
//        byte[]  userName = toBytes(receptionist.getUsername(), TAMANO_USERNAME);
//        raf.write(userName);
//        byte[]  passWord = toBytes(receptionist.getPassword(), TAMANO_PASSWORD);
//        raf.write(passWord);
//
//    }
}