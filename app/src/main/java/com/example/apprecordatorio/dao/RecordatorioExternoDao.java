package com.example.apprecordatorio.dao;

import android.util.Log;

import com.example.apprecordatorio.entidades.Alarma;
import com.example.apprecordatorio.entidades.Paciente;
import com.example.apprecordatorio.interfaces.IRecordatorioExterno;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class RecordatorioExternoDao implements IRecordatorioExterno {

    private Conexion con;

    public RecordatorioExternoDao ()
    {

    }

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



}
