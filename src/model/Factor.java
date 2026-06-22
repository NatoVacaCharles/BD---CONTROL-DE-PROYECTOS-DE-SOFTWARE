package model;

import java.math.BigDecimal;

public class Factor {
    private int codigo; // TINYINT UNSIGNED
    private String nombre; // VARCHAR(40)
    private BigDecimal porcentajeBase; // DECIMAL(5,2)
    private String estado; // CHAR(1)

    public Factor() {
    }

    public Factor(int codigo, String nombre, BigDecimal porcentajeBase, String estado) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.porcentajeBase = porcentajeBase;
        this.estado = estado;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPorcentajeBase() {
        return porcentajeBase;
    }

    public void setPorcentajeBase(BigDecimal porcentajeBase) {
        this.porcentajeBase = porcentajeBase;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }
}