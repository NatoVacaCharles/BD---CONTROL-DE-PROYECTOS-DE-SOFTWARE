package model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Modelo para P3T_PROYECTO_ETAPA.
 * PK compuesta: ProEtaCliCod + ProEtaTipCod + ProEtaSec + ProEtaCod
 * FK: (ProEtaCliCod, ProEtaTipCod, ProEtaSec) → P2M_PROYECTO
 *     ProEtaCod → P3M_ETAPA
 * Registra la asignación y avance de etapas en un proyecto.
 */
public class ProyectoEtapa {
    private int    proEtaCliCod;   // INT UNSIGNED (FK Proyecto)
    private int    proEtaTipCod;   // TINYINT UNSIGNED (FK Proyecto)
    private int    proEtaSec;      // TINYINT UNSIGNED (FK Proyecto)
    private int    proEtaCod;      // SMALLINT UNSIGNED (FK P3M_ETAPA)
    private BigDecimal proEtaTpoEstAju; // DECIMAL(6,2) > 0
    private LocalDate  proEtaFecIni;   // DATE nullable
    private LocalDate  proEtaFecFin;   // DATE nullable
    private String proEtaEstReg;   // CHAR(1) — estado lógico (A/I/*)

    public ProyectoEtapa() {}

    public int getProEtaCliCod() { return proEtaCliCod; }
    public void setProEtaCliCod(int v) { this.proEtaCliCod = v; }

    public int getProEtaTipCod() { return proEtaTipCod; }
    public void setProEtaTipCod(int v) { this.proEtaTipCod = v; }

    public int getProEtaSec() { return proEtaSec; }
    public void setProEtaSec(int v) { this.proEtaSec = v; }

    public int getProEtaCod() { return proEtaCod; }
    public void setProEtaCod(int v) { this.proEtaCod = v; }

    public BigDecimal getProEtaTpoEstAju() { return proEtaTpoEstAju; }
    public void setProEtaTpoEstAju(BigDecimal v) { this.proEtaTpoEstAju = v; }

    public LocalDate getProEtaFecIni() { return proEtaFecIni; }
    public void setProEtaFecIni(LocalDate v) { this.proEtaFecIni = v; }

    public LocalDate getProEtaFecFin() { return proEtaFecFin; }
    public void setProEtaFecFin(LocalDate v) { this.proEtaFecFin = v; }

    public String getProEtaEstReg() { return proEtaEstReg; }
    public void setProEtaEstReg(String v) { this.proEtaEstReg = v; }

    @Override
    public String toString() {
        return "Pro[" + proEtaCliCod + "|" + proEtaTipCod + "|" + proEtaSec + "] Eta=" + proEtaCod;
    }
}
