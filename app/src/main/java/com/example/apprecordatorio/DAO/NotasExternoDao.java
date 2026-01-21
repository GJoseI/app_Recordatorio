package com.example.apprecordatorio.DAO;

import android.util.Log;

import com.example.apprecordatorio.Entidades.Recordatorio;
import com.example.apprecordatorio.Interfaces.INotaExterno;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import com.example.apprecordatorio.Util.HttpUtils;

public class NotasExternoDao implements INotaExterno {

    //private Conexion con;

    private final String BASE_URL = "http://10.0.2.2/pruebaphp/";

    //private final String BASE_URL = "http://marvelous-vision-production-c97b.up.railway.app/";

    @Override
    public int add(Recordatorio r) {
        String url = BASE_URL+ "addNota.php";
        if(r.getDescripcion()==null)r.setDescripcion("");
        if(r.getImagenUrl()==null)r.setImagenUrl("");

        HashMap<String, String> params = new HashMap<>();
        params.put("titulo", r.getTitulo());
        params.put("descripcion", r.getDescripcion());
        params.put("imagen", r.getImagenUrl());
        params.put("id_paciente", String.valueOf(r.getPacienteId()));
        params.put("baja_logica", r.isBajaLogica() ? "1" : "0");
        params.put("updated_at", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(r.getUpdatedAt()));
        //params.put("id", String.valueOf(r.getId()));

        try
        {
            String response = HttpUtils.post(url, params);
            JSONObject json = new JSONObject(response);
            Log.e("ADD",response);
            return  json.getInt("id");
            //return json.optBoolean("success", false);
        }catch (Exception e)
        {
            e.printStackTrace();
            return 0;
        }
    }



    @Override
    public ArrayList<Recordatorio> readAllFrom(int idPaciente) {
        String url = BASE_URL + "readAllNotasFrom.php";


        HashMap<String, String> params = new HashMap<>();
        params.put("id_paciente", String.valueOf(idPaciente));
        ArrayList<Recordatorio> lista = new ArrayList<>();

        try
        {
            String response = HttpUtils.post(url, params);
            JSONObject json = new JSONObject(response);

            Log.d("NOTA EXTERNO","response: "+response);
            if (json.optBoolean("success", false)) {
                JSONArray array = json.optJSONArray("data");
                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);

                        Recordatorio r = new Recordatorio();
                        r.setIdRemoto(o.getInt("id"));
                        r.setTitulo(o.getString("titulo"));
                        r.setDescripcion(o.getString("descripcion"));
                        r.setImagenUrl(o.getString("imagen"));
                        r.setPacienteId(o.getInt("id_paciente"));
                        r.setBajaLogica(o.getInt("baja_logica") == 1);
                        String tsString = o.getString("updated_at");
                        long ts = parseTimestamp(tsString);
                        r.setUpdatedAt(ts);

                        Log.d("NOTA EXTERNO","id: "+r.getIdRemoto()+" titulo: "+r.getTitulo()+" ts: "+r.getUpdatedAt());

                        lista.add(r);
                    }
                }
            }

            return lista;
        }catch (Exception e)
        {
            return lista;
        }

    }


    public ArrayList<Recordatorio> readAllSync (int idPaciente) {
        String url = BASE_URL + "readAllNotasSync.php";

        HashMap<String, String> params = new HashMap<>();
        params.put("id_paciente", String.valueOf(idPaciente));
        ArrayList<Recordatorio> lista = new ArrayList<>();

        try
        {
            String response = HttpUtils.post(url, params);
            JSONObject json = new JSONObject(response);


            if (json.optBoolean("success", false)) {
                JSONArray array = json.optJSONArray("data");
                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);

                        Recordatorio r = new Recordatorio();
                        r.setIdRemoto(o.getInt("id"));
                        r.setTitulo(o.getString("titulo"));
                        r.setDescripcion(o.getString("descripcion"));
                        r.setImagenUrl(o.getString("imagen"));
                        r.setPacienteId(o.getInt("id_paciente"));
                        r.setBajaLogica(o.getInt("baja_logica") == 1);
                        String tsString = o.getString("updated_at");
                        long ts = parseTimestamp(tsString);
                        r.setUpdatedAt(ts);

                        lista.add(r);
                    }
                }
            }

            return lista;
        }catch (Exception e)
        {
            return lista;
        }

    }
/*
 @Override
    public int add(Recordatorio r) {

        try {
            ApiService api = ApiClient.getClient()
                    .create(ApiService.class);

            Call<ApiResponse> call =
                    api.addNota(r.getPacienteId(),
                            r.getTitulo(),
                            r.getDescripcion() != null ? r.getDescripcion() : "",
                            r.getImagenUrl() != null ? r.getImagenUrl() : "",
                            r.isBajaLogica() ? 1 : 0,
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
    public List<Recordatorio> readAllFrom(int idPaciente)
    {
        List<Recordatorio> lista =null;
        try
        {
            ApiService api = ApiClient.getClient()
                    .create(ApiService.class);

            Call<NotasResponse> call = api.readAllNotasFrom(idPaciente);
            Response<NotasResponse> response = call.execute();

            Log.d("API_DEBUG", "HTTP CODE: " + response.code());
            Log.d("API_DEBUG", "BODY: " + response.body());
            Log.d("API_DEBUG", "ERROR BODY: " + response.errorBody());

            if (response.body() != null) {
                Log.d("API_DEBUG", "SUCCESS: " + response.body().isSuccess());
                Log.d("API_DEBUG", "Notas: " + response.body().getNotas());
                Log.d("API_DEBUG", "ERROR: " + response.body().getError());
            }

            if (response.isSuccessful()
                    && response.body() != null
                    && response.body().isSuccess()) {

                return response.body().getNotas();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Recordatorio> readAllSync(int idPaciente)
    {
        List<Recordatorio> lista =null;
        try
        {
            ApiService api = ApiClient.getClient()
                    .create(ApiService.class);

            Call<NotasResponse> call = api.readAllNotasSync(idPaciente);
            Response<NotasResponse> response = call.execute();

            Log.d("API_DEBUG", "HTTP CODE: " + response.code());
            Log.d("API_DEBUG", "BODY: " + response.body());
            Log.d("API_DEBUG", "ERROR BODY: " + response.errorBody());

            if (response.body() != null) {
                Log.d("API_DEBUG", "SUCCESS: " + response.body().isSuccess());
                Log.d("API_DEBUG", "Notas: " + response.body().getNotas());
                Log.d("API_DEBUG", "ERROR: " + response.body().getError());
            }

            if (response.isSuccessful()
                    && response.body() != null
                    && response.body().isSuccess()) {

                return response.body().getNotas();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return lista;
    }
*/

    public boolean update(Recordatorio r) {
        String url = BASE_URL + "updateNota.php";

        Log.d("sync up notas","id remoto: "+r.getIdRemoto()+" paciente id"+r.getPacienteId());

        if(r.getDescripcion()==null)r.setDescripcion("");
        if(r.getImagenUrl()==null)r.setImagenUrl("");

        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(r.getIdRemoto()));
        params.put("id_paciente", String.valueOf(r.getPacienteId()));
        params.put("titulo", r.getTitulo());
        params.put("descripcion", r.getDescripcion());
        params.put("imagen", r.getImagenUrl());
        params.put("baja_logica", r.isBajaLogica() ? "1" : "0");
        params.put("updated_at", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(r.getUpdatedAt()));

        try
        {
            String response = HttpUtils.post(url, params);
            Log.d("sync up notas","response: ");
            Log.d("sync up notas",response);

            JSONObject json = new JSONObject(response);
            return json.optBoolean("success", false);
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }


    }

    @Override
    public boolean delete(Recordatorio r) {
        String url = BASE_URL + "deleteNota.php";

        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(r.getIdRemoto()));
        params.put("id_paciente", String.valueOf(r.getPacienteId()));
        params.put("updated_at", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(r.getUpdatedAt()));

        try {
            String response = HttpUtils.post(url, params);
            JSONObject json =  new JSONObject(response);
            return json.optBoolean("success", false);

        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public Recordatorio readOne(int id, int idPaciente) {
        String url = BASE_URL + "readOneNota.php";

        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(id));
        params.put("id_paciente", String.valueOf(idPaciente));

        try
        {
            String response = HttpUtils.post(url, params);
            JSONObject json = new JSONObject(response);

            if (json.optBoolean("success", false)) {
                JSONObject o = json.optJSONObject("data");

                if (o != null) {
                    Recordatorio r = new Recordatorio();
                    r.setIdRemoto(o.getInt("id"));
                    r.setTitulo(o.getString("titulo"));
                    r.setDescripcion(o.getString("descripcion"));
                    r.setImagenUrl(o.getString("imagen"));
                    r.setPacienteId(o.getInt("id_paciente"));
                    r.setBajaLogica(o.getInt("baja_logica") == 1);
                    String tsString = o.getString("updated_at");
                    long ts = parseTimestamp(tsString);
                    r.setUpdatedAt(ts);
                    return r;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public int getLastId(int idPaciente) {
        String url = BASE_URL + "getLastNotaId.php";

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

}