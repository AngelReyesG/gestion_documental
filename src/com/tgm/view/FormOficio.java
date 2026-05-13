package com.tgm.view;

import com.tgm.model.Oficio;
import com.tgm.dao.OficioDAO;
import javax.swing.*;
import java.awt.*;

public class FormOficio extends JDialog {
    private JTextField txtFolio = new JTextField(20);
    private JTextField txtAsunto = new JTextField(20);
    private JTextArea txtCuerpo = new JTextArea(5, 20);
    private JButton btnGuardar = new JButton("Guardar Oficio");

    public FormOficio(JFrame parent) {
        super(parent, "Registrar Nuevo Oficio", true);
        setLayout(new GridLayout(4, 2, 10, 10));
        setSize(400, 300);
        setLocationRelativeTo(parent);

        add(new JLabel("Folio:")); add(txtFolio);
        add(new JLabel("Asunto:")); add(txtAsunto);
        add(new JLabel("Cuerpo:")); add(new JScrollPane(txtCuerpo));
        add(new JLabel("")); add(btnGuardar);

        btnGuardar.addActionListener(e -> guardar());
    }

    private void guardar() {

        Oficio nuevo = new Oficio(txtFolio.getText(), txtAsunto.getText(), txtCuerpo.getText(), 1);
        OficioDAO dao = new OficioDAO();

        if (dao.crearOficio(nuevo)) {
            JOptionPane.showMessageDialog(this, "Oficio guardado y enviado a la base de datos.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar.");
        }
    }
}
