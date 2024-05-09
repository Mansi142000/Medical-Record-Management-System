package src;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A JFrame class for updating assessment information associated with a patient's ailment.
 * This class provides a graphical user interface for updating assessment details, including
 * ailment name, case description, and severity level.
 */
public class UpdateAssessment extends JFrame{
  ConnectionModel model;
  JLabel ailmentTextField;
  int assessId;
  int pracID;
  int patientID;

  public UpdateAssessment(ConnectionModel model, int assessId, int pracId, int patientId) {
    this.model = model;
    this.assessId = assessId;
    this.pracID = pracId;
    this.patientID = patientId;

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

    JPanel topPanel = new JPanel(new FlowLayout());
    JLabel ailmentLabel = new JLabel("Ailment:");
    topPanel.add(ailmentLabel);

    ailmentTextField = new JLabel();
    ailmentTextField.setPreferredSize(new Dimension(150, 30));
    topPanel.add(ailmentTextField);
    topPanel.setOpaque(false);

    JPanel middlePanel = new JPanel(new FlowLayout());

    JLabel caseLabel = new JLabel("Case Description:");
    middlePanel.add(caseLabel);

    JTextField caseField = new JTextField(20);
    middlePanel.add(caseField);
    middlePanel.setOpaque(false);

    JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
    JPanel bottomLeft = new JPanel(new FlowLayout());
    JPanel bottomRight = new JPanel(new FlowLayout());
    bottomPanel.add(bottomLeft);
    bottomPanel.add(bottomRight);
    bottomPanel.setOpaque(false);

    String[] severityOptions = {"", "Minimal", "Low", "Minor", "Moderate", "Medium", "Significant", "High", "Critical", "Severe", "Extreme"};
    JComboBox<String> severityComboBox = new JComboBox<>(severityOptions);
    bottomLeft.add(new JLabel("Severity:"));
    bottomLeft.add(severityComboBox);

    try {
      ResultSet assessmentInfo = model.getAssessmentDetails(assessId);
      assessmentInfo.next();
      caseField.setText(assessmentInfo.getString("case_description"));
      ailmentTextField.setText(assessmentInfo.getString("ailment_name"));
      severityComboBox.setSelectedItem(assessmentInfo.getString("severity"));
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(this,e.getMessage());
    }

    JButton addAilmentButton = new JButton("Update Ailment");
    addAilmentButton.addActionListener(e -> {
      if (ailmentTextField.getText().isEmpty() || caseField.getText().isEmpty() || severityComboBox.getSelectedItem().equals("")) {
        JOptionPane.showMessageDialog(this, "All fields must be filled", "Error", JOptionPane.ERROR_MESSAGE);
      } else {
        this.dispose();
        try {

          model.updateAssessment(assessId,
                  severityComboBox.getSelectedItem().toString(),
                  caseField.getText(),
                  pracID);
          JOptionPane.showMessageDialog(this, "Updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
          this.dispose();
          new seeAilment(model, pracID, patientID);

        } catch (SQLException ex) {
          JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

      }
    });
    bottomRight.add(addAilmentButton);

    JPanel centerPanel = new JPanel(new GridLayout(3, 1));
    centerPanel.add(topPanel);
    centerPanel.add(middlePanel);
    centerPanel.add(bottomPanel);
    this.add(centerPanel, BorderLayout.CENTER);
    bottomLeft.setOpaque(false);
    bottomRight.setOpaque(false);
    centerPanel.setOpaque(false);
    this.setSize(700, 350);
    this.setVisible(true);
  }
}
