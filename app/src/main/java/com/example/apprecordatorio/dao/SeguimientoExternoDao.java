package com.example.apprecordatorio.dao;

import android.util.Log;

import com.example.apprecordatorio.entidades.Alarma;
import com.example.apprecordatorio.entidades.Seguimiento;
import com.example.apprecordatorio.interfaces.ISeguimientoExterno;
import com.example.apprecordatorio.util.HttpUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class SeguimientoExternoDao implements ISeguimientoExterno {

    private  String BASE_URL = "http://10.0.2.2/pruebaphp/";
    @Override
    public boolean add(Seguimiento s) {
        String url = BASE_URL + "addSeguimiento.php";

        HashMap<String, String> params = new HashMap<>();
        params.put("atendida", s.isAtendida() ? "1" : "0");
        params.put("id_alarma", String.valueOf(s.getAlarma().getId()));
        params.put("id_paciente", String.valueOf(s.getAlarma().getPacienteId()));

        String json = HttpUtils.post(url, params);

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
/*
    @Override
    public boolean add(Seguimiento s) {
        Conexion cn = new Conexion();
        Connection conn = null;
        PreparedStatement ps = null;

        String query = "INSERT INTO seguimiento (atendida, id_alarma, id_paciente) VALUES (?, ?,?)";

        try {
            conn = cn.abrirConexion();
            ps = conn.prepareStatement(query);

            ps.setBoolean(1, s.isAtendida());
            ps.setInt(2, s.getAlarma().getId());
            ps.setInt(3,s.getAlarma().getPacienteId());

            int resultado = ps.executeUpdate();

            return resultado > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;

        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) cn.cerrar();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public ArrayList<Seguimiento> readAllFromPaciente(int id) {
        ArrayList<Seguimiento> lista = new ArrayList<>();

        Conexion cn = new Conexion();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT * FROM seguimiento WHERE id_paciente = ?";

        try {
            conn = cn.abrirConexion();
            ps = conn.prepareStatement(query);
            ps.setInt(1,id);
            rs = ps.executeQuery();

            while (rs.next()) {
                Seguimiento s = new Seguimiento();

                s.setId(rs.getInt("id"));
                s.setAtendida(rs.getBoolean("atendida"));

                // cargar alarma (solo ID por ahora)
                Alarma a = new Alarma();
                a.setId(rs.getInt("id_alarma"));
                a.setPacienteId(id);
                s.setAlarma(a);
                s.setTimestamp(rs.getTimestamp("fecha_hora").toString());

                // campo timestamp como String
                s.setTimestamp(rs.getString("fecha_hora"));

                lista.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) cn.cerrar();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return lista;
    }*/
    }
