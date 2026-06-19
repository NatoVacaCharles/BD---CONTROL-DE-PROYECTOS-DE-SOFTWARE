package model;

public class EstadoRegistro {
    private String codigo; // CHAR(1)
    private String nombre; // VARCHAR(30)

    public EstadoRegistro() {
    }

    public EstadoRegistro(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public String getCodigo() { return codigo; }

    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }
}