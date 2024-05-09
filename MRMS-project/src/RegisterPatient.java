package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Objects;

/**
 * A graphical user interface class for registering a new patient in the Medical Records System (MRS) application.
 * This class allows users to input and submit their personal and medical information for registration.
 */
public class RegisterPatient extends JFrame {

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField phoneNoField;
    private JTextField addressField;
    private JTextField streetNoField;
    private JTextField streetNameField;
    private JTextField stateField;
    private JTextField zipcodeField;
    private JTextField ethnicityField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> bloodGroupComboBox;
    private JRadioButton maleRadioButton;
    private JRadioButton femaleRadioButton;
    private JFormattedTextField dobTextField;

    ConnectionModel model;

    /**
     * Constructs an instance of the RegisterPatient class for user registration.
     *
     * @param model The connection model for database access.
     */
    public RegisterPatient(ConnectionModel model) {
        this.model = model;
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        setTitle("Registration Page");
        setSize(500, 600);
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

        firstNameField = createTextField();
        lastNameField = createTextField();
        phoneNoField = createTextField();
        addressField = createTextField();
        streetNoField = createTextField();
        streetNameField = createTextField();
        stateField = createTextField();
        zipcodeField = createTextField();
        emailField = createTextField();
        ethnicityField = createTextField();
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200,30));
        bloodGroupComboBox = createBloodGroupComboBox();
        maleRadioButton = new JRadioButton("Male");
        femaleRadioButton = new JRadioButton("Female");
        dobTextField = createDOBTextField();

        JButton registerButton = new JButton("Register");

        this.setLayout(new BorderLayout());
        JPanel main = new JPanel(new GridLayout(17, 1));
        main.setOpaque(false);

        main.setPreferredSize(new Dimension(100, 100));

        createFieldPanel(main, "First Name:", firstNameField);
        createFieldPanel(main, "Last Name:", lastNameField);
        createFieldPanel(main, "Phone No:", phoneNoField);
        createFieldPanel(main, "Address:", addressField);
        createFieldPanel(main, "Street No:", streetNoField);
        createFieldPanel(main, "Street Name:", streetNameField);
        createFieldPanel(main, "State:", stateField);
        createFieldPanel(main, "Zipcode:", zipcodeField);
        createFieldPanel(main, "Email:", emailField);
        createFieldPanel(main, "Password:", passwordField);
        createFieldPanel(main, "Blood Group:", bloodGroupComboBox);
        createRadioButtonsPanel(main, "Sex:", maleRadioButton, femaleRadioButton);
        createFieldPanel(main, "Ethnicity:", ethnicityField);
        createFieldPanel(main, "Date of Birth (yyyy-MM-dd):", dobTextField);

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.add(registerButton);
        main.add(btnPanel);

        JButton back = new JButton("Back");
        back.setHorizontalAlignment(JLabel.RIGHT);
        back.addActionListener(e -> {
            new OpeningFrame().setVisible(true);
            this.dispose();
        });
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(back);
        topPanel.setOpaque(false);
        this.add(topPanel, BorderLayout.NORTH);
        this.add(main, BorderLayout.CENTER);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validateRegistration();
            }
        });

        setVisible(true);
    }

    /**
     * Creates and returns a JTextField component with a preferred size of 200x30 pixels.
     *
     * @return A JTextField component with the specified preferred size.
     */
    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(200, 30));
        return textField;
    }

    /**
     * Creates and returns a JComboBox for selecting blood groups with predefined options.
     *
     * @return A JComboBox for selecting blood groups with a preferred size of 200x30 pixels.
     */
    private JComboBox<String> createBloodGroupComboBox() {
        String[] bloodGroups = {"A-","A+", "B-","B+", "AB-","AB+", "O+","O-"};
        JComboBox<String> comboBox = new JComboBox<>(bloodGroups);
        comboBox.setPreferredSize(new Dimension(200, 30));
        return comboBox;
    }

    /**
     * Creates and returns a JFormattedTextField for entering dates of birth with the "yyyy-MM-dd" date format.
     *
     * @return A JFormattedTextField for entering dates of birth with a preferred size of 200x30 pixels.
     */
    private JFormattedTextField createDOBTextField() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        JFormattedTextField textField = new JFormattedTextField(dateFormat);
        textField.setPreferredSize(new Dimension(200, 30));
        return textField;
    }

    /**
     * Creates a JPanel that contains a label and a specified input component and adds it to the main panel.
     *
     * @param mainPanel  The main panel where the field panel will be added.
     * @param labelText  The label text for the field.
     * @param component  The input component to be added to the field panel.
     */
    private void createFieldPanel(JPanel mainPanel, String labelText, JComponent component) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setOpaque(false);
        JLabel label = new JLabel(labelText);
        fieldPanel.add(label);
        fieldPanel.add(component);
        mainPanel.add(fieldPanel);
    }

    /**
     * Creates a JPanel that contains radio buttons with a specified label and adds it to the main panel.
     *
     * @param mainPanel  The main panel where the radio button panel will be added.
     * @param labelText  The label text for the radio button group.
     * @param buttons    An array of radio buttons to be added to the radio button panel.
     */
    private void createRadioButtonsPanel(JPanel mainPanel, String labelText, JRadioButton... buttons) {
        JPanel radioPanel = new JPanel();
        radioPanel.setOpaque(false);
        JLabel label = new JLabel(labelText);
        radioPanel.add(label);

        ButtonGroup buttonGroup = new ButtonGroup();
        for (JRadioButton button : buttons) {
            buttonGroup.add(button);
            radioPanel.add(button);
        }

        mainPanel.add(radioPanel);
    }

    /**
     * Validates the registration input provided by the user and attempts to register the patient if the input is valid.
     * Displays error messages for invalid input and database errors.
     */
    private void validateRegistration() {
        // Retrieve user input from the text fields
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String phoneNo = phoneNoField.getText();
        String address = addressField.getText();
        String streetNo = streetNoField.getText();
        String streetName = streetNameField.getText();
        String state = stateField.getText();
        String zipcode = zipcodeField.getText();
        String ethnicity = ethnicityField.getText();
        String email = emailField.getText();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);
        String bloodGroup = Objects.requireNonNull(bloodGroupComboBox.getSelectedItem()).toString();
        String sex = maleRadioButton.isSelected() ? "M" : (femaleRadioButton.isSelected() ? "F" : "");
        String dob = dobTextField.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || phoneNo.isEmpty() || address.isEmpty() || streetNo.isEmpty() ||
                streetName.isEmpty() || state.isEmpty() || zipcode.isEmpty() || email.isEmpty() || password.isEmpty() ||
                bloodGroup.isEmpty() || sex.isEmpty() || dob.isEmpty() || ethnicity.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Invalid email format", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters", "Error", JOptionPane.ERROR_MESSAGE);
        }else{
            try {
                ResultSet rs = model.patientRegister(email, password, firstName, lastName, phoneNo, address, streetNo, streetName, state, zipcode, bloodGroup, ethnicity, sex, dob);
                JOptionPane.showMessageDialog(this, "Record Created!", "Success", JOptionPane.INFORMATION_MESSAGE);

            }catch (IllegalArgumentException e){
                JOptionPane.showMessageDialog(this, "Database Error :"+ e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Checks if an email address is in a valid format.
     *
     * @param email The email address to be validated.
     * @return True if the email address is in a valid format, otherwise false.
     */
    private boolean isValidEmail(String email) {

            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
}