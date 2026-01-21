package com.example.apprecordatorio.DAO;

import android.util.Log;

import com.example.apprecordatorio.Entidades.Alarma;
import com.example.apprecordatorio.DAOInterfaces.IRecordatorioExterno;
import com.example.apprecordatorio.Util.HttpUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RecordatorioExternoDao implements IRecordatorioExterno {

    private Conexion con;

    private final String BASE_URL = "http://10.0.2.2/pruebaphp/";
   // private final String BASE_URL = "http://marvelous-vision-production-c97b.up.railway.app/";
    public RecordatorioExternoDao ()
    {

    }


    @Override
    public ArrayList<Alarma> readAllFromPaciente(int idPaciente) {

        ArrayList<Alarma> list = new ArrayList<>();

        try {

            Map<String, String> params = new HashMap<>();
            params.put("id_paciente", String.valueOf(idPaciente));


            String response = HttpUtils.post(BASE_URL + "readAllAlarmas.php", params);


            JSONObject json = new JSONObject(response);

            if (json.getBoolean("success")) {

                JSONArray arr = json.getJSONArray("alarmas");

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);

                    Alarma a = new Alarma();

                    a.setIdRemoto(o.getInt("id"));
                    a.setPacienteId(o.getInt("id_paciente"));
                    a.setTitulo(o.getString("titulo"));
                    a.setDescripcion(o.getString("descripcion"));
                    a.setTono(o.getString("tono"));
                    a.setImagenUrl(o.getString("imagen"));
                    a.setEstado(o.getInt("estado") == 1);

                    a.setDomingo(o.getInt("domingo") == 1);
                    a.setLunes(o.getInt("lunes") == 1);
                    a.setMartes(o.getInt("martes") == 1);
                    a.setMiercoles(o.getInt("miercoles") == 1);
                    a.setJueves(o.getInt("jueves") == 1);
                    a.setViernes(o.getInt("viernes") == 1);
                    a.setSabado(o.getInt("sabado") == 1);

                    a.setBajaLogica(o.getInt("baja_logica") == 1);
                    a.setHora(o.getInt("hora"));
                    a.setMinuto(o.getInt("minuto"));

                    String tsString = o.getString("updated_at");
                    long ts = parseTimestamp(tsString);
                    a.setUpdatedAt(ts);

                    list.add(a);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public ArrayList<Alarma> readAllSync (int idPaciente) {

        ArrayList<Alarma> list = new ArrayList<>();

        try {
            URL url = new URL(BASE_URL+"readAllAlarmasSync.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            String data = "id_paciente=" + idPaciente;

            OutputStream os = conn.getOutputStream();
            os.write(data.getBytes());
            os.flush();
            os.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) sb.append(line);
            br.close();

            String response = sb.toString();
            Log.e("SYNC-RAW", "RESPUESTA RAW DEL SERVIDOR:\n" + response);

            JSONObject json = new JSONObject(sb.toString());

            if (json.getBoolean("success")) {

                JSONArray arr = json.getJSONArray("alarmas");

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);

                    Alarma a = new Alarma();

                    a.setIdRemoto(o.getInt("id"));
                    a.setPacienteId(o.getInt("id_paciente"));
                    a.setTitulo(o.getString("titulo"));
                    a.setDescripcion(o.getString("descripcion"));
                    a.setTono(o.getString("tono"));
                    a.setImagenUrl(o.getString("imagen"));
                    a.setEstado(o.getInt("estado") == 1);

                    a.setDomingo(o.getInt("domingo") == 1);
                    a.setLunes(o.getInt("lunes") == 1);
                    a.setMartes(o.getInt("martes") == 1);
                    a.setMiercoles(o.getInt("miercoles") == 1);
                    a.setJueves(o.getInt("jueves") == 1);
                    a.setViernes(o.getInt("viernes") == 1);
                    a.setSabado(o.getInt("sabado") == 1);

                    a.setBajaLogica(o.getInt("baja_logica") == 1);
                    a.setHora(o.getInt("hora"));
                    a.setMinuto(o.getInt("minuto"));

                    String tsString = o.getString("updated_at");
                    long ts = parseTimestamp(tsString);
                    a.setUpdatedAt(ts);

                    list.add(a);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    @Override
    public int add(Alarma a) {
        int result = 0;

        try {
            URL url = new URL(BASE_URL+"addAlarma.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            if(a.getDescripcion()==null)a.setDescripcion("");
            if(a.getImagenUrl()==null)a.setImagenUrl("");

            String data =
                  //  "id=" + a.getId() +
                            "id_paciente=" + a.getPacienteId() +
                            "&titulo=" + URLEncoder.encode(a.getTitulo(), "UTF-8") +
                           // "&titulo=" + a.getTitulo() +
                            "&descripcion=" + URLEncoder.encode(a.getDescripcion(), "UTF-8") +
                           // "&descripcion=" + a.getDescripcion() +
                            "&tono=" + URLEncoder.encode(a.getTono(), "UTF-8") +
                           // "&tono=" + a.getTono() +
                            "&imagen=" + URLEncoder.encode(a.getImagenUrl(), "UTF-8") +
                           // "&imagen=" + a.getImagenUrl() +
                            "&estado=" + (a.isEstado() ? 1 : 0) +
                            "&domingo=" + (a.isDomingo() ? 1 : 0) +
                            "&lunes=" + (a.isLunes() ? 1 : 0) +
                            "&martes=" + (a.isMartes() ? 1 : 0) +
                            "&miercoles=" + (a.isMiercoles() ? 1 : 0) +
                            "&jueves=" + (a.isJueves() ? 1 : 0) +
                            "&viernes=" + (a.isViernes() ? 1 : 0) +
                            "&sabado=" + (a.isSabado() ? 1 : 0) +
                            "&baja_logica=0" +
                            "&hora=" + a.getHora() +
                            "&minuto=" + a.getMinuto() +
                                    "&updated_at=" + URLEncoder.encode(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(a.getUpdatedAt()), "UTF-8");

            OutputStream os = conn.getOutputStream();
            os.write(data.getBytes());
            os.flush();
            os.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();

            Log.e("RAW ADD", sb.toString());

           // Log.d("RESPUESTA_PHP_ADD_ALARMA", sb.toString());

            JSONObject json = new JSONObject(sb.toString());

            if (json.getBoolean("success")) {
                result = 1; // se agregó OK
            }
            return json.getInt("id");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

/*
    @Override
    public int add(Alarma r) {

        try {
            ApiService api = ApiClient.getClient()
                    .create(ApiService.class);

            Call<ApiResponse> call =
                    api.addAlarma(r.getPacienteId(),
                            r.getTitulo(),
                            r.getDescripcion() != null ? r.getDescripcion() : "",
                            r.getTono(),
                            r.getImagenUrl() != null ? r.getImagenUrl() : "",
                            r.isEstado()?1:0,
                            r.isDomingo() ? 1 : 0,
                            r.isLunes() ? 1 : 0,
                            r.isMartes() ? 1 : 0,
                            r.isMiercoles() ? 1 : 0,
                            r.isJueves() ? 1 : 0,
                            r.isViernes() ? 1 : 0,
                            r.isSabado() ? 1 : 0,
                            r.isBajaLogica() ? 1 : 0,
                            r.getHora(),
                            r.getMinuto(),
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(r.getUpdatedAt())
                    );

            Response<ApiResponse> response = call.execute();

            Log.d("API_DEBUG", "HTTP CODE: " + response.code());
            Log.d("API_DEBUG", "BODY: " + response.body());
            Log.d("API_DEBUG", "ERROR BODY: " + response.errorBody());

            if (response.body() != null) {
                Log.d("API_DEBUG", "SUCCESS: " + response.body().isSuccess());
                Log.d("API_DEBUG", "ID: " + response.body().getId());
                Log.d("API_DEBUG", "ERROR: " + response.body().getError());
            }

            if (response.isSuccessful()
                    && response.body() != null
                    && response.body().isSuccess()) {

                return response.body().getId();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public List<Alarma> readAllFromPaciente(int idPaciente)
    {
        List<Alarma> lista =null;
        try
        {
            ApiService api = ApiClient.getClient()
                    .create(ApiService.class);

            Call<AlarmasResponse> call = api.readAllAlarmasFrom(idPaciente);
            Response<AlarmasResponse> response = call.execute();

            Log.d("API_DEBUG", "HTTP CODE: " + response.code());
            Log.d("API_DEBUG", "BODY: " + response.body());
            Log.d("API_DEBUG", "ERROR BODY: " + response.errorBody());

            if (response.body() != null) {
                Log.d("API_DEBUG", "SUCCESS: " + response.body().isSuccess());
                Log.d("API_DEBUG", "Notas: " + response.body().getAlarmas());
                Log.d("API_DEBUG", "ERROR: " + response.body().getError());
            }

            if (response.isSuccessful()
                    && response.body() != null
                    && response.body().isSuccess()) {

                return response.body().getAlarmas();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Alarma> readAllSync(int idPaciente)
    {
        List<Alarma> lista =null;
        try
        {
            ApiService api = ApiClient.getClient()
                    .create(ApiService.class);

            Call<AlarmasResponse> call = api.readAllAlarmasSync(idPaciente);
            Response<AlarmasResponse> response = call.execute();

            Log.d("API_DEBUG", "HTTP CODE: " + response.code());
            Log.d("API_DEBUG", "BODY: " + response.body());
            Log.d("API_DEBUG", "ERROR BODY: " + response.errorBody());

            if (response.body() != null) {
                Log.d("API_DEBUG", "SUCCESS: " + response.body().isSuccess());
                Log.d("API_DEBUG", "Notas: " + response.body().getAlarmas());
                Log.d("API_DEBUG", "ERROR: " + response.body().getError());
            }

            if (response.isSuccessful()
                    && response.body() != null
                    && response.body().isSuccess()) {

                return response.body().getAlarmas();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return lista;
    }
*/
    @Override
    public Alarma readOneFrom(int id, int idPaciente) {
        Alarma a = null;

        Log.d("ID R EX","ID: "+id);

        try {
            URL url = new URL(BASE_URL+"readOneAlarmaFrom.php?id=" + id + "&id_paciente=" + idPaciente);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder(); String line;
            while ((line = br.readLine()) != null) sb.append(line);

            JSONObject json = new JSONObject(sb.toString());

            if (json.getBoolean("success")) {
                JSONObject o = json.getJSONObject("alarma");

                a = new Alarma();
                a.setIdRemoto(o.getInt("id"));
                a.setPacienteId(o.getInt("id_paciente"));
                a.setTitulo(o.getString("titulo"));
                a.setDescripcion(o.getString("descripcion"));
                a.setTono(o.getString("tono"));
                a.setImagenUrl(o.getString("imagen"));
                a.setEstado(o.getInt("estado")==1);
                a.setDomingo(o.getInt("domingo")==1);
                a.setLunes(o.getInt("lunes")==1);
                a.setMartes(o.getInt("martes")==1);
                a.setMiercoles(o.getInt("miercoles")==1);
                a.setJueves(o.getInt("jueves")==1);
                a.setViernes(o.getInt("viernes")==1);
                a.setSabado(o.getInt("sabado")==1);
                a.setBajaLogica(o.getInt("baja_logica")==1);
                a.setHora(o.getInt("hora"));
                a.setMinuto(o.getInt("minuto"));
                String tsString = o.getString("updated_at");
                long ts = parseTimestamp(tsString);
                a.setUpdatedAt(ts);
            }else {
                Log.e("dao alarma","ey"+json.getBoolean("success"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return a;
    }


    public boolean update(Alarma a) {
        try {
            if (a.getDescripcion() == null) a.setDescripcion("");
            if (a.getImagenUrl() == null) a.setImagenUrl("");

            Log.d("ALARMA DAO","ID REMOTO"+a.getIdRemoto());
            Log.d("ALARMA DAO","ID REMOTO"+ a.getIdRemoto());


            Map<String, String> params = new HashMap<>();
            params.put("id", String.valueOf(a.getIdRemoto()));
            params.put("id_paciente", String.valueOf(a.getPacienteId()));
            params.put("titulo", a.getTitulo());
            params.put("descripcion", a.getDescripcion());
            params.put("tono", a.getTono());
            params.put("imagen", a.getImagenUrl());
            params.put("estado", a.isEstado() ? "1" : "0");
            params.put("domingo", a.isDomingo() ? "1" : "0");
            params.put("lunes", a.isLunes() ? "1" : "0");
            params.put("martes", a.isMartes() ? "1" : "0");
            params.put("miercoles", a.isMiercoles() ? "1" : "0");
            params.put("jueves", a.isJueves() ? "1" : "0");
            params.put("viernes", a.isViernes() ? "1" : "0");
            params.put("sabado", a.isSabado() ? "1" : "0");
            params.put("baja_logica", a.isBajaLogica() ? "1" : "0");
            params.put("hora", String.valueOf(a.getHora()));
            params.put("minuto", String.valueOf(a.getMinuto()));
            params.put("updated_at",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(a.getUpdatedAt()));


            String json = HttpUtils.post(BASE_URL + "updateAlarma.php", params);

            Log.d("ALARMA DAO",json);

            JSONObject result = new JSONObject(json);
            return result.getBoolean("success");

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    public boolean delete(Alarma a) {
        try {
            URL url = new URL(BASE_URL + "deleteAlarma.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            String data =
                    "id=" + a.getIdRemoto() +
                            "&id_paciente=" + a.getPacienteId() +
                            "&updated_at=" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(a.getUpdatedAt());

            OutputStream os = conn.getOutputStream();
            os.write(data.getBytes());
            os.flush();
            os.close();

            String json = readResponse(conn);
            JSONObject result = new JSONObject(json);

            return result.getBoolean("success");

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public int getLastId(int idPaciente) {
        String url = BASE_URL + "getLastAlarmaId.php";

        HashMap<String, String> params = new HashMap<>();
        params.put("id_paciente", String.valueOf(idPaciente));

        String response = HttpUtils.post(url, params);

        try {
            JSONObject json = new JSONObject(response);
            if (json.optBoolean("success")) {
                return json.optInt("id", 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    private long parseTimestamp(String mysqlTs) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date d = sdf.parse(mysqlTs);
            return d != null ? d.getTime() : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    private String readResponse(HttpURLConnection conn) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) sb.append(line);
        br.close();

        return sb.toString();
    }



}
