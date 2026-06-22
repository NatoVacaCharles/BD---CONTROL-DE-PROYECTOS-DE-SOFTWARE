package model;

import java.math.BigDecimal;

/**
 * Modelo para la tabla P3M_ESTANDAR.
 * PK compuesta: EstActEtaCod + EstActActCod + EstCod
 * FK: (EstActEtaCod, EstActActCod) → P3M_ACTIVIDAD  (PK compuesta de Actividad)
 *     EstTipCod → GZZ_TIPO_ESTANDAR
 *
 * Restricción CHECK: uno de EstValNum o EstValTxt debe ser NOT NULL, el otro NULL.
 */
public class Estandar {
    private int estActEtaCod;    // SMALLINT UNSIGNED (parte de FK + PK)
    private int estActActCod;    // TINYINT UNSIGNED (parte de FK + PK)
    private int estCod;          // TINYINT UNSIGNED (parte de PK)
    private int estTipCod;       // TINYINT UNSIGNED (FK GZZ_TIPO_ESTANDAR)
    private String estNom;       // VARCHAR(80)
    private BigDecimal estValNum; // DECIMAL(8,2) nullable (exclusivo con EstValTxt)
    private String estValTxt;    // VARCHAR(60) nullable (exclusivo con EstValNum)
    private String estUni;       // VARCHAR(20)
    private String estEstReg;    // CHAR(1) — estado lógico (A/I/*)

    public Estandar() {}

    // Getters y Setters
    public int getEstActEtaCod() { return estActEtaCod; }
    public void setEstActEtaCod(int v) { this.estActEtaCod = v; }

    public int getEstActActCod() { return estActActCod; }
    public void setEstActActCod(int v) { this.estActActCod = v; }

    public int getEstCod() { return estCod; }
    public void setEstCod(int v) { this.estCod = v; }

    public int getEstTipCod() { return estTipCod; }
    public void setEstTipCod(int v) { this.estTipCod = v; }

    public String getEstNom() { return estNom; }
    public void setEstNom(String v) { this.estNom = v; }

    public BigDecimal getEstValNum() { return estValNum; }
    public void setEstValNum(BigDecimal v) { this.estValNum = v; }

    public String getEstValTxt() { return estValTxt; }
    public void setEstValTxt(String v) { this.estValTxt = v; }

    public String getEstUni() { return estUni; }
    public void setEstUni(String v) { this.estUni = v; }

    public String getEstEstReg() { return estEstReg; }
    public void setEstEstReg(String v) { this.estEstReg = v; }

    @Override
    public String toString() {
        return "Est[eta=" + estActEtaCod + ",act=" + estActActCod + ",cod=" + estCod + "] " + estNom;
    }
}
