package src;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;

/**
 * A graphical user interface class representing the detailed information of a patient in the Medical Records System (MRS) application.
 * This class displays various attributes and details of a patient, such as their name, email, blood group, ethnicity, and more.
 */
public class patientDetails extends JFrame {

  ConnectionModel model;

  int id;

  /**
   * Constructs an instance of the patientDetails class to display patient information.
   *
   * @param model The connection model for database access.
   * @param id    The identifier of the patient whose details are to be displayed.
   */
  patientDetails(ConnectionModel model, int id){

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
    ResultSet detailsList = model.patientDetails(id);
    try {
      detailsList.next();
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(null, e.getMessage());
    }

    try {
      JPanel firstNamePnl = new JPanel(new FlowLayout());
      firstNamePnl.setOpaque(false);
      firstNamePnl.add(new JLabel("First Name: " + detailsList.getString("first_name")));
      this.add(firstNamePnl);

      JPanel lastNamePnl = new JPanel(new FlowLayout());
      lastNamePnl.setOpaque(false);
      lastNamePnl.add(new JLabel("Last Name: " + detailsList.getString("last_name")));
      this.add(lastNamePnl);

      JPanel emailPnl = new JPanel(new FlowLayout());
      emailPnl.setOpaque(false);
      emailPnl.add(new JLabel("Email: " + detailsList.getString("email_id")));
      this.add(emailPnl);

      JPanel bloodGroupPnl = new JPanel(new FlowLayout());
      bloodGroupPnl.setOpaque(false);
      bloodGroupPnl.add(new JLabel("Blood Group: " + detailsList.getString("blood_group")));
      this.add(bloodGroupPnl);

      JPanel ethnicityPnl = new JPanel(new FlowLayout());
      ethnicityPnl.setOpaque(false);
      ethnicityPnl.add(new JLabel("Ethnicity: " + detailsList.getString("ethnicity")));
      this.add(ethnicityPnl);

      JPanel sexPnl = new JPanel(new FlowLayout());
      sexPnl.setOpaque(false);
      sexPnl.add(new JLabel("Sex: " + detailsList.getString("sex")));
      this.add(sexPnl);

      JPanel dobPnl = new JPanel(new FlowLayout());
      dobPnl.setOpaque(false);
      dobPnl.add(new JLabel("Date of Birth: " + detailsList.getDate("date_of_birth").toString()));
      this.add(dobPnl);

      JPanel phonePnl = new JPanel(new FlowLayout());
      phonePnl.setOpaque(false);
      phonePnl.add(new JLabel("Phone: " + detailsList.getString("phone_no")));
      this.add(phonePnl);

      JPanel addressPnl = new JPanel(new FlowLayout());
      addressPnl.setOpaque(false);
      addressPnl.add(new JLabel("Address: " + detailsList.getString("address")));
      this.add(addressPnl);

      JPanel stNoPnl = new JPanel(new FlowLayout());
      stNoPnl.setOpaque(false);
      stNoPnl.add(new JLabel("Street Number: " + detailsList.getString("st_no")));
      this.add(stNoPnl);

      JPanel stNamePnl = new JPanel(new FlowLayout());
      stNamePnl.setOpaque(false);
      stNamePnl.add(new JLabel("Street Name: " + detailsList.getString("st_name")));
      this.add(stNamePnl);

      JPanel statePnl = new JPanel(new FlowLayout());
      statePnl.setOpaque(false);
      statePnl.add(new JLabel("State: " + detailsList.getString("zipcode")));
      this.add(statePnl);

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    setTitle("Patient Details");
    setSize(200, 400);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new GridLayout(12, 1));
    setVisible(true);
  }
}
