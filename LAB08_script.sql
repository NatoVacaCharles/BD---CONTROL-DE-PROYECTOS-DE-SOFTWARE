-- ============================================================================
-- LABORATORIO 08 — Recuperación de Datos y Procedimientos Almacenados
-- Base de datos: control_proyectos_sw
-- Sistema de Control de Proyectos de Software
-- ============================================================================

USE control_proyectos_sw;


-- ============================================================================
-- 1. PREPARACIÓN DEL AMBIENTE DE TRABAJO
-- ============================================================================

-- 1.1 Verificación de la base de datos previamente cargada
SHOW TABLES;
SELECT DATABASE();


-- ----------------------------------------------------------------------------
-- 1.2 Carga de datos de prueba
-- ----------------------------------------------------------------------------

INSERT INTO GZZ_CARGO_PERSONAL (CarPerCod, CarPerNom, CarPerEstReg) VALUES
(1,'Gerente de Proyectos','A'),(2,'Arquitecto de Software','A'),
(3,'Desarrollador Senior','A'),(4,'Desarrollador Junior','A'),(5,'Administrador de BD','A');

INSERT INTO GZZ_FACTOR (FacCod, FacNom, FacPorBas, FacEstReg) VALUES
(1,'Contingencia',10.00,'A'),(2,'Gastos Administrativos',15.00,'A'),(3,'Impuestos (IGV)',18.00,'A');

INSERT INTO GZZ_TIPO_ESTANDAR (TipEstCod, TipEstNom, TipEstUniDef, TipEstEstReg) VALUES
(1,'Métrica de Código','KLOC','A'),(2,'Cobertura y Calidad','%','A'),(3,'Rendimiento del Sistema','ms','A');

INSERT INTO P1M_CLIENTE (CliTipCod, CliNom, CliFecIng, CliFecCes, CliFecUltProCer, CliEstCli, CliEstReg) VALUES
(4,'Corporación Minera INCA S.A.C.','2022-01-15',NULL,'2024-06-10','1','A'),
(5,'Universidad Nacional del Sur','2021-03-20',NULL,NULL,'3','A'),
(2,'Carlos Alberto Paredes Quispe','2023-05-10',NULL,NULL,'4','A'),
(4,'Gobierno Regional de Arequipa','2020-07-01',NULL,'2023-11-20','1','A'),
(6,'Ana Lucía Torres Mendoza','2024-01-08','2024-08-30',NULL,'2','A');

INSERT INTO P3M_PERSONAL (PerCodCar, PerNom, PerCosHorCar, PerFecIng, PerEstReg) VALUES
(1,'Javier Mamani Ccoa',45.00,'2019-01-10','A'),
(2,'Roxana Flores Quispe',40.00,'2020-03-15','A'),
(3,'Diego Lazo Puma',30.00,'2021-06-01','A'),
(4,'Valeria Cáceres Luna',28.00,'2022-02-20','A'),
(5,'Omar Huanca Tito',35.00,'2020-09-05','A');

INSERT INTO P3M_ETAPA (EtaCod, EtaNom, EtaEstReg) VALUES
(1,'Análisis de Requisitos','A'),(2,'Diseño del Sistema','A'),
(3,'Implementación','A'),(4,'Pruebas y Validación','A'),(5,'Despliegue y Cierre','A');

INSERT INTO P3M_ACTIVIDAD (ActEtaCod, ActCod, ActNom, ActTpoEst, ActEstReg) VALUES
(1,1,'Relevamiento de Información',8.00,'A'),
(1,2,'Especificación de Requerimientos',12.00,'A'),
(2,1,'Diseño de Arquitectura',16.00,'A'),
(2,2,'Diseño de Base de Datos',10.00,'A'),
(3,1,'Desarrollo de Módulos',40.00,'A'),
(3,2,'Integración de Componentes',20.00,'A'),
(4,1,'Pruebas Unitarias',16.00,'A'),
(4,2,'Pruebas de Integración',12.00,'A'),
(5,1,'Instalación en Producción',8.00,'A'),
(5,2,'Capacitación de Usuarios',6.00,'A');

INSERT INTO P3M_ESTANDAR (EstActEtaCod,EstActActCod,EstCod,EstTipCod,EstNom,EstValNum,EstValTxt,EstUni,EstEstReg) VALUES
(1,2,1,2,'Porcentaje Mínimo de Requisitos Cubiertos',95.00,NULL,'%','A'),
(3,1,1,1,'KLOC Entregadas por Sprint',2.50,NULL,'KLOC','A'),
(4,1,1,2,'Cobertura Mínima de Pruebas Unitarias',80.00,NULL,'%','A'),
(5,1,1,3,'Tiempo Máximo de Respuesta Aceptable',500.00,NULL,'ms','A');

INSERT INTO P3T_PERSONAL_CARGO_PRY (PerCarPerCod, PerCarCodCar, PerCarEstReg) VALUES
(1,1,'A'),(1,2,'A'),(2,2,'A'),(2,5,'A'),(3,3,'A'),(4,3,'A'),(5,4,'A');

INSERT INTO P2M_PROYECTO
  (ProCliCod,ProTipProCod,ProSecPro,ProFecCon,ProFecPac,ProFecIni,ProFecEnt,ProFecCie,
   ProMonPro,ProMonProCos,ProMonProGas,ProMonProUti,ProEstPro,ProEstReg) VALUES
(1,1,1,'2024-01-15','2024-02-01','2024-02-05',NULL,NULL,85000.00,60000.00,12000.00,13000.00,4,'A'),
(2,1,1,'2023-06-10','2023-07-01','2023-07-15','2024-01-10','2024-01-20',45000.00,30000.00,7000.00,8000.00,5,'A'),
(3,1,1,'2025-01-10','2025-02-15',NULL,NULL,NULL,25000.00,NULL,NULL,NULL,2,'A'),
(4,5,1,'2025-03-01','2025-04-01','2025-04-10',NULL,NULL,120000.00,90000.00,18000.00,12000.00,3,'A');

INSERT INTO P2H_PROYECTO_ESTADO
  (HisProCliCod,HisProTipCod,HisProSec,HisProSecCam,HisProEstAnt,HisProEstNue,HisProFecCam,HisProPerCod,HisProEstReg) VALUES
(1,1,1,1,1,2,'2024-01-15',1,'A'),
(1,1,1,2,2,3,'2024-01-20',1,'A'),
(1,1,1,3,3,4,'2024-02-05',1,'A'),
(2,1,1,1,1,2,'2023-06-10',1,'A'),
(2,1,1,2,2,3,'2023-06-15',1,'A'),
(2,1,1,3,3,4,'2023-07-15',1,'A'),
(2,1,1,4,4,5,'2024-01-10',1,'A');

INSERT INTO P2T_PROYECTO_FACTOR
  (ProFacCliCod,ProFacTipCod,ProFacSec,ProFacCod,ProFacPorApl,ProFacEstReg) VALUES
(1,1,1,1,10.00,'A'),(1,1,1,2,15.00,'A'),(2,1,1,3,18.00,'A'),(4,5,1,1,10.00,'A');

INSERT INTO P3T_EQUIPO_PROYECTO
  (EqpProCliCod,EqpProTipCod,EqpProSec,EqpPerCod,EqpCarCod,EqpEstReg) VALUES
(1,1,1,1,1,'A'),(1,1,1,2,2,'A'),(1,1,1,3,3,'A'),(1,1,1,5,4,'A'),
(2,1,1,1,1,'A'),(2,1,1,4,3,'A'),
(4,5,1,1,1,'A'),(4,5,1,2,5,'A');

INSERT INTO P3T_PERSONAL_DISPONIBILIDAD
  (PerDisPerCod,PerDisFecDes,PerDisFecHas,PerDisEstDis,PerDisEstReg) VALUES
(1,'2024-02-05',NULL,'2','A'),
(2,'2024-02-05',NULL,'2','A'),
(3,'2024-01-15','2024-02-29','4','A'),
(3,'2024-03-01',NULL,'2','A'),
(4,'2024-01-01','2024-01-31','1','A'),
(4,'2024-02-01',NULL,'2','A'),
(5,'2024-02-05',NULL,'2','A');

INSERT INTO P3T_PROYECTO_ETAPA
  (ProEtaCliCod,ProEtaTipCod,ProEtaSec,ProEtaCod,ProEtaTpoEstAju,ProEtaFecIni,ProEtaFecFin,ProEtaEstReg) VALUES
(1,1,1,1,20.00,'2024-02-05','2024-02-20','A'),
(1,1,1,2,26.00,'2024-02-21','2024-03-10','A'),
(1,1,1,3,60.00,'2024-03-11',NULL,'A'),
(2,1,1,1,20.00,'2023-07-15','2023-07-31','A'),
(2,1,1,3,80.00,'2023-08-01','2023-12-31','A'),
(2,1,1,4,28.00,'2024-01-02','2024-01-10','A'),
(4,5,1,1,24.00,'2025-04-10','2025-04-30','A'),
(4,5,1,2,32.00,'2025-05-01',NULL,'A');

INSERT INTO P4T_MOVIMIENTO
  (MovCliCod,MovTipProCod,MovSecPro,MovPerCod,MovCarCod,MovEtaCod,MovEtaSec,
   MovFecRegEta,MovEtaHrsTra,MovEtaMinTra,MovEstReg) VALUES
(1,1,1,1,1,1,1,'2024-02-06',4,30,'A'),
(1,1,1,1,1,1,2,'2024-02-07',3,0,'A'),
(1,1,1,2,2,1,1,'2024-02-06',6,0,'A'),
(1,1,1,2,2,1,2,'2024-02-07',6,0,'A'),
(1,1,1,5,4,2,1,'2024-03-01',5,30,'A'),
(1,1,1,2,2,2,1,'2024-02-25',4,15,'A'),
(1,1,1,3,3,3,1,'2024-03-12',8,0,'A'),
(1,1,1,3,3,3,2,'2024-03-13',7,45,'A'),
(2,1,1,1,1,1,1,'2023-07-15',6,0,'A'),
(2,1,1,4,3,3,1,'2023-08-05',8,0,'A'),
(2,1,1,4,3,3,2,'2023-08-06',8,0,'A'),
(2,1,1,4,3,3,3,'2023-09-10',7,0,'A'),
(2,1,1,1,1,4,1,'2024-01-02',4,0,'A');

COMMIT;


-- ----------------------------------------------------------------------------
-- 1.3 Verificación de registros almacenados
-- ----------------------------------------------------------------------------

-- Figura 3 — Conteo de registros por tabla
SELECT 'GZZ_CARGO_PERSONAL'           AS Tabla, COUNT(*) AS Registros FROM GZZ_CARGO_PERSONAL  UNION ALL
SELECT 'GZZ_FACTOR',                             COUNT(*)             FROM GZZ_FACTOR           UNION ALL
SELECT 'GZZ_TIPO_ESTANDAR',                      COUNT(*)             FROM GZZ_TIPO_ESTANDAR    UNION ALL
SELECT 'P1M_CLIENTE',                            COUNT(*)             FROM P1M_CLIENTE           UNION ALL
SELECT 'P3M_PERSONAL',                           COUNT(*)             FROM P3M_PERSONAL          UNION ALL
SELECT 'P3M_ETAPA',                              COUNT(*)             FROM P3M_ETAPA             UNION ALL
SELECT 'P3M_ACTIVIDAD',                          COUNT(*)             FROM P3M_ACTIVIDAD         UNION ALL
SELECT 'P3M_ESTANDAR',                           COUNT(*)             FROM P3M_ESTANDAR          UNION ALL
SELECT 'P3T_PERSONAL_CARGO_PRY',                 COUNT(*)             FROM P3T_PERSONAL_CARGO_PRY UNION ALL
SELECT 'P2M_PROYECTO',                           COUNT(*)             FROM P2M_PROYECTO          UNION ALL
SELECT 'P2H_PROYECTO_ESTADO',                    COUNT(*)             FROM P2H_PROYECTO_ESTADO   UNION ALL
SELECT 'P2T_PROYECTO_FACTOR',                    COUNT(*)             FROM P2T_PROYECTO_FACTOR   UNION ALL
SELECT 'P3T_EQUIPO_PROYECTO',                    COUNT(*)             FROM P3T_EQUIPO_PROYECTO   UNION ALL
SELECT 'P3T_PERSONAL_DISPONIBILIDAD',            COUNT(*)             FROM P3T_PERSONAL_DISPONIBILIDAD UNION ALL
SELECT 'P3T_PROYECTO_ETAPA',                     COUNT(*)             FROM P3T_PROYECTO_ETAPA    UNION ALL
SELECT 'P4T_MOVIMIENTO',                         COUNT(*)             FROM P4T_MOVIMIENTO;

-- Figura 4 — Verificación de registros en tablas maestras y transaccionales
SELECT 'PERSONAL'  AS Fuente, PerCod   AS ID, PerNom    AS Nombre, PerFecIng    AS Fecha FROM P3M_PERSONAL
UNION ALL
SELECT 'PROYECTO',           ProCliCod,       ProEstPro,          ProFecCon            FROM P2M_PROYECTO
UNION ALL
SELECT 'MOVIMIENTO',         MovCliCod,       MovPerCod,          MovFecRegEta         FROM P4T_MOVIMIENTO
LIMIT 20;


-- ============================================================================
-- 2. CONSULTAS SQL DE RECUPERACIÓN
-- ============================================================================

-- ----------------------------------------------------------------------------
-- 2.1 Consulta simple sobre una tabla
-- Figura 5/6 — Clientes registrados con tipo y estado descriptivos
-- ----------------------------------------------------------------------------
SELECT
    c.CliCod                AS Codigo,
    tc.TipCliNom            AS TipoCliente,
    c.CliNom                AS NombreCliente,
    c.CliFecIng             AS FechaIngreso,
    c.CliFecCes             AS FechaCese,
    ec.EstCliNom            AS EstadoCliente,
    er.EstRegNom            AS EstadoRegistro
FROM P1M_CLIENTE c
INNER JOIN GZZ_TIPO_CLIENTE     tc ON c.CliTipCod  = tc.TipCliCod
INNER JOIN GZZ_ESTADO_CLIENTE   ec ON c.CliEstCli  = ec.EstCliCod
INNER JOIN GZZ_ESTADO_REGISTRO  er ON c.CliEstReg  = er.EstRegCod
ORDER BY c.CliFecIng DESC;


-- ----------------------------------------------------------------------------
-- 2.2 Consulta sobre tabla maestra con funciones agregadas
-- Figura 7/8 — Resumen financiero de proyectos agrupado por estado
-- ----------------------------------------------------------------------------
SELECT
    ep.EstProNom                        AS EstadoProyecto,
    COUNT(*)                            AS TotalProyectos,
    SUM(p.ProMonPro)                    AS MontoTotalEstimado,
    ROUND(AVG(p.ProMonPro), 2)          AS MontoPromedio,
    MIN(p.ProMonPro)                    AS MontoMinimo,
    MAX(p.ProMonPro)                    AS MontoMaximo
FROM P2M_PROYECTO p
INNER JOIN GZZ_ESTADO_PROYECTO ep ON p.ProEstPro = ep.EstProCod
WHERE p.ProEstReg = 'A'
GROUP BY ep.EstProCod, ep.EstProNom
ORDER BY ep.EstProCod;


-- ----------------------------------------------------------------------------
-- 2.3 Consulta multitabla (JOIN)
-- Figura 9/10 — Proyectos con cliente y tipo de proyecto
-- ----------------------------------------------------------------------------
SELECT
    cli.CliNom          AS Cliente,
    cli.CliFecIng       AS ClienteDesde,
    tp.TipProNom        AS TipoProyecto,
    ep.EstProNom        AS EstadoProyecto,
    p.ProFecCon         AS FechaContrato,
    p.ProFecPac         AS FechaEntregaPactada,
    p.ProMonPro         AS MontoEstimado
FROM P2M_PROYECTO p
INNER JOIN P1M_CLIENTE         cli ON p.ProCliCod    = cli.CliCod
INNER JOIN GZZ_TIPO_PROYECTO    tp ON p.ProTipProCod = tp.TipProCod
INNER JOIN GZZ_ESTADO_PROYECTO  ep ON p.ProEstPro    = ep.EstProCod
WHERE p.ProEstReg = 'A'
ORDER BY p.ProMonPro DESC;


-- ----------------------------------------------------------------------------
-- 2.4 Consultas complementarias (valor agregado)
-- ----------------------------------------------------------------------------

-- Figura 11/12 — Filtrado y ordenamiento: personal activo con antigüedad >= 3 años
SELECT
    p.PerNom                                        AS NombreCompleto,
    cp.CarPerNom                                    AS CargoEmpresa,
    p.PerCosHorCar                                  AS TarifaHora,
    p.PerFecIng                                     AS FechaIngreso,
    TIMESTAMPDIFF(YEAR, p.PerFecIng, CURDATE())     AS AntiguedadAnios
FROM P3M_PERSONAL p
INNER JOIN GZZ_CARGO_PERSONAL cp ON p.PerCodCar = cp.CarPerCod
WHERE p.PerEstReg = 'A'
  AND TIMESTAMPDIFF(YEAR, p.PerFecIng, CURDATE()) >= 3
ORDER BY p.PerCosHorCar DESC;

-- Figura 13/14 — Funciones de agregación: estadísticas de horas por persona
SELECT
    per.PerNom                                          AS Personal,
    cp.CarProNom                                        AS RolEnProyecto,
    COUNT(m.MovEtaSec)                                  AS TotalRegistros,
    SUM(m.MovEtaHrsTra)                                 AS TotalHoras,
    ROUND(AVG(m.MovEtaHrsTra), 2)                       AS PromedioHorasPorDia,
    MAX(m.MovEtaHrsTra)                                 AS MaxHorasEnUnDia,
    ROUND(
        SUM(m.MovEtaHrsTra + m.MovEtaMinTra / 60.0)
        * per.PerCosHorCar, 2)                          AS CostoManoObra
FROM P4T_MOVIMIENTO m
INNER JOIN P3M_PERSONAL       per ON m.MovPerCod = per.PerCod
INNER JOIN GZZ_CARGO_PROYECTO  cp ON m.MovCarCod = cp.CarProCod
WHERE m.MovEstReg = 'A'
GROUP BY per.PerNom, per.PerCosHorCar, cp.CarProNom
ORDER BY CostoManoObra DESC;


-- ----------------------------------------------------------------------------
-- 2.5 Validación de rendimiento de consultas (valor agregado)
-- Figura 15/16 — Plan de ejecución de la consulta multitabla
-- ----------------------------------------------------------------------------
EXPLAIN
SELECT
    cli.CliNom              AS Cliente,
    tp.TipProNom            AS TipoProyecto,
    ep.EstProNom            AS EstadoProyecto,
    p.ProFecCon             AS FechaContrato,
    p.ProMonPro             AS MontoEstimado
FROM P2M_PROYECTO p
INNER JOIN P1M_CLIENTE         cli ON p.ProCliCod    = cli.CliCod
INNER JOIN GZZ_TIPO_PROYECTO    tp ON p.ProTipProCod = tp.TipProCod
INNER JOIN GZZ_ESTADO_PROYECTO  ep ON p.ProEstPro    = ep.EstProCod
WHERE p.ProEstReg = 'A'
ORDER BY p.ProMonPro DESC;


-- ============================================================================
-- 3. IMPLEMENTACIÓN DE VISTAS
-- ============================================================================

-- ----------------------------------------------------------------------------
-- 3.1 Vista sobre una tabla maestra
-- Figura 17/18 — VW_CLIENTES
-- ----------------------------------------------------------------------------
CREATE OR REPLACE VIEW VW_CLIENTES AS
SELECT
    c.CliCod                AS Codigo,
    tc.TipCliNom            AS TipoCliente,
    c.CliNom                AS NombreCliente,
    c.CliFecIng             AS FechaIngreso,
    c.CliFecCes             AS FechaCese,
    c.CliFecUltProCer       AS UltimoProyectoCerrado,
    ec.EstCliNom            AS EstadoCliente,
    er.EstRegNom            AS EstadoRegistro
FROM P1M_CLIENTE c
INNER JOIN GZZ_TIPO_CLIENTE     tc ON c.CliTipCod = tc.TipCliCod
INNER JOIN GZZ_ESTADO_CLIENTE   ec ON c.CliEstCli = ec.EstCliCod
INNER JOIN GZZ_ESTADO_REGISTRO  er ON c.CliEstReg = er.EstRegCod;

SELECT * FROM VW_CLIENTES;


-- ----------------------------------------------------------------------------
-- 3.2 Vista sobre dos tablas relacionadas
-- Figura 19/20 — VW_PROYECTO_CLIENTE
-- ----------------------------------------------------------------------------
CREATE OR REPLACE VIEW VW_PROYECTO_CLIENTE AS
SELECT
    p.ProCliCod             AS CodigoCliente,
    p.ProTipProCod          AS CodigoTipo,
    p.ProSecPro             AS Secuencia,
    cli.CliNom              AS NombreCliente,
    tp.TipProNom            AS TipoProyecto,
    ep.EstProNom            AS EstadoProyecto,
    p.ProFecCon             AS FechaContrato,
    p.ProFecPac             AS FechaEntregaPactada,
    p.ProFecIni             AS FechaInicio,
    p.ProFecEnt             AS FechaEntregaReal,
    p.ProMonPro             AS MontoEstimado,
    CASE
        WHEN p.ProMonProRea IS NULL THEN 0.00
        ELSE p.ProMonProRea
    END                     AS MontoEjecutado
FROM P2M_PROYECTO p
INNER JOIN P1M_CLIENTE         cli ON p.ProCliCod    = cli.CliCod
INNER JOIN GZZ_TIPO_PROYECTO    tp ON p.ProTipProCod = tp.TipProCod
INNER JOIN GZZ_ESTADO_PROYECTO  ep ON p.ProEstPro    = ep.EstProCod
WHERE p.ProEstReg = 'A';

SELECT * FROM VW_PROYECTO_CLIENTE;


-- ----------------------------------------------------------------------------
-- 3.3 Vista integral del sistema
-- Figura 21/22 — VW_PROYECTO_GENERAL
-- ----------------------------------------------------------------------------
CREATE OR REPLACE VIEW VW_PROYECTO_GENERAL AS
SELECT
    p.ProCliCod             AS CliCod,
    p.ProTipProCod          AS TipCod,
    p.ProSecPro             AS SecuenciaPro,
    cli.CliNom              AS NombreCliente,
    tc.TipCliNom            AS TipoCliente,
    cli.CliFecIng           AS ClienteDesde,
    tp.TipProNom            AS TipoProyecto,
    ep.EstProNom            AS EstadoProyecto,
    p.ProFecCon             AS FechaContrato,
    p.ProFecPac             AS FechaEntregaPactada,
    p.ProFecIni             AS FechaInicio,
    p.ProFecEnt             AS FechaEntregaReal,
    p.ProMonPro             AS MontoEstimado,
    COALESCE(p.ProMonProRea, 0.00)
                            AS MontoEjecutado,
    CASE p.ProEstPro
        WHEN 1 THEN '0%   - Prospecto'
        WHEN 2 THEN '20%  - Con Contrato'
        WHEN 3 THEN '40%  - Planificación'
        WHEN 4 THEN '70%  - En Desarrollo'
        WHEN 5 THEN '100% - Cerrado'
        ELSE 'N/D'
    END                     AS AvanceEstimado
FROM P2M_PROYECTO p
INNER JOIN P1M_CLIENTE         cli ON p.ProCliCod    = cli.CliCod
INNER JOIN GZZ_TIPO_CLIENTE     tc ON cli.CliTipCod  = tc.TipCliCod
INNER JOIN GZZ_TIPO_PROYECTO    tp ON p.ProTipProCod = tp.TipProCod
INNER JOIN GZZ_ESTADO_PROYECTO  ep ON p.ProEstPro    = ep.EstProCod
WHERE p.ProEstReg = 'A';

SELECT * FROM VW_PROYECTO_GENERAL
ORDER BY MontoEstimado DESC;


-- ----------------------------------------------------------------------------
-- 3.4 Administración de vistas
-- ----------------------------------------------------------------------------

-- Figura 23 — Listado de vistas creadas
SHOW FULL TABLES IN control_proyectos_sw
WHERE TABLE_TYPE = 'VIEW';

-- Figura 24 — Estructura interna de una vista
SHOW CREATE VIEW VW_PROYECTO_GENERAL;


-- ============================================================================
-- 4. PREPARACIÓN DE DATOS PARA TRIGGERS
-- ============================================================================

-- ----------------------------------------------------------------------------
-- 4.1 Identificación del atributo de estado
-- ----------------------------------------------------------------------------

-- Figura 25 — Estructura de la tabla seleccionada
DESCRIBE P3M_PERSONAL;

-- Figura 26 — Registros con estados susceptibles de modificación
SELECT
    p.PerCod        AS Codigo,
    p.PerNom        AS Personal,
    p.PerEstReg     AS EstadoActual,
    er.EstRegNom    AS DescripcionEstado
FROM P3M_PERSONAL p
INNER JOIN GZZ_ESTADO_REGISTRO er ON p.PerEstReg = er.EstRegCod
ORDER BY p.PerCod;


-- ----------------------------------------------------------------------------
-- 4.2 Preparación de escenarios de disparo
-- ----------------------------------------------------------------------------

-- Figura 27 — Estado inicial de disponibilidad de Diego Lazo (PerCod=3)
SELECT
    p.PerCod            AS Codigo,
    p.PerNom            AS Personal,
    p.PerEstReg         AS EstadoRegistro,
    pd.PerDisEstDis     AS EstadoDisponibilidad,
    ed.EstDisNom        AS DescripcionDisponibilidad,
    pd.PerDisFecDes     AS FechaDesde,
    pd.PerDisFecHas     AS FechaHasta
FROM P3M_PERSONAL p
INNER JOIN P3T_PERSONAL_DISPONIBILIDAD pd ON p.PerCod        = pd.PerDisPerCod
INNER JOIN GZZ_ESTADO_DISPONIBILIDAD   ed ON pd.PerDisEstDis = ed.EstDisCod
WHERE p.PerCod = 3
ORDER BY pd.PerDisFecDes;

-- Figura 28 — Registro candidato para activar TRG_02 (Licencia -> Asignado)
SELECT
    pd.PerDisPerCod     AS CodigoPersonal,
    p.PerNom            AS Personal,
    pd.PerDisFecDes     AS FechaDesde,
    pd.PerDisEstDis     AS CodigoEstado,
    ed.EstDisNom        AS EstadoActual,
    'Asignado'          AS EstadoQueSeIntentara,
    'TRIGGER bloqueará' AS Resultado
FROM P3T_PERSONAL_DISPONIBILIDAD pd
INNER JOIN P3M_PERSONAL              p ON pd.PerDisPerCod  = p.PerCod
INNER JOIN GZZ_ESTADO_DISPONIBILIDAD ed ON pd.PerDisEstDis = ed.EstDisCod
WHERE pd.PerDisEstDis = '4';


-- ============================================================================
-- 5. IMPLEMENTACIÓN DE TRIGGERS
-- ============================================================================

-- ----------------------------------------------------------------------------
-- 5.1 Trigger simple
-- Figura 29 — TRG_01_VALIDAR_BAJA_PERSONAL
-- Tabla: P3M_PERSONAL | Evento: BEFORE UPDATE
-- Impide eliminar lógicamente (EstReg='*') a un personal Activo
-- sin pasar primero por el estado Inactivo ('I').
-- ----------------------------------------------------------------------------
DELIMITER $$

DROP TRIGGER IF EXISTS TRG_01_VALIDAR_BAJA_PERSONAL$$

CREATE TRIGGER TRG_01_VALIDAR_BAJA_PERSONAL
BEFORE UPDATE ON P3M_PERSONAL
FOR EACH ROW
BEGIN
    IF NEW.PerEstReg = '*' AND OLD.PerEstReg = 'A' THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT =
                'RESTRICCIÓN: No se puede eliminar directamente '
                'a un personal Activo. Debe inactivarse primero '
                '(A -> I) antes de proceder a la baja lógica (I -> *).';
    END IF;
END$$

DELIMITER ;

-- Figura 30 — Estado del registro antes del evento disparador
SELECT
    PerCod      AS Codigo,
    PerNom      AS Personal,
    PerEstReg   AS EstadoActual
FROM P3M_PERSONAL
WHERE PerCod = 3;

-- Figura 31/32 — Evento que dispara y bloquea el trigger
UPDATE P3M_PERSONAL
SET PerEstReg = '*'
WHERE PerCod = 3;


-- ----------------------------------------------------------------------------
-- 5.2 Trigger de mediana complejidad
-- Figura 33 — TRG_02_VALIDAR_TRANSICION_DISPONIBILIDAD
-- Tabla: P3T_PERSONAL_DISPONIBILIDAD | Evento: BEFORE UPDATE
-- Valida transiciones de disponibilidad mediante subconsultas.
-- Regla: Licencia ('4') no puede pasar directamente a Asignado ('2').
-- ----------------------------------------------------------------------------
DELIMITER $$

DROP TRIGGER IF EXISTS TRG_02_VALIDAR_TRANSICION_DISPONIBILIDAD$$

CREATE TRIGGER TRG_02_VALIDAR_TRANSICION_DISPONIBILIDAD
BEFORE UPDATE ON P3T_PERSONAL_DISPONIBILIDAD
FOR EACH ROW
BEGIN
    DECLARE v_est_ant_nom  VARCHAR(30)  DEFAULT 'Desconocido';
    DECLARE v_est_nue_nom  VARCHAR(30)  DEFAULT 'Desconocido';
    DECLARE v_per_nom      VARCHAR(80)  DEFAULT 'Desconocido';
    DECLARE v_mensaje      VARCHAR(255) DEFAULT '';

    IF NEW.PerDisEstDis <> OLD.PerDisEstDis THEN

        SELECT EstDisNom INTO v_est_ant_nom
        FROM GZZ_ESTADO_DISPONIBILIDAD
        WHERE EstDisCod = OLD.PerDisEstDis;

        SELECT EstDisNom INTO v_est_nue_nom
        FROM GZZ_ESTADO_DISPONIBILIDAD
        WHERE EstDisCod = NEW.PerDisEstDis;

        SELECT PerNom INTO v_per_nom
        FROM P3M_PERSONAL
        WHERE PerCod = NEW.PerDisPerCod;

        IF OLD.PerDisEstDis = '4' AND NEW.PerDisEstDis = '2' THEN
            SET v_mensaje = CONCAT(
                'ALERTA RRHH: El personal ', v_per_nom,
                ' no puede pasar de ', v_est_ant_nom,
                ' a ', v_est_nue_nom,
                ' directamente. Debe ser Disponible primero.'
            );
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = v_mensaje;
        END IF;

    END IF;
END$$

DELIMITER ;

-- Verificación del registro de Diego Lazo antes del disparo
SELECT PerDisPerCod, PerDisFecDes, PerDisEstDis
FROM P3T_PERSONAL_DISPONIBILIDAD
WHERE PerDisPerCod = 3;

-- Figura 34/35 — Evento que dispara y bloquea TRG_02
UPDATE P3T_PERSONAL_DISPONIBILIDAD
SET    PerDisEstDis = '2'
WHERE  PerDisPerCod = 3
  AND  PerDisFecDes = '2024-01-15';


-- ----------------------------------------------------------------------------
-- 5.3 Trigger de mayor complejidad
-- Figura 36 — TRG_03_VALIDAR_ESTADO_PROYECTO
-- Tabla: P2M_PROYECTO | Evento: BEFORE UPDATE
-- Valida que el estado no retroceda e invoca al procedimiento
-- PA_00_VALIDAR_AVANCE_PROYECTO para validaciones adicionales.
-- ----------------------------------------------------------------------------

-- Procedimiento auxiliar invocado por el trigger
DELIMITER $$

DROP PROCEDURE IF EXISTS PA_00_VALIDAR_AVANCE_PROYECTO$$

CREATE PROCEDURE PA_00_VALIDAR_AVANCE_PROYECTO(
    IN p_cli_cod  INT UNSIGNED,
    IN p_tip_cod  TINYINT UNSIGNED,
    IN p_sec_pro  TINYINT UNSIGNED,
    IN p_est_nue  TINYINT UNSIGNED
)
BEGIN
    DECLARE v_cli_nom     VARCHAR(80)  DEFAULT 'N/D';
    DECLARE v_tip_nom     VARCHAR(40)  DEFAULT 'N/D';
    DECLARE v_total_eqp   INT          DEFAULT 0;
    DECLARE v_tiene_jefe  INT          DEFAULT 0;
    DECLARE v_etapas_plan INT          DEFAULT 0;
    DECLARE v_mensaje     VARCHAR(255) DEFAULT '';

    SELECT CliNom INTO v_cli_nom
    FROM P1M_CLIENTE WHERE CliCod = p_cli_cod;

    SELECT TipProNom INTO v_tip_nom
    FROM GZZ_TIPO_PROYECTO WHERE TipProCod = p_tip_cod;

    SELECT COUNT(*) INTO v_total_eqp
    FROM P3T_EQUIPO_PROYECTO
    WHERE EqpProCliCod = p_cli_cod
      AND EqpProTipCod = p_tip_cod
      AND EqpProSec    = p_sec_pro
      AND EqpEstReg    = 'A';

    SELECT COUNT(*) INTO v_tiene_jefe
    FROM P3T_EQUIPO_PROYECTO
    WHERE EqpProCliCod = p_cli_cod
      AND EqpProTipCod = p_tip_cod
      AND EqpProSec    = p_sec_pro
      AND EqpCarCod    = 1
      AND EqpEstReg    = 'A';

    SELECT COUNT(*) INTO v_etapas_plan
    FROM P3T_PROYECTO_ETAPA
    WHERE ProEtaCliCod = p_cli_cod
      AND ProEtaTipCod = p_tip_cod
      AND ProEtaSec    = p_sec_pro;

    IF p_est_nue = 4 THEN
        IF v_total_eqp = 0 THEN
            SET v_mensaje = CONCAT(
                'BLOQUEO: El proyecto de ', v_cli_nom,
                ' no puede iniciar Desarrollo. ',
                'No tiene equipo asignado.'
            );
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = v_mensaje;
        END IF;

        IF v_tiene_jefe = 0 THEN
            SET v_mensaje = CONCAT(
                'BLOQUEO: El proyecto de ', v_cli_nom,
                ' no tiene Jefe de Proyecto asignado.'
            );
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = v_mensaje;
        END IF;
    END IF;

    SET @validacion_proyecto = CONCAT(
        'Proyecto: ', v_cli_nom, ' [', v_tip_nom, '] | ',
        'Equipo: ', v_total_eqp, ' miembro(s) | ',
        'Jefe: ', IF(v_tiene_jefe > 0, 'SI', 'NO'), ' | ',
        'Etapas: ', v_etapas_plan
    );
END$$

DELIMITER ;

-- Trigger de mayor complejidad
DELIMITER $$

DROP TRIGGER IF EXISTS TRG_03_VALIDAR_ESTADO_PROYECTO$$

CREATE TRIGGER TRG_03_VALIDAR_ESTADO_PROYECTO
BEFORE UPDATE ON P2M_PROYECTO
FOR EACH ROW
BEGIN
    DECLARE v_est_ant_nom VARCHAR(40)  DEFAULT '';
    DECLARE v_est_nue_nom VARCHAR(40)  DEFAULT '';
    DECLARE v_mensaje     VARCHAR(255) DEFAULT '';

    IF NEW.ProEstPro <> OLD.ProEstPro THEN

        SELECT EstProNom INTO v_est_ant_nom
        FROM GZZ_ESTADO_PROYECTO
        WHERE EstProCod = OLD.ProEstPro;

        SELECT EstProNom INTO v_est_nue_nom
        FROM GZZ_ESTADO_PROYECTO
        WHERE EstProCod = NEW.ProEstPro;

        IF NEW.ProEstPro < OLD.ProEstPro THEN
            SET v_mensaje = CONCAT(
                'ERROR: El proyecto no puede retroceder de estado. ',
                'Estado actual: ', v_est_ant_nom,
                ' - Estado solicitado: ', v_est_nue_nom
            );
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = v_mensaje;
        END IF;

        CALL PA_00_VALIDAR_AVANCE_PROYECTO(
            NEW.ProCliCod,
            NEW.ProTipProCod,
            NEW.ProSecPro,
            NEW.ProEstPro
        );

    END IF;
END$$

DELIMITER ;

-- Figura 37/38 — Evento que dispara TRG_03 (Proyecto 3 sin equipo asignado)
UPDATE P2M_PROYECTO
SET    ProEstPro = 4
WHERE  ProCliCod    = 3
  AND  ProTipProCod = 1
  AND  ProSecPro    = 1;

SELECT @validacion_proyecto AS ResumenValidacion;


-- ----------------------------------------------------------------------------
-- 5.4 / 5.5 Validación y administración de triggers
-- ----------------------------------------------------------------------------

-- Figura 39 — Verificación de ausencia de INSERT, UPDATE, DELETE
SELECT
    TRIGGER_NAME                AS NombreTrigger,
    EVENT_MANIPULATION          AS Evento,
    EVENT_OBJECT_TABLE          AS Tabla,
    ACTION_TIMING               AS Momento,
    ACTION_STATEMENT            AS CuerpoTrigger
FROM INFORMATION_SCHEMA.TRIGGERS
WHERE TRIGGER_SCHEMA = 'control_proyectos_sw'
ORDER BY TRIGGER_NAME;

-- Figura 40 — Definición completa del trigger
SHOW CREATE TRIGGER TRG_01_VALIDAR_BAJA_PERSONAL;

-- Figura 41 — Listado completo de triggers creados
SELECT
    TRIGGER_NAME        AS NombreTrigger,
    EVENT_MANIPULATION  AS Evento,
    EVENT_OBJECT_TABLE  AS Tabla,
    ACTION_TIMING       AS Momento
FROM INFORMATION_SCHEMA.TRIGGERS
WHERE TRIGGER_SCHEMA = 'control_proyectos_sw'
ORDER BY TRIGGER_NAME;

-- Figura 42 — Verificación desde INFORMATION_SCHEMA con fecha de creación
SELECT
    TRIGGER_NAME        AS NombreTrigger,
    EVENT_MANIPULATION  AS Evento,
    EVENT_OBJECT_TABLE  AS Tabla,
    ACTION_TIMING       AS Momento,
    CREATED             AS FechaCreacion
FROM INFORMATION_SCHEMA.TRIGGERS
WHERE TRIGGER_SCHEMA = 'control_proyectos_sw'
ORDER BY CREATED;


-- ============================================================================
-- 6. IMPLEMENTACIÓN DE PROCEDIMIENTOS ALMACENADOS
-- ============================================================================

-- ----------------------------------------------------------------------------
-- 6.1 Procedimiento Almacenado N.° 1
-- Figura 43 — PA_Muestra_01: reporte de personal por cargo
-- Recibe el código de cargo como parámetro; si p_car_cod = 0
-- retorna la totalidad del personal. Solo lectura.
-- ----------------------------------------------------------------------------
DELIMITER $$

DROP PROCEDURE IF EXISTS PA_Muestra_01$$

CREATE PROCEDURE PA_Muestra_01(
    IN p_car_cod SMALLINT UNSIGNED
)
BEGIN
    -- Resultado 1: Listado del personal filtrado por cargo
    SELECT
        p.PerCod                                        AS Codigo,
        p.PerNom                                        AS NombreCompleto,
        cp.CarPerNom                                    AS CargoEmpresa,
        p.PerCosHorCar                                  AS TarifaHora,
        p.PerFecIng                                     AS FechaIngreso,
        TIMESTAMPDIFF(YEAR, p.PerFecIng, CURDATE())     AS AntiguedadAnios,
        er.EstRegNom                                    AS EstadoRegistro
    FROM P3M_PERSONAL p
    INNER JOIN GZZ_CARGO_PERSONAL  cp ON p.PerCodCar = cp.CarPerCod
    INNER JOIN GZZ_ESTADO_REGISTRO er ON p.PerEstReg = er.EstRegCod
    WHERE (p_car_cod = 0 OR p.PerCodCar = p_car_cod)
    ORDER BY cp.CarPerNom, p.PerNom;

    -- Resultado 2: Resumen estadístico del grupo
    SELECT
        COUNT(*)                        AS TotalPersonal,
        ROUND(AVG(p.PerCosHorCar), 2)   AS TarifaPromedio,
        MIN(p.PerCosHorCar)             AS TarifaMinima,
        MAX(p.PerCosHorCar)             AS TarifaMaxima,
        SUM(p.PerCosHorCar)             AS CostoHorarioTotal
    FROM P3M_PERSONAL p
    WHERE (p_car_cod = 0 OR p.PerCodCar = p_car_cod);
END$$

DELIMITER ;

-- Figura 44/45 — Llamada al procedimiento (0 = todo el personal)
CALL PA_Muestra_01(0);


-- ----------------------------------------------------------------------------
-- 6.2 Procedimiento Almacenado N.° 2
-- Figura 46 — PA_Muestra_02: estadísticas de proyectos por estado y tipo
-- Sin parámetros. Consume la vista VW_PROYECTO_GENERAL. Solo lectura.
-- ----------------------------------------------------------------------------
DELIMITER $$

DROP PROCEDURE IF EXISTS PA_Muestra_02$$

CREATE PROCEDURE PA_Muestra_02()
BEGIN
    -- Resultado 1: Estadísticas financieras por estado de proyecto
    SELECT
        EstadoProyecto,
        COUNT(*)                                AS TotalProyectos,
        SUM(MontoEstimado)                      AS TotalEstimado,
        ROUND(AVG(MontoEstimado), 2)            AS PromedioEstimado,
        MIN(MontoEstimado)                      AS MontoMinimo,
        MAX(MontoEstimado)                      AS MontoMaximo,
        SUM(MontoEjecutado)                     AS TotalEjecutado,
        ROUND(
            CASE WHEN SUM(MontoEstimado) > 0
                 THEN SUM(MontoEjecutado) / SUM(MontoEstimado) * 100
                 ELSE 0
            END, 1)                             AS PorcentajeEjecucion
    FROM VW_PROYECTO_GENERAL
    GROUP BY EstadoProyecto
    ORDER BY PorcentajeEjecucion DESC;

    -- Resultado 2: Resumen por tipo de proyecto
    SELECT
        TipoProyecto,
        COUNT(*)                                AS TotalProyectos,
        SUM(MontoEstimado)                      AS TotalEstimado,
        ROUND(AVG(MontoEstimado), 2)            AS PromedioEstimado
    FROM VW_PROYECTO_GENERAL
    GROUP BY TipoProyecto
    ORDER BY TotalEstimado DESC;

    -- Resultado 3: Ranking de clientes por volumen
    SELECT
        NombreCliente,
        TipoCliente,
        COUNT(*)                                AS NumProyectos,
        SUM(MontoEstimado)                      AS TotalFacturado
    FROM VW_PROYECTO_GENERAL
    GROUP BY NombreCliente, TipoCliente
    ORDER BY TotalFacturado DESC;
END$$

DELIMITER ;

-- Figura 47/48 — Llamada al procedimiento sin parámetros
CALL PA_Muestra_02();


-- ----------------------------------------------------------------------------
-- 6.3 Procedimiento Almacenado N.° 3
-- Figura 49 — PA_Muestra_03: detalle de horas trabajadas por proyecto
-- Recibe los identificadores del proyecto. Reutiliza VW_PROYECTO_GENERAL.
-- ----------------------------------------------------------------------------
DELIMITER $$

DROP PROCEDURE IF EXISTS PA_Muestra_03$$

CREATE PROCEDURE PA_Muestra_03(
    IN p_cli_cod  INT UNSIGNED,
    IN p_tip_cod  TINYINT UNSIGNED,
    IN p_sec_pro  TINYINT UNSIGNED
)
BEGIN
    -- Consulta la vista VW_PROYECTO_GENERAL con parámetros
    -- y calcula horas trabajadas del proyecto solicitado
    SELECT
        vg.NombreCliente,
        vg.TipoProyecto,
        vg.EstadoProyecto,
        vg.AvanceEstimado,
        vg.MontoEstimado,
        vg.FechaContrato,
        vg.FechaEntregaPactada,
        COALESCE(SUM(m.MovEtaHrsTra), 0)   AS TotalHorasTrabajadas,
        COALESCE(SUM(m.MovEtaMinTra), 0)   AS TotalMinutosTrabajados
    FROM VW_PROYECTO_GENERAL vg
    LEFT JOIN P4T_MOVIMIENTO m
           ON vg.CliCod       = m.MovCliCod
          AND vg.TipCod       = m.MovTipProCod
          AND vg.SecuenciaPro = m.MovSecPro
    WHERE vg.CliCod       = p_cli_cod
      AND vg.TipCod       = p_tip_cod
      AND vg.SecuenciaPro = p_sec_pro
    GROUP BY
        vg.NombreCliente, vg.TipoProyecto,
        vg.EstadoProyecto, vg.AvanceEstimado,
        vg.MontoEstimado, vg.FechaContrato,
        vg.FechaEntregaPactada;
END$$

DELIMITER ;

-- Figura 50 — Llamada al procedimiento con el Proyecto 1
CALL PA_Muestra_03(1, 1, 1);

-- Figura 51 — Vista consumida por PA_Muestra_03
SELECT * FROM VW_PROYECTO_GENERAL
WHERE CliCod = 1
  AND TipCod = 1
  AND SecuenciaPro = 1;


-- ----------------------------------------------------------------------------
-- 6.4 Validación de restricciones de los procedimientos almacenados
-- ----------------------------------------------------------------------------

-- Figura 53 — Verificación de ausencia de INSERT, UPDATE, DELETE
SELECT
    ROUTINE_NAME        AS Procedimiento,
    ROUTINE_DEFINITION  AS Cuerpo
FROM INFORMATION_SCHEMA.ROUTINES
WHERE ROUTINE_SCHEMA = 'control_proyectos_sw'
  AND ROUTINE_TYPE   = 'PROCEDURE'
  AND ROUTINE_NAME  != 'PA_00_VALIDAR_AVANCE_PROYECTO'
ORDER BY ROUTINE_NAME;

-- Figura 54 — Definición completa de PA_Muestra_01
SHOW CREATE PROCEDURE PA_Muestra_01;


-- ----------------------------------------------------------------------------
-- 6.5 Relación entre procedimientos y vistas
-- ----------------------------------------------------------------------------

-- Figura 55 — PA_Muestra_03 consume VW_PROYECTO_GENERAL
SHOW CREATE PROCEDURE PA_Muestra_03;

-- Figura 56 — Resultado reutilizando la vista con el Proyecto 2
CALL PA_Muestra_03(2, 1, 1);


-- ----------------------------------------------------------------------------
-- 6.6 Administración de procedimientos almacenados
-- ----------------------------------------------------------------------------

-- Figura 57 — Listado de procedimientos almacenados creados
SELECT
    ROUTINE_NAME    AS Procedimiento,
    ROUTINE_TYPE    AS Tipo,
    CREATED         AS FechaCreacion
FROM INFORMATION_SCHEMA.ROUTINES
WHERE ROUTINE_SCHEMA = 'control_proyectos_sw'
  AND ROUTINE_TYPE   = 'PROCEDURE'
ORDER BY ROUTINE_NAME;

-- Figura 58 — Verificación desde INFORMATION_SCHEMA.ROUTINES
SELECT
    ROUTINE_NAME        AS Procedimiento,
    ROUTINE_TYPE        AS Tipo,
    CREATED             AS FechaCreacion,
    LAST_ALTERED        AS UltimaModificacion
FROM INFORMATION_SCHEMA.ROUTINES
WHERE ROUTINE_SCHEMA = 'control_proyectos_sw'
  AND ROUTINE_TYPE   = 'PROCEDURE'
ORDER BY CREATED;


-- ============================================================================
-- 7. ESTADO FINAL DEL MODELO RELACIONAL
-- ============================================================================

-- Figura 67 — Listado completo de vistas, triggers y procedimientos
SELECT
    'VISTA'         AS TipoObjeto,
    TABLE_NAME      AS NombreObjeto
FROM INFORMATION_SCHEMA.VIEWS
WHERE TABLE_SCHEMA = 'control_proyectos_sw'

UNION ALL

SELECT
    'TRIGGER'       AS TipoObjeto,
    TRIGGER_NAME    AS NombreObjeto
FROM INFORMATION_SCHEMA.TRIGGERS
WHERE TRIGGER_SCHEMA = 'control_proyectos_sw'

UNION ALL

SELECT
    'PROCEDIMIENTO' AS TipoObjeto,
    ROUTINE_NAME    AS NombreObjeto
FROM INFORMATION_SCHEMA.ROUTINES
WHERE ROUTINE_SCHEMA = 'control_proyectos_sw'
  AND ROUTINE_TYPE   = 'PROCEDURE'

ORDER BY TipoObjeto, NombreObjeto;

-- Figura 68 — Verificación detallada de todos los objetos implementados
SELECT
    'VISTA'             AS TipoObjeto,
    TABLE_NAME          AS NombreObjeto,
    VIEW_DEFINITION     AS Detalle
FROM INFORMATION_SCHEMA.VIEWS
WHERE TABLE_SCHEMA = 'control_proyectos_sw'

UNION ALL

SELECT
    'TRIGGER'           AS TipoObjeto,
    TRIGGER_NAME        AS NombreObjeto,
    EVENT_OBJECT_TABLE  AS Detalle
FROM INFORMATION_SCHEMA.TRIGGERS
WHERE TRIGGER_SCHEMA = 'control_proyectos_sw'

UNION ALL

SELECT
    'PROCEDIMIENTO'     AS TipoObjeto,
    ROUTINE_NAME        AS NombreObjeto,
    ROUTINE_DEFINITION  AS Detalle
FROM INFORMATION_SCHEMA.ROUTINES
WHERE ROUTINE_SCHEMA = 'control_proyectos_sw'
  AND ROUTINE_TYPE   = 'PROCEDURE'

ORDER BY TipoObjeto, NombreObjeto;

-- ============================================================================
-- FIN DEL SCRIPT
-- ============================================================================
