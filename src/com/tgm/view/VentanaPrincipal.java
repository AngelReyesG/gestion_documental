package com.tgm.view;
import com.tgm.model.Oficio;
import com.tgm.dao.OficioDAO;
import com.tgm.service.ReporteGenerador;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
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
        //Campo de superior búsqueda
        JTextField txtBuscador = new JTextField(20);
        txtBuscador.setPreferredSize(new Dimension(300,30));

        //Panel Superior
        JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelNorte.add(new JLabel("Buscar por Folio o Asunto:"));
        panelNorte.add(txtBuscador);
        add(panelNorte, BorderLayout.NORTH);

        txtBuscador.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                String criterio = txtBuscador.getText();
                actualizarTablaBusqueda(criterio);
            }
        });

        //Panel Lateral
        JPanel panelMenu = new JPanel();
        panelMenu.setBackground(new Color(41, 54, 71));
        panelMenu.setPreferredSize(new Dimension(200, 0));
        panelMenu.setLayout(new GridLayout(10, 1, 10, 10));

        JButton btnNuevo = createMenuButton("Nuevo Oficio");
        JButton btnRefrescar = createMenuButton("Actualizar Lista");
        JButton btnRevisar = createMenuButton("Revisar oficio");
        JButton btnExportar = createMenuButton("Exportar a PDF");

        btnExportar.addActionListener(e -> btnExportarPdfActionPerformed());
        btnNuevo.addActionListener(e -> {
            FormOficio form = new FormOficio(this);
            form.setVisible(true);
            cargarDatos();
        });
        btnRevisar.addActionListener(e -> {
            int fila = tablaOficios.getSelectedRow();
            if (fila != -1) {
                int idOficio = (int) modeloTabla.getValueAt(fila, 0);
                //Llama al DAO
                if (oficioDao.actualizarEstado(idOficio, 1, "En Revisión", "Se envía para visto bueno")) {
                    JOptionPane.showMessageDialog(this, "Estado actualizado.");
                    cargarDatos();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, selecciona un oficio.");
            }
        });
        panelMenu.add(new JLabel(" TGM - TI "));
        panelMenu.add(btnNuevo);
        panelMenu.add(btnRefrescar);
        panelMenu.add(btnRevisar);
        panelMenu.add(btnExportar);
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

        //Colores según el estado
        tablaOficios.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                   boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setHorizontalAlignment(SwingConstants.CENTER);
                c.setFont(c.getFont().deriveFont(Font.BOLD));
                String estado = (value != null) ? value.toString() : "";

                //Lógica de colores
                switch (estado) {
                    case "Borrador":
                        c.setBackground(new Color(210, 215, 211));
                        c.setForeground(Color.BLACK);
                        break;
                    case "En Revisión":
                        c.setBackground(new Color(241, 196, 15));
                        c.setForeground(Color.BLACK);
                        break;
                    case "Autorizado":
                        c.setBackground(new Color(46, 204, 113));
                        c.setForeground(Color.WHITE);
                        break;
                    case "Observado":
                        c.setBackground(new Color(231, 76, 60));
                        c.setForeground(Color.WHITE);
                        break;
                    default:
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                        break;
                }

                if (isSelected){
                    c.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
                } else {
                    c.setBorder(null);
                }

                c.setOpaque(true);
                return c;
            }
        });
    }

    //Metodo para actualizar tabla de búsqueda
    private void actualizarTablaBusqueda(String criterio) {
        modeloTabla.setRowCount(0);
        List<Oficio> resultados = oficioDao.buscarOficios(criterio);
        for (Oficio o : resultados) {
            Object[] fila = {o.getId(), o.getFolio(), o.getAsunto(), o.getEstado(), o.getIdCreador()};
            modeloTabla.addRow(fila);
        }
    }

    //Metodo para cargar datos de registro
    private void btnExportarPdfActionPerformed() {
        int fila = tablaOficios.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un oficio de la tabla");
            return;
        }
        //Obtener ID del oficio
        int idOficio = (int) modeloTabla.getValueAt(fila, 0);
        Oficio seleccionado = oficioDao.consultarOficioPorId(idOficio);

        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this,"Error al recuperar los datos del oficio.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        //Configuración de guardado de archivo
        JFileChooser selector = new JFileChooser();
        selector.setDialogTitle("Guardar Oficio como PDF");

        if (selector.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String ruta = selector.getSelectedFile().getAbsolutePath();

            if (!ruta.endsWith(".pdf")) {
                ruta += ".pdf";
            }
            //Llamamos servicios para construir PDF
            ReporteGenerador generador = new ReporteGenerador();
            generador.generarPdfOficio(seleccionado, ruta);

            JOptionPane.showMessageDialog(this, "Docuento generado correctamente");
        }
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0); //Limpiar tabla
        List<Oficio> lista = oficioDao.consultarOficios();
        for (Oficio o : lista) {
            Object[] fila = {o.getId(), o.getFolio(), o.getAsunto(), o.getEstado(), o.getIdCreador()};
            modeloTabla.addRow(fila);
        }
    }

    //Botones
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