# 🏗️ Sistema de Mantenimiento de Tablas Referenciales - Control de Proyectos de Software

Sistema de escritorio desarrollado en Java (Swing) para el mantenimiento (CRUD) de las tablas referenciales del proyecto "Control de Proyectos de Software". El sistema implementa una arquitectura orientada a objetos con patrón DAO, interfaz gráfica reutilizable y lógica de control de operaciones mediante banderas (flag).

---

## 📋 Descripción del Proyecto

Este proyecto fue desarrollado como parte de un laboratorio académico que exigía:

- Implementar un programa de mantenimiento para tablas referenciales.
- Control estricto de operaciones mediante una **bandera de actualización** (`CarFlaAct`).
- Protección de campos y bloqueo de botones según la operación.
- Eliminación lógica (`A` = Activo, `I` = Inactivo, `*` = Eliminado).
- Reutilización de código mediante clases genéricas.

El sistema permite gestionar **10 tablas referenciales** del proyecto "Control de Proyectos de Software", con una lógica consistente en todas ellas.

---

## 🗂️ Tablas Referenciales Implementadas

| # | Tabla | Descripción |
|---|-------|-------------|
| 1 | `GZZ_TIPO_CLIENTE` | Tipos de clientes |
| 2 | `GZZ_ESTADO_REGISTRO` | Estados de registro (Activo, Inactivo, Eliminado) |
| 3 | `GZZ_ESTADO_CLIENTE` | Estados de cliente |
| 4 | `GZZ_TIPO_PROYECTO` | Tipos de proyecto |
| 5 | `GZZ_ESTADO_PROYECTO` | Estados de proyecto |
| 6 | `GZZ_CARGO_PERSONAL` | Cargos del personal |
| 7 | `GZZ_CARGO_PROYECTO` | Cargos en proyecto |
| 8 | `GZZ_FACTOR` | Factores de evaluación |
| 9 | `GZZ_ESTADO_DISPONIBILIDAD` | Estados de disponibilidad |
| 10 | `GZZ_TIPO_ESTANDAR` | Tipos de estándar |

---

## 🛠️ Tecnologías Utilizadas

- **Lenguaje:** Java 8+
- **Interfaz Gráfica:** Swing
- **Base de Datos:** MySQL
- **Conector JDBC:** MySQL Connector/J 8.x
- **Patrón de Diseño:** DAO (Data Access Object)
- **IDE Recomendado:** IntelliJ IDEA, Eclipse, NetBeans

---

## ⚙️ Requisitos Previos

1. **Java JDK 8 o superior**  
   [Descargar](https://www.oracle.com/java/technologies/downloads/)

2. **MySQL Server 5.7 o superior**  
   [Descargar](https://dev.mysql.com/downloads/mysql/)

3. **MySQL Connector/J**  
   [Descargar](https://dev.mysql.com/downloads/connector/j/)

4. **IDE (opcional)**  
   IntelliJ IDEA, Eclipse, NetBeans o cualquier editor de texto.

---

## 🚀 Instalación y Configuración

### 1. Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/control-proyectos-sw.git
cd control-proyectos-sw
