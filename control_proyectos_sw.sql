-- MySQL dump 10.13  Distrib 8.0.36, for Linux (x86_64)
--
-- Host: localhost    Database: control_proyectos_sw
-- ------------------------------------------------------
-- Server version	8.0.46-0ubuntu0.24.04.2

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `GZZ_CARGO_PERSONAL`
--

DROP TABLE IF EXISTS `GZZ_CARGO_PERSONAL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `GZZ_CARGO_PERSONAL` (
  `CarPerCod` smallint unsigned NOT NULL,
  `CarPerNom` varchar(50) COLLATE utf8mb4_spanish_ci NOT NULL,
  `CarPerEstReg` char(1) COLLATE utf8mb4_spanish_ci NOT NULL DEFAULT 'A',
  PRIMARY KEY (`CarPerCod`),
  KEY `fk_CarPer_EstReg` (`CarPerEstReg`),
  CONSTRAINT `fk_CarPer_EstReg` FOREIGN KEY (`CarPerEstReg`) REFERENCES `GZZ_ESTADO_REGISTRO` (`EstRegCod`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GZZ_CARGO_PERSONAL`
--

LOCK TABLES `GZZ_CARGO_PERSONAL` WRITE;
/*!40000 ALTER TABLE `GZZ_CARGO_PERSONAL` DISABLE KEYS */;
/*!40000 ALTER TABLE `GZZ_CARGO_PERSONAL` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GZZ_CARGO_PROYECTO`
--

DROP TABLE IF EXISTS `GZZ_CARGO_PROYECTO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `GZZ_CARGO_PROYECTO` (
  `CarProCod` tinyint unsigned NOT NULL,
  `CarProNom` varchar(40) COLLATE utf8mb4_spanish_ci NOT NULL,
  `CarProEstReg` char(1) COLLATE utf8mb4_spanish_ci NOT NULL DEFAULT 'A',
  PRIMARY KEY (`CarProCod`),
  KEY `fk_CarPro_EstReg` (`CarProEstReg`),
  CONSTRAINT `fk_CarPro_EstReg` FOREIGN KEY (`CarProEstReg`) REFERENCES `GZZ_ESTADO_REGISTRO` (`EstRegCod`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GZZ_CARGO_PROYECTO`
--

LOCK TABLES `GZZ_CARGO_PROYECTO` WRITE;
/*!40000 ALTER TABLE `GZZ_CARGO_PROYECTO` DISABLE KEYS */;
INSERT INTO `GZZ_CARGO_PROYECTO` VALUES (1,'Jefe de Proyecto','A'),(2,'Analista','A'),(3,'Desarrollador','A'),(4,'DBA','A'),(5,'Diseñador','A');
/*!40000 ALTER TABLE `GZZ_CARGO_PROYECTO` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GZZ_ESTADO_CLIENTE`
--

DROP TABLE IF EXISTS `GZZ_ESTADO_CLIENTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `GZZ_ESTADO_CLIENTE` (
  `EstCliCod` char(1) COLLATE utf8mb4_spanish_ci NOT NULL,
  `EstCliNom` varchar(20) COLLATE utf8mb4_spanish_ci NOT NULL,
  `EstCliEstReg` char(1) COLLATE utf8mb4_spanish_ci NOT NULL DEFAULT 'A',
  PRIMARY KEY (`EstCliCod`),
  KEY `fk_EstCli_EstReg` (`EstCliEstReg`),
  CONSTRAINT `fk_EstCli_EstReg` FOREIGN KEY (`EstCliEstReg`) REFERENCES `GZZ_ESTADO_REGISTRO` (`EstRegCod`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GZZ_ESTADO_CLIENTE`
--

LOCK TABLES `GZZ_ESTADO_CLIENTE` WRITE;
/*!40000 ALTER TABLE `GZZ_ESTADO_CLIENTE` DISABLE KEYS */;
INSERT INTO `GZZ_ESTADO_CLIENTE` VALUES ('1','Activo','A'),('2','Cesado','A'),('3','Activo','A'),('4','Activo','A'),('5','Cesado','A');
/*!40000 ALTER TABLE `GZZ_ESTADO_CLIENTE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GZZ_ESTADO_DISPONIBILIDAD`
--

DROP TABLE IF EXISTS `GZZ_ESTADO_DISPONIBILIDAD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `GZZ_ESTADO_DISPONIBILIDAD` (
  `EstDisCod` char(1) COLLATE utf8mb4_spanish_ci NOT NULL,
  `EstDisNom` varchar(30) COLLATE utf8mb4_spanish_ci NOT NULL,
  `EstDisEstReg` char(1) COLLATE utf8mb4_spanish_ci NOT NULL DEFAULT 'A',
  PRIMARY KEY (`EstDisCod`),
  KEY `fk_EstDis_EstReg` (`EstDisEstReg`),
  CONSTRAINT `fk_EstDis_EstReg` FOREIGN KEY (`EstDisEstReg`) REFERENCES `GZZ_ESTADO_REGISTRO` (`EstRegCod`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GZZ_ESTADO_DISPONIBILIDAD`
--

LOCK TABLES `GZZ_ESTADO_DISPONIBILIDAD` WRITE;
/*!40000 ALTER TABLE `GZZ_ESTADO_DISPONIBILIDAD` DISABLE KEYS */;
INSERT INTO `GZZ_ESTADO_DISPONIBILIDAD` VALUES ('1','Disponible','A'),('2','Asignado','A'),('3','Vacaciones','A'),('4','Licencia','A');
/*!40000 ALTER TABLE `GZZ_ESTADO_DISPONIBILIDAD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GZZ_ESTADO_PROYECTO`
--

DROP TABLE IF EXISTS `GZZ_ESTADO_PROYECTO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `GZZ_ESTADO_PROYECTO` (
  `EstProCod` tinyint unsigned NOT NULL,
  `EstProNom` varchar(40) COLLATE utf8mb4_spanish_ci NOT NULL,
  `EstProEstReg` char(1) COLLATE utf8mb4_spanish_ci NOT NULL DEFAULT 'A',
  PRIMARY KEY (`EstProCod`),
  KEY `fk_EstPro_EstReg` (`EstProEstReg`),
  CONSTRAINT `fk_EstPro_EstReg` FOREIGN KEY (`EstProEstReg`) REFERENCES `GZZ_ESTADO_REGISTRO` (`EstRegCod`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GZZ_ESTADO_PROYECTO`
--

LOCK TABLES `GZZ_ESTADO_PROYECTO` WRITE;
/*!40000 ALTER TABLE `GZZ_ESTADO_PROYECTO` DISABLE KEYS */;
INSERT INTO `GZZ_ESTADO_PROYECTO` VALUES (1,'Prospecto','A'),(2,'Con Contrato','A'),(3,'Planificación Detalle','A'),(4,'Desarrollo-Ejecución','A'),(5,'Cerrado','A');
/*!40000 ALTER TABLE `GZZ_ESTADO_PROYECTO` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GZZ_ESTADO_REGISTRO`
--

DROP TABLE IF EXISTS `GZZ_ESTADO_REGISTRO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `GZZ_ESTADO_REGISTRO` (
  `EstRegCod` char(1) COLLATE utf8mb4_spanish_ci NOT NULL,
  `EstRegNom` varchar(30) COLLATE utf8mb4_spanish_ci NOT NULL,
  PRIMARY KEY (`EstRegCod`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GZZ_ESTADO_REGISTRO`
--

LOCK TABLES `GZZ_ESTADO_REGISTRO` WRITE;
/*!40000 ALTER TABLE `GZZ_ESTADO_REGISTRO` DISABLE KEYS */;
INSERT INTO `GZZ_ESTADO_REGISTRO` VALUES ('*','Eliminado'),('A','Activo'),('I','Inactivo');
/*!40000 ALTER TABLE `GZZ_ESTADO_REGISTRO` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GZZ_FACTOR`
--

DROP TABLE IF EXISTS `GZZ_FACTOR`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `GZZ_FACTOR` (
  `FacCod` tinyint unsigned NOT NULL,
  `FacNom` varchar(40) COLLATE utf8mb4_spanish_ci NOT NULL,
  `FacPorBas` decimal(5,2) NOT NULL DEFAULT '0.00',
  `FacEstReg` char(1) COLLATE utf8mb4_spanish_ci NOT NULL DEFAULT 'A',
  PRIMARY KEY (`FacCod`),
  KEY `fk_Fac_EstReg` (`FacEstReg`),
  CONSTRAINT `fk_Fac_EstReg` FOREIGN KEY (`FacEstReg`) REFERENCES `GZZ_ESTADO_REGISTRO` (`EstRegCod`),
  CONSTRAINT `chk_FacPorBas` CHECK ((`FacPorBas` between 0 and 100))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GZZ_FACTOR`
--

LOCK TABLES `GZZ_FACTOR` WRITE;
/*!40000 ALTER TABLE `GZZ_FACTOR` DISABLE KEYS */;
/*!40000 ALTER TABLE `GZZ_FACTOR` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GZZ_TIPO_CLIENTE`
--

DROP TABLE IF EXISTS `GZZ_TIPO_CLIENTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `GZZ_TIPO_CLIENTE` (
  `TipCliCod` tinyint unsigned NOT NULL,
  `TipCliNom` varchar(30) COLLATE utf8mb4_spanish_ci NOT NULL,
  `TipCliEstReg` char(1) COLLATE utf8mb4_spanish_ci NOT NULL DEFAULT 'A',
  PRIMARY KEY (`TipCliCod`),
  KEY `fk_TipCli_EstReg` (`TipCliEstReg`),
  CONSTRAINT `fk_TipCli_EstReg` FOREIGN KEY (`TipCliEstReg`) REFERENCES `GZZ_ESTADO_REGISTRO` (`EstRegCod`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GZZ_TIPO_CLIENTE`
--

LOCK TABLES `GZZ_TIPO_CLIENTE` WRITE;
/*!40000 ALTER TABLE `GZZ_TIPO_CLIENTE` DISABLE KEYS */;
INSERT INTO `GZZ_TIPO_CLIENTE` VALUES (1,'Persona Natural','I'),(2,'Persona Natural','A'),(3,'Persona Natural','*'),(4,'Persona Jurídica','A'),(5,'Persona Jurídica','A'),(6,'Persona Natural','A'),(7,'Persona Jurídica','*');
/*!40000 ALTER TABLE `GZZ_TIPO_CLIENTE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GZZ_TIPO_ESTANDAR`
--

DROP TABLE IF EXISTS `GZZ_TIPO_ESTANDAR`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `GZZ_TIPO_ESTANDAR` (
  `TipEstCod` tinyint unsigned NOT NULL,
  `TipEstNom` varchar(40) COLLATE utf8mb4_spanish_ci NOT NULL,
  `TipEstUniDef` varchar(20) COLLATE utf8mb4_spanish_ci NOT NULL,
  `TipEstEstReg` char(1) COLLATE utf8mb4_spanish_ci NOT NULL DEFAULT 'A',
  PRIMARY KEY (`TipEstCod`),
  KEY `fk_TipEst_EstReg` (`TipEstEstReg`),
  CONSTRAINT `fk_TipEst_EstReg` FOREIGN KEY (`TipEstEstReg`) REFERENCES `GZZ_ESTADO_REGISTRO` (`EstRegCod`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GZZ_TIPO_ESTANDAR`
--

LOCK TABLES `GZZ_TIPO_ESTANDAR` WRITE;
/*!40000 ALTER TABLE `GZZ_TIPO_ESTANDAR` DISABLE KEYS */;
/*!40000 ALTER TABLE `GZZ_TIPO_ESTANDAR` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GZZ_TIPO_PROYECTO`
--

DROP TABLE IF EXISTS `GZZ_TIPO_PROYECTO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `GZZ_TIPO_PROYECTO` (
  `TipProCod` tinyint unsigned NOT NULL,
  `TipProNom` varchar(40) COLLATE utf8mb4_spanish_ci NOT NULL,
  `TipProEstReg` char(1) COLLATE utf8mb4_spanish_ci NOT NULL DEFAULT 'A',
  PRIMARY KEY (`TipProCod`),
  KEY `fk_TipPro_EstReg` (`TipProEstReg`),
  CONSTRAINT `fk_TipPro_EstReg` FOREIGN KEY (`TipProEstReg`) REFERENCES `GZZ_ESTADO_REGISTRO` (`EstRegCod`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GZZ_TIPO_PROYECTO`
--

LOCK TABLES `GZZ_TIPO_PROYECTO` WRITE;
/*!40000 ALTER TABLE `GZZ_TIPO_PROYECTO` DISABLE KEYS */;
INSERT INTO `GZZ_TIPO_PROYECTO` VALUES (1,'Software','A'),(2,'Hardware','A'),(3,'Telecomunicaciones','A'),(4,'Hardware','A'),(5,'Seguridad Informática','A');
/*!40000 ALTER TABLE `GZZ_TIPO_PROYECTO` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `P1M_CLIENTE`
--

DROP TABLE IF EXISTS `P1M_CLIENTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `P1M_CLIENTE` (
  `CliCod` int unsigned NOT NULL AUTO_INCREMENT,
  `CliTipCod` tinyint unsigned NOT NULL,
  `CliNom` varchar(80) COLLATE utf8mb4_spanish_ci NOT NULL,
  `CliFecIng` date NOT NULL,
  `CliFecCes` date DEFAULT NULL,
  `CliFecUltProCer` date DEFAULT NULL,
  `CliEstCli` char(1) COLLATE utf8mb4_spanish_ci NOT NULL,
  `CliEstReg` char(1) COLLATE utf8mb4_spanish_ci NOT NULL DEFAULT 'A',
  PRIMARY KEY (`CliCod`),
  KEY `fk_Cli_TipCli` (`CliTipCod`),
  KEY `fk_Cli_EstCli` (`CliEstCli`),
  KEY `fk_Cli_EstReg` (`CliEstReg`),
  CONSTRAINT `fk_Cli_EstCli` FOREIGN KEY (`CliEstCli`) REFERENCES `GZZ_ESTADO_CLIENTE` (`EstCliCod`),
  CONSTRAINT `fk_Cli_EstReg` FOREIGN KEY (`CliEstReg`) REFERENCES `GZZ_ESTADO_REGISTRO` (`EstRegCod`),
  CONSTRAINT `fk_Cli_TipCli` FOREIGN KEY (`CliTipCod`) REFERENCES `GZZ_TIPO_CLIENTE` (`TipCliCod`),
  CONSTRAINT `chk_CliFecCes` CHECK (((`CliFecCes` is null) or (`CliFecCes` >= `CliFecIng`)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `P1M_CLIENTE`
--

LOCK TABLES `P1M_CLIENTE` WRITE;
/*!40000 ALTER TABLE `P1M_CLIENTE` DISABLE KEYS */;
/*!40000 ALTER TABLE `P1M_CLIENTE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `P2H_PROYECTO_ESTADO`
--

DROP TABLE IF EXISTS `P2H_PROYECTO_ESTADO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `P2H_PROYECTO_ESTADO` (
  `HisProCliCod` int unsigned NOT NULL,
  `HisProTipCod` tinyint unsigned NOT NULL,
  `HisProSec` tinyint unsigned NOT NULL,
  `HisProSecCam` smallint unsigned NOT NULL,
  `HisProEstAnt` tinyint unsigned NOT NULL,
  `HisProEstNue` tinyint unsigned NOT NULL,
  `HisProFecCam` date NOT NULL,
  `HisProPerCod` int unsigned NOT NULL,
  `HisProEstReg` char(1) COLLATE utf8mb4_spanish_ci NOT NULL DEFAULT 'A',
  PRIMARY KEY (`HisProCliCod`,`HisProTipCod`,`HisProSec`,`HisProSecCam`),
  KEY `fk_His_EstAnt` (`HisProEstAnt`),
  KEY `fk_His_EstNue` (`HisProEstNue`),
  KEY `fk_His_EstReg` (`HisProEstReg`),
  KEY `fk_His_Per` (`HisProPerCod`),
  CONSTRAINT `fk_His_EstAnt` FOREIGN KEY (`HisProEstAnt`) REFERENCES `GZZ_ESTADO_PROYECTO` (`EstProCod`),
  CONSTRAINT `fk_His_EstNue` FOREIGN KEY (`HisProEstNue`) REFERENCES `GZZ_ESTADO_PROYECTO` (`EstProCod`),
  CONSTRAINT `fk_His_EstReg` FOREIGN KEY (`HisProEstReg`) REFERENCES `GZZ_ESTADO_REGISTRO` (`EstRegCod`),
  CONSTRAINT `fk_His_Per` FOREIGN KEY (`HisProPerCod`) REFERENCES `P3M_PERSONAL` (`PerCod`),
  CONSTRAINT `fk_His_Pro` FOREIGN KEY (`HisProCliCod`, `HisProTipCod`, `HisProSec`) REFERENCES `P2M_PROYECTO` (`ProCliCod`, `ProTipProCod`, `ProSecPro`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `P2H_PROYECTO_ESTADO`
--

LOCK TABLES `P2H_PROYECTO_ESTADO` WRITE;
/*!40000 ALTER TABLE `P2H_PROYECTO_ESTADO` DISABLE KEYS */;
/*!40000 ALTER TABLE `P2H_PROYECTO_ESTADO` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `P2M_PROYECTO`
--

DROP TABLE IF EXISTS `P2M_PROYECTO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `P2M_PROYECTO` (
  `ProCliCod` int unsigned NOT NULL,
  `ProTipProCod` tinyint unsigned NOT NULL,
  `ProSecPro` tinyint unsigned NOT NULL,
  `ProFecCon` date NOT NULL,
  `ProFecPac` date NOT NULL,
  `ProFecIni` date DEFAULT NULL,
  `ProFecEnt` date DEFAULT NULL,
  `ProFecCie` date DEFAULT NULL,
  `ProMonProCos` decimal(12,2) DEFAULT NULL,
  `ProMonProGas` decimal(12,2) DEFAULT NULL,
  `ProMonProUti` decimal(12,2) DEFAULT NULL,
  `ProMonPro` decimal(12,2) NOT NULL,
  `ProMonProCosRea` decimal(12,2) DEFAULT NULL,
  `ProMonProGasRea` decimal(12,2) DEFAULT NULL,
  `ProMonProUtiRea` decimal(12,2) DEFAULT NULL,
  `ProMonProRea` decimal(12,2) DEFAULT NULL,
  `ProEstPro` tinyint unsigned NOT NULL,
  `ProEstReg` char(1) COLLATE utf8mb4_spanish_ci NOT NULL DEFAULT 'A',
  PRIMARY KEY (`ProCliCod`,`ProTipProCod`,`ProSecPro`),
  KEY `fk_Pro_TipPro` (`ProTipProCod`),
  KEY `fk_Pro_EstPro` (`ProEstPro`),
  KEY `fk_Pro_EstReg` (`ProEstReg`),
  CONSTRAINT `fk_Pro_Cli` FOREIGN KEY (`ProCliCod`) REFERENCES `P1M_CLIENTE` (`CliCod`),
  CONSTRAINT `fk_Pro_EstPro` FOREIGN KEY (`ProEstPro`) REFERENCES `GZZ_ESTADO_PROYECTO` (`EstProCod`),
  CONSTRAINT `fk_Pro_EstReg` FOREIGN KEY (`ProEstReg`) REFERENCES `GZZ_ESTADO_REGISTRO` (`EstRegCod`),
  CONSTRAINT `fk_Pro_TipPro` FOREIGN KEY (`ProTipProCod`) REFERENCES `GZZ_TIPO_PROYECTO` (`TipProCod`),
  CONSTRAINT `chk_ProFecEnt` CHECK (((`ProFecEnt` is null) or (`ProFecEnt` >= `ProFecIni`))),
  CONSTRAINT `chk_ProFecIni` CHECK (((`ProFecIni` is null) or (`ProFecIni` >= `ProFecPac`))),
  CONSTRAINT `chk_ProFecPac` CHECK ((`ProFecPac` >= `ProFecCon`)),
  CONSTRAINT `chk_ProMonPro` CHECK ((`ProMonPro` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `P2M_PROYECTO`
--

LOCK TABLES `P2M_PROYECTO` WRITE;
/*!40000 ALTER TABLE `P2M_PROYECTO` DISABLE KEYS */;
/*!40000 ALTER TABLE `P2M_PROYECTO` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `P2T_PROYECTO_FACTOR`
--

DROP TABLE IF EXISTS `P2T_PROYECTO_FACTOR`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `P2T_PROYECTO_FACTOR` (
  `ProFacCliCod` int unsigned NOT NULL,
  `ProFacTipCod` tinyint unsigned NOT NULL,
  `ProFacSec` tinyint unsigned NOT NULL,
  `ProFacCod` tinyint unsigned NOT NULL,
  `ProFacPorApl` decimal(5,2) NOT NULL,
  `ProFacEstReg` char(1) COLLATE utf8mb4_spanish_ci NOT NULL DEFAULT 'A',
  PRIMARY KEY (`ProFacCliCod`,`ProFacTipCod`,`ProFacSec`,`ProFacCod`),
  KEY `fk_ProFac_Fac` (`ProFacCod`),
  KEY `fk_ProFac_EstReg` (`ProFacEstReg`),
  CONSTRAINT `fk_ProFac_EstReg` FOREIGN KEY (`ProFacEstReg`) REFERENCES `GZZ_ESTADO_REGISTRO` (`EstRegCod`),
  CONSTRAINT `fk_ProFac_Fac` FOREIGN KEY (`ProFacCod`) REFERENCES `GZZ_FACTOR` (`FacCod`),
  CONSTRAINT `fk_ProFac_Pro` FOREIGN KEY (`ProFacCliCod`, `ProFacTipCod`, `ProFacSec`) REFERENCES `P2M_PROYECTO` (`ProCliCod`, `ProTipProCod`, `ProSecPro`),
  CONSTRAINT `chk_ProFacPor` CHECK ((`ProFacPorApl` between 0 and 100))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `P2T_PROYECTO_FACTOR`
--

LOCK TABLES `P2T_PROYECTO_FACTOR` WRITE;
/*!40000 ALTER TABLE `P2T_PROYECTO_FACTOR` DISABLE KEYS */;
/*!40000 ALTER TABLE `P2T_PROYECTO_FACTOR` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `P3M_ACTIVIDAD`
--

DROP TABLE IF EXISTS `P3M_ACTIVIDAD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `P3M_ACTIVIDAD` (
  `ActEtaCod` smallint unsigned NOT NULL,
  `ActCod` tinyint unsigned NOT NULL,
  `ActNom` varchar(60) COLLATE utf8mb4_spanish_ci NOT NULL,
  `ActTpoEst` decimal(6,2) NOT NULL,
  `ActEstReg` char(1) COLLATE utf8mb4_spanish_ci NOT NULL DEFAULT 'A',
  PRIMARY KEY (`ActEtaCod`,`ActCod`),
  KEY `fk_Act_EstReg` (`ActEstReg`),
  CONSTRAINT `fk_Act_EstReg` FOREIGN KEY (`ActEstReg`) REFERENCES `GZZ_ESTADO_REGISTRO` (`EstRegCod`),
  CONSTRAINT `fk_Act_Eta` FOREIGN KEY (`ActEtaCod`) REFERENCES `P3M_ETAPA` (`EtaCod`),
  CONSTRAINT `chk_ActTpoEst` CHECK ((`ActTpoEst` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `P3M_ACTIVIDAD`
--

LOCK TABLES `P3M_ACTIVIDAD` WRITE;
/*!40000 ALTER TABLE `P3M_ACTIVIDAD` DISABLE KEYS */;
/*!40000 ALTER TABLE `P3M_ACTIVIDAD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `P3M_ESTANDAR`
--

DROP TABLE IF EXISTS `P3M_ESTANDAR`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `P3M_ESTANDAR` (
  `EstActEtaCod` smallint unsigned NOT NULL,
  `EstActActCod` tinyint unsigned NOT NULL,
  `EstCod` tinyint unsigned NOT NULL,
  `EstTipCod` tinyint unsigned NOT NULL,
  `EstNom` varchar(80) COLLATE utf8mb4_spanish_ci NOT NULL,
  `EstValNum` decimal(8,2) DEFAULT NULL,
  `EstValTxt` varchar(60) COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  `EstUni` varchar(20) COLLATE utf8mb4_spanish_ci NOT NULL,
  `EstEstReg` char(1) COLLATE utf8mb4_spanish_ci NOT NULL DEFAULT 'A',
  PRIMARY KEY (`EstActEtaCod`,`EstActActCod`,`EstCod`),
  KEY `fk_Est_TipEst` (`EstTipCod`),
  KEY `fk_Est_EstReg` (`EstEstReg`),
  CONSTRAINT `fk_Est_Act` FOREIGN KEY (`EstActEtaCod`, `EstActActCod`) REFERENCES `P3M_ACTIVIDAD` (`ActEtaCod`, `ActCod`),
  CONSTRAINT `fk_Est_EstReg` FOREIGN KEY (`EstEstReg`) REFERENCES `GZZ_ESTADO_REGISTRO` (`EstRegCod`),
  CONSTRAINT `fk_Est_TipEst` FOREIGN KEY (`EstTipCod`) REFERENCES `GZZ_TIPO_ESTANDAR` (`TipEstCod`),
  CONSTRAINT `chk_EstVal` CHECK ((((`EstValNum` is not null) and (`EstValTxt` is null)) or ((`EstValTxt` is not null) and (`EstValNum` is null)))),
  CONSTRAINT `chk_EstValNum` CHECK (((`EstValNum` is null) or (`EstValNum` > 0)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `P3M_ESTANDAR`
--

LOCK TABLES `P3M_ESTANDAR` WRITE;
/*!40000 ALTER TABLE `P3M_ESTANDAR` DISABLE KEYS */;
/*!40000 ALTER TABLE `P3M_ESTANDAR` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `P3M_ETAPA`
--

DROP TABLE IF EXISTS `P3M_ETAPA`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `P3M_ETAPA` (
  `EtaCod` smallint unsigned NOT NULL,
  `EtaNom` varchar(60) COLLATE utf8mb4_spanish_ci NOT NULL,
  `EtaEstReg` char(1) COLLATE utf8mb4_spanish_ci NOT NULL DEFAULT 'A',
  PRIMARY KEY (`EtaCod`),
  KEY `fk_Eta_EstReg` (`EtaEstReg`),
  CONSTRAINT `fk_Eta_EstReg` FOREIGN KEY (`EtaEstReg`) REFERENCES `GZZ_ESTADO_REGISTRO` (`EstRegCod`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `P3M_ETAPA`
--

LOCK TABLES `P3M_ETAPA` WRITE;
/*!40000 ALTER TABLE `P3M_ETAPA` DISABLE KEYS */;
/*!40000 ALTER TABLE `P3M_ETAPA` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `P3M_PERSONAL`
--

DROP TABLE IF EXISTS `P3M_PERSONAL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `P3M_PERSONAL` (
  `PerCod` int unsigned NOT NULL AUTO_INCREMENT,
  `PerCodCar` smallint unsigned NOT NULL,
  `PerNom` varchar(80) COLLATE utf8mb4_spanish_ci NOT NULL,
  `PerCosHorCar` decimal(8,2) NOT NULL,
  `PerFecIng` date NOT NULL,
  `PerEstReg` char(1) COLLATE utf8mb4_spanish_ci NOT NULL DEFAULT 'A',
  PRIMARY KEY (`PerCod`),
  KEY `fk_Per_CarPer` (`PerCodCar`),
  KEY `fk_Per_EstReg` (`PerEstReg`),
  CONSTRAINT `fk_Per_CarPer` FOREIGN KEY (`PerCodCar`) REFERENCES `GZZ_CARGO_PERSONAL` (`CarPerCod`),
  CONSTRAINT `fk_Per_EstReg` FOREIGN KEY (`PerEstReg`) REFERENCES `GZZ_ESTADO_REGISTRO` (`EstRegCod`),
  CONSTRAINT `chk_PerCosHor` CHECK ((`PerCosHorCar` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `P3M_PERSONAL`
--

LOCK TABLES `P3M_PERSONAL` WRITE;
/*!40000 ALTER TABLE `P3M_PERSONAL` DISABLE KEYS */;
/*!40000 ALTER TABLE `P3M_PERSONAL` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `P3T_EQUIPO_PROYECTO`
--

DROP TABLE IF EXISTS `P3T_EQUIPO_PROYECTO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `P3T_EQUIPO_PROYECTO` (
  `EqpProCliCod` int unsigned NOT NULL,
  `EqpProTipCod` tinyint unsigned NOT NULL,
  `EqpProSec` tinyint unsigned NOT NULL,
  `EqpPerCod` int unsigned NOT NULL,
  `EqpCarCod` tinyint unsigned NOT NULL,
  `EqpEstReg` char(1) COLLATE utf8mb4_spanish_ci NOT NULL DEFAULT 'A',
  PRIMARY KEY (`EqpProCliCod`,`EqpProTipCod`,`EqpProSec`,`EqpPerCod`,`EqpCarCod`),
  KEY `fk_Eqp_PerCar` (`EqpPerCod`,`EqpCarCod`),
  KEY `fk_Eqp_EstReg` (`EqpEstReg`),
  CONSTRAINT `fk_Eqp_EstReg` FOREIGN KEY (`EqpEstReg`) REFERENCES `GZZ_ESTADO_REGISTRO` (`EstRegCod`),
  CONSTRAINT `fk_Eqp_PerCar` FOREIGN KEY (`EqpPerCod`, `EqpCarCod`) REFERENCES `P3T_PERSONAL_CARGO_PRY` (`PerCarPerCod`, `PerCarCodCar`),
  CONSTRAINT `fk_Eqp_Pro` FOREIGN KEY (`EqpProCliCod`, `EqpProTipCod`, `EqpProSec`) REFERENCES `P2M_PROYECTO` (`ProCliCod`, `ProTipProCod`, `ProSecPro`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `P3T_EQUIPO_PROYECTO`
--

LOCK TABLES `P3T_EQUIPO_PROYECTO` WRITE;
/*!40000 ALTER TABLE `P3T_EQUIPO_PROYECTO` DISABLE KEYS */;
/*!40000 ALTER TABLE `P3T_EQUIPO_PROYECTO` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `P3T_PERSONAL_CARGO_PRY`
--

DROP TABLE IF EXISTS `P3T_PERSONAL_CARGO_PRY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `P3T_PERSONAL_CARGO_PRY` (
  `PerCarPerCod` int unsigned NOT NULL,
  `PerCarCodCar` tinyint unsigned NOT NULL,
  `PerCarEstReg` char(1) COLLATE utf8mb4_spanish_ci NOT NULL DEFAULT 'A',
  PRIMARY KEY (`PerCarPerCod`,`PerCarCodCar`),
  KEY `fk_PerCar_CarPro` (`PerCarCodCar`),
  KEY `fk_PerCar_EstReg` (`PerCarEstReg`),
  CONSTRAINT `fk_PerCar_CarPro` FOREIGN KEY (`PerCarCodCar`) REFERENCES `GZZ_CARGO_PROYECTO` (`CarProCod`),
  CONSTRAINT `fk_PerCar_EstReg` FOREIGN KEY (`PerCarEstReg`) REFERENCES `GZZ_ESTADO_REGISTRO` (`EstRegCod`),
  CONSTRAINT `fk_PerCar_Per` FOREIGN KEY (`PerCarPerCod`) REFERENCES `P3M_PERSONAL` (`PerCod`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `P3T_PERSONAL_CARGO_PRY`
--

LOCK TABLES `P3T_PERSONAL_CARGO_PRY` WRITE;
/*!40000 ALTER TABLE `P3T_PERSONAL_CARGO_PRY` DISABLE KEYS */;
/*!40000 ALTER TABLE `P3T_PERSONAL_CARGO_PRY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `P3T_PERSONAL_DISPONIBILIDAD`
--

DROP TABLE IF EXISTS `P3T_PERSONAL_DISPONIBILIDAD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `P3T_PERSONAL_DISPONIBILIDAD` (
  `PerDisPerCod` int unsigned NOT NULL,
  `PerDisFecDes` date NOT NULL,
  `PerDisFecHas` date DEFAULT NULL,
  `PerDisEstDis` char(1) COLLATE utf8mb4_spanish_ci NOT NULL,
  `PerDisEstReg` char(1) COLLATE utf8mb4_spanish_ci NOT NULL DEFAULT 'A',
  PRIMARY KEY (`PerDisPerCod`,`PerDisFecDes`),
  KEY `fk_PerDis_EstDis` (`PerDisEstDis`),
  KEY `fk_PerDis_EstReg` (`PerDisEstReg`),
  CONSTRAINT `fk_PerDis_EstDis` FOREIGN KEY (`PerDisEstDis`) REFERENCES `GZZ_ESTADO_DISPONIBILIDAD` (`EstDisCod`),
  CONSTRAINT `fk_PerDis_EstReg` FOREIGN KEY (`PerDisEstReg`) REFERENCES `GZZ_ESTADO_REGISTRO` (`EstRegCod`),
  CONSTRAINT `fk_PerDis_Per` FOREIGN KEY (`PerDisPerCod`) REFERENCES `P3M_PERSONAL` (`PerCod`),
  CONSTRAINT `chk_PerDisFec` CHECK (((`PerDisFecHas` is null) or (`PerDisFecHas` >= `PerDisFecDes`)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `P3T_PERSONAL_DISPONIBILIDAD`
--

LOCK TABLES `P3T_PERSONAL_DISPONIBILIDAD` WRITE;
/*!40000 ALTER TABLE `P3T_PERSONAL_DISPONIBILIDAD` DISABLE KEYS */;
/*!40000 ALTER TABLE `P3T_PERSONAL_DISPONIBILIDAD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `P3T_PROYECTO_ETAPA`
--

DROP TABLE IF EXISTS `P3T_PROYECTO_ETAPA`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `P3T_PROYECTO_ETAPA` (
  `ProEtaCliCod` int unsigned NOT NULL,
  `ProEtaTipCod` tinyint unsigned NOT NULL,
  `ProEtaSec` tinyint unsigned NOT NULL,
  `ProEtaCod` smallint unsigned NOT NULL,
  `ProEtaTpoEstAju` decimal(6,2) NOT NULL,
  `ProEtaFecIni` date DEFAULT NULL,
  `ProEtaFecFin` date DEFAULT NULL,
  `ProEtaEstReg` char(1) COLLATE utf8mb4_spanish_ci NOT NULL DEFAULT 'A',
  PRIMARY KEY (`ProEtaCliCod`,`ProEtaTipCod`,`ProEtaSec`,`ProEtaCod`),
  KEY `fk_ProEta_Eta` (`ProEtaCod`),
  KEY `fk_ProEta_EstReg` (`ProEtaEstReg`),
  CONSTRAINT `fk_ProEta_EstReg` FOREIGN KEY (`ProEtaEstReg`) REFERENCES `GZZ_ESTADO_REGISTRO` (`EstRegCod`),
  CONSTRAINT `fk_ProEta_Eta` FOREIGN KEY (`ProEtaCod`) REFERENCES `P3M_ETAPA` (`EtaCod`),
  CONSTRAINT `fk_ProEta_Pro` FOREIGN KEY (`ProEtaCliCod`, `ProEtaTipCod`, `ProEtaSec`) REFERENCES `P2M_PROYECTO` (`ProCliCod`, `ProTipProCod`, `ProSecPro`),
  CONSTRAINT `chk_ProEtaFec` CHECK (((`ProEtaFecFin` is null) or (`ProEtaFecIni` is null) or (`ProEtaFecFin` >= `ProEtaFecIni`))),
  CONSTRAINT `chk_ProEtaTpo` CHECK ((`ProEtaTpoEstAju` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `P3T_PROYECTO_ETAPA`
--

LOCK TABLES `P3T_PROYECTO_ETAPA` WRITE;
/*!40000 ALTER TABLE `P3T_PROYECTO_ETAPA` DISABLE KEYS */;
/*!40000 ALTER TABLE `P3T_PROYECTO_ETAPA` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `P4T_MOVIMIENTO`
--

DROP TABLE IF EXISTS `P4T_MOVIMIENTO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `P4T_MOVIMIENTO` (
  `MovCliCod` int unsigned NOT NULL,
  `MovTipProCod` tinyint unsigned NOT NULL,
  `MovSecPro` tinyint unsigned NOT NULL,
  `MovPerCod` int unsigned NOT NULL,
  `MovCarCod` tinyint unsigned NOT NULL,
  `MovEtaCod` smallint unsigned NOT NULL,
  `MovEtaSec` smallint unsigned NOT NULL,
  `MovFecRegEta` date NOT NULL,
  `MovEtaHrsTra` tinyint unsigned NOT NULL,
  `MovEtaMinTra` tinyint unsigned NOT NULL,
  `MovEstReg` char(1) COLLATE utf8mb4_spanish_ci NOT NULL DEFAULT 'A',
  PRIMARY KEY (`MovCliCod`,`MovTipProCod`,`MovSecPro`,`MovPerCod`,`MovCarCod`,`MovEtaCod`,`MovEtaSec`),
  KEY `fk_Mov_ProEta` (`MovCliCod`,`MovTipProCod`,`MovSecPro`,`MovEtaCod`),
  KEY `fk_Mov_EstReg` (`MovEstReg`),
  CONSTRAINT `fk_Mov_Eqp` FOREIGN KEY (`MovCliCod`, `MovTipProCod`, `MovSecPro`, `MovPerCod`, `MovCarCod`) REFERENCES `P3T_EQUIPO_PROYECTO` (`EqpProCliCod`, `EqpProTipCod`, `EqpProSec`, `EqpPerCod`, `EqpCarCod`),
  CONSTRAINT `fk_Mov_EstReg` FOREIGN KEY (`MovEstReg`) REFERENCES `GZZ_ESTADO_REGISTRO` (`EstRegCod`),
  CONSTRAINT `fk_Mov_ProEta` FOREIGN KEY (`MovCliCod`, `MovTipProCod`, `MovSecPro`, `MovEtaCod`) REFERENCES `P3T_PROYECTO_ETAPA` (`ProEtaCliCod`, `ProEtaTipCod`, `ProEtaSec`, `ProEtaCod`),
  CONSTRAINT `chk_MovHrs` CHECK ((`MovEtaHrsTra` between 0 and 12)),
  CONSTRAINT `chk_MovHrs12` CHECK (((`MovEtaHrsTra` <> 12) or (`MovEtaMinTra` <= 0))),
  CONSTRAINT `chk_MovMin` CHECK ((`MovEtaMinTra` in (0,15,30,45))),
  CONSTRAINT `chk_MovTiempo` CHECK (((`MovEtaHrsTra` <> 0) or (`MovEtaMinTra` <> 0)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `P4T_MOVIMIENTO`
--

LOCK TABLES `P4T_MOVIMIENTO` WRITE;
/*!40000 ALTER TABLE `P4T_MOVIMIENTO` DISABLE KEYS */;
/*!40000 ALTER TABLE `P4T_MOVIMIENTO` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-21 22:28:52
