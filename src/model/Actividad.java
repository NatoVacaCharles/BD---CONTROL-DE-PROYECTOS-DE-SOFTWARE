package model;

import java.math.BigDecimal;

/**
 * Modelo para la tabla P3M_ACTIVIDAD.
 * PK compuesta: ActEtaCod + ActCod
 * FK: ActEtaCod → P3M_ETAPA
 */
public class Actividad {
    private int actEtaCod;       // SMALLINT UNSIGNED (FK P3M_ETAPA, parte de PK)
    private int actCod;          // TINYINT UNSIGNED (parte de PK)
    private String actNom;       // VARCHAR(60)
    private BigDecimal actTpoEst; // DECIMAL(6,2) > 0
    private String actEstReg;    // CHAR(1) — estado lógico (A/I/*)

    public Actividad() {}

    public Actividad(int actEtaCod, int actCod, String actNom, BigDecimal actTpoEst, String actEstReg) {
        this.actEtaCod = actEtaCod;
        this.actCod = actCod;
        this.actNom = actNom;
        this.actTpoEst = actTpoEst;
        this.actEstReg = actEstReg;
    }

    // Getters y Setters
    public int getActEtaCod() { return actEtaCod; }
    public void setActEtaCod(int v) { this.actEtaCod = v; }

    public int getActCod() { return actCod; }
    public void setActCod(int v) { this.actCod = v; }

    public String getActNom() { return actNom; }
    public void setActNom(String v) { this.actNom = v; }

    public BigDecimal getActTpoEst() { return actTpoEst; }
    public void setActTpoEst(BigDecimal v) { this.actTpoEst = v; }

    public String getActEstReg() { return actEstReg; }
    public void setActEstReg(String v) { this.actEstReg = v; }

    @Override
    public String toString() { return actEtaCod + "-" + actCod + " " + actNom; }
}
