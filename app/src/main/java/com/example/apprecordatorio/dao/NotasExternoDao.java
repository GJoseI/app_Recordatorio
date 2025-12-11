package com.example.apprecordatorio.dao;

import android.util.Log;

import com.example.apprecordatorio.entidades.Recordatorio;
import com.example.apprecordatorio.interfaces.INotaExterno;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import com.example.apprecordatorio.util.HttpUtils;

public class NotasExternoDao implements INotaExterno {

    //private Conexion con;

    private final String BASE_URL = "http://10.0.2.2/pruebaphp/";

    @Override
    public int add(Recordatorio r) {
        String url = BASE_URL + "addNota.php";
        if(r.getDescripcion()==null)r.setDescripcion("");
        if(r.getImagenUrl()==null)r.setImagenUrl("");

        HashMap<String, String> params = new HashMap<>();
        params.put("titulo", r.getTitulo());
        params.put("descripcion", r.getDescripcion());
        params.put("imagen", r.getImagenUrl());
        params.put("id_paciente", String.valueOf(r.getPacienteId()));
        params.put("baja_logica", r.isBajaLogica() ? "1" : "0");
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
        String url = BASE_URL + "readAllFromNotas.php";

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
/*
    @Override
    public boolean add(Recordatorio r) {
        int res = 0;
        Connection c = null;

        try {
            con = new Conexion();
            c = con.abrirConexion();

            String sql = "INSERT INTO notas (titulo, descripcion, imagen, id_paciente, baja_logica,id) " +
                    "VALUES (?, ?, ?, ?, ?,?)";

            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, r.getTitulo());
            ps.setString(2, r.getDescripcion());
            ps.setString(3, r.getImagenUrl());
            ps.setInt(4, r.getPacienteId());
            ps.setBoolean(5, r.isBajaLogica());
            ps.setInt(6,r.getId());

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
    public ArrayList<Recordatorio> readAllFrom(int id) {
        ArrayList<Recordatorio> lista = new ArrayList<>();
        Connection c = null;

        try {
            con = new Conexion();
            c = con.abrirConexion();

            String sql = "SELECT * FROM notas WHERE baja_logica = 0 and id_paciente = "+id;
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Recordatorio r = new Recordatorio();
                r.setId(rs.getInt("id"));
                r.setTitulo(rs.getString("titulo"));
                r.setDescripcion(rs.getString("descripcion"));
                r.setImagenUrl(rs.getString("imagen"));
                r.setPacienteId(rs.getInt("id_paciente"));
                r.setBajaLogica(rs.getBoolean("baja_logica"));

                lista.add(r);
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
    public boolean delete(Recordatorio r) {
        int res = 0;
        Connection c = null;

        try {
            con = new Conexion();
            c = con.abrirConexion();

            // Baja lógica
            String sql = "UPDATE notas SET baja_logica = 1 WHERE id = ? and id_paciente = ?";
            PreparedStatement ps = c.prepareStatement(sql);

            ps.setInt(1, r.getId());
            ps.setInt(2,r.getPacienteId());
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
    public boolean update(Recordatorio r) {
        int res = 0;
        Connection c = null;

        try {
            con = new Conexion();
            c = con.abrirConexion();

            String sql = "UPDATE notas SET titulo=?, descripcion=?, imagen=?, id_paciente=?, baja_logica=? " +
                    "WHERE id=? and id_paciente = ?";

            PreparedStatement ps = c.prepareStatement(sql);

            ps.setString(1, r.getTitulo());
            ps.setString(2, r.getDescripcion());
            ps.setString(3, r.getImagenUrl());
            ps.setInt(4, r.getPacienteId());
            ps.setBoolean(5, r.isBajaLogica());
            ps.setInt(6, r.getId());
            ps.setInt(7, r.getPacienteId());

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
    public Recordatorio readOne(int id, int idPaciente) {
        Connection c = null;
        Recordatorio r = null;

        try {
            con = new Conexion();
            c = con.abrirConexion();

            String sql = "SELECT * FROM notas WHERE id = ? and id_paciente = ?";
            PreparedStatement ps = c.prepareStatement(sql);

            ps.setInt(1, id);
            ps.setInt(2,idPaciente);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                r = new Recordatorio();
                r.setId(rs.getInt("id"));
                r.setTitulo(rs.getString("titulo"));
                r.setDescripcion(rs.getString("descripcion"));
                r.setImagenUrl(rs.getString("imagen"));
                r.setPacienteId(rs.getInt("id_paciente"));
                r.setBajaLogica(rs.getBoolean("baja_logica"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try { con.cerrar(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }

        return r;
    }
*/

}