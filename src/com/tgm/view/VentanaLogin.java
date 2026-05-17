package com.tgm.view;

import com.tgm.dao.UsuarioDAO;
import com.tgm.model.Usuario;
import javax.swing.*;
import java.awt.*;

public class VentanaLogin extends JFrame {
    private JTextField txtCorreo = new JTextField();
    private JPasswordField txtPassword = new JPasswordField();
    private JButton btnEntrar = new JButton("Iniciar Sesión");

    public VentanaLogin() {
        setTitle("Acceso al Sistema de Gestión Documental - TGM");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1, 10, 10));

        add(new JLabel("  Correo Electrónico:", JLabel.CENTER));
        add(txtCorreo);
        add(new JLabel("  Contraseña:", JLabel.CENTER));
        add(txtPassword);
        add(btnEntrar);

        btnEntrar.addActionListener(e -> ingresar());
    }

    private void ingresar() {
        String correo = txtCorreo.getText();
        String pass = new String(txtPassword.getPassword());

        UsuarioDAO dao = new UsuarioDAO();
        Usuario user = dao.validarUsuario(correo, pass);

        if (user != null) {
            JOptionPane.showMessageDialog(this, "¡Bienvenido, " + user.getNombre() + "!");
            new VentanaPrincipal().setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaLogin().setVisible(true));
    }
}
