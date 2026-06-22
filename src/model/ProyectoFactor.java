package model;

import java.math.BigDecimal;

/**
 * Modelo para P2T_PROYECTO_FACTOR.
 * PK compuesta: ProFacCliCod + ProFacTipCod + ProFacSec + ProFacCod
 * FK: (ProFacCliCod, ProFacTipCod, ProFacSec) → P2M_PROYECTO
 *     ProFacCod → GZZ_FACTOR
 * Almacena el porcentaje de aplicación de cada factor en un proyecto.
 */
public class ProyectoFactor {
    private int        proFacCliCod;  // INT UNSIGNED
    private int        proFacTipCod;  // TINYINT UNSIGNED
    private int        proFacSec;     // TINYINT UNSIGNED
    private int        proFacCod;     // TINYINT UNSIGNED (FK GZZ_FACTOR)
    private BigDecimal proFacPorApl;  // DECIMAL(5,2) entre 0 y 100
    private String     proFacEstReg;  // CHAR(1) — estado lógico (A/I/*)

    public ProyectoFactor() {}

    public int getProFacCliCod() { return proFacCliCod; }
    public void setProFacCliCod(int v) { this.proFacCliCod = v; }

    public int getProFacTipCod() { return proFacTipCod; }
    public void setProFacTipCod(int v) { this.proFacTipCod = v; }

    public int getProFacSec() { return proFacSec; }
    public void setProFacSec(int v) { this.proFacSec = v; }

    public int getProFacCod() { return proFacCod; }
    public void setProFacCod(int v) { this.proFacCod = v; }

    public BigDecimal getProFacPorApl() { return proFacPorApl; }
    public void setProFacPorApl(BigDecimal v) { this.proFacPorApl = v; }

    public String getProFacEstReg() { return proFacEstReg; }
    public void setProFacEstReg(String v) { this.proFacEstReg = v; }

    @Override
    public String toString() {
        return "Pro[" + proFacCliCod + "|" + proFacTipCod + "|" + proFacSec + "] Fac=" + proFacCod;
    }
}
