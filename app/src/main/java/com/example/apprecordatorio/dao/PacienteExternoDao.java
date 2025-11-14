package com.example.apprecordatorio.dao;

import com.example.apprecordatorio.entidades.Paciente;
import com.example.apprecordatorio.interfaces.IPacienteExterno;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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


    @Override
    public boolean add(Paciente p) {
        int r = 0;
        Connection c = null;

        try {
            con = new Conexion();
            c = con.abrirConexion();

            String sql = "INSERT INTO paciente (nombre) VALUES (?)";
            PreparedStatement ps = c.prepareStatement(sql);

            ps.setString(1, p.getNombre());

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
