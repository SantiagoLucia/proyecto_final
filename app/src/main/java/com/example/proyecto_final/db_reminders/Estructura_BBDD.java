package com.example.proyecto_final.db_reminders;

public class Estructura_BBDD {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private Estructura_BBDD() {}

    /* Inner class that defines the table contents */
    // public static class FeedEntry implements BaseColumns {
    public static final String TABLE_NAME = "alertas";
    public static final String NOMBRE_COL1 = "Id";
    public static final String NOMBRE_COL2 = "Nombre";
    public static final String NOMBRE_COL3 = "fecha";
    //}

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Estructura_BBDD.TABLE_NAME + " (" +
                    Estructura_BBDD.NOMBRE_COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Estructura_BBDD.NOMBRE_COL2 + " TEXT," +
                    Estructura_BBDD.NOMBRE_COL3 + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Estructura_BBDD.TABLE_NAME;
}
