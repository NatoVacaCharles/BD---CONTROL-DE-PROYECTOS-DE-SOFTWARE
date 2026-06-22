package model;

/**
 * Modelo para P3T_EQUIPO_PROYECTO.
 * PK compuesta de 5 partes: EqpProCliCod + EqpProTipCod + EqpProSec + EqpPerCod + EqpCarCod
 * FK: (EqpProCliCod, EqpProTipCod, EqpProSec) → P2M_PROYECTO
 *     (EqpPerCod, EqpCarCod) → P3T_PERSONAL_CARGO_PRY
 * Representa a los miembros que forman el equipo de un proyecto.
 */
public class EquipoProyecto {
    private int eqpProCliCod;  // INT UNSIGNED (FK Proyecto parte 1)
    private int eqpProTipCod;  // TINYINT UNSIGNED (FK Proyecto parte 2)
    private int eqpProSec;     // TINYINT UNSIGNED (FK Proyecto parte 3)
    private int eqpPerCod;     // INT UNSIGNED (FK PersonalCargoPry parte 1)
    private int eqpCarCod;     // TINYINT UNSIGNED (FK PersonalCargoPry parte 2)
    private String eqpEstReg;  // CHAR(1) — estado lógico (A/I/*)

    public EquipoProyecto() {}

    public EquipoProyecto(int eqpProCliCod, int eqpProTipCod, int eqpProSec,
                          int eqpPerCod, int eqpCarCod, String eqpEstReg) {
        this.eqpProCliCod = eqpProCliCod;
        this.eqpProTipCod = eqpProTipCod;
        this.eqpProSec    = eqpProSec;
        this.eqpPerCod    = eqpPerCod;
        this.eqpCarCod    = eqpCarCod;
        this.eqpEstReg    = eqpEstReg;
    }

    public int getEqpProCliCod() { return eqpProCliCod; }
    public void setEqpProCliCod(int v) { this.eqpProCliCod = v; }

    public int getEqpProTipCod() { return eqpProTipCod; }
    public void setEqpProTipCod(int v) { this.eqpProTipCod = v; }

    public int getEqpProSec() { return eqpProSec; }
    public void setEqpProSec(int v) { this.eqpProSec = v; }

    public int getEqpPerCod() { return eqpPerCod; }
    public void setEqpPerCod(int v) { this.eqpPerCod = v; }

    public int getEqpCarCod() { return eqpCarCod; }
    public void setEqpCarCod(int v) { this.eqpCarCod = v; }

    public String getEqpEstReg() { return eqpEstReg; }
    public void setEqpEstReg(String v) { this.eqpEstReg = v; }

    @Override
    public String toString() {
        return "Pro[" + eqpProCliCod + "|" + eqpProTipCod + "|" + eqpProSec
             + "] Per[" + eqpPerCod + "|" + eqpCarCod + "]";
    }
}
