package model;

import java.time.LocalDate;

/**
 * Modelo para P4T_MOVIMIENTO.
 * PK compuesta de 7 columnas:
 *   MovCliCod + MovTipProCod + MovSecPro   → FK (P2M_PROYECTO y P3T_EQUIPO_PROYECTO)
 *   MovPerCod + MovCarCod                  → FK P3T_EQUIPO_PROYECTO (personal+cargo)
 *   MovEtaCod                              → FK P3T_PROYECTO_ETAPA
 *   MovEtaSec                              → secuencia del movimiento en la etapa (AUTO calculada)
 *
 * Campos de datos:
 *   MovFecRegEta  DATE NOT NULL
 *   MovEtaHrsTra  TINYINT 0-12 (horas trabajadas)
 *   MovEtaMinTra  TINYINT (0, 15, 30, 45)
 *   MovEstReg     CHAR(1) — estado lógico (A/I/*)
 */
public class Movimiento {
    private int    movCliCod;     // INT UNSIGNED
    private int    movTipProCod;  // TINYINT UNSIGNED
    private int    movSecPro;     // TINYINT UNSIGNED
    private int    movPerCod;     // INT UNSIGNED
    private int    movCarCod;     // TINYINT UNSIGNED
    private int    movEtaCod;     // SMALLINT UNSIGNED
    private int    movEtaSec;     // SMALLINT UNSIGNED (auto-calculado)
    private LocalDate movFecRegEta; // DATE NOT NULL
    private int    movEtaHrsTra;  // TINYINT (0-12)
    private int    movEtaMinTra;  // TINYINT (0,15,30,45)
    private String movEstReg;     // CHAR(1) — estado lógico (A/I/*)

    public Movimiento() {}

    public int    getMovCliCod()    { return movCliCod; }
    public void   setMovCliCod(int v) { this.movCliCod = v; }

    public int    getMovTipProCod() { return movTipProCod; }
    public void   setMovTipProCod(int v) { this.movTipProCod = v; }

    public int    getMovSecPro()    { return movSecPro; }
    public void   setMovSecPro(int v) { this.movSecPro = v; }

    public int    getMovPerCod()    { return movPerCod; }
    public void   setMovPerCod(int v) { this.movPerCod = v; }

    public int    getMovCarCod()    { return movCarCod; }
    public void   setMovCarCod(int v) { this.movCarCod = v; }

    public int    getMovEtaCod()    { return movEtaCod; }
    public void   setMovEtaCod(int v) { this.movEtaCod = v; }

    public int    getMovEtaSec()    { return movEtaSec; }
    public void   setMovEtaSec(int v) { this.movEtaSec = v; }

    public LocalDate getMovFecRegEta() { return movFecRegEta; }
    public void   setMovFecRegEta(LocalDate v) { this.movFecRegEta = v; }

    public int    getMovEtaHrsTra() { return movEtaHrsTra; }
    public void   setMovEtaHrsTra(int v) { this.movEtaHrsTra = v; }

    public int    getMovEtaMinTra() { return movEtaMinTra; }
    public void   setMovEtaMinTra(int v) { this.movEtaMinTra = v; }

    public String getMovEstReg()    { return movEstReg; }
    public void   setMovEstReg(String v) { this.movEstReg = v; }

    @Override
    public String toString() {
        return "Mov[Pro=" + movCliCod + "|" + movTipProCod + "|" + movSecPro
             + " Per=" + movPerCod + "|" + movCarCod
             + " Eta=" + movEtaCod + " Sec=" + movEtaSec + "]";
    }
}
