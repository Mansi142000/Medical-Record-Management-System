package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The main entry point for the Medical Records System (MRS) application.
 * This class represents the opening frame of the application where users can choose between
 * practitioner and patient login.
 */
public class OpeningFrame extends JFrame {

  /**
   * The connection model for database access.
   */
ConnectionModel model;

  /**
   * Constructs an instance of the OpeningFrame.
   * Initializes the database connection model and sets up the user interface.
   * If an invalid argument exception occurs during database connection initialization,
   * it displays an error message and closes the frame.
   */
  public OpeningFrame() {

    try {
      ConnectionModel model = new ConnectionModel("root","password");
      this.model = model;
    }catch (IllegalArgumentException e){
      JOptionPane.showMessageDialog(this,"Wrong:"+e);
      this.dispose();
    }

    try {
      UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
    } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }

    setTitle("Hospital Login");
    setSize(800, 600);
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
    setLayout(new GridLayout(2,1));

    JButton practitionerButton = new JButton("Practitioner Login");
    JButton patientButton = new JButton("Patient Login");

    JPanel buttonPanel = new JPanel();
    buttonPanel.setOpaque(false);
    buttonPanel.add(practitionerButton);
    buttonPanel.add(patientButton);

    JPanel heading= new JPanel(new BorderLayout());
    heading.setOpaque(false);
    JLabel head = new JLabel("Welcome to MRS! \n choose your login");
    head.setHorizontalAlignment(JLabel.CENTER);
    head.setVerticalAlignment(JLabel.BOTTOM);

    heading.add(head, BorderLayout.SOUTH);
    add(heading);
    add(buttonPanel);

    practitionerButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        openPractitionerLogin(model);
      }
    });

    patientButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        openPatientLogin(model);
      }
    });
  }

  /**
   * Opens the practitioner login frame using the specified connection model.
   *
   * @param model The connection model for database access.
   */
  private void openPractitionerLogin(ConnectionModel model) {
    PractitionerLogin practitionerLogin = new PractitionerLogin(model);
    practitionerLogin.setVisible(true);
    this.dispose();
  }

  /**
   * Opens the patient login frame using the specified connection model.
   *
   * @param model The connection model for database access.
   */
  private void openPatientLogin(ConnectionModel model) {
    PatientLogin patientLogin = new PatientLogin(model);
    patientLogin.setVisible(true);
    this.dispose();
  }
}