package com.example.apprecordatorio.util;

import android.content.Context;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileUtil {


    public Uri copiarImagenLocal(Uri sourceUri, Context context) {
        try (InputStream in = context.getContentResolver().openInputStream(sourceUri)) {
            File dir = new File(context.getFilesDir(), "imagenes");
            if (!dir.exists()) dir.mkdirs();

            File destino = new File(dir, System.currentTimeMillis() + ".jpg");

            Files.copy(in, destino.toPath(), StandardCopyOption.REPLACE_EXISTING);

            return Uri.fromFile(destino);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void borrarImagenAnterior(String viejoUrl) {
        if (viejoUrl == null || viejoUrl.isEmpty()) return;

        try {
            Uri uri = Uri.parse(viejoUrl);

            // solo borrar si es un archivo local (file://)
            if ("file".equals(uri.getScheme())) {
                File file = new File(uri.getPath());
                if (file.exists()) {
                    boolean deleted = file.delete();
                    Log.d("BORRAR_IMAGEN", "Archivo " + file.getName() + " eliminado: " + deleted);
                }
            } else {
                Log.d("BORRAR_IMAGEN", "No se borra (no es file://): " + viejoUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String uriToBase64(Context context, Uri uri) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(uri);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        byte[] data = new byte[4096];
        int nRead;
        while ((nRead = is.read(data)) != -1) {
            buffer.write(data, 0, nRead);
        }

        return Base64.encodeToString(buffer.toByteArray(), Base64.NO_WRAP);
    }

    public Uri descargarImagenDesdeUrl(String urlImagen, Context context) {
        try {
            URL url = new URL(urlImagen);
            InputStream in = url.openStream();

            File dir = new File(context.getFilesDir(), "imagenes");
            if (!dir.exists()) dir.mkdirs();

            File destino = new File(dir, System.currentTimeMillis() + ".jpg");

            Files.copy(in, destino.toPath(), StandardCopyOption.REPLACE_EXISTING);

            in.close();
            return Uri.fromFile(destino);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
