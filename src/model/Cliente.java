package model;

import java.time.LocalDate;

/**
 * Modelo para la tabla P1M_CLIENTE.
 * PK: CliCod (AUTO_INCREMENT)
 * FK: CliTipCod -> GZZ_TIPO_CLIENTE
 *     CliEstCli -> GZZ_ESTADO_CLIENTE
 */
public class Cliente {
    private int codigo;           // CliCod INT UNSIGNED AUTO_INCREMENT
    private int tipClienteCod;    // CliTipCod TINYINT UNSIGNED (FK GZZ_TIPO_CLIENTE)
    private String nombre;        // CliNom VARCHAR(80)
    private LocalDate fechaIngreso;  // CliFecIng DATE NOT NULL
    private LocalDate fechaCese;     // CliFecCes DATE (nullable)
    private LocalDate fechaUltProCer; // CliFecUltProCer DATE (nullable)
    private String estCli;        // CliEstCli CHAR(1) FK -> GZZ_ESTADO_CLIENTE
    private String estReg;        // CliEstReg CHAR(1) FK -> GZZ_ESTADO_REGISTRO

    public Cliente() {}

    public Cliente(int codigo, int tipClienteCod, String nombre,
                   LocalDate fechaIngreso, LocalDate fechaCese, LocalDate fechaUltProCer,
                   String estCli, String estReg) {
        this.codigo = codigo;
        this.tipClienteCod = tipClienteCod;
        this.nombre = nombre;
        this.fechaIngreso = fechaIngreso;
        this.fechaCese = fechaCese;
        this.fechaUltProCer = fechaUltProCer;
        this.estCli = estCli;
        this.estReg = estReg;
    }

    // Getters y Setters
    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }

    public int getTipClienteCod() { return tipClienteCod; }
    public void setTipClienteCod(int tipClienteCod) { this.tipClienteCod = tipClienteCod; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public LocalDate getFechaCese() { return fechaCese; }
    public void setFechaCese(LocalDate fechaCese) { this.fechaCese = fechaCese; }

    public LocalDate getFechaUltProCer() { return fechaUltProCer; }
    public void setFechaUltProCer(LocalDate fechaUltProCer) { this.fechaUltProCer = fechaUltProCer; }

    public String getEstCli() { return estCli; }
    public void setEstCli(String estCli) { this.estCli = estCli; }

    public String getEstReg() { return estReg; }
    public void setEstReg(String estReg) { this.estReg = estReg; }

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }
}
