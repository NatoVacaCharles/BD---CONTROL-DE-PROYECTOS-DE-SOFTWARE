package model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Modelo para la tabla P3M_PERSONAL.
 * PK: PerCod (INT AUTO_INCREMENT)
 * FK: PerCodCar -> GZZ_CARGO_PERSONAL
 */
public class Personal {
    private int codigo;          // PerCod INT UNSIGNED AUTO_INCREMENT
    private int codCargo;        // PerCodCar SMALLINT UNSIGNED (FK GZZ_CARGO_PERSONAL)
    private String nombre;       // PerNom VARCHAR(80)
    private BigDecimal costoHoraCargo; // PerCosHorCar DECIMAL(8,2) > 0
    private LocalDate fechaIngreso;    // PerFecIng DATE NOT NULL
    private String estReg;       // PerEstReg CHAR(1) (A/I/*)

    public Personal() {}

    public Personal(int codigo, int codCargo, String nombre,
                    BigDecimal costoHoraCargo, LocalDate fechaIngreso, String estReg) {
        this.codigo = codigo;
        this.codCargo = codCargo;
        this.nombre = nombre;
        this.costoHoraCargo = costoHoraCargo;
        this.fechaIngreso = fechaIngreso;
        this.estReg = estReg;
    }

    // Getters y Setters
    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }

    public int getCodCargo() { return codCargo; }
    public void setCodCargo(int codCargo) { this.codCargo = codCargo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public BigDecimal getCostoHoraCargo() { return costoHoraCargo; }
    public void setCostoHoraCargo(BigDecimal v) { this.costoHoraCargo = v; }

    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate v) { this.fechaIngreso = v; }

    public String getEstReg() { return estReg; }
    public void setEstReg(String estReg) { this.estReg = estReg; }

    @Override
    public String toString() { return codigo + " - " + nombre; }
}
