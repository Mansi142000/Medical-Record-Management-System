package src;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;

/**
 * The PatientHomePage class represents a graphical user interface for displaying patient
 * information and providing various options for patients.
 * This frame includes options to view assessments, prescriptions, personal details, and appointments.
 */
public class PatientHomePage extends JFrame{
  ConnectionModel model;
  int patientId;

  /**
   * Constructs a new PatientHomePage frame for displaying patient information and options.
   *
   * @param model The ConnectionModel for database access.
   * @param rs    The ResultSet containing patient data.
   */
  public PatientHomePage(ConnectionModel model, ResultSet rs) {
    this.model = model;
    try {
      rs.next();
      this.patientId = rs.getInt("patient_id");
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(null,e.getMessage());
    }

    try {
      UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
    } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
             IllegalAccessException e) {
      e.printStackTrace();
    }

    setTitle("Patient Home Page");
    setSize(1000, 500);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    JPanel backgroundPanel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon imageIcon = new ImageIcon("blur-hospital.jpg");
        Image image = imageIcon.getImage();
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
      }
    };
    setContentPane(backgroundPanel);
    setLayout(new BorderLayout());

    JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JButton logout = new JButton("Log-Out");
    logout.addActionListener(e->{
      new OpeningFrame().setVisible(true);
      dispose();
    });
    topPanel.add(logout);
    topPanel.setOpaque(false);
    JPanel centralPanel = new JPanel(new FlowLayout());

    JButton seeAssessments = new JButton("See Assessments");
    seeAssessments.addActionListener(e ->{
      new seeAilmentPatient(this.model, patientId);

    });
    JButton seePrescriptions = new JButton("See Prescriptions");
    seePrescriptions.addActionListener(e ->{
      new seePrescriptionPatient(model,patientId);
    });
    JButton seeTests = new JButton("See Tests Record");
    seeTests.addActionListener(e ->{
      new seeTestPatient(model,patientId);
    });
    JButton seeDetails = new JButton("See Details");
    seeDetails.addActionListener(e ->{
      new patientDetails(model,patientId);
    });
    JButton appointments = new JButton("Appointments");
    appointments.addActionListener(e ->{
      System.out.println("ho jayega");
      new seeAppointment(model,patientId);
    });

    centralPanel.add(appointments);
    centralPanel.add(seeDetails);
    centralPanel.add(seePrescriptions);
    centralPanel.add(seeAssessments);
    centralPanel.add(seeTests);
    this.add(topPanel, BorderLayout.NORTH);
    centralPanel.setOpaque(false);

    JPanel rightPanel = new JPanel(new FlowLayout());

    ResultSet detailsList = model.patientDetails(patientId);
    try {
      detailsList.next();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    try {

      JPanel firstNamePnl = new JPanel(new FlowLayout());
      firstNamePnl.setOpaque(false);
      firstNamePnl.add(new JLabel("First Name: " + detailsList.getString("first_name")));
      rightPanel.add(firstNamePnl);

      JPanel lastNamePnl = new JPanel(new FlowLayout());
      lastNamePnl.setOpaque(false);
      lastNamePnl.add(new JLabel("Last Name: " + detailsList.getString("last_name")));
      rightPanel.add(lastNamePnl);

      JPanel emailPnl = new JPanel(new FlowLayout());
      emailPnl.setOpaque(false);
      emailPnl.add(new JLabel("Email: " + detailsList.getString("email_id")));
      rightPanel.add(emailPnl);

      JPanel bloodGroupPnl = new JPanel(new FlowLayout());
      bloodGroupPnl.setOpaque(false);
      bloodGroupPnl.add(new JLabel("Blood Group: " + detailsList.getString("blood_group")));
      rightPanel.add(bloodGroupPnl);

      JPanel ethnicityPnl = new JPanel(new FlowLayout());
      ethnicityPnl.setOpaque(false);
      ethnicityPnl.add(new JLabel("Ethnicity: " + detailsList.getString("ethnicity")));

      JPanel sexPnl = new JPanel(new FlowLayout());
      sexPnl.setOpaque(false);
      sexPnl.add(new JLabel("Sex: " + detailsList.getString("sex")));
      rightPanel.add(sexPnl);

      JPanel dobPnl = new JPanel(new FlowLayout());
      dobPnl.setOpaque(false);
      dobPnl.add(new JLabel("Date of Birth: " + detailsList.getDate("date_of_birth").toString()));
      rightPanel.add(dobPnl);

      JPanel phonePnl = new JPanel(new FlowLayout());
      phonePnl.setOpaque(false);
      phonePnl.add(new JLabel("Phone: " + detailsList.getString("phone_no")));
      rightPanel.add(phonePnl);

      JPanel addressPnl = new JPanel(new FlowLayout());
      addressPnl.setOpaque(false);
      addressPnl.add(new JLabel("Address: " + detailsList.getString("address")));
      rightPanel.add(addressPnl);

      JPanel stNoPnl = new JPanel(new FlowLayout());
      stNoPnl.setOpaque(false);
      stNoPnl.add(new JLabel("Street Number: " + detailsList.getString("st_no")));
      rightPanel.add(stNoPnl);

      JPanel stNamePnl = new JPanel(new FlowLayout());
      stNamePnl.setOpaque(false);
      stNamePnl.add(new JLabel("Street Name: " + detailsList.getString("st_name")));
      rightPanel.add(stNamePnl);

      JPanel statePnl = new JPanel(new FlowLayout());
      statePnl.setOpaque(false);
      statePnl.add(new JLabel("State: " + detailsList.getString("zipcode")));
      rightPanel.add(statePnl);
      rightPanel.setOpaque(false);

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    rightPanel.setLayout(new GridLayout(12, 1));
    rightPanel.setVisible(true);

    JPanel mainPanel = new JPanel(new GridLayout(1,2));
    mainPanel.add(rightPanel);
    mainPanel.add(centralPanel);
    mainPanel.setOpaque(false);
    this.add(mainPanel, BorderLayout.CENTER);
    setVisible(true);
  }
}
