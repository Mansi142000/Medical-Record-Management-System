package src;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;

/**
 * The {@code AssessmentDetails} class represents a frame for displaying details of a specific assessment
 * in a healthcare application. It displays various attributes of an assessment such as practitioner name,
 * patient name, ailment details, and assessment date.
 */
public class AssessmentDetails extends JFrame {

  ConnectionModel model;

  int id;

  /**
   * Constructs a new {@code AssessmentDetails} frame with the specified model and assessment ID.
   * It sets up the user interface for displaying details of an assessment.
   *
   * @param model The {@link ConnectionModel} for database interactions.
   * @param id    The identifier of the assessment whose details are to be displayed.
   */
  AssessmentDetails(ConnectionModel model, int id){
    try {
      UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
    } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }
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

    this.model = model;
    this.id = id;
    ResultSet detailsList = null;
    try {
      detailsList = model.getAssessmentDetails(id);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    try {
      detailsList.next();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    try {
      JPanel Practitioner = new JPanel(new FlowLayout());
      Practitioner.setOpaque(false);
      Practitioner.add(new JLabel("Assessing Practitioner Name " + detailsList.getString("prac_fname") + " "+
              detailsList.getString("prac_lname")));
      this.add(Practitioner);

      JPanel Patient = new JPanel(new FlowLayout());
      Patient.setOpaque(false);

      Patient.add(new JLabel("Patient Name " + detailsList.getString("p_fname") + " "+
              detailsList.getString("p_lname")));
      this.add(Patient);

      // Email
      JPanel Ailment = new JPanel(new FlowLayout());
      Ailment.setOpaque(false);
      Ailment.add(new JLabel("Ailment name: " + detailsList.getString("ailment_name")));
      this.add(Ailment);

      // Blood Group
      JPanel ailment_description = new JPanel(new FlowLayout());
      ailment_description.setOpaque(false);
      ailment_description.add(new JLabel("Ailment description: " + detailsList.getString("ailment_description")));
      this.add(ailment_description);

      // Ethnicity
      JPanel case_description = new JPanel(new FlowLayout());
      case_description.setOpaque(false);
      case_description.add(new JLabel("Case description: " + detailsList.getString("case_description")));
      this.add(case_description);

      // Sex
      JPanel severity = new JPanel(new FlowLayout());
      severity.setOpaque(false);
      severity.add(new JLabel("severity: " + detailsList.getString("severity")));
      this.add(severity);

      // Date of Birth
      JPanel Assessment_Date = new JPanel(new FlowLayout());
      Assessment_Date.setOpaque(false);
      Assessment_Date.add(new JLabel("Assessment Date: " + detailsList.getDate("assessement_Date").toString()));
      this.add(Assessment_Date);

    } catch (SQLException e) {
      JOptionPane.showMessageDialog(null, e.getMessage());
    }
    setTitle("Assessment Details");
    setSize(350, 400);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new GridLayout(12, 1));
    setVisible(true);

  }
}
