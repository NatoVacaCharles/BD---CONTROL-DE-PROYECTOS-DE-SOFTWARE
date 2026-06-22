package model;

/**
 * Modelo para la tabla P3M_ETAPA.
 * PK: EtaCod SMALLINT UNSIGNED (no auto-increment, ingreso manual).
 * No tiene FKs adicionales (solo EstReg → GZZ_ESTADO_REGISTRO).
 */
public class Etapa {
    private int codigo;     // EtaCod SMALLINT UNSIGNED
    private String nombre;  // EtaNom VARCHAR(60)
    private String estReg;  // EtaEstReg CHAR(1) — estado lógico (A/I/*)

    public Etapa() {}

    public Etapa(int codigo, String nombre, String estReg) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.estReg = estReg;
    }

    // Getters y Setters
    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEstReg() { return estReg; }
    public void setEstReg(String estReg) { this.estReg = estReg; }

    @Override
    public String toString() { return codigo + " - " + nombre; }
}
