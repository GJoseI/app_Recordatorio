package com.example.apprecordatorio.dao;

import com.example.apprecordatorio.entidades.Recordatorio;
import com.example.apprecordatorio.interfaces.INotaExterno;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class NotasExternoDao implements INotaExterno {

    private Conexion con;

    @Override
    public boolean add(Recordatorio r) {
        int res = 0;
        Connection c = null;

        try {
            con = new Conexion();
            c = con.abrirConexion();

            String sql = "INSERT INTO notas (titulo, descripcion, imagen, id_paciente, baja_logica) " +
                    "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, r.getTitulo());
            ps.setString(2, r.getDescripcion());
            ps.setString(3, r.getImagenUrl());
            ps.setInt(4, r.getPacienteId());
            ps.setBoolean(5, r.isBajaLogica());

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
    public ArrayList<Recordatorio> readAll() {
        ArrayList<Recordatorio> lista = new ArrayList<>();
        Connection c = null;

        try {
            con = new Conexion();
            c = con.abrirConexion();

            String sql = "SELECT * FROM notas WHERE baja_logica = 0";
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
            String sql = "UPDATE notas SET baja_logica = 1 WHERE id = ?";
            PreparedStatement ps = c.prepareStatement(sql);

            ps.setInt(1, r.getId());
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
                    "WHERE id=?";

            PreparedStatement ps = c.prepareStatement(sql);

            ps.setString(1, r.getTitulo());
            ps.setString(2, r.getDescripcion());
            ps.setString(3, r.getImagenUrl());
            ps.setInt(4, r.getPacienteId());
            ps.setBoolean(5, r.isBajaLogica());
            ps.setInt(6, r.getId());

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
    public Recordatorio readOne(int id) {
        Connection c = null;
        Recordatorio r = null;

        try {
            con = new Conexion();
            c = con.abrirConexion();

            String sql = "SELECT * FROM notas WHERE id = ?";
            PreparedStatement ps = c.prepareStatement(sql);

            ps.setInt(1, id);
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
}