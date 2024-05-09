package src;

import javax.swing.*;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A graphical user interface class representing the login page for practitioners in the Medical Records System (MRS) application.
 * This class allows practitioners to enter their credentials (username and password) to access the system.
 */
public class PractitionerLogin extends JFrame {
  private JTextField usernameField;
  private JPasswordField passwordField;

  private ConnectionModel model;

  /**
   * Constructs an instance of the PractitionerLogin class to display the login page for practitioners.
   *
   * @param model The connection model for database access.
   */
  public PractitionerLogin(ConnectionModel model) {

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

    JLabel usernameLabel = new JLabel("Username:");
    usernameLabel.setHorizontalTextPosition(JLabel.RIGHT);
    JLabel passwordLabel = new JLabel("Password:");
    passwordLabel.setHorizontalTextPosition(JLabel.RIGHT);
    usernameField = new JTextField();
    usernameField.setPreferredSize(new Dimension(200,30));
    passwordField = new JPasswordField();
    passwordField.setPreferredSize(new Dimension(200,30));
    JButton loginButton = new JButton("Login");

    this.setLayout(new BorderLayout());
    JPanel main = new JPanel(new GridLayout(4,1) );

    main.setPreferredSize(new Dimension(100,100));
    JPanel username = new JPanel();
    username.setOpaque(false);
    username.add(usernameLabel);
    username.add(usernameField);


    JPanel password = new JPanel();
    password.setOpaque(false);
    password.add(passwordLabel);
    password.add(passwordField);

    JPanel btnPanel = new JPanel();
    btnPanel.setOpaque(false);
    btnPanel.add(loginButton);

    JPanel title = new JPanel();
    title.add(new JLabel("Practitioner Login"));
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

    loginButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        validateLogin();
      }
    });

    setVisible(true);
  }

  /**
   * Validates the user login by checking the entered username and password.
   * If the login is successful, it opens the PractitionerHomePage.
   * Otherwise, it displays an error message.
   */
  private void validateLogin() {
    String username = usernameField.getText();
    char[] passwordChars = passwordField.getPassword();
    String password = new String(passwordChars);

    if (username.isEmpty() || password.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Username and password are required", "Error", JOptionPane.ERROR_MESSAGE);
    } else {
      try {
        ResultSet rs = model.practitonerLogin(username, password);
        JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        new PractitionerHomePage(model, rs);
        this.dispose();
      }catch (IllegalArgumentException e){
        JOptionPane.showMessageDialog(this,"Wrong:"+e.getMessage());
      } catch (SQLException e) {
        JOptionPane.showMessageDialog(null,e.getMessage());
      }
     }
  }

}
