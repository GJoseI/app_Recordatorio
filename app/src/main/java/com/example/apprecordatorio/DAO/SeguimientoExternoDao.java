package com.example.apprecordatorio.DAO;

import android.util.Log;

import com.example.apprecordatorio.Entidades.Alarma;
import com.example.apprecordatorio.Entidades.Seguimiento;
import com.example.apprecordatorio.DAOInterfaces.ISeguimientoExterno;
import com.example.apprecordatorio.Util.HttpUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SeguimientoExternoDao implements ISeguimientoExterno {

    private  String BASE_URL = "http://10.0.2.2/pruebaphp/";
    //private final String BASE_URL = "http://marvelous-vision-production-c97b.up.railway.app/";
    @Override
    public boolean add(Seguimiento s) {
        String url = BASE_URL + "addSeguimiento.php";

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
    }
    @Override
    public ArrayList<Seguimiento> readAllFromPaciente(int id) {

        ArrayList<Seguimiento> lista = new ArrayList<>();

        String url = BASE_URL + "readAllSeguimientoFrom.php?id_paciente=" + id;
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
    }

    }
