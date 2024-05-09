package src;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

/**
 * A class for managing the database connection and executing various database operations.
 */
public class ConnectionModel {
  private String url = "jdbc:mysql://localhost:3306/healthcaredb1";
  private final String driver = "com.mysql.cj.jdbc.Driver";
  Connection connection;

  /**
   * Constructor to initialize a ConnectionModel instance with the provided username and password.
   *
   * @param username The username for the database connection.
   * @param password The password for the database connection.
   */
  public ConnectionModel(String username, String password) {
    try {
      Class.forName(driver);
      connection = DriverManager.getConnection(url, username, password);
    } catch (ClassNotFoundException | SQLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Performs a practitioner login operation with the given username and password.
   *
   * @param username The username for practitioner login.
   * @param password The password for practitioner login.
   * @return A ResultSet containing the result of the practitioner login operation.
   */
  public ResultSet practitonerLogin(String username, String password){
    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("CALL CheckPractitionerCredentials(?, ?)");
      stmt.setString(1,username);
      stmt.setString(2,password);
      ResultSet rs = stmt.executeQuery();;
      return rs;
    } catch (ClassNotFoundException | SQLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Performs a patient login operation with the given email and password.
   *
   * @param email The email for patient login.
   * @param password The password for patient login.
   * @return A ResultSet containing the result of the patient login operation.
   */
  public ResultSet patientLogin(String email, String password){
    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("CALL CheckPatientCredentials(?, ?)");
      stmt.setString(1,email);
      stmt.setString(2,password);
      ResultSet rs = stmt.executeQuery();;
      return rs;
    } catch (ClassNotFoundException | SQLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Registers a new patient with the provided information.
   *
   * @param email The email address of the patient.
   * @param password The password for the patient's account.
   * @param firstName The first name of the patient.
   * @param lastName The last name of the patient.
   * @param phone The phone number of the patient.
   * @param address The address of the patient.
   * @param stNo The street number of the patient's address.
   * @param stName The street name of the patient's address.
   * @param state The state of the patient's address.
   * @param zip The ZIP code of the patient's address.
   * @param blood The blood type of the patient.
   * @param ethnicity The ethnicity of the patient.
   * @param sex The gender of the patient.
   * @param dob The date of birth of the patient.
   * @return A ResultSet containing the result of the patient registration operation.
   */
  public ResultSet patientRegister(String email,
                                   String password,
                                   String firstName,
                                   String lastName,
                                   String phone,
                                   String address,
                                   String stNo,
                                   String stName,
                                   String state,
                                   String zip,
                                   String blood,
                                   String ethnicity,
                                   String sex,
                                   String dob){
    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("CALL InsertPatient(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
      stmt.setString(1,email);
      stmt.setString(2,password);
      stmt.setString(3,firstName);
      stmt.setString(4,lastName);
      stmt.setString(5,phone);
      stmt.setString(6,address);
      stmt.setString(7,stNo);
      stmt.setString(8,stName);
      stmt.setString(9,state);
      stmt.setString(10,zip);
      stmt.setString(11,blood);
      stmt.setString(12,ethnicity);
      stmt.setString(13,sex);
      stmt.setDate(14, java.sql.Date.valueOf(dob));

      ResultSet rs = stmt.executeQuery();;
      return rs;
    } catch (ClassNotFoundException | SQLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Retrieves the specializations associated with a practitioner.
   *
   * @param pracId The ID of the practitioner for whom to retrieve specializations.
   * @return A ResultSet containing the specializations of the practitioner.
   * @throws IllegalArgumentException If there is an issue with the database connection.
   */
  public ResultSet specialization( int pracId){
    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("CALL GetPractitionerSpecializations(?)");
      stmt.setInt(1,pracId);

      ResultSet rs = stmt.executeQuery();;
      return rs;
    } catch (ClassNotFoundException | SQLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Retrieves detailed information about a patient based on their ID.
   *
   * @param patient_id The ID of the patient for whom to retrieve details.
   * @return A ResultSet containing detailed information about the patient.
   * @throws IllegalArgumentException If there is an issue with the database connection.
   */
  public ResultSet patientDetails(int patient_id){
    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("CALL  GetPatientInfo(?)");
      stmt.setInt(1,patient_id);

      ResultSet rs = stmt.executeQuery();;
      return rs;
    } catch (ClassNotFoundException | SQLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Searches for patients based on a name and practitioner ID and returns the results as a CachedRowSet.
   *
   * @param name    The name of the patient or a portion of the name to search for.
   * @param pracId  The ID of the practitioner who is searching for patients.
   * @return A CachedRowSet containing the search results for patients.
   * @throws IllegalArgumentException If there is an issue with the database connection or the query.
   */
  public CachedRowSet patientResults(String name,int pracId) {
    try {
      CallableStatement stmt = connection.prepareCall("CALL search_patient(?,?)");
      stmt.setString(1, name);
      stmt.setInt(2,pracId);

      // Create a CachedRowSet from the original ResultSet
      CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
      cachedRowSet.populate(stmt.executeQuery());

      return cachedRowSet;
    } catch (SQLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Counts the number of patients with a given name.
   *
   * @param name The name of the patient or a portion of the name to search for.
   * @return The count of patients with the specified name.
   * @throws IllegalArgumentException If there is an issue with the database connection or the query.
   */
  public int searchCount(String name){
    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("CALL SearchCountPatient(?)");
      stmt.setString(1,name);

      ResultSet rs = stmt.executeQuery();;
      rs.next();
      return Integer.parseInt(rs.getString("count(*)"));
    } catch (ClassNotFoundException | SQLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Closes the database connection associated with this ConnectionModel.
   *
   * @throws SQLException If an SQL exception occurs while closing the connection.
   */
  public void closeConnection() throws SQLException {
    connection.close();
  }

  /**
   * Retrieves a list of assessments for a specific patient based on the given search text.
   *
   * @param patientId   The ID of the patient for whom assessments are retrieved.
   * @param searchText  The search text to filter the assessments.
   * @return A ResultSet containing the list of assessments.
   * @throws IllegalArgumentException If there's an issue with the provided arguments.
   */
  public ResultSet getAssessmentList(int patientId, String searchText) {
    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("{CALL GetAssessmentDetails(?,?)}");

      stmt.setString(1,searchText);
      stmt.setInt(2,patientId);

      ResultSet rs = stmt.executeQuery();;
      return rs;
    } catch (ClassNotFoundException | SQLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Retrieves a list of ailments based on the provided search text.
   *
   * @param text The search text used to filter the list of ailments.
   * @return A ResultSet containing the list of ailments.
   * @throws IllegalArgumentException If there's an issue with the provided search text.
   */
  public ResultSet getAilmentList(String text) {
    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("{CALL GetAilmentNameDetails(?)}");

      stmt.setString(1,text);

      ResultSet rs = stmt.executeQuery();;
      return rs;
    } catch (ClassNotFoundException | SQLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Retrieves the name of an ailment based on the provided ailment ID.
   *
   * @param ailmentId The unique identifier of the ailment.
   * @return A ResultSet containing the name of the ailment.
   * @throws IllegalArgumentException If there's an issue with the provided ailment ID.
   */
  public ResultSet getAilmentName(int ailmentId) {
    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("{call GetAilmentName(?)}");

      stmt.setInt(1,ailmentId);

      ResultSet rs = stmt.executeQuery();;
      return rs;
    } catch (ClassNotFoundException | SQLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Inserts a new assessment into the database.
   *
   * @param ailmentSetId The unique identifier of the ailment set associated with the assessment.
   * @param patientId The unique identifier of the patient for whom the assessment is performed.
   * @param pracId The unique identifier of the practitioner who conducted the assessment.
   * @param case_desc A description of the assessment case.
   * @param severity The severity level of the assessment.
   * @throws IllegalArgumentException If there's an issue with the provided parameters.
   * @throws SQLException If a database error occurs during the insertion.
   */
  public void insertAssessment(int ailmentSetId, int patientId , int pracId , String case_desc, String severity) throws IllegalArgumentException, SQLException {
    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("{CALL AddAssessment(?,?,?,?,?,?)}");
      stmt.setInt(1,ailmentSetId);
      stmt.setInt(2,pracId);
      stmt.setInt(3,patientId);
      stmt.setString(4,severity);
      stmt.setDate(5,new Date(System.currentTimeMillis())); //putcurrent date here);
      stmt.setString(6,case_desc);
      ResultSet rs = stmt.executeQuery();
    } catch (ClassNotFoundException | SQLException e) {
      throw new SQLException(e);
    }
  }

  /**
   * Deletes an assessment from the database.
   *
   * @param assessID The unique identifier of the assessment to be deleted.
   * @throws SQLException If a database error occurs during the deletion.
   */
  public void deleteAssessment(int assessID) throws SQLException {
    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("{CALL DeleteAssessment(?)}");
      stmt.setInt(1,assessID);
      ResultSet rs = stmt.executeQuery();
    } catch (ClassNotFoundException | SQLException e) {
      throw new SQLException(e);
    }
  }

  /**
   * Updates an existing assessment in the database.
   *
   * @param assessID   The unique identifier of the assessment to be updated.
   * @param severity   The severity of the assessment.
   * @param description A description of the assessment.
   * @param pracID     The unique identifier of the practitioner associated with the assessment.
   * @throws SQLException If a database error occurs during the update.
   */
  public void updateAssessment(int assessID, String severity, String description, int pracID) throws SQLException {
    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("{CALL UpdateAssessment(?,?,?,?)}");
      stmt.setInt(1,assessID);
      stmt.setString(2,severity);
      stmt.setString(3,description);
      stmt.setInt(4, pracID);
      ResultSet rs = stmt.executeQuery();
    } catch (ClassNotFoundException | SQLException e) {
      throw new SQLException(e);
    }
  }

  /**
   * Retrieves the details of an assessment from the database based on its unique identifier.
   *
   * @param assessId The unique identifier of the assessment to retrieve.
   * @return A ResultSet containing the assessment details, or null if not found.
   * @throws SQLException If a database error occurs during the retrieval.
   */
  public ResultSet getAssessmentDetails(int assessId) throws SQLException {
    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("{CALL getAssessment(?)}");
      stmt.setInt(1,assessId);
      return stmt.executeQuery();

    } catch (ClassNotFoundException | SQLException e) {
      throw new SQLException(e);
    }
  }

  /**
   * Retrieves a ResultSet containing prescription details for a specific patient and medication.
   *
   * @param text      The text to search for in medication names.
   * @param patientID The unique identifier of the patient.
   * @return A ResultSet containing prescription details, or null if not found.
   * @throws SQLException If a database error occurs during retrieval.
   */
  public ResultSet getPrescriptions(String text, int patientID) throws SQLException {
    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("{CALL GetTreatmentDetails(?,?)}");
      stmt.setString(1,text);
      stmt.setInt(2,patientID);
      return stmt.executeQuery();

    } catch (ClassNotFoundException | SQLException e) {
      throw new SQLException(e);
    }
  }

  /**
   * Inserts a new prescription into the database.
   *
   * @param pracID           The unique identifier of the practitioner.
   * @param assessment_id    The unique identifier of the assessment.
   * @param medicine_id      The unique identifier of the medicine.
   * @param startDate        The start date of the prescription.
   * @param number_of_time_a_day The number of times per day to take the medicine.
   * @param quantity_in_mg   The quantity of medicine in milligrams.
   * @param duration_in_days The duration of the prescription in days.
   * @throws SQLException If a database error occurs during insertion.
   */
  public void insertPrescription( int pracID,
                                  int assessment_id,
                                  int medicine_id,
                                  String startDate,
                                  int number_of_time_a_day,
                                  int quantity_in_mg,
                                  int duration_in_days,
                                  String dosage_desciption ) throws SQLException {
    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("{CALL AddPrescription(?,?,?,?,?,?,?,?,?)}");

      stmt.setInt(1,pracID);
      stmt.setInt(2,assessment_id);
      stmt.setInt(3,medicine_id);
      stmt.setDate(4,new Date(System.currentTimeMillis())); //putcurrent date here);
      stmt.setDate(5,java.sql.Date.valueOf(startDate));
      stmt.setInt(6,number_of_time_a_day);
      stmt.setInt(7,quantity_in_mg);
      stmt.setInt(8,duration_in_days);
      stmt.setString(9,dosage_desciption);
      ResultSet rs = stmt.executeQuery();
    } catch (ClassNotFoundException | SQLException e) {
      throw new SQLException(e);
    }
  }

  /**
   * Retrieves a ResultSet containing medicine details based on a search text.
   *
   * @param text The text to search for in medicine names.
   * @return A ResultSet containing medicine details, or null if not found.
   * @throws SQLException If a database error occurs during retrieval.
   */
  public ResultSet getMedicineList(String text) {
    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("{CALL SearchMedicine(?)}");
      stmt.setString(1,text);
      return stmt.executeQuery();

    } catch (ClassNotFoundException | SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Retrieves a ResultSet containing details of a specific medicine.
   *
   * @param iD The unique identifier of the medicine.
   * @return A ResultSet containing medicine details, or null if not found.
   * @throws SQLException If a database error occurs during retrieval.
   */
  public ResultSet getMedicineDetails(int iD) {
    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("{CALL getMedicineDetails(?)}");
      stmt.setInt(1,iD);
      return stmt.executeQuery();

    } catch (ClassNotFoundException | SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Deletes a prescription from the database based on its unique identifier.
   *
   * @param prescriptionId The unique identifier of the prescription to delete.
   * @throws SQLException If a database error occurs during deletion.
   */
  public void deletePrescription(int prescriptionId) {
    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("{CALL DeletePrescription(?)}");
      stmt.setInt(1,prescriptionId);
      stmt.executeQuery();

    } catch (ClassNotFoundException | SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Updates prescription details in the database.
   *
   * @param p_prescription_id The unique identifier of the prescription to update.
   * @param p_quantity_in_mg  The updated quantity in milligrams.
   * @param p_duration_in_days The updated duration in days.
   * @param p_dosage_desc     The updated dosage description.
   * @param start_date        The updated start date of the prescription.
   * @throws SQLException If a database error occurs during update.
   */
  public void updatePrescription(int p_prescription_id,
                                 int p_quantity_in_mg,
                                 int p_duration_in_days,
                                 String p_dosage_desc,
                                 String start_date) throws SQLException {
    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("{CALL UpdatePrescription(?,?,?,?,?)}");

      stmt.setInt(1,p_prescription_id);
      stmt.setInt(2,p_quantity_in_mg);
      stmt.setInt(3,p_duration_in_days);
      stmt.setString(4,p_dosage_desc); //putcurrent date here);
      stmt.setDate(5,Date.valueOf(start_date));
      ResultSet rs = stmt.executeQuery();
    } catch (ClassNotFoundException | SQLException | IllegalArgumentException e ) {
      throw new SQLException(e);
    }
  }

  /**
   * Retrieves a ResultSet containing prescription details for a specific prescription.
   *
   * @param prescriptionID The unique identifier of the prescription.
   * @return A ResultSet containing prescription details, or null if not found.
   * @throws SQLException If a database error occurs during retrieval.
   */
  public ResultSet getPrescriptionDetails(int prescriptionID) throws SQLException {
    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("{CALL GetPrescriptionDetails(?)}");
      stmt.setInt(1,prescriptionID);
      return stmt.executeQuery();

    } catch (ClassNotFoundException | SQLException e) {
      throw new SQLException(e);
    }
  }

  /**
   * Retrieves a ResultSet containing perform details for a specific performance and search text.
   *
   * @param performId   The unique identifier of the performance.
   * @param searchText The text to search for in performance details.
   * @return A ResultSet containing perform details, or null if not found.
   */
  public ResultSet getPerformList(int performId, String searchText) {
    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("{CALL get_performs(?,?)}");

      stmt.setString(1,searchText);
      stmt.setInt(2,performId);

      ResultSet rs = stmt.executeQuery();;
      return rs;
    } catch (ClassNotFoundException | SQLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Retrieves a ResultSet containing details of a specific performance based on its unique identifier.
   *
   * @param performanceId The unique identifier of the performance.
   * @return A ResultSet containing performance details, or null if not found.
   * @throws SQLException If a database error occurs during retrieval.
   */
  public ResultSet getTestDetails(int performanceId) throws SQLException {
    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("{CALL get_performance_details(?)}");
      stmt.setInt(1,performanceId);
      return stmt.executeQuery();

    } catch (ClassNotFoundException | SQLException e) {
      throw new SQLException(e);
    }
  }


  /**
   * Retrieves a ResultSet containing details of a specific test based on its name.
   *
   * @param testName The name of the test to retrieve details for.
   * @return A ResultSet containing test details, or null if not found.
   * @throws SQLException If a database error occurs during retrieval.
   */
  public ResultSet getTestData(String testName) throws SQLException {
    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("{CALL GetTest(?)}");
      stmt.setString(1,testName);
      return stmt.executeQuery();

    } catch (ClassNotFoundException | SQLException e) {
      throw new SQLException(e);
    }
  }

  /**
   * Inserts a new performance record into the database.
   *
   * @param testId    The unique identifier of the test.
   * @param patientId The unique identifier of the patient.
   * @param pracId    The unique identifier of the practitioner.
   * @param test_date The date of the test.
   * @param findings  The findings from the test.
   * @throws IllegalArgumentException If an invalid argument is provided.
   * @throws SQLException           If a database error occurs during insertion.
   */
  public void insertPerform(int testId, int patientId ,int pracId, String test_date, String findings) throws IllegalArgumentException, SQLException {
    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("{CALL InsertPerforms(?,?,?,?,?)}");
      stmt.setInt(1,testId);
      stmt.setInt(2,patientId);
      stmt.setInt(3,pracId);
      stmt.setDate(4, Date.valueOf(test_date));
      stmt.setString(5,findings);
      ResultSet rs = stmt.executeQuery();
    } catch (ClassNotFoundException | SQLException e) {
      throw new SQLException(e);
    }
  }

  /**
   * Retrieves a ResultSet containing details of a specific test based on its unique identifier.
   *
   * @param iD The unique identifier of the test.
   * @return A ResultSet containing test details, or null if not found.
   * @throws SQLException If a database error occurs during retrieval.
   */
  public ResultSet getTestName(int iD) throws SQLException {

    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("{CALL get_test_details(?)}");
      stmt.setInt(1,iD);
      ResultSet rs = stmt.executeQuery();
      return  rs;
    } catch (ClassNotFoundException | SQLException e) {
      throw new SQLException(e);
    }
  }

  /**
   * Retrieves a ResultSet containing details of a specific performance based on its unique identifier.
   *
   * @param performId The unique identifier of the performance.
   * @return A ResultSet containing performance details, or null if not found.
   * @throws SQLException If a database error occurs during retrieval.
   */
  public ResultSet getPerform(int performId) throws SQLException {
    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("{CALL get_performance_details(?)}");
      stmt.setInt(1,performId);
      ResultSet rs = stmt.executeQuery();
      return  rs;
    } catch (ClassNotFoundException | SQLException e) {
      throw new SQLException(e);
    }

  }

  /**
   * Updates the details of a performance in the database.
   *
   * @param performId The unique identifier of the performance to update.
   * @param date      The updated date of the performance.
   * @param findings  The updated findings from the performance.
   * @throws SQLException If a database error occurs during update.
   */
  public void updatePerform(int performId, String date, String findings) throws SQLException {
    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("{CALL UpdatePerforms(?,?,?)}");
      stmt.setInt(1,performId);
      stmt.setDate(2, Date.valueOf(date));
      stmt.setString(3,findings);
      ResultSet rs = stmt.executeQuery();
    } catch (ClassNotFoundException | IllegalArgumentException | SQLException e) {
      throw new SQLException(e);
    }
  }


  /**
   * Deletes a performance record from the database based on its unique identifier.
   *
   * @param performID The unique identifier of the performance to delete.
   * @throws SQLException If a database error occurs during deletion.
   */
  public void deletePerform(int performID) throws SQLException {
    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("{CALL DeletePerforms(?)}");
      stmt.setInt(1,performID);
      ResultSet rs = stmt.executeQuery();
    } catch (ClassNotFoundException | SQLException e) {
      throw new SQLException(e);
    }
  }

  /**
   * Retrieves a ResultSet containing a list of practitioners based on a search text.
   *
   * @param text The text to search for in practitioner names.
   * @return A ResultSet containing practitioner details, or null if not found.
   * @throws SQLException If a database error occurs during retrieval.
   */
  public ResultSet practitonerList(String text) throws SQLException {
    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("{CALL search_practitioner(?)}");
      stmt.setString(1,text);
      return stmt.executeQuery();
    } catch (ClassNotFoundException | SQLException e) {
      throw new SQLException(e);
    }
  }
  public ResultSet add_appointment(int a_practitioner_id,
                                   int a_patient_id,
                                   String a_appointment_date,
                                   int a_appointment_hour,
                                   String a_appointment_status
  ) throws SQLException {
    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("{CALL add_appointment(?,?,?,?,?,?)}");
      stmt.setInt(1,a_practitioner_id);
      stmt.setInt(2,a_patient_id);
      stmt.setDate(3,Date.valueOf(a_appointment_date));
      stmt.setInt(4,a_appointment_hour);
      stmt.setDate(5, new Date(System.currentTimeMillis()));
      stmt.setString(6,a_appointment_status);
      return stmt.executeQuery();
    } catch (ClassNotFoundException | SQLException e) {
      throw new SQLException(e);
    }
  }

  /**
   * Retrieves a ResultSet containing pending appointments for a specific patient and search text.
   *
   * @param patientId The unique identifier of the patient.
   * @param text      The text to search for in appointment details.
   * @return A ResultSet containing pending appointment details, or null if not found.
   * @throws SQLException If a database error occurs during retrieval.
   */
  public ResultSet patientPendingAppointments(int patientId, String text) throws SQLException {

    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("{CALL get_pending_appointments(?,?)}");
      stmt.setInt(1,patientId);
      stmt.setString(2,text);
      return stmt.executeQuery();
    } catch (ClassNotFoundException | SQLException e) {
      throw new SQLException(e);
    }
  }

  /**
   * Retrieves a ResultSet containing not pending appointments for a specific patient and search text.
   *
   * @param patientId The unique identifier of the patient.
   * @param text      The text to search for in appointment details.
   * @return A ResultSet containing not pending appointment details, or null if not found.
   * @throws SQLException If a database error occurs during retrieval.
   */
  public ResultSet patientNotPendingAppointments(int patientId, String text) throws SQLException {

    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("{CALL get_not_pending_appointments(?,?)}");
      stmt.setInt(1,patientId);
      stmt.setString(2,text);
      return stmt.executeQuery();
    } catch (ClassNotFoundException | SQLException e) {
      throw new SQLException(e);
    }
  }

  /**
   * Deletes an appointment from the database.
   *
   * @param appointmentID The ID of the appointment to be deleted.
   * @throws SQLException If an SQL error occurs or the class for the SQL driver is not found.
   */
  public void deleteAppointment(int appointmentID) throws SQLException {
    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("{CALL delete_appointment(?)}");
      stmt.setInt(1,appointmentID);
      stmt.executeQuery();
    } catch (ClassNotFoundException | SQLException e) {
      throw new SQLException(e);
    }

  }

  /**
   * Retrieves pending appointments for a specific practitioner.
   *
   * @param pracId The ID of the practitioner.
   * @param text Additional text parameter for the query.
   * @return A ResultSet containing the pending appointments.
   * @throws SQLException If an SQL error occurs or the class for the SQL driver is not found.
   */
  public ResultSet pracPendingAppointments(int pracId, String text) throws SQLException{
    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("{CALL get_practitioner_pending_appointments(?,?)}");
      stmt.setInt(1,pracId);
      stmt.setString(2,text);
      return stmt.executeQuery();
    } catch (ClassNotFoundException | SQLException e) {
      throw new SQLException(e);
    }
  }

  /**
   * Retrieves non-pending appointments for a specific practitioner.
   *
   * @param pracId The ID of the practitioner.
   * @param text Additional text parameter for the query.
   * @return A ResultSet containing the non-pending appointments.
   * @throws SQLException If an SQL error occurs or the class for the SQL driver is not found.
   */
  public ResultSet pracNotPendingAppointments(int pracId, String text) throws SQLException{
    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("{CALL get_practitioner_not_pending_appointments(?,?)}");
      stmt.setInt(1,pracId);
      stmt.setString(2,text);
      return stmt.executeQuery();
    } catch (ClassNotFoundException | SQLException e) {
      throw new SQLException(e);
    }
  }

  /**
   * Accepts an appointment by changing its status in the database.
   *
   * @param appointmentID The ID of the appointment to accept.
   * @throws SQLException If an SQL error occurs or the class for the SQL driver is not found.
   */
  public void acceptAppointment(int appointmentID) throws SQLException {
    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("{CALL set_status_accepted(?)}");
      stmt.setInt(1,appointmentID);
      stmt.executeQuery();
    } catch (ClassNotFoundException | SQLException e) {
      throw new SQLException(e);
    }
  }

  /**
   * Rejects an appointment by changing its status in the database.
   *
   * @param appointmentID The ID of the appointment to reject.
   * @throws SQLException If an SQL error occurs or the class for the SQL driver is not found.
   */
  public void rejectAppointment(int appointmentID) throws SQLException {
    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("{CALL set_status_rejected(?)}");
      stmt.setInt(1,appointmentID);
      stmt.executeQuery();
    } catch (ClassNotFoundException | SQLException e) {
      throw new SQLException(e);
    }
  }

  /**
   * Counts the number of pending appointments for a specific practitioner.
   *
   * @param pracId The ID of the practitioner.
   * @return A ResultSet containing the count of pending appointments.
   * @throws SQLException If an SQL error occurs or the class for the SQL driver is not found.
   */
  public ResultSet getPendingCount(int pracId) throws SQLException {
    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("{CALL count_pending_appointments(?)}");
      stmt.setInt(1,pracId);
      return stmt.executeQuery();
    } catch (ClassNotFoundException | SQLException e) {
      throw new SQLException(e);
    }
  }

  /**
   * Retrieves the specialization group of a specific practitioner.
   *
   * @param pracId The ID of the practitioner.
   * @return A ResultSet containing the specialization details.
   * @throws SQLException If an SQL error occurs or the class for the SQL driver is not found.
   */
  public ResultSet specilizationGroup(int pracId) throws SQLException{
    try {
      Class.forName(driver);
      CallableStatement stmt = connection.prepareCall("{CALL get_practitioner_specialization(?)}");
      stmt.setInt(1,pracId);
      return stmt.executeQuery();
    } catch (ClassNotFoundException | SQLException e) {
      throw new SQLException(e);
    }
  }
}
