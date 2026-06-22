package model;

import java.time.LocalDate;

/**
 * Modelo para P2H_PROYECTO_ESTADO.
 * PK compuesta: HisProCliCod + HisProTipCod + HisProSec + HisProSecCam
 * FK: (HisProCliCod, HisProTipCod, HisProSec) → P2M_PROYECTO
 *     HisProEstAnt → GZZ_ESTADO_PROYECTO (estado anterior)
 *     HisProEstNue → GZZ_ESTADO_PROYECTO (estado nuevo)
 *     HisProPerCod → P3M_PERSONAL (quién realizó el cambio)
 * HisProSecCam se calcula automáticamente como MAX()+1 por proyecto.
 * Tabla histórica: no hay UPDATE de datos, solo cambio de estado lógico.
 */
public class ProyectoEstadoHistoria {
    private int       hisProCliCod;  // INT UNSIGNED
    private int       hisProTipCod;  // TINYINT UNSIGNED
    private int       hisProSec;     // TINYINT UNSIGNED
    private int       hisProSecCam;  // SMALLINT UNSIGNED (auto-calculado)
    private int       hisProEstAnt;  // TINYINT UNSIGNED (FK GZZ_ESTADO_PROYECTO)
    private int       hisProEstNue;  // TINYINT UNSIGNED (FK GZZ_ESTADO_PROYECTO)
    private LocalDate hisProFecCam;  // DATE NOT NULL
    private int       hisProPerCod;  // INT UNSIGNED (FK P3M_PERSONAL)
    private String    hisProEstReg;  // CHAR(1) — estado lógico (A/I/*)

    public ProyectoEstadoHistoria() {}

    public int       getHisProCliCod()  { return hisProCliCod; }
    public void      setHisProCliCod(int v) { this.hisProCliCod = v; }

    public int       getHisProTipCod()  { return hisProTipCod; }
    public void      setHisProTipCod(int v) { this.hisProTipCod = v; }

    public int       getHisProSec()     { return hisProSec; }
    public void      setHisProSec(int v) { this.hisProSec = v; }

    public int       getHisProSecCam()  { return hisProSecCam; }
    public void      setHisProSecCam(int v) { this.hisProSecCam = v; }

    public int       getHisProEstAnt()  { return hisProEstAnt; }
    public void      setHisProEstAnt(int v) { this.hisProEstAnt = v; }

    public int       getHisProEstNue()  { return hisProEstNue; }
    public void      setHisProEstNue(int v) { this.hisProEstNue = v; }

    public LocalDate getHisProFecCam()  { return hisProFecCam; }
    public void      setHisProFecCam(LocalDate v) { this.hisProFecCam = v; }

    public int       getHisProPerCod()  { return hisProPerCod; }
    public void      setHisProPerCod(int v) { this.hisProPerCod = v; }

    public String    getHisProEstReg()  { return hisProEstReg; }
    public void      setHisProEstReg(String v) { this.hisProEstReg = v; }

    @Override
    public String toString() {
        return "His[Pro=" + hisProCliCod + "|" + hisProTipCod + "|" + hisProSec
             + " Cam=" + hisProSecCam + "]";
    }
}
