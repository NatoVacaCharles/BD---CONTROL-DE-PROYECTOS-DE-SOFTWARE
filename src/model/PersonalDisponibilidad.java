package model;

import java.time.LocalDate;

/**
 * Modelo para P3T_PERSONAL_DISPONIBILIDAD.
 * PK compuesta: PerDisPerCod + PerDisFecDes
 * FK: PerDisPerCod → P3M_PERSONAL
 *     PerDisEstDis → GZZ_ESTADO_DISPONIBILIDAD
 * Registra los períodos de disponibilidad del personal.
 */
public class PersonalDisponibilidad {
    private int    perDisPerCod;   // INT UNSIGNED (FK P3M_PERSONAL)
    private LocalDate perDisFecDes; // DATE NOT NULL (parte de PK)
    private LocalDate perDisFecHas; // DATE nullable ("hasta")
    private String perDisEstDis;   // CHAR(1) (FK GZZ_ESTADO_DISPONIBILIDAD)
    private String perDisEstReg;   // CHAR(1) — estado lógico (A/I/*)

    public PersonalDisponibilidad() {}

    public int getPerDisPerCod() { return perDisPerCod; }
    public void setPerDisPerCod(int v) { this.perDisPerCod = v; }

    public LocalDate getPerDisFecDes() { return perDisFecDes; }
    public void setPerDisFecDes(LocalDate v) { this.perDisFecDes = v; }

    public LocalDate getPerDisFecHas() { return perDisFecHas; }
    public void setPerDisFecHas(LocalDate v) { this.perDisFecHas = v; }

    public String getPerDisEstDis() { return perDisEstDis; }
    public void setPerDisEstDis(String v) { this.perDisEstDis = v; }

    public String getPerDisEstReg() { return perDisEstReg; }
    public void setPerDisEstReg(String v) { this.perDisEstReg = v; }

    @Override
    public String toString() {
        return perDisPerCod + " desde " + perDisFecDes;
    }
}
