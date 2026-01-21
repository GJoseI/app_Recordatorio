package com.example.apprecordatorio.DAO;

import android.util.Log;

import com.example.apprecordatorio.Entidades.Paciente;
import com.example.apprecordatorio.DAOInterfaces.IPacienteExterno;
import com.example.apprecordatorio.Util.HttpUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PacienteExternoDao implements IPacienteExterno {

    private Conexion con;

   // private final String BASE_URL = "http://marvelous-vision-production-c97b.up.railway.app/";

    private final String  BASE_URL = "http://10.0.2.2/pruebaphp/";

    @Override
    public ArrayList<Paciente> readAll() {
        ArrayList<Paciente> lista = new ArrayList<>();
        Connection c = null;

        try {
            con = new Conexion();
            c = con.abrirConexion();

            String sql = "SELECT * FROM paciente";
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Paciente p = new Paciente();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                lista.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try { con.cerrar(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }

        return lista;
    }

    @Override
    public int add(Paciente p) {
        int newId = 0;

        try {
            String url = BASE_URL + "addPaciente.php";


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

    /*
    @Override
    public int add(Paciente p) {

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
*/
    @Override
    public boolean update(Paciente p) {
        int r = 0;
        Connection c = null;

        try {
            con = new Conexion();
            c = con.abrirConexion();

            String sql = "UPDATE paciente SET nombre=? WHERE id=?";
            PreparedStatement ps = c.prepareStatement(sql);

            ps.setString(1, p.getNombre());
            ps.setInt(2, p.getId());

            r = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try { con.cerrar(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }

        return r > 0;
    }


/*
    @Override
    public Paciente readOne(int id) {
        Connection c = null;
        Paciente paciente = null;

        try {
            con = new Conexion();
            c = con.abrirConexion();

            String sql = "SELECT * FROM paciente WHERE id = ?";
            PreparedStatement ps = c.prepareStatement(sql);

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                paciente = new Paciente();
                paciente.setId(rs.getInt("id"));
                paciente.setNombre(rs.getString("nombre"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try { con.cerrar(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }

        return paciente;
    }
*/
    public Paciente readOne(int id)
    {
        Paciente paciente = null;

        try {
            URL url = new URL(BASE_URL+"getPaciente.php?id=" + id);
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


    @Override
    public boolean delete(Paciente p) {
        int r = 0;
        Connection c = null;

        try {
            con = new Conexion();
            c = con.abrirConexion();

            String sql = "DELETE FROM paciente WHERE id=?";
            PreparedStatement ps = c.prepareStatement(sql);

            ps.setInt(1, p.getId());

            r = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try { con.cerrar(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }

        return r > 0;
    }
}
