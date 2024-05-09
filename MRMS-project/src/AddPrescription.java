package src;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The {@code AddPrescription} class represents a frame for adding prescription records
 * in a healthcare application. It allows users to select assessments and medicines,
 * set prescription details, and submit them to a database.
 */
public class AddPrescription extends JFrame implements ActionListener {
  ConnectionModel model;
  JLabel assessmentLabelField;
  JLabel MedicineLabelFieldName;
  int assessmentSetId;
  private int medicineID;

  /**
   * Constructs a new {@code AddPrescription} frame with the specified model, practitioner ID, and patient ID.
   * It sets up the user interface for adding prescription records.
   *
   * @param model    The {@link ConnectionModel} for database interactions.
   * @param pracID   The practitioner's identifier.
   * @param patientID The patient's identifier.
   */
  public AddPrescription(ConnectionModel model, int pracID, int patientID) {
    this.model = model;
    try {
      UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
    } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
             IllegalAccessException e) {
      e.printStackTrace();
    }

    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    JPanel backgroundPanel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon imageIcon = new ImageIcon("blur-hospital.jpg");
        Image image = imageIcon.getImage();
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
      }
    };
    this.setContentPane(backgroundPanel);
    backgroundPanel.setOpaque(false);

    this.setLayout(new BorderLayout());

    JPanel searchPanel = new JPanel(new FlowLayout());

    JTextField searchAssessment = new JTextField("");
    searchAssessment.setPreferredSize(new Dimension(150, 30));
    searchPanel.add(searchAssessment);

    JButton searchAssessmentButton = new JButton("Search Ailment");
    searchPanel.add(searchAssessmentButton);

    JTextField searchMedicine = new JTextField("");
    searchMedicine.setPreferredSize(new Dimension(150, 30));
    searchPanel.add(searchMedicine);
    JButton searchMedicineButton = new JButton("Search Medicine");
    searchPanel.add(searchMedicineButton);


    searchPanel.setOpaque(false);

    JPanel topPanel = new JPanel(new FlowLayout());

    JLabel assessmentLabel = new JLabel("Assessment:");
    topPanel.add(assessmentLabel);

    assessmentLabelField = new JLabel();
    assessmentLabelField.setPreferredSize(new Dimension(150, 30));
    topPanel.add(assessmentLabelField);

    JLabel medicineLabel = new JLabel("Medicine:");
    topPanel.add(medicineLabel);

    MedicineLabelFieldName = new JLabel();
    MedicineLabelFieldName.setPreferredSize(new Dimension(150, 30));
    topPanel.add(MedicineLabelFieldName);

    topPanel.setOpaque(false);


    JPanel middlePanel = new JPanel(new GridLayout(10,1));

    JLabel Start_Date = new JLabel(" Start Date:(YYYY-MM-DD Format) :");
    middlePanel.add(Start_Date);

    JTextField startDateField = new JTextField(20);
    middlePanel.add(startDateField);
    middlePanel.setOpaque(false);

    JLabel quantityLabel = new JLabel(" Quantity in mg: ");
    middlePanel.add(quantityLabel);

    JTextField quantityField = new JTextField(20);
    middlePanel.add(quantityField);
    middlePanel.setOpaque(false);

    JLabel timesADayLabel = new JLabel(" Number of times a day :");
    middlePanel.add(timesADayLabel);

    JTextField timesADayField = new JTextField(20);
    middlePanel.add(timesADayField);
    middlePanel.setOpaque(false);

    JLabel durationLabel = new JLabel(" Duration in days :");
    middlePanel.add(durationLabel);

    JTextField durationTextField = new JTextField(20);
    middlePanel.add(durationTextField);
    middlePanel.setOpaque(false);

    JLabel dosageLabel = new JLabel(" Dosage Description :");
    middlePanel.add(dosageLabel);

    JTextField dosageTextField = new JTextField(20);
    middlePanel.add(dosageTextField);
    middlePanel.setOpaque(false);

    JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
    JPanel bottomLeft = new JPanel(new FlowLayout());
    JPanel bottomRight = new JPanel(new FlowLayout());
    bottomPanel.add(bottomLeft);
    bottomPanel.add(bottomRight);
    bottomPanel.setOpaque(false);

    JButton addAssessmentButton = new JButton("Add Ailment");
    addAssessmentButton.addActionListener(e -> {
      if (assessmentLabelField.getText().isEmpty() || startDateField.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this, "All fields must be filled", "Error", JOptionPane.ERROR_MESSAGE);
      } else {
        try {
          model.insertPrescription( pracID,
                  assessmentSetId,
                  medicineID,
                  startDateField.getText(),
                  Integer.parseInt(timesADayField.getText()),
                  Integer.parseInt(quantityField.getText()),
                  Integer.parseInt(durationTextField.getText()),
                  dosageTextField.getText());
          JOptionPane.showMessageDialog(this, "Record Inserted", "Success", JOptionPane.INFORMATION_MESSAGE);
          this.dispose();
          new seePrecriptions(model, pracID, patientID);

        } catch (SQLException ex) {
          JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

      }
    });
    bottomRight.add(addAssessmentButton);

    ResultSet assessmentList = model.getAssessmentList(patientID, searchAssessment.getText());
    String[] strings = {"ailment_name", "assessement_date", "case_description", "btn:set"};
    JPanel assessmentPanel = new JPanel(new FlowLayout());
    JScrollPane table = createResultSetPanel(assessmentList, strings, "assess_id");
    assessmentPanel.add(table);

    searchAssessmentButton.addActionListener(e -> {
      assessmentPanel.removeAll();
      JScrollPane pane = createResultSetPanel(model.getAssessmentList(patientID, searchAssessment.getText()), strings, "ailment_id");
      assessmentPanel.add(pane);
      this.setVisible(false);
      this.setVisible(true);

    });

    ResultSet MedicineList = model.getMedicineList(searchMedicine.getText());
    String[] stringList = {"medicine_name", "medicine_desc", "btn:set medicine"};
    JPanel medicinePanel = new JPanel(new FlowLayout());
    JScrollPane table2 = createResultSetPanel(MedicineList, stringList, "medicine_id");
    medicinePanel.add(table2);

    searchAssessmentButton.addActionListener(e -> {
      medicinePanel.removeAll();
      JScrollPane pane = createResultSetPanel(model.getMedicineList( searchMedicine.getText()), stringList, "medicine_id");
      medicinePanel.add(pane);
      this.setVisible(false);
      this.setVisible(true);

    });

    JPanel centerPanel = new JPanel(new BorderLayout());
    this.add(searchPanel, BorderLayout.NORTH);
    JPanel tablePanel = new JPanel(new GridLayout(2,1));
    tablePanel.add(medicinePanel);
    tablePanel.add(assessmentPanel);
    this.add(tablePanel);
    centerPanel.add(topPanel,BorderLayout.NORTH);
    centerPanel.add(middlePanel,BorderLayout.CENTER);
    centerPanel.add(bottomPanel,BorderLayout.SOUTH);
    this.add(centerPanel, BorderLayout.SOUTH);
    bottomLeft.setOpaque(false);
    bottomRight.setOpaque(false);
    centerPanel.setOpaque(false);

    table.setPreferredSize(new Dimension(1200, 150));
    table2.setPreferredSize(new Dimension(1200, 150));

    this.setSize(700, 500);
    this.pack();

    this.setVisible(true);
    this.setLocationRelativeTo(null);
  }

  /**
   * Creates a {@link JScrollPane} containing a {@link JPanel} with data from the {@link ResultSet}.
   * The panel includes headers and rows for each record, with each field in a separate cell.
   *
   * @param resultSet The {@link ResultSet} containing data to be displayed.
   * @param fields    The array of field names to be displayed.
   * @param id        The identifier field in the {@link ResultSet}.
   * @return A {@link JScrollPane} containing the formatted data panel.
   */
  public JScrollPane createResultSetPanel(ResultSet resultSet, String[] fields, String id) {
    JPanel panel = new JPanel(new GridLayout(0, fields.length));
    int cellPadding = 10;

    try {
      for (String field : fields) {
        JLabel headerLabel = new JLabel(getCleanFieldName(field));
        headerLabel.setBackground(Color.DARK_GRAY); // Set background color for the header
        headerLabel.setForeground(Color.WHITE); // Set text color for the header
        headerLabel.setHorizontalAlignment(JLabel.CENTER);
        headerLabel.setVerticalAlignment(JLabel.CENTER);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(cellPadding, cellPadding, cellPadding, cellPadding));
        headerLabel.setOpaque(true);
        panel.add(headerLabel);
      }

      while (resultSet.next()) {
        int set_id = resultSet.getInt(id);
        for (String field : fields) {
          if (field.startsWith("int:")) {
            int intValue = resultSet.getInt(field.substring(4));
            JLabel label = createStyledLabel(String.valueOf(intValue), cellPadding);
            panel.add(label);
          } else if (field.startsWith("date:")) {
            java.sql.Date dateValue = resultSet.getDate(field.substring(5));
            JLabel label = createStyledLabel(String.valueOf(dateValue), cellPadding);
            panel.add(label);
          } else if (field.startsWith("btn:")) {
            String btnText = field.substring(4);
            JButton button = createStyledButton(btnText, set_id);
            panel.add(button);
          } else {
            String stringValue = resultSet.getString(field);
            JLabel label = createStyledLabel(stringValue, cellPadding);
            panel.add(label);
          }
        }
      }
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(null, e.getMessage());
      e.printStackTrace();
    }

    JScrollPane scrollPane = new JScrollPane(panel);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

    return scrollPane;
  }

  /**
   * Creates a styled {@link JLabel} with specified text and padding.
   *
   * @param text    The text to be displayed in the label.
   * @param padding The padding around the label.
   * @return A styled {@link JLabel}.
   */
  private static JLabel createStyledLabel(String text, int padding) {
    JLabel label = new JLabel(text);
    label.setHorizontalAlignment(JLabel.CENTER);
    label.setVerticalAlignment(JLabel.CENTER);
    label.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
    return label;
  }

  /**
   * Creates a styled {@link JButton} with specified text and an identifier.
   * The button triggers an action based on the identifier when clicked.
   *
   * @param text   The text to be displayed on the button.
   * @param set_id The identifier associated with the button.
   * @return A styled {@link JButton}.
   */
  private JButton createStyledButton(String text, int set_id) {
    JButton button = new JButton(text);
    button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          handleButtons(text, set_id);
        } catch (SQLException ex) {
          throw new RuntimeException(ex);
        }
        System.out.println("Button clicked: " + set_id + text);
      }
    });
    return button;
  }

  /**
   * Cleans the field name by removing prefixes like "btn:" or "int:".
   *
   * @param field The original field name.
   * @return The cleaned field name.
   */
  private static String getCleanFieldName(String field) {
    if (field.startsWith("btn:") || field.startsWith("int:")) {
      return field.substring(4);
    }
    return field;
  }

  /**
   * Handles button clicks by setting the assessment or medicine ID and updating the corresponding labels.
   * This method is triggered when a button in the assessment or medicine data panel is clicked.
   *
   * @param btnName The name of the button that was clicked.
   * @param iD      The identifier associated with the button.
   * @throws SQLException If an SQL error occurs.
   */
  private void handleButtons(String btnName, int iD) throws SQLException {

    switch (btnName) {
      case "set":
        this.assessmentSetId = iD;
        ResultSet result = model.getAssessmentDetails(iD);
        result.next();
        this.assessmentLabelField.setText("Ailment: " + result.getString("ailment_name") + " Case: " + result.getString("case_description"));
        break;
      case "set medicine":
        this.medicineID = iD;
        ResultSet medicine = model.getMedicineDetails(iD);
        medicine.next();
        this.MedicineLabelFieldName.setText(medicine.getString("medicine_name"));

        break;
    }
  }


  /**
   * Handles action events triggered by user interactions with the GUI components.
   * Currently, this method does not implement any specific functionality.
   *
   * @param e The action event that occurred.
   */
  @Override
  public void actionPerformed(ActionEvent e) {

  }
}
