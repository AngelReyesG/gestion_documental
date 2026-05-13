package com.tgm.main;

import com.tgm.dao.OficioDAO;
import com.tgm.dao.UsuarioDAO;
import com.tgm.model.Oficio;
import com.tgm.model.Usuario;
import java.util.List;

public class Main {
  public static void main(String[] args){
    OficioDAO dao = new OficioDAO();
    List<Oficio> misOficios = dao.consultarOficios();

    System.out.println("---LISTADO DE OFICIOS REGISTRADOS---");
    for (Oficio f : misOficios) {
      System.out.println("Estado: " + f.getEstado() + "Folio: " + f.getFolio() + "Asunto : " + f.getAsunto());
    }
  }
}