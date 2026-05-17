package com.tgm.dao;

import com.tgm.config.ConexionDB;
import com.tgm.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    //Metodo para registrar usuarios
    public boolean registrarUsuario (Usuario user) {
        String sql = "INSERT INTO usuarios (nombre, correo, rol, password) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, user.getNombre());
            ps.setString(2, user.getCorreo());
            ps.setString(3, user.getRol());
            ps.setString(4, user.getPassword());

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error al registrar el usuario: " + e.getMessage());
            return false;
        }
    }

    //Metodo para validar usuarios
    public Usuario validarUsuario(String correo, String password) {
        Usuario usuarioEncontrado = null;
        String sql = "SELECT * FROM usuarios WHERE correo = ? AND password = ?";

        try(Connection con = ConexionDB.conectar();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuarioEncontrado = new Usuario();
                    usuarioEncontrado.setId(rs.getInt("id_usuario"));
                    usuarioEncontrado.setNombre(rs.getString("nombre"));
                    usuarioEncontrado.setCorreo(rs.getString("correo"));
                    usuarioEncontrado.setRol(rs.getString("rol"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al validar usuario: " + e.getMessage());
        }
        return usuarioEncontrado;
    }
}
