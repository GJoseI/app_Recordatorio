package com.example.apprecordatorio.dao;

import android.util.Log;

import com.example.apprecordatorio.entidades.Alarma;
import com.example.apprecordatorio.entidades.Paciente;
import com.example.apprecordatorio.interfaces.IRecordatorioExterno;
import com.example.apprecordatorio.util.HttpUtils;

import org.json.JSONArray;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RecordatorioExternoDao implements IRecordatorioExterno {

    private Conexion con;

    private final String BASE_URL = "http://10.0.2.2/pruebaphp/";
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
                            "&minuto=" + a.getMinuto();

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

            /*if (json.getBoolean("success")) {
                result = 1; // se agregó OK
            }*/
            return json.getInt("id");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


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
            Log.d("ALARMA DAO","ID REMOTO"+ String.valueOf(a.getIdRemoto()));

            // Armamos el mapa de parámetros
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


            String json = HttpUtils.post(BASE_URL + "updateAlarma.php", params);

            Log.d("ALARMA DAO",json);

            JSONObject result = new JSONObject(json);
            return result.getBoolean("success");

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    //-------------------------------------------------------
    // DELETE (BAJA LÓGICA)
    //-------------------------------------------------------
    public boolean delete(Alarma a) {
        try {
            URL url = new URL(BASE_URL + "deleteAlarma.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            String data =
                    "id=" + a.getIdRemoto() +
                            "&id_paciente=" + a.getPacienteId();

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
/*
    public ArrayList<Alarma> readAllFromPaciente(int id)
    {
        ArrayList<Alarma> result = new ArrayList<Alarma>();

        try {


            Connection c;
            con = new Conexion();
            c = con.abrirConexion();

            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM alarma WHERE id_paciente ="+id+"and baja_logica = 0");

            while (rs.next()) {
                Alarma a = new Alarma();

                a.setId(Integer.parseInt(rs.getString("id")));
                a.setPacienteId(Integer.parseInt(rs.getString("id_paciente")));
                a.setTitulo(rs.getString("titulo"));
                a.setDescripcion(rs.getString("descripcion"));
                a.setTono(rs.getString("tono"));
                a.setImagenUrl(rs.getString("imagen"));
                a.setEstado(rs.getBoolean("estado"));
                a.setDomingo(rs.getBoolean("domingo"));
                a.setLunes(rs.getBoolean("lunes"));
                a.setMartes(rs.getBoolean("martes"));
                a.setMiercoles(rs.getBoolean("miercoles"));
                a.setJueves(rs.getBoolean("jueves"));
                a.setViernes(rs.getBoolean("viernes"));
                a.setSabado(rs.getBoolean("sabado"));
                a.setBajaLogica(rs.getBoolean("baja_logica"));
                a.setHora(rs.getInt("hora"));
                a.setMinuto(rs.getInt("minuto"));

                result.add(a);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(con!=null)con.cerrar();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }


    public int add(Alarma a) {
        int r = 0;

        Log.d("EN ADD DAOEX","ID ALARMA"+a.getId()+" id paciente"+a.getPacienteId() );
        int newId =0;
        try {

            con = new Conexion();
            Connection c = con.abrirConexion();
            if (c == null) {

                Log.e("PacienteExternoDao", "No hay conexión a la BD externa");
                return 0;
            }

            String sql = "INSERT INTO alarma (id,id_paciente, titulo, descripcion, tono, imagen, estado, " +
                    "domingo, lunes, martes, miercoles, jueves, viernes, sabado,baja_logica,hora,minuto) " +
                    "VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";

            PreparedStatement ps = c.prepareStatement(sql);

            ps.setInt(1,a.getId());
            ps.setInt(2, a.getPacienteId());
            ps.setString(3, a.getTitulo());
            ps.setString(4, a.getDescripcion());
            ps.setString(5, a.getTono());
            ps.setString(6, a.getImagenUrl());
            ps.setBoolean(7, a.isEstado());

            ps.setBoolean(8, a.isDomingo());
            ps.setBoolean(9, a.isLunes());
            ps.setBoolean(10, a.isMartes());
            ps.setBoolean(11, a.isMiercoles());
            ps.setBoolean(12, a.isJueves());
            ps.setBoolean(13, a.isViernes());
            ps.setBoolean(14, a.isSabado());
            ps.setBoolean(15,false);
            ps.setInt(16,a.getHora());
            ps.setInt(17,a.getMinuto());

            r = ps.executeUpdate();




        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (con!=null) {
                try {
                    con.cerrar();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return r;
    }





    public boolean update(Alarma a)
    {
        int r = 0;
        try
        {
            con = new Conexion();
            Connection c = con.abrirConexion();

            Log.d("dao ex update"," cod paciente: "+a.getPacienteId());
            //Log.d("dao ex update","cod paciente en read",)

            String sql = "UPDATE alarma SET titulo = ?, descripcion = ?, tono = ?, imagen = ?, estado = ?, " +
                    "domingo = ?, lunes = ?, martes=?, miercoles = ?, jueves = ?, viernes = ?, sabado = ?, baja_logica = ?, " +
                    "hora = ?, minuto = ? " +
                    "WHERE id = ? AND id_paciente = ?";

            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, a.getTitulo());
            ps.setString(2, a.getDescripcion());
            ps.setString(3, a.getTono());
            ps.setString(4, a.getImagenUrl());
            ps.setBoolean(5, a.isEstado());
            ps.setBoolean(6, a.isDomingo());
            ps.setBoolean(7, a.isLunes());
            ps.setBoolean(8, a.isMartes());
            ps.setBoolean(9, a.isMiercoles());
            ps.setBoolean(10, a.isJueves());
            ps.setBoolean(11, a.isViernes());
            ps.setBoolean(12, a.isSabado());
            ps.setBoolean(13, a.isBajaLogica());
            ps.setInt(14,a.getHora());
            ps.setInt(15,a.getMinuto());
            ps.setInt(16, a.getId());
            ps.setInt(17,a.getPacienteId());


            r = ps.executeUpdate();
        }catch (Exception e)
        {
            e.printStackTrace();
            Log.e("update alarma", "Error al actualizar: " + e.getMessage());
        }finally {
            try {
                if(con!=null)con.cerrar();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if(r>0)
        {
            return true;
        }else {
            return false;
        }

    }
    public boolean delete(Alarma a) {
        int res = 0;

        try {

            Log.d("dao ex delete"," cod paciente: "+a.getPacienteId());
            Log.d("dao ex delete"," cod alarma: "+a.getId());
            con = new Conexion();
            Connection c = con.abrirConexion();

            // Baja lógica
            String sql = "UPDATE alarma SET baja_logica = 1 WHERE id = ? and id_paciente = ?";
            PreparedStatement ps = c.prepareStatement(sql);

            ps.setInt(1, a.getId());
            ps.setInt(2, a.getPacienteId());

            res = ps.executeUpdate();

            Log.d("DAO DEL","ID ALARAMA "+a.getId()+"el resultado es "+res);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try { con.cerrar(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }

        return res > 0;
    }

    public Alarma readOneFrom(int id,int idPaciente)
    {
        Alarma a = new Alarma();

        try {
            Connection c;
            con = new Conexion();
            c = con.abrirConexion();

            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM alarma where id ="+id+" and id_paciente ="+idPaciente);

            while (rs.next()) {


                a.setId(Integer.parseInt(rs.getString("id")));
                a.setPacienteId(Integer.parseInt(rs.getString("id_paciente")));
                a.setTitulo(rs.getString("titulo"));
                a.setDescripcion(rs.getString("descripcion"));
                a.setTono(rs.getString("tono"));
                a.setImagenUrl(rs.getString("imagen"));
                a.setEstado(rs.getBoolean("estado"));
                a.setDomingo(rs.getBoolean("domingo"));
                a.setLunes(rs.getBoolean("lunes"));
                a.setMartes(rs.getBoolean("martes"));
                a.setMiercoles(rs.getBoolean("miercoles"));
                a.setJueves(rs.getBoolean("jueves"));
                a.setViernes(rs.getBoolean("viernes"));
                a.setSabado(rs.getBoolean("sabado"));
                a.setBajaLogica(rs.getBoolean("baja_logica"));


            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(con!=null)con.cerrar();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return a;
    }
*/


}
