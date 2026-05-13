package com.tgm.model;

public class Oficio {
    private int id;
    private String folio;
    private String asunto;
    private String cuerpo;
    private String estado;
    private int idCreador;

    //Constructor del ofico
    public Oficio(String folio, String asunto, String cuerpo, int idCreador) {
        this.folio = folio;
        this.asunto = asunto;
        this.cuerpo = cuerpo;
        this.idCreador = idCreador;
        this.estado = "Borrador";
    }

    //Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFolio() { return folio; }
    public void setFolio(String folio) { this.folio = folio; }

    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) {this.asunto = asunto; }

    public String getCuerpo() { return cuerpo; }
    public void setCuerpo(String cuerpo) { this.cuerpo = cuerpo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public int getIdCreador() { return idCreador; }
    public void setIdCreador(int idCreador) { this.idCreador = idCreador; }

}
