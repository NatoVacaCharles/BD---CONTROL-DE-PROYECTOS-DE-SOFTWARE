package model;

public class EstadoDisponibilidad {
    private String codigo; // CHAR(1)
    private String nombre; // VARCHAR(30)
    private String estado; // CHAR(1)

    public EstadoDisponibilidad() {
    }

    public EstadoDisponibilidad(String codigo, String nombre, String estado) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}