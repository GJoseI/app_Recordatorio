package com.example.apprecordatorio.dao;

import android.util.Log;

import com.example.apprecordatorio.entidades.Paciente;
import com.example.apprecordatorio.entidades.Tutor;
import com.example.apprecordatorio.interfaces.ITutorExterno;
import com.example.apprecordatorio.util.HttpUtils;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class TutorExternoDao implements ITutorExterno {

   // private Conexion con;


    private String BASE_URL = "http://10.0.2.2/pruebaphp/";
    @Override
    public boolean add(Tutor t) {
        String url = BASE_URL + "addTutor.php";

        HashMap<String, String> params = new HashMap<>();
        params.put("nombre_usuario", t.getUsername());
        params.put("email", t.getEmail());
        params.put("password", t.getPassword());

        String response = HttpUtils.post(url, params);

        try {
            JSONObject json = new JSONObject(response);
            return json.optBoolean("success", false);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Tutor login(String username, String password) {
        String url = BASE_URL + "loginTutor.php";

        HashMap<String, String> params = new HashMap<>();
        params.put("nombre_usuario", username);
        params.put("password", password);

        Tutor t;
        String response = HttpUtils.post(url, params);

        try {
            JSONObject json = new JSONObject(response);

            if (json.optBoolean("success")) {
                t = new Tutor();
                t.setId(json.getInt("id"));
                t.setUsername(json.getString("nombre_usuario"));
                t.setEmail(json.getString("email"));
                int idPaciente = json.optInt("id_paciente", 0);
                //if(idPaciente>0)
                //{
                    Paciente paciente = new Paciente();
                    paciente.setId(idPaciente);
                    t.setP(paciente);
                //}
                return t;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // login incorrecto
    }

    @Override
    public boolean vincular(Tutor t) {
        String url = BASE_URL + "vincularTutor.php";

        HashMap<String, String> params = new HashMap<>();
        params.put("id_tutor", String.valueOf(t.getId()));
        params.put("id_paciente", String.valueOf(t.getP().getId()));

        String response = HttpUtils.post(url, params);

        try {
            JSONObject json = new JSONObject(response);
            return json.optBoolean("success", false);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean desvincular(int idTutor) {
        String url = BASE_URL + "desvincular.php";

        HashMap<String, String> params = new HashMap<>();
        params.put("id_tutor", String.valueOf(idTutor));

        String response = HttpUtils.post(url, params);

        try {
            JSONObject json = new JSONObject(response);
            return json.getBoolean("success");
        } catch (Exception e) {
            return false;
        }
    }


    /*
    @Override
    public boolean add(Tutor t) {
        int res = 0;

        try {
            con = new Conexion();
            Connection c = con.abrirConexion();
            if (c == null) {

                Log.e("TutorExternoDao", "No hay conexión a la BD externa");
                return false;
            }

            String sql = "INSERT INTO tutor (nombre_usuario, email, password) " +
                    "VALUES (?, ?, ?)";

            PreparedStatement ps = c.prepareStatement(sql);

            ps.setString(1, t.getUsername());
            ps.setString(2, t.getEmail());
            //ps.setInt(3, t.getP().getId());   // el paciente lo asignamos despues
            ps.setString(3, t.getPassword());

            res = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                Log.d("DEbug con", "intento de cierre de coneccion");
                try { con.cerrar(); } catch (SQLException e) { throw new RuntimeException(e); }
            }
        }

        return res > 0;
    }



    @Override
    public ArrayList<Tutor> readAll() {
        ArrayList<Tutor> lista = new ArrayList<>();

        try {
            Connection c;
            con = new Conexion();
            c = con.abrirConexion();

            String sql = "SELECT * FROM tutor";
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Tutor t = new Tutor();
                t.setId(rs.getInt("id"));
                t.setUsername(rs.getString("nombre_usuario"));
                t.setEmail(rs.getString("email"));
                t.setPassword(rs.getString("password"));

                // Paciente asociado (solo ID)
                Paciente p = new Paciente();
                p.setId(rs.getInt("id_paciente"));
                t.setP(p);

                lista.add(t);
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
    public boolean update(Tutor t) {
        int res = 0;

        try {
            con = new Conexion();
            Connection c = con.abrirConexion();

            String sql = "UPDATE tutor SET nombre_usuario=?, email=?, id_paciente=?, password=? WHERE id=?";

            PreparedStatement ps = c.prepareStatement(sql);

            ps.setString(1, t.getUsername());
            ps.setString(2, t.getEmail());
            ps.setInt(3, t.getP().getId());
            ps.setString(4, t.getPassword());
            ps.setInt(5, t.getId());

            res = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try { con.cerrar(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }

        return res > 0;
    }


    public boolean vincular(Tutor t) {
        int res = 0;

        try {
            con = new Conexion();
            Connection c = con.abrirConexion();

            String sql = "UPDATE tutor SET id_paciente=? WHERE id=?";

            PreparedStatement ps = c.prepareStatement(sql);


            ps.setInt(1, t.getP().getId());
            ps.setInt(2, t.getId());

            res = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try { con.cerrar(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }

        return res > 0;
    }



    @Override
    public Tutor readOne(int id) {
        Connection c = null;
        Tutor t = null;

        try {
            con = new Conexion();
            c = con.abrirConexion();

            String sql = "SELECT * FROM tutor WHERE id = ?";
            PreparedStatement ps = c.prepareStatement(sql);

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                t = new Tutor();
                t.setId(rs.getInt("id"));
                t.setUsername(rs.getString("nombre_usuario"));
                t.setEmail(rs.getString("email"));
                t.setPassword(rs.getString("password"));

                // Paciente asociado
                Paciente p = new Paciente();
                p.setId(rs.getInt("id_paciente"));
                t.setP(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try { con.cerrar(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }

        return t;
    }



    @Override
    public boolean delete(Tutor t) {
        int res = 0;
        Connection c = null;

        try {
            con = new Conexion();
            c = con.abrirConexion();

            String sql = "DELETE FROM tutor WHERE id=?";
            PreparedStatement ps = c.prepareStatement(sql);

            ps.setInt(1, t.getId());
            res = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try { con.cerrar(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }

        return res > 0;
    }

    public Tutor obtenerTutor(String user, String pass, String email){
        TutorExternoDao tutorExternoDao = new TutorExternoDao();
        ArrayList<Tutor> listaTutores = tutorExternoDao.readAll();
        for(Tutor tutor : listaTutores){
            if(user == null && pass == null) {
                if(tutor.getEmail().toString().equals(email)){
                    return tutor;
                }
            }
            else {
                if(email == null) {
                    if (tutor.getPassword().toString().equals(pass) && tutor.getUsername().toString().equals(user)) {
                        return tutor;
                    }
                }
            }
        }
        return null;
    }
    */

}
