package com.example.apprecordatorio.dao;

import com.example.apprecordatorio.entidades.Paciente;
import com.example.apprecordatorio.entidades.Tutor;
import com.example.apprecordatorio.interfaces.ITutorExterno;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TutorExternoDao implements ITutorExterno {

    private Conexion con;

    @Override
    public boolean add(Tutor t) {
        int res = 0;

        Connection c = null;

        try {
            con = new Conexion();
            c = con.abrirConexion();

            String sql = "INSERT INTO tutor (nombre_usuario, email, password) " +
                    "VALUES (?, ?, ?, ?)";

            PreparedStatement ps = c.prepareStatement(sql);

            ps.setString(1, t.getUsername());
            ps.setString(2, t.getEmail());
            //ps.setInt(3, t.getP().getId());   // el paciente lo asignamos despues
            ps.setString(4, t.getPassword());

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
    public ArrayList<Tutor> readAll() {
        ArrayList<Tutor> lista = new ArrayList<>();
        Connection c = null;

        try {
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
        Connection c = null;

        try {
            con = new Conexion();
            c = con.abrirConexion();

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
}
