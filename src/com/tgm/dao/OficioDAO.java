package com.tgm.dao;
import com.tgm.config.ConexionDB;
import com.tgm.model.Oficio;
import java.sql.*;
import java.util.*;

public class OficioDAO {

    //Registro de oficios
    public boolean crearOficio(Oficio oficio) {
        String sqlOficio = "INSERT INTO oficios (folio, asunto, cuerpo, estado, id_creador) VALUES (?, ?, ?, ?, ?)";
        String sqlTrazabilidad = "INSERT INTO trazabilidad (id_oficio, id_usuario, accion, estado_nuevo, comentarios) VALUES (?, ?, ?, ?, ?)";

        Connection con = null;
        try {
            con = ConexionDB.conectar();
            con.setAutoCommit(false);

            //Insertar oficio
            PreparedStatement psOficio = con.prepareStatement(sqlOficio, PreparedStatement.RETURN_GENERATED_KEYS);
            psOficio.setString(1, oficio.getFolio());
            psOficio.setString(2, oficio.getAsunto());
            psOficio.setString(3, oficio.getCuerpo());
            psOficio.setString(4, "Borrador");
            psOficio.setInt(5, oficio.getIdCreador());
            psOficio.executeUpdate();
            ResultSet rs = psOficio.getGeneratedKeys();

            //Insertar trazabilidad
            if (rs.next()) {
                int idGenerado = rs.getInt(1);
                PreparedStatement psTraza = con.prepareStatement(sqlTrazabilidad);
                psTraza.setInt(1, idGenerado);
                psTraza.setInt(2, oficio.getIdCreador());
                psTraza.setString(3, "Creación de documento");
                psTraza.setString(4, "Borrador");
                psTraza.setString(5, "Inicio del proceso administrativo");
                psTraza.executeUpdate();
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            if (con != null) try {
                con.rollback();
            } catch (SQLException ex) {
            }
            System.out.println("Error en transacción" + e.getMessage());
            return false;
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    System.out.println("Error cerrando conexión: " + e.getMessage());
                }
            }
        }
    }

        //Consulta de oficios
        public List<Oficio> consultarOficios () {
            List<Oficio> lista = new ArrayList<>();
            String sql = "SELECT * FROM oficios ORDER BY fecha_creacion DESC";

            try (Connection con = ConexionDB.conectar();
                 PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Oficio oficio = new Oficio(
                            rs.getString("folio"),
                            rs.getString("asunto"),
                            rs.getString("cuerpo"),
                            rs.getInt("id_creador")
                    );

                    oficio.setId(rs.getInt("id_oficio"));
                    oficio.setEstado(rs.getString("estado"));
                    lista.add(oficio);
                }
            } catch (SQLException e) {
                System.out.println("Error al consultar oficios: " + e.getMessage());
            }
            return lista;
        }

        //Actualizar de estado los oficios
        public boolean actualizarEstado ( int idOficio, int idUsuario, String nuevoEstado, String comentario){
            String sqlUpdate = "UPDATE oficios SET estado = ? WHERE id_oficio = ?";
            String sqlTraza = "INSERT INTO trazabilidad (id_oficio, id_usuario, accion, estado_nuevo, comentarios) VALUES (?, ?, ?, ?, ?)";

            Connection con = null;
            try {
                con = ConexionDB.conectar();
                con.setAutoCommit(false);

                //Actualizar el oficio
                PreparedStatement psUpdate = con.prepareStatement(sqlUpdate);
                psUpdate.setString(1, nuevoEstado);
                psUpdate.setInt(2, idOficio);
                psUpdate.executeUpdate();

                //Registrar movimiento en trazabilidad
                PreparedStatement psTraza = con.prepareStatement(sqlTraza);
                psTraza.setInt(1, idOficio);
                psTraza.setInt(2, idUsuario);
                psTraza.setString(3, "Cambio de Estado");
                psTraza.setString(4, nuevoEstado);
                psTraza.setString(5, comentario);
                psTraza.executeUpdate();

                con.commit();
                return true;
            } catch (SQLException e) {
                if (con != null) try {
                    con.rollback();
                } catch (SQLException ex) {
                    System.out.println("Error al actualizar estado: " + e.getMessage());
                }
                return false;
            } finally {
                if (con != null) {
                    try { con.close(); } catch (SQLException e) { System.out.println("Error cerrando conexión: " + e.getMessage()); }
                }
            }
        }
    }
