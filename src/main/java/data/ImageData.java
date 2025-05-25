package data;

import domain.Image;
import domain.Room;

import java.io.*;

public class ImageData {
    private RandomAccessFile raf;
    private final int TAMANO_ROOMNUMBER = 30;
    private final int TAMANO_IMAGE = 150_000;
    private final int TAMANO_REGISTRO = 180_000; // 100 KB por imagen

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
        raf.write(image.getImage());
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
