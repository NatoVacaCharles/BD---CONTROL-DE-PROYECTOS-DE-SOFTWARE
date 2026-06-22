package model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Modelo para la tabla P2M_PROYECTO.
 * PK compuesta: ProCliCod + ProTipProCod + ProSecPro
 * FK: ProCliCod -> P1M_CLIENTE
 *     ProTipProCod -> GZZ_TIPO_PROYECTO
 *     ProEstPro -> GZZ_ESTADO_PROYECTO
 */
public class Proyecto {
    private int proCliCod;          // INT UNSIGNED (FK P1M_CLIENTE)
    private int proTipProCod;       // TINYINT UNSIGNED (FK GZZ_TIPO_PROYECTO)
    private int proSecPro;          // TINYINT UNSIGNED (parte de la PK compuesta)
    private LocalDate proFecCon;    // DATE NOT NULL
    private LocalDate proFecPac;    // DATE NOT NULL
    private LocalDate proFecIni;    // DATE nullable
    private LocalDate proFecEnt;    // DATE nullable
    private LocalDate proFecCie;    // DATE nullable
    private BigDecimal proMonProCos;   // DECIMAL(12,2) nullable
    private BigDecimal proMonProGas;   // DECIMAL(12,2) nullable
    private BigDecimal proMonProUti;   // DECIMAL(12,2) nullable
    private BigDecimal proMonPro;      // DECIMAL(12,2) NOT NULL
    private BigDecimal proMonProCosRea; // nullable
    private BigDecimal proMonProGasRea; // nullable
    private BigDecimal proMonProUtiRea; // nullable
    private BigDecimal proMonProRea;    // nullable
    private int proEstPro;          // TINYINT UNSIGNED (FK GZZ_ESTADO_PROYECTO)
    private String proEstReg;       // CHAR(1) — estado lógico (A/I/*)

    public Proyecto() {}

    // Getters y Setters
    public int getProCliCod() { return proCliCod; }
    public void setProCliCod(int v) { this.proCliCod = v; }

    public int getProTipProCod() { return proTipProCod; }
    public void setProTipProCod(int v) { this.proTipProCod = v; }

    public int getProSecPro() { return proSecPro; }
    public void setProSecPro(int v) { this.proSecPro = v; }

    public LocalDate getProFecCon() { return proFecCon; }
    public void setProFecCon(LocalDate v) { this.proFecCon = v; }

    public LocalDate getProFecPac() { return proFecPac; }
    public void setProFecPac(LocalDate v) { this.proFecPac = v; }

    public LocalDate getProFecIni() { return proFecIni; }
    public void setProFecIni(LocalDate v) { this.proFecIni = v; }

    public LocalDate getProFecEnt() { return proFecEnt; }
    public void setProFecEnt(LocalDate v) { this.proFecEnt = v; }

    public LocalDate getProFecCie() { return proFecCie; }
    public void setProFecCie(LocalDate v) { this.proFecCie = v; }

    public BigDecimal getProMonProCos() { return proMonProCos; }
    public void setProMonProCos(BigDecimal v) { this.proMonProCos = v; }

    public BigDecimal getProMonProGas() { return proMonProGas; }
    public void setProMonProGas(BigDecimal v) { this.proMonProGas = v; }

    public BigDecimal getProMonProUti() { return proMonProUti; }
    public void setProMonProUti(BigDecimal v) { this.proMonProUti = v; }

    public BigDecimal getProMonPro() { return proMonPro; }
    public void setProMonPro(BigDecimal v) { this.proMonPro = v; }

    public BigDecimal getProMonProCosRea() { return proMonProCosRea; }
    public void setProMonProCosRea(BigDecimal v) { this.proMonProCosRea = v; }

    public BigDecimal getProMonProGasRea() { return proMonProGasRea; }
    public void setProMonProGasRea(BigDecimal v) { this.proMonProGasRea = v; }

    public BigDecimal getProMonProUtiRea() { return proMonProUtiRea; }
    public void setProMonProUtiRea(BigDecimal v) { this.proMonProUtiRea = v; }

    public BigDecimal getProMonProRea() { return proMonProRea; }
    public void setProMonProRea(BigDecimal v) { this.proMonProRea = v; }

    public int getProEstPro() { return proEstPro; }
    public void setProEstPro(int v) { this.proEstPro = v; }

    public String getProEstReg() { return proEstReg; }
    public void setProEstReg(String v) { this.proEstReg = v; }

    @Override
    public String toString() {
        return "Proyecto[cli=" + proCliCod + ", tip=" + proTipProCod + ", sec=" + proSecPro + "]";
    }
}
