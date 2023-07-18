package com.grupom.pm1examen2grupo2.Procesos;

public class InfoFirma {

    Integer id;
    String Informacion, img;

    public InfoFirma() {

    }
    public InfoFirma(Integer id, String informacion, String imagen) {
        this.id = id;
        this.Informacion = informacion;
        this.img = imagen;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInformacion() {
        return Informacion;
    }

    public void setInformacion(String informacion) {
        Informacion = informacion;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
