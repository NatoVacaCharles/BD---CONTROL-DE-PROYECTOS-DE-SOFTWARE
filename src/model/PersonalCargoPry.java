package model;

/**
 * Modelo para P3T_PERSONAL_CARGO_PRY.
 * PK compuesta: PerCarPerCod + PerCarCodCar
 * FK: PerCarPerCod → P3M_PERSONAL
 *     PerCarCodCar → GZZ_CARGO_PROYECTO
 * Registra qué cargos de proyecto puede desempeñar cada personal.
 */
public class PersonalCargoPry {
    private int perCarPerCod;   // INT UNSIGNED (FK P3M_PERSONAL)
    private int perCarCodCar;   // TINYINT UNSIGNED (FK GZZ_CARGO_PROYECTO)
    private String perCarEstReg; // CHAR(1) — estado lógico (A/I/*)

    public PersonalCargoPry() {}

    public PersonalCargoPry(int perCarPerCod, int perCarCodCar, String perCarEstReg) {
        this.perCarPerCod = perCarPerCod;
        this.perCarCodCar = perCarCodCar;
        this.perCarEstReg = perCarEstReg;
    }

    public int getPerCarPerCod() { return perCarPerCod; }
    public void setPerCarPerCod(int v) { this.perCarPerCod = v; }

    public int getPerCarCodCar() { return perCarCodCar; }
    public void setPerCarCodCar(int v) { this.perCarCodCar = v; }

    public String getPerCarEstReg() { return perCarEstReg; }
    public void setPerCarEstReg(String v) { this.perCarEstReg = v; }

    @Override
    public String toString() { return perCarPerCod + "|" + perCarCodCar; }
}
