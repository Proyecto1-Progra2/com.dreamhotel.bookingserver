package data;

import domain.Image;
import domain.Room;

import java.io.*;
import java.util.ArrayList;

public class ImageData {
    private RandomAccessFile raf;
    private final int TAMANO_ROOMNUMBER = 30;
    private final int TAMANO_HOTELNUMBER = 30;
    private final int TAMANO_IMAGE = 100_000;
    private final int TAMANO_REGISTRO = 160_000; // 100 KB por imagen

    public ImageData() throws IOException {
        this.raf = new RandomAccessFile("image.dat", "rw");
    }

    private String readString(int tamanoString, long posicion) throws IOException {
        raf.seek(posicion);
        byte[] datos = new byte[tamanoString];
        raf.readFully(datos);
        String dato = new String(datos).trim();
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

    public void insert(Image image) throws IOException {
        //aumentar el tamaño del archivo en 64 bytes (TAMAÑO_REGISTRO)
        raf.setLength(raf.length() + TAMANO_REGISTRO);
        raf.seek(raf.length() - TAMANO_REGISTRO);
        // Se debe transformar el string en un arreglo de bytes para
        // poder escribirlo en el archivo
        byte roomNumber[] = toBytes(image.getRoomNumber(), TAMANO_ROOMNUMBER);
        raf.write(roomNumber);
        byte hotelNumber[] = toBytes(image.getHotelNumber(), TAMANO_HOTELNUMBER);
        raf.write(hotelNumber);
        raf.write(image.getImage());
    }

    public ArrayList<Image> findByRoomNumber(String roomNumberBuscado, String hotelNumberBuscado) throws IOException {
        ArrayList<Image> imagenes = new ArrayList<>();
        raf.seek(0);

        long totalRegistros = raf.length() / TAMANO_REGISTRO;

        for (int i = 0; i < totalRegistros; i++) {
            long pos = i * TAMANO_REGISTRO;
            String roomNumber = readString(TAMANO_ROOMNUMBER, pos);
            String hotelNumber = readString(TAMANO_HOTELNUMBER, pos + TAMANO_ROOMNUMBER);
            if (roomNumber.equals(roomNumberBuscado) && hotelNumber.equals(hotelNumberBuscado)) {
                // Leer los bytes de la imagen
                raf.seek(pos + TAMANO_ROOMNUMBER + TAMANO_HOTELNUMBER);
                byte[] imageBytes = new byte[TAMANO_IMAGE];
                raf.readFully(imageBytes);
                imagenes.add(new Image(roomNumber, hotelNumber, imageBytes));
            }
        }

        return imagenes;
    }


    // Convertir imagen (desde recursos o disco) a byte[]
    public static byte[] imageToBytes(String ruta, int tamanoFile) throws IOException {
        try (InputStream is = ImageData.class.getResourceAsStream(ruta);
             ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            if (is == null) throw new IOException("No se encontró la imagen: " + ruta);
            byte[] datos = new byte[tamanoFile];
            int n;
            while ((n = is.read(datos)) != -1) buffer.write(datos, 0, n);
            return buffer.toByteArray();
        }
    }
}
