package com.tgm.view;
import com.tgm.model.Oficio;
import com.tgm.dao.OficioDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaPrincipal extends JFrame {
    private JTable tablaOficios;
    private DefaultTableModel modeloTabla;
    private OficioDAO oficioDao = new OficioDAO();

    public VentanaPrincipal() {
        //Configuración básica
        setTitle("GESTIÓN DOCUMENTAL TGM");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        //Panel Lateral
        JPanel panelMenu = new JPanel();
        panelMenu.setBackground(new Color(41, 54, 71));
        panelMenu.setPreferredSize(new Dimension(200, 0));
        panelMenu.setLayout(new GridLayout(10, 1, 10, 10));

        JButton btnNuevo = createMenuButton("Nuevo Oficio");
        btnNuevo.addActionListener(e -> {
            FormOficio form = new FormOficio(this);
            form.setVisible(true);
            cargarDatos();
        });
        JButton btnRefrescar = createMenuButton("Actualizar Lista");

        panelMenu.add(new JLabel(" TGM - TI "));
        panelMenu.add(btnNuevo);
        panelMenu.add(btnRefrescar);
        add(panelMenu, BorderLayout.WEST);

        //Tabla Central
        String[] columnas = {"ID", "Folio", "Asunto", "Estado", "Creador"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaOficios = new JTable(modeloTabla);

        //Estilo de tabla
        tablaOficios.setRowHeight(30);
        tablaOficios.getTableHeader().setFont(new Font("Noto Sans", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(tablaOficios);
        add(scrollPane, BorderLayout.CENTER);

        //Cargar datos
        cargarDatos();

        //Refrescar
        btnRefrescar.addActionListener(e -> cargarDatos());
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0); //Limpiar tabla
        List<Oficio> lista = oficioDao.consultarOficios();
        for (Oficio o : lista) {
            Object[] fila = {o.getId(), o.getFolio(), o.getAsunto(), o.getEstado(), o.getIdCreador()};
            modeloTabla.addRow(fila);
        }
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
    }
}