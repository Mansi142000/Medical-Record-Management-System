package src;

import javax.swing.*;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

/**
 * A graphical user interface class representing the login page for patients in the Medical Records System (MRS) application.
 * This class allows patients to enter their email and password to log in to their accounts or register as new patients.
 */
public class PatientLogin extends JFrame {
  private JTextField emailId;
  private JPasswordField passwordField;

  private ConnectionModel model;

  /**
   * Constructs an instance of the PatientLogin class to display the login page for patients.
   *
   * @param model The connection model for database access.
   */
  public PatientLogin(ConnectionModel model) {

    this.model = model;
    try {
      UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
    } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }

    setTitle("Login Page");
    setSize(500, 350);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    // Create background panel with hospital-themed image
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

    JLabel emailLabel = new JLabel("Email ID:");
    emailLabel.setHorizontalTextPosition(JLabel.RIGHT);
    JLabel passwordLabel = new JLabel("Password:");
    passwordLabel.setHorizontalTextPosition(JLabel.RIGHT);
    emailId = new JTextField();
    emailId.setPreferredSize(new Dimension(200,30));
    passwordField = new JPasswordField();
    passwordField.setPreferredSize(new Dimension(200,30));
    JButton loginButton = new JButton("Login");
    JButton registerButton = new JButton("Register");

    this.setLayout(new BorderLayout());
    JPanel main = new JPanel(new GridLayout(4,1) );

    main.setPreferredSize(new Dimension(100,100));
    JPanel username = new JPanel();
    username.setOpaque(false);
    username.add(emailLabel);
    username.add(emailId);

    JPanel password = new JPanel();
    password.setOpaque(false);
    password.add(passwordLabel);
    password.add(passwordField);

    JPanel btnPanel = new JPanel();
    btnPanel.setOpaque(false);
    btnPanel.add(registerButton);
    btnPanel.add(loginButton);

    JPanel title = new JPanel();
    title.add(new JLabel("Patient Login"));
    title.setOpaque(false);

    main.setOpaque(false);
    main.add(title);
    main.add(username);
    main.add(password);
    main.add(btnPanel);


    JButton back = new JButton("Back");
    back.setHorizontalAlignment(JLabel.RIGHT);
    back.addActionListener(e->{
      new OpeningFrame().setVisible(true);
      this.dispose();
    });
    JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    topPanel.add(back);
    topPanel.setOpaque(false);
    this.add(topPanel,BorderLayout.NORTH);
    this.add(main,BorderLayout.CENTER);

    registerButton.addActionListener(e ->{
      new RegisterPatient(model);
      this.dispose();
    });

    loginButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        validateLogin();
      }
    });

    setVisible(true);
  }

  /**
   * Validates the login by checking the entered email and password, and either logs in the patient or displays an error message.
   */
  private void validateLogin() {
    String email = emailId.getText();
    char[] passwordChars = passwordField.getPassword();
    String password = new String(passwordChars);
    if (email.isEmpty() || password.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Username and password are required", "Error", JOptionPane.ERROR_MESSAGE);
    } else {
      try {
        ResultSet rs = model.patientLogin(email, password);
        JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        new PatientHomePage(model, rs);
        this.dispose();
      }catch (IllegalArgumentException e){
        JOptionPane.showMessageDialog(this,"Wrong:"+e.getMessage());
      }
    }
  }

}
