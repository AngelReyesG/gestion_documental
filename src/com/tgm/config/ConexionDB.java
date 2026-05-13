package com.tgm.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    //Credenciales de DB
    private static final String URL = "jdbc:mysql://bompsczrcxd7dnssnkfa-mysql.services.clever-cloud.com:3306/bompsczrcxd7dnssnkfa";
    private static final String USER = "um41a897z1zt6xj8";
    private static final String PASS = "4Q1Bi5WZrqsaA3A4Gc8O";

    //Conexión a DB
    public static Connection conectar() {
        Connection conexion = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Conexión exitosa con la Base de Datos");
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Error: No se encontró el driver JDBC.");
        }
        return conexion;
    }

}
