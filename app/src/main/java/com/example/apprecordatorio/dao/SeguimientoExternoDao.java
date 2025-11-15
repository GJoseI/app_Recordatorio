package com.example.apprecordatorio.dao;

import com.example.apprecordatorio.entidades.Alarma;
import com.example.apprecordatorio.entidades.Seguimiento;
import com.example.apprecordatorio.interfaces.ISeguimientoExterno;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SeguimientoExternoDao implements ISeguimientoExterno {

    @Override
    public boolean add(Seguimiento s) {
        Conexion cn = new Conexion();
        Connection conn = null;
        PreparedStatement ps = null;

        String query = "INSERT INTO seguimiento (atendida, id_alarma, id_paciente) VALUES (?, ?,?)";

        try {
            conn = cn.abrirConexion();
            ps = conn.prepareStatement(query);

            ps.setBoolean(1, s.isAtendida());
            ps.setInt(2, s.getAlarma().getId());
            ps.setInt(3,s.getAlarma().getPacienteId());

            int resultado = ps.executeUpdate();

            return resultado > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;

        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) cn.cerrar();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public ArrayList<Seguimiento> readAllFromPaciente(int id) {
        ArrayList<Seguimiento> lista = new ArrayList<>();

        Conexion cn = new Conexion();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT * FROM seguimiento WHERE id_paciente = ?";

        try {
            conn = cn.abrirConexion();
            ps = conn.prepareStatement(query);
            ps.setInt(1,id);
            rs = ps.executeQuery();

            while (rs.next()) {
                Seguimiento s = new Seguimiento();

                s.setId(rs.getInt("id"));
                s.setAtendida(rs.getBoolean("atendida"));

                // cargar alarma (solo ID por ahora)
                Alarma a = new Alarma();
                a.setId(rs.getInt("id_alarma"));
                a.setPacienteId(id);
                s.setAlarma(a);

                // campo timestamp como String
                s.setTimestamp(rs.getString("fecha_hora"));

                lista.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) cn.cerrar();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return lista;
    }
    }
