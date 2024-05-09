package src;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;

/**
 * A JFrame class for updating prescription information associated with a patient's assessment.
 * This class provides a graphical user interface for updating prescription details, including
 * medication quantity, start date, duration, and dosage description.
 */
public class UpdatePrescription extends JFrame implements ActionListener{
  ConnectionModel model;
  JLabel assessmentLabelField;
  JLabel MedicineLabelFieldName;
  int assessmentSetId;
  private int medicineID;

  public UpdatePrescription(ConnectionModel model, int prescriptionID) {
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

    middlePanel.setPreferredSize(new Dimension(500,200));
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

    try {
      ResultSet info = model.getPrescriptionDetails(prescriptionID);
      info.next();
      assessmentSetId = info.getInt("assess_id");
      medicineID = info.getInt("medicine_id");
      ResultSet medicine = model.getMedicineDetails(medicineID);
      medicine.next();
      MedicineLabelFieldName.setText(medicine.getString("medicine_name"));
      ResultSet assessment = model.getAssessmentDetails(assessmentSetId);
      assessment.next();
      assessmentLabelField.setText("Ailment: " + assessment.getString("ailment_name") + " Case: " + assessment.getString("case_description"));
      startDateField.setText(info.getString("prescription_start_date"));
      quantityField.setText(info.getString("quantity_in_mg"));
      timesADayField.setText(info.getString("no_of_times_a_day"));
      durationTextField.setText(info.getString("duration_in_days"));
      dosageTextField.setText(info.getString("dosage_desc"));

    } catch (SQLException e) {
      JOptionPane.showMessageDialog(null,e.getMessage());
    }

    JButton updatePrescription = new JButton("Update Prescription");
    updatePrescription.addActionListener(e -> {
      try {
        model.updatePrescription(prescriptionID,Integer.parseInt(quantityField.getText()),
                Integer.parseInt(durationTextField.getText()),dosageTextField.getText(), startDateField.getText());
        JOptionPane.showMessageDialog(null, "Updated successfully!");
        this.dispose();
      } catch (SQLException | NumberFormatException ex) {
        JOptionPane.showMessageDialog(null,ex.getMessage());
      }
    });
    bottomRight.add(updatePrescription);

    JPanel centerPanel = new JPanel(new BorderLayout());
    centerPanel.add(topPanel,BorderLayout.NORTH);
    centerPanel.add(middlePanel,BorderLayout.CENTER);
    centerPanel.add(bottomPanel,BorderLayout.SOUTH);
    this.add(centerPanel, BorderLayout.CENTER);
    bottomLeft.setOpaque(false);
    bottomRight.setOpaque(false);
    centerPanel.setOpaque(false);

    this.setSize(700, 400);
    this.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {

  }
}