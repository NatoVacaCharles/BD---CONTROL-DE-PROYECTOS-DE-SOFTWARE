package model;

public class TipoEstandar {
    private int codigo; // TINYINT UNSIGNED
    private String nombre; // VARCHAR(40)
    private String unidadDefinicion; // VARCHAR(20) -> unidad por defecto
    private String estado; // CHAR(1)

    public TipoEstandar() {
    }

    public TipoEstandar(int codigo, String nombre, String unidadDefinicion, String estado) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.unidadDefinicion = unidadDefinicion;
        this.estado = estado;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUnidadDefinicion() {
        return unidadDefinicion;
    }

    public void setUnidadDefinicion(String unidadDefinicion) {
        this.unidadDefinicion = unidadDefinicion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}