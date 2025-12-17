package com.example.apprecordatorio.util;

import android.content.Context;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileUtil {

    private final String URL_BASE = "http://10.0.2.2/pruebaphp/";

    public Uri copiarImagenLocal(Uri sourceUri, Context context) {
        try (InputStream in = context.getContentResolver().openInputStream(sourceUri)) {
            File dir = new File(context.getFilesDir(), "imagenes");
            if (!dir.exists()) dir.mkdirs();

            File destino = new File(dir, System.currentTimeMillis() + ".jpg");

            Files.copy(in, destino.toPath(), StandardCopyOption.REPLACE_EXISTING);

            return Uri.fromFile(destino);
        } catch (Exception e) {
            Log.e("FileUtil", "Error copiando imagen local: " + sourceUri, e);
            return null;
        }
    }

    public void borrarImagenAnterior(String viejoUrl) {
        if (viejoUrl == null || viejoUrl.isEmpty()) return;

        try {
            Uri uri = Uri.parse(viejoUrl);

            // solo borrar si es un archivo local (file://)
            if (uri != null && "file".equals(uri.getScheme())) {
                File file = new File(uri.getPath());
                if (file.exists()) {
                    boolean deleted = file.delete();
                    Log.d("BORRAR_IMAGEN", "Archivo " + file.getName() + " eliminado: " + deleted);
                }
            } else {
                Log.d("BORRAR_IMAGEN", "No se borra (no es file://): " + viejoUrl);
            }
        } catch (Exception e) {
            Log.e("FileUtil", "Error borrando imagen anterior: " + viejoUrl, e);
        }
    }

    public String uriToBase64(Context context, Uri uri) throws IOException {
        if (uri == null) throw new IOException("URI nula");
        Log.d("uriToBase64", "scheme=" + uri.getScheme() + " path=" + uri.getPath());
        InputStream is = null;

        try {
            String scheme = uri.getScheme();

            if ("content".equals(scheme) || scheme == null) {
                // imágenes de galería / SAF
                is = context.getContentResolver().openInputStream(uri);
            } else if ("file".equals(scheme)) {
                // imágenes locales internas
                if (uri.getPath() == null) throw new IOException("URI file con path nulo");
                is = new FileInputStream(new File(uri.getPath()));
            } else {
                throw new IOException("Esquema de URI no soportado: " + scheme);
            }

            if (is == null) throw new IOException("No se pudo abrir InputStream para la URI: " + uri);

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[4096];
            int nRead;

            while ((nRead = is.read(data)) != -1) {
                buffer.write(data, 0, nRead);
            }

            return Base64.encodeToString(buffer.toByteArray(), Base64.NO_WRAP);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    public Uri descargarImagenDesdeUrl(String urlImagen, Context context) {
        if (urlImagen == null || urlImagen.trim().isEmpty()) return null;

        InputStream in = null;
        try {
            String fullUrl = urlImagen;
            if (!(urlImagen.startsWith("http://") || urlImagen.startsWith("https://"))) {
                fullUrl = URL_BASE + urlImagen;
            }

            URL url = new URL(fullUrl);
            in = url.openStream();

            File dir = new File(context.getFilesDir(), "imagenes");
            if (!dir.exists()) dir.mkdirs();

            File destino = new File(dir, System.currentTimeMillis() + ".jpg");

            Files.copy(in, destino.toPath(), StandardCopyOption.REPLACE_EXISTING);

            return Uri.fromFile(destino);

        } catch (Exception e) {
            Log.e("FileUtil", "Error descargando imagen desde URL: " + urlImagen, e);
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

}
