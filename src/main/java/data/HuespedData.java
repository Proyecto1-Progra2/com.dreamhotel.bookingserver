package data;

import domain.Host;
import domain.Receptionist;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class HuespedData {

    private RandomAccessFile raf;
    private final int TAMANO_REGISTRO = 185; //total por cantidad de registros
    private final int TAMANO_ID = 15;
    private final int TAMANO_NAME = 40;
    private final int TAMANO_LASTNAME = 40;
    private final int TAMANO_PHONENUMBER = 20;
    private final int TAMANO_ADDRESS = 20;
    private final int TAMANO_EMAIL = 30;
    private final int TAMANO_COUNTRY = 20;


    public HuespedData() throws FileNotFoundException {
        this.raf = new RandomAccessFile(new File("host.dat"), "rw");
    }

    private String readString(int tamanoString, long posicion) throws IOException {
        raf.seek(posicion);
        byte[] datos = new byte[tamanoString];
        raf.readFully(datos);
        String dato = new String(datos).trim();
        return dato;

    }//readString

    private byte[] toBytes(String dato, int tamanoString){
        byte[] datos = new byte[tamanoString];
        byte[] temp = dato.getBytes();
        for (int i = 0; i < tamanoString; i++) {
            if (i<temp.length)
                datos[i] =temp[i];
        }
        return datos;
    }

    public void insert(Host host) throws IOException {
        //aumentar el tamaño del archivo en 64 bytes (TAMAÑO_REGISTRO)
        raf.setLength(raf.length() + TAMANO_REGISTRO);
        raf.seek(raf.length() - TAMANO_REGISTRO);
        // Se debe transformar el string en un arreglo de bytes para
        // poder escribirlo en el archivo
        byte id[] = toBytes(host.getId(), TAMANO_ID);
        raf.write(id);
        byte nombre[] = toBytes(host.getName(),TAMANO_NAME);
        raf.write(nombre);
        byte apellidos[]= toBytes(host.getLastName(),TAMANO_LASTNAME);
        raf.write(apellidos);
        raf.writeInt(host.getPhoneNumber());//numero de telefono
        byte address[]= toBytes(host.getAddress(),TAMANO_ADDRESS);
        raf.write(address);
        byte email[]= toBytes(host.getEmail(),TAMANO_EMAIL);
        raf.write(email);
        byte country[]= toBytes(host.getCountry(),TAMANO_COUNTRY);
        raf.write(country);
    }

    public ArrayList<Host> findAll() throws IOException {
        int totalRegistros = (int)(raf.length()/TAMANO_REGISTRO);
        ArrayList<Host> huespedes = new ArrayList<>();
        for(int i=0; i<totalRegistros; i++){
            raf.seek(i*TAMANO_REGISTRO);

            String id= this.readString(TAMANO_ID,raf.getFilePointer());
            String name = this.readString(TAMANO_NAME, raf.getFilePointer());
            String lastName = this.readString(TAMANO_LASTNAME, raf.getFilePointer());
            int phoneNumber = raf.readInt();
            String address = this.readString(TAMANO_ADDRESS, raf.getFilePointer());
            String email = this.readString(TAMANO_EMAIL, raf.getFilePointer());
            String country = this.readString(TAMANO_COUNTRY, raf.getFilePointer());

            huespedes.add(new Host(id,name,lastName, phoneNumber, address, email, country ));
        }//for
        return huespedes;
    }
    public Host hostLogin(String name, String lastName) throws IOException {
        int totalRegistros = (int) (this.raf.length() / TAMANO_REGISTRO);
        int numReg = 0;
        Host host = null;

        while (numReg < totalRegistros) {
            int posInicioRegistro = numReg * TAMANO_REGISTRO;

            // Leer campos
            raf.seek(posInicioRegistro);
            String id = readString(TAMANO_ID, posInicioRegistro).trim();

            int posName = posInicioRegistro + TAMANO_ID;
            String nameActual = readString(TAMANO_NAME, posName).trim();

            int posLastName = posName + TAMANO_NAME;
            String lastNameActual = readString(TAMANO_LASTNAME, posLastName).trim();

            int posPhone = posLastName + TAMANO_LASTNAME;
            raf.seek(posPhone);
            int phoneNumber = raf.readInt(); // importante leer el int

            int posAddress = posPhone + 4;
            String address = readString(TAMANO_ADDRESS, posAddress).trim();

            int posEmail = posAddress + TAMANO_ADDRESS;
            String email = readString(TAMANO_EMAIL, posEmail).trim();

            int posCountry = posAddress + TAMANO_EMAIL;
            String country = readString(TAMANO_COUNTRY, posCountry).trim();

            if (name.equalsIgnoreCase(nameActual) && lastNameActual.equalsIgnoreCase(lastNameActual)) {
                host = new Host(id, nameActual, lastNameActual, phoneNumber, address, email, country);
            }

            numReg++;
        }
        return host;
    }
}