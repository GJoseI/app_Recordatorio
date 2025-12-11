package com.example.apprecordatorio.dao;

import android.util.Log;

import com.example.apprecordatorio.entidades.Paciente;
import com.example.apprecordatorio.interfaces.IPacienteExterno;

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

public class PacienteExternoDao implements IPacienteExterno {

    private Conexion con;

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

/*
    @Override
    public int add(Paciente p) {
        int r = 0;
        int newId = 0;
        Connection c = null;

        try {
            con = new Conexion();
            c = con.abrirConexion();

            String sql = "INSERT INTO paciente (nombre) VALUES (?)";
            PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, p.getNombre());

            r = ps.executeUpdate();

            if (r > 0) {
                // Recuperar ID autogenerado
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                     newId = rs.getInt(1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try { con.cerrar(); } catch (Exception ignored) {}
            }
        }

        return newId;
    }
*/
    @Override
    public int add(Paciente p)
    {
        int newId = 0;

        try {
           // URL url = new URL("https://tu-dominio.onrender.com/add_paciente.php");
            URL url = new URL("http://10.0.2.2/pruebaphp/addPaciente.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            String data = "nombre=" + URLEncoder.encode(p.getNombre(), "UTF-8");

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

            Log.d("RESPUESTA_PHP", sb.toString());
            // leer JSON
            JSONObject json = new JSONObject(sb.toString());

            if (json.getBoolean("success")) {
                newId = json.getInt("id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("NEW ID ","NUEVO ID:"+newId);

        return newId;
    }

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
            URL url = new URL("http://10.0.2.2/pruebaphp/getPaciente.php?id=" + id);
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
