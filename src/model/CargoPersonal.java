package model;

public class CargoPersonal {
    private int codigo; // SMALLINT UNSIGNED (usamos int)
    private String nombre; // VARCHAR(50)
    private String estado; // CHAR(1)

    public CargoPersonal() {
    }

    public CargoPersonal(int codigo, String nombre, String estado) {
        this.codigo = codigo;
        this.nombre = nombre;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}