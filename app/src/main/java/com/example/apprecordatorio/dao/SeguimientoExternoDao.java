package com.example.apprecordatorio.dao;

import android.util.Log;

import com.example.apprecordatorio.entidades.Alarma;
import com.example.apprecordatorio.entidades.Seguimiento;
import com.example.apprecordatorio.entidades.SeguimientoDto;
import com.example.apprecordatorio.interfaces.ISeguimientoExterno;
import com.example.apprecordatorio.retrofit.ApiClient;
import com.example.apprecordatorio.retrofit.ApiResponse;
import com.example.apprecordatorio.retrofit.ApiService;
import com.example.apprecordatorio.retrofit.SeguimientosResponse;
import com.example.apprecordatorio.util.BaseUrl;
import com.example.apprecordatorio.util.HttpUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Response;

public class SeguimientoExternoDao implements ISeguimientoExterno {

    /*@Override
    public boolean add(Seguimiento s) {
        String url = BaseUrl.BASE_URL + "addSeguimiento.php";

        HashMap<String, String> params = new HashMap<>();
        params.put("atendida", s.isAtendida() ? "1" : "0");
        params.put("id_alarma", String.valueOf(s.getAlarma().getIdRemoto()));
        params.put("id_paciente", String.valueOf(s.getAlarma().getPacienteId()));
        params.put("fecha_hora",String.valueOf(s.getTimestamp()));

        String json = HttpUtils.post(url, params);
        Log.d("sync up seg daoex",json);
        try {
            JSONObject obj = new JSONObject(json);
            return obj.optBoolean("success", false);
        } catch (Exception e) {
            return false;
        }
    }*/
    @Override
    public boolean add(Seguimiento s) {

        try {
            ApiService api = ApiClient
                    .getClient()
                    .create(ApiService.class);



            Call<ApiResponse> call = api.addSeguimiento(
                    s.isAtendida(),
                    s.getAlarma().getIdRemoto(),
                    s.getAlarma().getPacienteId(),
                    String.valueOf(s.getTimestamp())
            );

            Response<ApiResponse> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                ApiResponse body = response.body();


                return body.isSuccess();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
   /* @Override
    public ArrayList<Seguimiento> readAllFromPaciente(int id) {

        ArrayList<Seguimiento> lista = new ArrayList<>();

        String url = BaseUrl.BASE_URL + "readAllSeguimientoFrom.php?id_paciente=" + id;
        String json = HttpUtils.get(url);

        try {
            JSONObject obj = new JSONObject(json);

            if (!obj.getBoolean("success"))
            {
                Log.e("DAO SEG", "FALLO EL REQUEST");
                return lista;
            }

            JSONArray arr = obj.getJSONArray("data");

            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);

                Seguimiento s = new Seguimiento();
                s.setId(o.getInt("id"));
                s.setAtendida(o.getInt("atendida") == 1);
                s.setTimestamp(o.getString("fecha_hora"));

                Alarma a = new Alarma();
                a.setIdRemoto(o.getInt("id_alarma"));
                a.setPacienteId(o.getInt("id_paciente"));
                s.setAlarma(a);

                lista.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }*/

    @Override
    public ArrayList<Seguimiento> readAllFromPaciente(int idPaciente) {

        ArrayList<Seguimiento> lista = new ArrayList<>();

        try {
            ApiService api = ApiClient
                    .getClient()
                    .create(ApiService.class);

            Call<SeguimientosResponse> call =
                    api.readAllSeguimientoFrom(idPaciente);

            Response<SeguimientosResponse> response = call.execute();

            if (!response.isSuccessful() || response.body() == null) {
                Log.e("DAO SEG", "Request falló");
                return lista;
            }

            SeguimientosResponse body = response.body();

            if (!body.isSuccess()) {
                Log.e("DAO SEG", "Success = false");
                return lista;
            }

            for (SeguimientoDto dto : body.getSeguimientos()) {

                Seguimiento s = new Seguimiento();
                s.setId(dto.getId());
                s.setAtendida(dto.isAtendida());
                s.setTimestamp(dto.getFechaHora());


                Alarma a = new Alarma();
                a.setIdRemoto(dto.getIdAlarma());
                a.setPacienteId(dto.getIdPaciente());
                a.setTitulo(dto.getTitulo());

                s.setAlarma(a);

                lista.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    }
