package com.example.apprecordatorio.dao;

import android.util.Log;

import com.example.apprecordatorio.entidades.Alarma;
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

    public ArrayList<Alarma> readAll()
    {
        ArrayList<Alarma> result = new ArrayList<Alarma>();

        try {


            Connection c;
            con = new Conexion();
            c = con.abrirConexion();

            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM alarma");

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


    public boolean add(Alarma a) {
        int r = 0;

        try {

            con = new Conexion();
            Connection c = con.abrirConexion();
            if (c == null) {

                Log.e("PacienteExternoDao", "No hay conexión a la BD externa");
                return false;
            }

            String sql = "INSERT INTO alarma (id_paciente, titulo, descripcion, tono, imagen, estado, " +
                    "domingo, lunes, martes, miercoles, jueves, viernes, sabado,baja_logica) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

            PreparedStatement ps = c.prepareStatement(sql);

            /*Statement st = c.createStatement();
            r = st.executeUpdate("INSERT INTO alarma (id_paciente, titulo, descripcion, tono, imagen, estado, " +
                    "domingo, lunes, martes, miercoles, jueves, viernes, sabado,baja_logica) " +
                            "VALUES ("+a.getPacienteId()+",'"+a.getTitulo()+"', '"+a.getDescripcion()+"'," +
                    "'"+a.getTono()+"',"+a.getImagenUrl()+", "+a.isEstado()+", "+a.isDomingo()+"," +
                    " "+a.isLunes()+", "+a.isMartes()+", "+a.isMiercoles()+", "+a.isJueves()+", "+a.isViernes()+"," +
                    a.isSabado()+","+a.isBajaLogica()+", +)");*/

            ps.setInt(1, a.getPacienteId());
            ps.setString(2, a.getTitulo());
            ps.setString(3, a.getDescripcion());
            ps.setString(4, a.getTono());
            ps.setString(5, a.getImagenUrl());
            ps.setBoolean(6, a.isEstado());

            ps.setBoolean(7, a.isDomingo());
            ps.setBoolean(8, a.isLunes());
            ps.setBoolean(9, a.isMartes());
            ps.setBoolean(10, a.isMiercoles());
            ps.setBoolean(11, a.isJueves());
            ps.setBoolean(12, a.isViernes());
            ps.setBoolean(13, a.isSabado());
            ps.setBoolean(14,false);

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

        return r > 0;
    }



    public boolean update(Alarma a)
    {
        int r = 0;
        try
        {
            con = new Conexion();
            Connection c = con.abrirConexion();

            String sql = "UPDATE alarma SET titulo=?, descripcion=?, tono=?,imagen=?,estado=?," +
                    "domingo=?,lunes=?,martes=?,miercoles=?,jueves=?,viernes=?,sabado=?,baja_logica=? WHERE id=?";
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
            ps.setInt(13, a.getId());
            ps.setBoolean(14, a.isBajaLogica());

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

            con = new Conexion();
            Connection c = con.abrirConexion();

            // Baja lógica
            String sql = "UPDATE alarma SET baja_logica = 1 WHERE id = ?";
            PreparedStatement ps = c.prepareStatement(sql);

            ps.setInt(1, a.getId());
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

    public Alarma readOne(int id)
    {
        Alarma a = new Alarma();

        try {
            Connection c;
            con = new Conexion();
            c = con.abrirConexion();

            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM alarma where id ="+id+"");

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
