package com.example.apprecordatorio.util;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {

    public Uri copiarImagenLocal(Uri sourceUri, Context context) {
        try {
            InputStream in = context.getContentResolver().openInputStream(sourceUri);
            File dir = new File(context.getFilesDir(), "imagenes");
            if (!dir.exists()) dir.mkdirs();

            File destino = new File(dir, System.currentTimeMillis() + ".jpg");
            OutputStream out = new FileOutputStream(destino);

            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }

            in.close();
            out.close();

            return Uri.fromFile(destino); // este sí es accesible siempre
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

}
