package com.example.apprecordatorio.dao;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

   // private String url= "jdbc:mysql://sql10.freesqldatabase.com:3306/sql10807656";
   // private String username = "sql10807656";
   // private String password = "4Vs4SEkCIZ";

    private Connection con = null;

   // private String url = "jdbc:mysql://10.0.2.2:3306/bdrecordatorio";

    //private String url= "jdbc:mysql://sql10.freesqldatabase.com:3306/sql10807656?useSSL=false&requireSSL=false&autoReconnect=true&serverTimezone=UTC";

    private  String url="jdbc:mysql://10.0.2.2:3306/bdrecordatorio?useSSL=false&requireSSL=false&autoReconnect=true&serverTimezone=UTC";
    private String username = "root";
    private String password = "root";


    public Conexion()
    {
    }

    public Connection abrirConexion()
    {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.e("Conexion", "Error: No se encontró el driver JDBC");
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("Conexion", "Error al conectar con la base de datos");
        }
        return con;
    }

    public void cerrar() throws SQLException {
        if (this.con != null && !this.con.isClosed()) {
            this.con.close();
        }
    }
}
