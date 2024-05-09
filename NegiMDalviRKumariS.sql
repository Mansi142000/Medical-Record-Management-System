CREATE DATABASE  IF NOT EXISTS `healthcaredb1` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `healthcaredb1`;
-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: healthcaredb1
-- ------------------------------------------------------
-- Server version	8.0.34

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
-- Table structure for table `ailment`
--

DROP TABLE IF EXISTS `ailment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ailment` (
  `ailment_id` int NOT NULL AUTO_INCREMENT,
  `ailment_name` varchar(255) NOT NULL,
  `ailment_description` varchar(1000) NOT NULL,
  PRIMARY KEY (`ailment_id`),
  UNIQUE KEY `ailment_name` (`ailment_name`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ailment`
--

LOCK TABLES `ailment` WRITE;
/*!40000 ALTER TABLE `ailment` DISABLE KEYS */;
INSERT INTO `ailment` VALUES (4,'Fractured Bone','Break or crack in a bone resulting from trauma or injury.'),(5,'Osteoarthritis','Degenerative joint disease causing cartilage breakdown and pain.'),(6,'Myocardial Infarction','Commonly known as a heart attack, caused by a blocked blood vessel to the heart.'),(7,'Heart Failure','Condition where the heart cannot pump blood effectively, leading to insufficient circulation.');
/*!40000 ALTER TABLE `ailment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `appointment`
--

DROP TABLE IF EXISTS `appointment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appointment` (
  `appointment_id` int NOT NULL AUTO_INCREMENT,
  `practitioner_id` int NOT NULL,
  `patient_id` int NOT NULL,
  `appointment_date` date NOT NULL,
  `appointment_hour` int NOT NULL,
  `booking_date` date NOT NULL,
  `appointment_status` varchar(20) NOT NULL,
  PRIMARY KEY (`appointment_id`),
  UNIQUE KEY `unique_appointment` (`practitioner_id`,`patient_id`,`appointment_date`,`appointment_hour`),
  KEY `patient_id` (`patient_id`),
  CONSTRAINT `appointment_ibfk_1` FOREIGN KEY (`practitioner_id`) REFERENCES `practitioner` (`practitioner_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `appointment_ibfk_2` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointment`
--

LOCK TABLES `appointment` WRITE;
/*!40000 ALTER TABLE `appointment` DISABLE KEYS */;
INSERT INTO `appointment` VALUES (20,10,15,'2023-12-12',12,'2023-12-08','Accepted'),(21,10,16,'2023-12-15',8,'2023-12-08','Accepted'),(22,10,16,'2023-12-16',9,'2023-12-08','Accepted'),(23,9,16,'2023-12-17',10,'2023-12-08','pending'),(24,10,17,'2023-12-15',7,'2023-12-08','pending'),(25,10,17,'2023-12-17',8,'2023-12-08','Accepted'),(26,9,17,'2023-12-19',11,'2023-12-08','pending'),(27,10,18,'2023-12-20',8,'2023-12-08','Accepted'),(28,10,18,'2023-12-18',7,'2023-12-08','Accepted'),(29,9,18,'2023-12-16',9,'2023-12-08','pending'),(30,9,19,'2023-12-19',8,'2023-12-08','pending'),(31,10,19,'2023-12-17',6,'2023-12-08','pending'),(32,11,19,'2023-12-14',3,'2023-12-08','pending'),(33,10,20,'2023-12-16',14,'2023-12-08','Rejected'),(34,10,20,'2023-12-17',4,'2023-12-08','pending'),(35,9,20,'2023-12-15',13,'2023-12-08','pending'),(38,11,15,'2023-12-26',16,'2023-12-08','pending'),(39,8,15,'2023-12-26',12,'2023-12-08','pending');
/*!40000 ALTER TABLE `appointment` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `before_appointment_insert` BEFORE INSERT ON `appointment` FOR EACH ROW BEGIN
    IF NEW.appointment_hour < 0 OR NEW.appointment_hour > 24 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Invalid appointment hour. It should be between 0 and 24.';
    END IF;

    IF NEW.appointment_date <= NEW.booking_date THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Invalid dates. Appointment date should be after booking date.';
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `before_insert_appointment` BEFORE INSERT ON `appointment` FOR EACH ROW BEGIN
    DECLARE appointment_count INT;
    SELECT COUNT(*) INTO appointment_count
    FROM appointment
    WHERE patient_id = NEW.patient_id
    AND booking_date = NEW.booking_date;
    
    IF appointment_count >= 3 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Cannot create more than 3 appointments on the same booking_date';
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `assess`
--

DROP TABLE IF EXISTS `assess`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `assess` (
  `assess_id` int NOT NULL AUTO_INCREMENT,
  `ailment_id` int NOT NULL,
  `practitioner_id` int NOT NULL,
  `patient_id` int NOT NULL,
  `severity` varchar(255) NOT NULL,
  `assessement_date` date NOT NULL,
  `case_description` text,
  PRIMARY KEY (`assess_id`),
  KEY `ailment_id` (`ailment_id`),
  KEY `practitioner_id` (`practitioner_id`),
  KEY `patient_id` (`patient_id`),
  CONSTRAINT `assess_ibfk_1` FOREIGN KEY (`ailment_id`) REFERENCES `ailment` (`ailment_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `assess_ibfk_2` FOREIGN KEY (`practitioner_id`) REFERENCES `practitioner` (`practitioner_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `assess_ibfk_3` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assess`
--

LOCK TABLES `assess` WRITE;
/*!40000 ALTER TABLE `assess` DISABLE KEYS */;
INSERT INTO `assess` VALUES (39,4,10,15,'High','2023-12-08','Ribs has been fractured'),(40,5,10,15,'Minor','2023-12-08','Joint pain in both the ankle'),(41,6,10,15,'Moderate','2023-12-08','Breating issues'),(42,4,9,16,'Medium','2023-12-08','Rib fractured'),(43,7,9,16,'High','2023-12-08','Heart blood vessel issues'),(44,5,9,16,'Moderate','2023-12-08','Pain in ankle joint'),(45,5,10,15,'High','2023-12-08','bad bones'),(46,4,10,15,'Moderate','2023-12-08','bad case'),(47,4,10,15,'High','2023-12-08','broken neck'),(48,5,10,15,'High','2023-12-08','Bone damage');
/*!40000 ALTER TABLE `assess` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hospital`
--

DROP TABLE IF EXISTS `hospital`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hospital` (
  `name` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `st_no` varchar(10) NOT NULL,
  `st_name` varchar(50) NOT NULL,
  `state` varchar(50) NOT NULL,
  `zipcode` varchar(10) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hospital`
--

LOCK TABLES `hospital` WRITE;
/*!40000 ALTER TABLE `hospital` DISABLE KEYS */;
INSERT INTO `hospital` VALUES ('BoneHeal Clinic','456 Wellness St','Unit 9','Pine Lane','New York','10001'),('BrainHealth Hospital','101 Mindfulness Ave','Apt 3B','Cypress Lane','Florida','33101'),('HeartCare Hospital','123 Health Blvd','Apt 5','Maple Avenue','California','90210'),('SkinWell Medical Center','789 Dermatology Dr','Suite 2','Oak Street','Texas','75001'),('Visionary Medical Center','234 Insight St','Unit 12C','Willow Avenue','Illinois','60601');
/*!40000 ALTER TABLE `hospital` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medicine`
--

DROP TABLE IF EXISTS `medicine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicine` (
  `medicine_id` int NOT NULL AUTO_INCREMENT,
  `medicine_name` varchar(255) NOT NULL,
  `medicine_desc` text,
  PRIMARY KEY (`medicine_id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicine`
--

LOCK TABLES `medicine` WRITE;
/*!40000 ALTER TABLE `medicine` DISABLE KEYS */;
INSERT INTO `medicine` VALUES (32,'Paracetamol','Used to relieve pain and reduce fever.'),(33,'Ibuprofen','Nonsteroidal anti-inflammatory drug (NSAID) for pain relief.'),(34,'Amoxicillin','Antibiotic used to treat bacterial infections.'),(35,'Aspirin','Commonly used for pain relief and anti-inflammatory purposes.'),(36,'Paracetamol','Used to relieve pain and reduce fever.'),(37,'Ibuprofen','Nonsteroidal anti-inflammatory drug (NSAID) for pain relief.'),(38,'Amoxicillin','Antibiotic used to treat bacterial infections.'),(39,'Aspirin','Commonly used for pain relief and anti-inflammatory purposes.'),(40,'Ibuprofen','Nonsteroidal anti-inflammatory drug (NSAID) for pain relief and inflammation.'),(41,'Acetaminophen','Pain reliever and fever reducer often used for mild to moderate pain.'),(42,'Codeine','Opioid analgesic used for moderate to severe pain.'),(43,'Calcium Supplement','Supports bone health and aids in bone healing.');
/*!40000 ALTER TABLE `medicine` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient`
--

DROP TABLE IF EXISTS `patient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patient` (
  `patient_id` int NOT NULL AUTO_INCREMENT,
  `email_id` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `phone_no` varchar(15) NOT NULL,
  `address` varchar(255) NOT NULL,
  `st_no` varchar(10) NOT NULL,
  `st_name` varchar(50) NOT NULL,
  `state` varchar(50) NOT NULL,
  `zipcode` varchar(10) NOT NULL,
  `blood_group` varchar(5) NOT NULL,
  `ethnicity` varchar(50) NOT NULL,
  `sex` varchar(10) NOT NULL,
  `date_of_birth` date NOT NULL,
  PRIMARY KEY (`patient_id`),
  UNIQUE KEY `email_id` (`email_id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient`
--

LOCK TABLES `patient` WRITE;
/*!40000 ALTER TABLE `patient` DISABLE KEYS */;
INSERT INTO `patient` VALUES (15,'olivia.jones@example.com','passOlivia123','Olivia','Jones','555-111-2222','123 Willow St','Apt 7','Cypress Lane','California','90210','O+','Caucasian','Female','1992-08-15'),(16,'malik.smith@example.com','secureMalik456','Malik','Smith','555-333-4444','456 Pine St','Unit 18','Maple Avenue','New York','10001','A+','African American','Male','1987-05-22'),(17,'isabella.nguyen@example.com','myIsabella789','Isabella','Nguyen','555-555-6666','789 Oak St','Suite 3B','Pine Lane','Texas','75001','B-','Asian','Female','1990-11-18'),(18,'mohammed.alvarez@example.com','passMohammed123','Mohammed','Alvarez','555-777-8888','101 Birch St','Apt 12C','Cedar Avenue','Florida','33101','AB+','Hispanic','Male','1984-03-10'),(19,'emily.kumar@example.com','secureEmily456','Emily','Kumar','555-999-0000','234 Cedar St','Unit 5A','Birch Lane','Illinois','60601','A-','Asian','Female','1995-09-28'),(20,'sean.gonzalez@example.com','mySean789','Sean','Gonzalez','555-123-4567','345 Maple St','Apt 15D','Willow Avenue','Florida','33102','B+','Hispanic','Male','1989-12-03'),(21,'zoey.white@example.com','passZoey123','Zoey','White','555-234-5678','567 Elm St','Suite 8','Pine Lane','California','90211','O-','Caucasian','Female','1993-07-14'),(22,'vivek.patel@example.com','secureVivek456','Vivek','Patel','555-345-6789','678 Oak St','Apt 23','Maple Avenue','Texas','75002','A+','Asian','Male','1986-01-25'),(23,'aubrey.diaz@example.com','myAubrey789','Aubrey','Diaz','555-456-7890','789 Cedar St','Unit 10B','Cypress Lane','New York','10002','AB-','Hispanic','Female','1991-04-07'),(24,'leo.nguyen@example.com','passLeo123','Leo','Nguyen','555-567-8901','890 Pine St','Suite 6','Birch Avenue','Illinois','60602','B+','Asian','Male','1983-06-12');
/*!40000 ALTER TABLE `patient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `performs`
--

DROP TABLE IF EXISTS `performs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `performs` (
  `performance_id` int NOT NULL AUTO_INCREMENT,
  `test_id` int NOT NULL,
  `patient_id` int NOT NULL,
  `practitioner_id` int NOT NULL,
  `test_date` date NOT NULL,
  `findings` text,
  PRIMARY KEY (`performance_id`),
  KEY `test_id` (`test_id`),
  KEY `practitioner_id` (`practitioner_id`),
  KEY `patient_id` (`patient_id`),
  CONSTRAINT `performs_ibfk_1` FOREIGN KEY (`test_id`) REFERENCES `test` (`test_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `performs_ibfk_2` FOREIGN KEY (`practitioner_id`) REFERENCES `practitioner` (`practitioner_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `performs_ibfk_3` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `performs`
--

LOCK TABLES `performs` WRITE;
/*!40000 ALTER TABLE `performs` DISABLE KEYS */;
INSERT INTO `performs` VALUES (4,8,15,10,'2023-12-08','Broken Rib'),(5,8,15,10,'2023-12-09','Broken Fingers'),(7,10,15,10,'2023-12-08','Normal'),(9,8,15,10,'2023-12-12','Broken bones');
/*!40000 ALTER TABLE `performs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `practitioner`
--

DROP TABLE IF EXISTS `practitioner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `practitioner` (
  `practitioner_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `phone_no` varchar(15) NOT NULL,
  `department` varchar(50) NOT NULL,
  `hospital_name` varchar(255) NOT NULL,
  PRIMARY KEY (`practitioner_id`),
  UNIQUE KEY `username` (`username`),
  KEY `hospital_name` (`hospital_name`),
  KEY `username_2` (`username`),
  CONSTRAINT `practitioner_ibfk_1` FOREIGN KEY (`hospital_name`) REFERENCES `hospital` (`name`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `practitioner`
--

LOCK TABLES `practitioner` WRITE;
/*!40000 ALTER TABLE `practitioner` DISABLE KEYS */;
INSERT INTO `practitioner` VALUES (8,'dr_smith','securepass123','John','Smith','123-456-7890','Cardiology','HeartCare Hospital'),(9,'doc_jones','mypassword456','Emily','Jones','987-654-3210','Orthopedics','BoneHeal Clinic'),(10,'dr_kumar','pass123word','Amit','Kumar','555-123-7890','Dermatology','SkinWell Medical Center'),(11,'dr_white','securepass789','Sarah','White','777-888-9999','Neurology','BrainHealth Hospital');
/*!40000 ALTER TABLE `practitioner` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `practitionerspecialization`
--

DROP TABLE IF EXISTS `practitionerspecialization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `practitionerspecialization` (
  `practitioner_id` int NOT NULL,
  `specialization_id` int NOT NULL,
  PRIMARY KEY (`practitioner_id`,`specialization_id`),
  UNIQUE KEY `unique_practitioner_specialization` (`practitioner_id`,`specialization_id`),
  UNIQUE KEY `uniquepractitionerspecialization` (`practitioner_id`,`specialization_id`),
  KEY `specialization_id` (`specialization_id`),
  CONSTRAINT `practitionerspecialization_ibfk_1` FOREIGN KEY (`practitioner_id`) REFERENCES `practitioner` (`practitioner_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `practitionerspecialization_ibfk_2` FOREIGN KEY (`specialization_id`) REFERENCES `specialization` (`specialization_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `practitionerspecialization`
--

LOCK TABLES `practitionerspecialization` WRITE;
/*!40000 ALTER TABLE `practitionerspecialization` DISABLE KEYS */;
INSERT INTO `practitionerspecialization` VALUES (8,11),(10,11),(9,12),(10,12),(9,13),(10,13),(8,14),(11,14),(9,15),(11,15);
/*!40000 ALTER TABLE `practitionerspecialization` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prescribe`
--

DROP TABLE IF EXISTS `prescribe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prescribe` (
  `prescription_id` int NOT NULL AUTO_INCREMENT,
  `practitioner_id` int NOT NULL,
  `assess_id` int NOT NULL,
  `medicine_id` int NOT NULL,
  `prescription_date` date NOT NULL,
  `start_date` date NOT NULL,
  `no_of_times_a_day` int NOT NULL,
  `quantity_in_mg` int NOT NULL,
  `duration_in_days` int NOT NULL,
  `dosage_desc` text,
  PRIMARY KEY (`prescription_id`),
  KEY `practitioner_id` (`practitioner_id`),
  KEY `assess_id` (`assess_id`),
  KEY `medicine_id` (`medicine_id`),
  CONSTRAINT `prescribe_ibfk_1` FOREIGN KEY (`practitioner_id`) REFERENCES `practitioner` (`practitioner_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `prescribe_ibfk_2` FOREIGN KEY (`assess_id`) REFERENCES `assess` (`assess_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `prescribe_ibfk_3` FOREIGN KEY (`medicine_id`) REFERENCES `medicine` (`medicine_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescribe`
--

LOCK TABLES `prescribe` WRITE;
/*!40000 ALTER TABLE `prescribe` DISABLE KEYS */;
INSERT INTO `prescribe` VALUES (2,10,39,41,'2023-12-08','2023-12-08',2,320,1,'Take it after dinner'),(3,10,39,43,'2023-12-08','2023-12-08',1,200,1,'Take it after breakfast'),(4,9,42,43,'2023-12-08','2023-12-09',3,300,1,'Take it after breakfast'),(5,9,42,41,'2023-12-08','2023-12-09',1,300,4,'Take it after lunch'),(6,9,42,40,'2023-12-08','2023-12-09',2,400,5,'Take it after dinner'),(7,10,46,33,'2023-12-08','2023-12-12',3,200,5,'take small doese'),(8,10,39,32,'2023-12-08','2023-12-12',4,3,5,'Take before lunch'),(9,10,39,32,'2023-12-08','2023-12-12',5,5,2,'1');
/*!40000 ALTER TABLE `prescribe` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `specialization`
--

DROP TABLE IF EXISTS `specialization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `specialization` (
  `specialization_id` int NOT NULL AUTO_INCREMENT,
  `specialization_name` varchar(255) NOT NULL,
  `specialization_desc` text NOT NULL,
  PRIMARY KEY (`specialization_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `specialization`
--

LOCK TABLES `specialization` WRITE;
/*!40000 ALTER TABLE `specialization` DISABLE KEYS */;
INSERT INTO `specialization` VALUES (11,'Cardiology','Specializing in heart-related conditions and diseases.'),(12,'Orthopedics','Dealing with musculoskeletal system issues and injuries.'),(13,'Dermatology','Focusing on skin disorders and conditions.'),(14,'Neurology','Specialized in disorders of the nervous system.'),(15,'Gastroenterology','Dealing with digestive system disorders and diseases.');
/*!40000 ALTER TABLE `specialization` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test`
--

DROP TABLE IF EXISTS `test`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `test` (
  `test_id` int NOT NULL AUTO_INCREMENT,
  `test_name` varchar(256) NOT NULL,
  `test_desc` varchar(1024) NOT NULL,
  PRIMARY KEY (`test_id`),
  UNIQUE KEY `test_name` (`test_name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test`
--

LOCK TABLES `test` WRITE;
/*!40000 ALTER TABLE `test` DISABLE KEYS */;
INSERT INTO `test` VALUES (7,'ECG','Electrocardiogram to measure the electrical activity of the heart.'),(8,'X-Ray','Radiographic imaging to visualize internal structures.'),(9,'Blood Test','Analysis of blood samples for various medical indicators.'),(10,'MRI','Magnetic Resonance Imaging for detailed internal imaging.');
/*!40000 ALTER TABLE `test` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'healthcaredb1'
--

--
-- Dumping routines for database 'healthcaredb1'
--
/*!50003 DROP PROCEDURE IF EXISTS `AddAssessment` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `AddAssessment`(
    IN p_ailment_id INT,
    IN p_practitioner_id INT,
    IN p_patient_id INT,
    IN p_severity VARCHAR(255),
    IN p_assessment_date DATE,
    IN p_case_description TEXT
)
BEGIN
    INSERT INTO Assess (
        ailment_id, 
        practitioner_id, 
        patient_id, 
        severity, 
        assessement_date, 
        case_description
    ) 
    VALUES (
        p_ailment_id, 
        p_practitioner_id, 
        p_patient_id, 
        p_severity, 
        p_assessment_date, 
        p_case_description
    );
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `AddPrescription` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `AddPrescription`(
    IN p_practitioner_id INT,
    IN p_assess_id INT,
    IN p_medicine_id INT,
    IN p_prescription_date DATE,
    IN p_start_date DATE,
    IN p_no_of_times_a_day INT,
    IN p_quantity_in_mg INT,
    IN p_duration_in_days INT,
    IN p_dosage_desc TEXT
)
BEGIN
    INSERT INTO Prescribe (practitioner_id, assess_id, medicine_id, prescription_date, start_date, no_of_times_a_day, quantity_in_mg, duration_in_days, dosage_desc) 
    VALUES (p_practitioner_id, p_assess_id, p_medicine_id, p_prescription_date, p_start_date, p_no_of_times_a_day, p_quantity_in_mg, p_duration_in_days, p_dosage_desc);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `add_appointment` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `add_appointment`(
    IN a_practitioner_id INT,
    IN a_patient_id INT,
    IN a_appointment_date DATE,
    IN a_appointment_hour INT,
    IN a_booking_date DATE,
    IN a_appointment_status VARCHAR(20)
)
BEGIN
    INSERT INTO appointment (practitioner_id, patient_id, appointment_date, appointment_hour, booking_date, appointment_status) 
    VALUES (a_practitioner_id, a_patient_id, a_appointment_date, a_appointment_hour, a_booking_date, a_appointment_status);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `CheckPatientCredentials` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `CheckPatientCredentials`(
    IN p_email_id VARCHAR(255),
    IN p_password VARCHAR(255)
)
BEGIN
    -- Check if the email and password match
    IF EXISTS (SELECT 1 FROM Patient WHERE email_id = p_email_id AND password = p_password) THEN
        -- If they match, select and return the patient's details
        SELECT * FROM Patient WHERE email_id = p_email_id;
    ELSE
        -- If not, return a custom error message using SIGNAL SQLSTATE
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error: Invalid credentials or patient does not exist.';
    END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `CheckPractitionerCredentials` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `CheckPractitionerCredentials`(
    IN p_username VARCHAR(255),
    IN p_password VARCHAR(255)
)
BEGIN
    -- Check if the username and password match
    IF EXISTS (SELECT 1 FROM Practitioner WHERE username = p_username AND password = p_password) THEN
        -- If they match, select and return the practitioner's details
        SELECT practitioner_id, first_name, last_name, department, hospital_name 
        FROM Practitioner 
        WHERE username = p_username;
    ELSE
        -- If not, return a custom error message using SIGNAL SQLSTATE
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error: Invalid credentials or practitioner does not exist.';
    END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `count_pending_appointments` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `count_pending_appointments`(IN p_practitioner_id INT)
BEGIN
    SELECT COUNT(*) AS pendingCount
    FROM appointment
    WHERE practitioner_id = p_practitioner_id
    AND appointment_status = 'Pending';
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `DeleteAssessment` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `DeleteAssessment`(IN p_assess_id INT)
BEGIN
    DELETE FROM Assess WHERE assess_id = p_assess_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `DeletePatient` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `DeletePatient`(IN p_patient_id INT)
BEGIN
    -- Delete related records from Access table
    DELETE FROM Assess WHERE patient_id = p_patient_id;

    -- Then delete the patient record
    DELETE FROM Patient WHERE patient_id = p_patient_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `DeletePerforms` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `DeletePerforms`(IN p_performance_id INT)
BEGIN
    DELETE FROM Performs WHERE performance_id = p_performance_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `DeletePrescription` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `DeletePrescription`(IN p_prescription_id INT)
BEGIN
    DELETE FROM Prescribe WHERE prescription_id = p_prescription_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `delete_appointment` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `delete_appointment`(IN a_appointment_id INT)
BEGIN
    DELETE FROM appointment WHERE appointment_id = a_appointment_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `GetAilmentByLikeString` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `GetAilmentByLikeString`(
    IN lik_string VARCHAR(255)
)
BEGIN
    SELECT * 
    FROM Ailment 
    WHERE ailment_name LIKE CONCAT('%', lik_string, '%') 
       AND ailment_description LIKE CONCAT('%', lik_string, '%');
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `GetAilmentDetails` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `GetAilmentDetails`(IN lik_string VARCHAR(255), IN p_patient_id INT)
BEGIN
    SELECT 
        a.ailment_name,
        CONCAT(p.first_name, ' ', p.last_name) AS patient_name, 
        CONCAT(pr.first_name, ' ', pr.last_name) AS practitioner_name, 
        asss.severity,
        asss.assessement_date
    FROM 
        Assess asss
        JOIN Ailment a ON asss.ailment_id = a.ailment_id
        JOIN Patient p ON asss.patient_id = p.patient_id
        JOIN Practitioner pr ON asss.practitioner_id = pr.practitioner_id
    WHERE 
        a.ailment_name LIKE CONCAT('%', lik_string, '%') 
        AND asss.patient_id = p_patient_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `GetAilmentName` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `GetAilmentName`(IN ailmentID int)
BEGIN
    SELECT ailment_name
    FROM 
    ailment
    WHERE 
        ailment_id = ailmentID;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `GetAilmentNameDetails` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `GetAilmentNameDetails`(IN lik_string VARCHAR(255))
BEGIN
    SELECT *
    FROM 
	Ailment
    WHERE 
        ailment_name LIKE CONCAT('%', lik_string, '%');
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `getAssessment` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getAssessment`(IN p_assess_id INT)
BEGIN
    SELECT practitioner.first_name as prac_fname,
    practitioner.last_name as prac_lname,
    patient.first_name as p_fname,
    patient.last_name as p_lname,
    ailment_name,
    ailment_description,
    assess.case_description,
    assess.severity,
    assess.assessement_Date
    FROM Assess 
    JOIN ailment on assess.ailment_id = ailment.ailment_id 
    JOIN practitioner on assess.practitioner_id = practitioner.practitioner_id
    JOIN patient on assess.patient_id = patient.patient_id
    WHERE assess_id = p_assess_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `GetAssessmentDetails` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `GetAssessmentDetails`(IN lik_string VARCHAR(255), IN p_patient_id INT)
BEGIN
    SELECT 
        *
    FROM 
        Assess asss
        JOIN Ailment a ON asss.ailment_id = a.ailment_id
        JOIN Patient p ON asss.patient_id = p.patient_id
        JOIN Practitioner pr ON asss.practitioner_id = pr.practitioner_id
    WHERE 
        a.ailment_name LIKE CONCAT('%', lik_string, '%') 
        AND asss.patient_id = p_patient_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `getMedicineDetails` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getMedicineDetails`(
    IN p_medicine_id INT
)
BEGIN
    SELECT * FROM medicine 
    WHERE medicine_id = p_medicine_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `GetPatientInfo` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `GetPatientInfo`(IN p_patient_id INT)
BEGIN
    SELECT * FROM Patient WHERE patient_id = p_patient_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `GetPatientPractitionerAilmentInfo` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `GetPatientPractitionerAilmentInfo`(
    IN p_patient_id INT,
    IN p_ailment_name_like VARCHAR(255)
)
BEGIN
    SELECT 
        Patient.*,
        Practitioner.*,
        Ailment.*
    FROM 
        Assess
    JOIN Patient ON Assess.patient_id = Patient.patient_id
    JOIN Practitioner ON Assess.practitioner_id = Practitioner.practitioner_id
    JOIN Ailment ON Assess.ailment_id = Ailment.ailment_id
    WHERE 
        Assess.patient_id = p_patient_id AND 
        Ailment.ailment_name LIKE CONCAT('%', p_ailment_name_like, '%');
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `GetPractitionerSpecializations` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `GetPractitionerSpecializations`(IN prac_id int)
BEGIN
    SELECT *
    FROM 
	practitionerspecialization
    WHERE 
        practitioner_id = prac_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `getPrescriptionDetail` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getPrescriptionDetail`(
	IN p_id INT
 )
BEGIN 
	SELECT * FROM prescribe
    WHERE prescribe.prescription_id = p_id;
 END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `GetPrescriptionDetails` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `GetPrescriptionDetails`(
    IN pres_id INT   
)
BEGIN
    SELECT 
        PS.prescription_id AS prescription_id,
        PS.prescription_date AS prescription_date,
        PS.start_date AS prescription_start_date,
        PS.no_of_times_a_day AS no_of_times_a_day,
        PS.quantity_in_mg AS quantity_in_mg,
	    PS.duration_in_days AS duration_in_days,
        PS.dosage_desc AS dosage_desc,
        P.practitioner_id AS prac_pres_id,
        PR.practitioner_id AS prac_assess_id,
        M.medicine_id AS medicine_id,
        AL.ailment_id AS ailment_id,
        PAI.patient_id AS patient_id,
        P.first_name AS prac_pres_fn,
        P.last_name AS prac_pres_ln,
        PR.first_name AS prac_assess_fn,
        PR.last_name AS prac_assess_ln,
        M.medicine_name AS medicine_name,
        AL.ailment_name AS ailment_name,
        PAI.first_name AS patient_fn,
        PAI.last_name AS patient_ln,
        A.assess_id AS assess_id
    FROM 
        Prescribe PS
        JOIN Assess A ON PS.assess_id = A.assess_id
        JOIN Ailment AL ON A.ailment_id = AL.ailment_id
        JOIN Practitioner PR ON A.practitioner_id = PR.practitioner_id
        JOIN Patient PAI ON A.patient_id = PAI.patient_id
        JOIN Medicine M ON PS.medicine_id = M.medicine_id
        JOIN Practitioner P ON PS.practitioner_id = P.practitioner_id
    WHERE 
       PS.prescription_id = pres_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `GetTest` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `GetTest`(
    IN text_like VARCHAR(255)
)
BEGIN
    SELECT 
	T.test_id as test_id,
    T.test_name as test_name,
    T.test_desc as test_desc
    FROM 
        Test T
    WHERE 
        T.test_name LIKE CONCAT('%', text_like, '%') OR
        T.test_desc LIKE CONCAT('%', text_like, '%');
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `GetTreatmentDetails` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `GetTreatmentDetails`(
    IN text_like VARCHAR(255),
    IN p_patient_id INT
)
BEGIN
    SELECT 
        PS.prescription_id AS prescription_id,
        P.practitioner_id AS prac_pres_id,
        PR.practitioner_id AS prac_assess_id,
        M.medicine_id AS medicine_id,
        AL.ailment_id AS ailment_id,
        PAI.patient_id AS patient_id,
        P.first_name AS prac_pres_fn,
        P.last_name AS prac_pres_ln,
        PR.first_name AS prac_assess_fn,
        PR.last_name AS prac_assess_ln,
        M.medicine_name AS medicine_name,
        AL.ailment_name AS ailment_name,
        PAI.first_name AS paitient_fn,
        PAI.last_name AS paitient_ln
    FROM 
        Prescribe PS
        JOIN Assess A ON PS.assess_id = A.assess_id
        JOIN Ailment AL ON A.ailment_id = AL.ailment_id
        JOIN Practitioner PR ON A.practitioner_id = PR.practitioner_id
        JOIN Patient PAI ON A.patient_id = PAI.patient_id
        JOIN Medicine M ON PS.medicine_id = M.medicine_id
        JOIN Practitioner P ON PS.practitioner_id = P.practitioner_id
    WHERE 
        (
        M.medicine_name LIKE CONCAT('%', text_like, '%') OR
        P.first_name LIKE CONCAT('%', text_like, '%') OR
        P.last_name LIKE CONCAT('%', text_like, '%') OR
        AL.ailment_name LIKE CONCAT('%', text_like, '%') OR
        PR.first_name LIKE CONCAT('%', text_like, '%') OR
        PR.last_name LIKE CONCAT('%', text_like, '%')
    )
    AND PAI.patient_id = p_patient_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `get_appointment` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_appointment`(
    a_appointment_id INT
)
BEGIN
    SELECT 
	A.appointment_id as appointment_id,
    A.practitioner_id as practitioner_id,
    A.patient_id as patient_id,
	A.appointment_date as appointment_date,
    A.appointment_status  as appointment_status 
    FROM 
        appointment A
    WHERE 
        a_appointment_id = appointment_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `get_not_pending_appointments` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_not_pending_appointments`(
    patient_id INT,
    IN text_like VARCHAR(255)
)
BEGIN
	SELECT 
    P.patient_id as patient_id,
    P.first_name as patient_fn,
    P.last_name as patient_ln,
    P.phone_no as phone_number,
    P.blood_group as blood_group,
    P.ethnicity as ethnicity,
    P.sex as sex,
    P.date_of_birth as date_of_birth,
    A.appointment_id  as appointment_id,
    A.appointment_date as appointment_date,
    A.appointment_hour as appointment_hour,
    A.booking_date as booking_date,
    A.appointment_status  as appointment_status,
    PR.practitioner_id as practitioner_id,
    PR.first_name as practitioner_fn,
    PR.last_name as practitioner_ln,
    PR.phone_no  as prac_phone_no,
    PR.department  as department,
    PR.hospital_name as hospital_name
    FROM 
        patient P
        JOIN appointment A ON A.patient_id = P.patient_id
        JOIN practitioner PR ON PR.practitioner_id = A.practitioner_id
    WHERE 
        (PR.first_name LIKE CONCAT('%', text_like, '%') OR
        PR.last_name LIKE CONCAT('%', text_like, '%'))AND
        P.patient_id = patient_id AND
        NOT A.appointment_status = 'Pending';
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `get_pending_appointments` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_pending_appointments`(
    patient_id INT,
    IN text_like VARCHAR(255)
)
BEGIN
    SELECT 
    P.patient_id as patient_id,
    P.first_name as patient_fn,
    P.last_name as patient_ln,
    P.phone_no as phone_number,
    P.blood_group as blood_group,
    P.ethnicity as ethnicity,
    P.sex as sex,
    P.date_of_birth as date_of_birth,
    A.appointment_id  as appointment_id,
    A.appointment_date as appointment_date,
    A.appointment_hour as appointment_hour,
    A.booking_date as booking_date,
    A.appointment_status  as appointment_status,
    PR.practitioner_id as practitioner_id,
    PR.first_name as practitioner_fn,
    PR.last_name as practitioner_ln,
    PR.phone_no  as prac_phone_no,
    PR.department  as department,
    PR.hospital_name as hospital_name
    FROM 
        patient P
        JOIN appointment A ON A.patient_id = P.patient_id
        JOIN practitioner PR ON PR.practitioner_id = A.practitioner_id
    WHERE 
        (PR.first_name LIKE CONCAT('%', text_like, '%') OR
        PR.last_name LIKE CONCAT('%', text_like, '%'))AND
        P.patient_id = patient_id AND
        A.appointment_status = 'Pending';
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `get_performance_details` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_performance_details`(
    performance_id INT
)
BEGIN
    SELECT 
	T.test_id as test_id,
    T.test_name as test_name,
    T.test_desc as test_desc,
	P.patient_id as patient_id,
    P.practitioner_id as practitioner_id,
    P.performance_id as performance_id,
    PA.first_name as patient_fn,
    PA.last_name as patient_ln,
    PRAC.first_name as practitioner_fn,
    PRAC.last_name as practitioner_ln,
    P.test_date as test_date,
    P.findings as findings
    FROM 
        test T
        JOIN performs P ON P.test_id = T.test_id
        JOIN patient PA ON PA.patient_id = P.patient_id
        JOIN practitioner PRAC ON P.practitioner_id = PRAC.practitioner_id
    WHERE 
        P.performance_id = performance_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `get_performs` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_performs`(
    IN text_like VARCHAR(255),
    patient_id INT
)
BEGIN
    SELECT 
	T.test_id as test_id,
    T.test_name as test_name,
    T.test_desc as test_desc,
	P.patient_id as patient_id,
    P.practitioner_id as practitioner_id,
    PA.first_name as patient_fn,
    PA.last_name as patient_ln,
    PRAC.first_name as practitioner_fn,
    PRAC.last_name as practitioner_ln,
    P.performance_id as performance_id,
    P.test_date as test_date,
    P.findings as findings
    FROM 
        test T
        JOIN performs P ON P.test_id = T.test_id
        JOIN patient PA ON PA.patient_id = P.patient_id
        JOIN practitioner PRAC ON P.practitioner_id = PRAC.practitioner_id
    WHERE 
        T.test_name LIKE CONCAT('%', text_like, '%') OR
        T.test_desc LIKE CONCAT('%', text_like, '%') OR
        PA.first_name LIKE CONCAT('%', text_like, '%') OR
        PA.last_name LIKE CONCAT('%', text_like, '%')OR
        PRAC.first_name LIKE CONCAT('%', text_like, '%') OR
        PRAC.last_name LIKE CONCAT('%', text_like, '%') AND
        PA.patient_id = patient_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `get_practitioner_name` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_practitioner_name`(
    IN text_like VARCHAR(255)
)
BEGIN
    SELECT 
    P.practitioner_id as practitioner_id,
    P.first_name as patient_fn,
    P.last_name as patient_ln,
    P.hospital_name as hospital_name
    FROM 
        practitioner P
    WHERE 
        P.first_name LIKE CONCAT('%', text_like, '%') OR
        P.last_name LIKE CONCAT('%', text_like, '%') OR
        P.hospital_name LIKE CONCAT('%', text_like, '%');
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `get_practitioner_not_pending_appointments` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_practitioner_not_pending_appointments`(
    practitioner_id INT,
    IN text_like VARCHAR(255)
)
BEGIN
    SELECT 
    P.patient_id as patient_id,
    P.first_name as patient_fn,
    P.last_name as patient_ln,
    P.phone_no as phone_number,
    P.blood_group as blood_group,
    P.ethnicity as ethnicity,
    P.sex as sex,
    P.date_of_birth as date_of_birth,
	A.appointment_id  as appointment_id,
    A.appointment_date as appointment_date,
    A.appointment_hour as appointment_hour,
    A.booking_date as booking_date,
    A.appointment_status  as appointment_status,
    PR.practitioner_id as practitioner_id,
    PR.first_name as practitioner_fn,
    PR.last_name as practitioner_ln,
    PR.phone_no  as prac_phone_no,
    PR.department  as department,
    PR.hospital_name as hospital_name
    FROM 
        Practitioner PR
        JOIN appointment A ON A.practitioner_id = PR.practitioner_id
        JOIN Patient P ON P.patient_id = A.patient_id
    WHERE 
        (P.first_name LIKE CONCAT('%', text_like, '%') OR
        P.last_name LIKE CONCAT('%', text_like, '%'))AND
        PR.practitioner_id = practitioner_id AND
        NOT A.appointment_status = 'pending';
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `get_practitioner_pending_appointments` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_practitioner_pending_appointments`(
    practitioner_id INT,
    IN text_like VARCHAR(255)
)
BEGIN
    SELECT 
    P.patient_id as patient_id,
    P.first_name as patient_fn,
    P.last_name as patient_ln,
    P.phone_no as phone_number,
    P.blood_group as blood_group,
    P.ethnicity as ethnicity,
    P.sex as sex,
    P.date_of_birth as date_of_birth,
	A.appointment_id  as appointment_id,
    A.appointment_date as appointment_date,
    A.appointment_hour as appointment_hour,
    A.booking_date as booking_date,
    A.appointment_status  as appointment_status,
    PR.practitioner_id as practitioner_id,
    PR.first_name as practitioner_fn,
    PR.last_name as practitioner_ln,
    PR.phone_no  as prac_phone_no,
    PR.department  as department,
    PR.hospital_name as hospital_name
    FROM 
        Practitioner PR
        JOIN appointment A ON A.practitioner_id = PR.practitioner_id
        JOIN Patient P ON P.patient_id = A.patient_id
    WHERE 
        (P.first_name LIKE CONCAT('%', text_like, '%') OR
        P.last_name LIKE CONCAT('%', text_like, '%'))AND
        PR.practitioner_id = practitioner_id AND
        A.appointment_status = 'pending';
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `get_practitioner_specialization` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_practitioner_specialization`(IN prac_id INT)
BEGIN
    SELECT GROUP_CONCAT(specialization_name) AS specialization_names
    FROM practitionerspecialization PS
    JOIN specialization S ON PS.specialization_id = S.specialization_id
    WHERE practitioner_id = prac_id
    GROUP BY practitioner_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `get_test_details` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_test_details`(
    test_id INT
)
BEGIN
    SELECT 
    T.test_name as test_name,
    T.test_desc as test_description
    FROM 
        test T
     WHERE 
         T.test_id = test_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `InsertAssess` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `InsertAssess`(
    IN p_ailment_id VARCHAR(255),
    IN p_practitioner_id INT,
    IN p_patient_id INT,
    IN p_severity VARCHAR(255),
    IN p_assessement_date DATE,
    IN p_case_description TEXT
)
BEGIN
    INSERT INTO Assess (ailment_id, practitioner_id, patient_id, severity, assessement_date, case_description)
    VALUES (p_ailment_id, p_practitioner_id, p_patient_id, p_severity, p_assessement_date, p_case_description);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `InsertPatient` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `InsertPatient`(
    email VARCHAR(255),pwd VARCHAR(255),fname VARCHAR(50),lname VARCHAR(50),phone VARCHAR(15),addr VARCHAR(255),stno VARCHAR(10),stname VARCHAR(50),state VARCHAR(50),zip VARCHAR(10),blood VARCHAR(5),ethni VARCHAR(50),sex CHAR(1),dob DATE)
BEGIN
    INSERT INTO Patient( email_id, password,first_name, last_name, phone_no, address, st_no, st_name, state, zipcode, blood_group, ethnicity, sex, date_of_birth)
    VALUES(email, pwd,fname, lname, phone, addr, stno, stname, state, zip, blood, ethni, sex, dob);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `InsertPerforms` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `InsertPerforms`(
    IN p_test_id INT,
    IN p_patient_id INT,
    IN p_practitioner_id INT,
    IN p_test_date DATE,
    IN p_findings TEXT
)
BEGIN
    INSERT INTO Performs (test_id, patient_id, practitioner_id, test_date, findings)
    VALUES (p_test_id, p_patient_id, p_practitioner_id, p_test_date, p_findings);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `InsertPractitioner` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `InsertPractitioner`(
    IN p_username VARCHAR(255),
    IN p_password VARCHAR(255),
    IN p_practitioner_id INT,
    IN p_first_name VARCHAR(50),
    IN p_last_name VARCHAR(50),
    IN p_phone_no VARCHAR(15),
    IN p_department VARCHAR(50),
    IN p_hospital_name VARCHAR(255)
)
BEGIN
    INSERT INTO Practitioner(username, password, practitioner_id, first_name, last_name, phone_no, department, hospital_name)
    VALUES(p_username, p_password, p_practitioner_id, p_first_name, p_last_name, p_phone_no, p_department, p_hospital_name);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `SearchCountPatient` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SearchCountPatient`(
    IN search_term VARCHAR(255)
)
BEGIN
    SELECT count(*) FROM Patient 
    WHERE first_name LIKE CONCAT(search_term, '%') 
       OR last_name LIKE CONCAT(search_term, '%') 
       OR email_id LIKE CONCAT(search_term, '%');
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `SearchMedicine` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SearchMedicine`(
    IN search_term VARCHAR(255)
)
BEGIN
    SELECT * FROM medicine 
    WHERE medicine_name LIKE CONCAT('%',search_term, '%') 
       OR medicine_desc LIKE CONCAT('%', search_term, '%') ;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `SearchPatient` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SearchPatient`(
    IN search_term VARCHAR(255)
)
BEGIN
    SELECT * FROM Patient 
    WHERE first_name LIKE CONCAT(search_term, '%') 
       OR last_name LIKE CONCAT(search_term, '%') 
       OR email_id LIKE CONCAT(search_term, '%');
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `search_patient` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `search_patient`(
    IN search_term VARCHAR(255),
    p_practitioner_id INT
)
BEGIN
    SELECT DISTINCT
        P.patient_id,
        P.email_id,
        P.first_name,
        P.last_name,
        P.phone_no,
        P.address,
        P.st_no,
        P.st_name,
        P.state,
        P.zipcode,
        P.blood_group,
        P.ethnicity,
        P.sex,
        P.date_of_birth
    FROM patient P
    JOIN appointment A ON P.patient_id = A.patient_id
    WHERE 
        (P.first_name LIKE CONCAT(search_term, '%') 
        OR P.last_name LIKE CONCAT(search_term, '%') 
        OR P.email_id LIKE CONCAT(search_term, '%'))
        AND A.practitioner_id = p_practitioner_id
        AND NOT A.appointment_status = 'Rejected';
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `search_practitioner` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `search_practitioner`(
    IN text_like VARCHAR(255)
)
BEGIN
    SELECT 
    PR.practitioner_id as practitioner_id,
    PR.first_name as practitioner_fn,
    PR.last_name as practitioner_ln,
    PR.phone_no  as prac_phone_no,
    PR.department  as department,
    PR.hospital_name as hospital_name
    ,
    IFNULL(group_concat(SA.specialization_name),"Null") as specializations 
    FROM 
        Practitioner PR
        LEFT JOIN practitionerspecialization as PSA on PR.practitioner_id = PSA.practitioner_id
        LEFT JOIN specialization as SA on SA.specialization_id = PSA.specialization_id
     WHERE 
         (PR.first_name LIKE CONCAT('%', text_like, '%') OR
         PR.last_name LIKE CONCAT('%', text_like, '%')  OR
         PR.hospital_name LIKE CONCAT('%', text_like, '%'))
         OR 
         PR.practitioner_id IN (SELECT PS.practitioner_id FROM 
         practitionerspecialization as PS 
         JOIN specialization as S ON S.specialization_id = PS.specialization_id 
         WHERE S.specialization_name LIKE CONCAT('%', text_like, '%') )
	group by
    PR.practitioner_id
         ;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `set_status_accepted` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `set_status_accepted`(
    a_appointment_id INT
)
BEGIN
    UPDATE appointment
    SET 
        appointment_status = 'Accepted'
    WHERE 
        appointment_id = a_appointment_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `set_status_rejected` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `set_status_rejected`(
    a_appointment_id INT
)
BEGIN
    UPDATE appointment
    SET 
        appointment_status = 'Rejected'
    WHERE 
        appointment_id = a_appointment_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `UpdateAssessment` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `UpdateAssessment`(
    IN p_assess_id INT,
    IN p_severity VARCHAR(255),
    IN p_case_description TEXT,
    IN p_practitioner_id INT
)
BEGIN
    UPDATE Assess
    SET 
        severity = p_severity,
        case_description = p_case_description,
        practitioner_id = p_practitioner_id
    WHERE 
        assess_id = p_assess_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `UpdatePatient` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `UpdatePatient`(
    IN p_patient_id INT,
    IN p_email_id VARCHAR(255),
    IN p_password VARCHAR(255),
    IN p_first_name VARCHAR(50),
    IN p_last_name VARCHAR(50),
    IN p_phone_no VARCHAR(15),
    IN p_address VARCHAR(255),
    IN p_st_no VARCHAR(10),
    IN p_st_name VARCHAR(50),
    IN p_state VARCHAR(50),
    IN p_zipcode VARCHAR(10),
    IN p_blood_group VARCHAR(5),
    IN p_ethnicity VARCHAR(50),
    IN p_sex VARCHAR(10),
    IN p_date_of_birth DATE
)
BEGIN
    UPDATE Patient
    SET 
        email_id = p_email_id,
        password = p_password,
        first_name = p_first_name,
        last_name = p_last_name,
        phone_no = p_phone_no,
        address = p_address,
        st_no = p_st_no,
        st_name = p_st_name,
        state = p_state,
        zipcode = p_zipcode,
        blood_group = p_blood_group,
        ethnicity = p_ethnicity,
        sex = p_sex,
        date_of_birth = p_date_of_birth
    WHERE 
        patient_id = p_patient_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `UpdatePerforms` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `UpdatePerforms`(
    IN p_performance_id INT,
    IN p_test_date DATE,
    IN p_findings TEXT)
BEGIN
    UPDATE Performs
    SET 
        test_date = p_test_date,
        findings = p_findings
    WHERE 
        performance_id = p_performance_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `UpdatePrescription` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `UpdatePrescription`(
    IN p_prescription_id INT,
    IN p_quantity_in_mg INT,
    IN p_duration_in_days INT,
    IN p_dosage_desc TEXT,
    IN p_start_date DATE
)
BEGIN
    UPDATE Prescribe 
    SET 
        quantity_in_mg = p_quantity_in_mg,
        duration_in_days = p_duration_in_days,
        dosage_desc = p_dosage_desc,
        start_date = p_start_date
    WHERE 
        prescription_id = p_prescription_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-12-08 23:33:09
