package com.example.apprecordatorio.dao;

import android.util.Log;

import com.example.apprecordatorio.entidades.Paciente;
import com.example.apprecordatorio.interfaces.IPacienteExterno;
import com.example.apprecordatorio.retrofit.ApiClient;
import com.example.apprecordatorio.retrofit.ApiResponse;
import com.example.apprecordatorio.retrofit.ApiService;
import com.example.apprecordatorio.retrofit.PacienteResponse;
import com.example.apprecordatorio.util.BaseUrl;
import com.example.apprecordatorio.util.HttpUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PacienteExternoDao  {



    public int add2(Paciente p) {
        int newId = 0;

        try {
            String url = BaseUrl.BASE_URL + "addPaciente.php";


            Map<String, String> params = new HashMap<>();
            params.put("nombre", p.getNombre());
            String response = HttpUtils.post(url, params);

            Log.d("RESPUESTA_PHP", response);

            // Parseo del JSON
            JSONObject json = new JSONObject(response);

            if (json.getBoolean("success")) {
                newId = json.getInt("id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("NEW_ID", "NUEVO ID: " + newId);
        return newId;
    }



    public int add(Paciente p) {
        Log.d("API_DEBUG", "en dao add paciente");

        try {
            ApiService api = ApiClient.getClient()
                    .create(ApiService.class);

            Call<ApiResponse> call =
                    api.addPaciente(p.getNombre());

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




    public Paciente readOne2(int id)
    {
        Paciente paciente = null;

        try {
            URL url = new URL(BaseUrl.BASE_URL+"getPaciente.php?id=" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();

            JSONObject json = new JSONObject(sb.toString());

            if (json.getBoolean("success")) {
                JSONObject p = json.getJSONObject("paciente");

                paciente = new Paciente();
                paciente.setId(p.getInt("id"));
                paciente.setNombre(p.getString("nombre"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return paciente;
    }

    public Paciente readOne(int id) {

        try {
            ApiService api = ApiClient.getClient()
                    .create(ApiService.class);

            Call<PacienteResponse> call =
                    api.getPaciente(id);

            Response<PacienteResponse> response = call.execute();

            Log.d("API_DEBUG", "HTTP CODE: " + response.code());

            if (!response.isSuccessful() || response.body() == null) {
                Log.e("API_DEBUG", "Respuesta inválida");
                return null;
            }

            PacienteResponse body = response.body();

            if (!body.isSuccess() || body.getPaciente() == null) {
                Log.e("API_DEBUG", "ERROR: " + body.getError());
                return null;
            }

            Paciente p = body.getPaciente();

            Paciente paciente = new Paciente();
            paciente.setId(p.getId());
            paciente.setNombre(p.getNombre());

            return paciente;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
