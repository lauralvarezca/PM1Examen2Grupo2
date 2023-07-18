package com.grupom.pm1examen2grupo2.Procesos;

public class Transacciones {

    public static final String NAME_DATABASE = "tareafirmas";
    public static final String TABLE_FIRMA = "firmas";
    public static final String id = "id";
    public static final String informacion = "informacion";
    public static final String imagen = "imagen";

    public static final String CREATE_TABLE_FIRMA = "CREATE TABLE "+ TABLE_FIRMA + "(id INTEGER PRIMARY KEY AUTOINCREMENT,"+ "informacion TEXT, imagen BLOB)";
    public static final String DROP_TABLE_FIRMA = "DROP TABLE IF EXISTS "+ TABLE_FIRMA;

    public static final String NameDataBase = "Tarea";

    public static final String TablaVideo = "grabarvideo";

    public static final String video = "video";

    public static final String CreateTableVideo = "CREATE TABLE grabarvideo (id INTEGER PRIMARY KEY AUTOINCREMENT,video BLOB)";
    public static final String DropTableVideo = "DROP TABLE IF EXISTS grabarvideo";
    public static final String test1 = "SELECT * FROM grabarvideo";
}
