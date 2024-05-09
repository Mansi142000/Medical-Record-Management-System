package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A JFrame class for updating test performance information associated with a patient.
 * This class provides a graphical user interface for updating test details, including
 * test date and findings.
 */
public class UpdatePerform extends JFrame implements ActionListener {
    ConnectionModel model;
    JLabel testTextField;
    JTextField dateField;

    JTextField findingsField;

    int testId = -1;
    int performId;
    int pracId;
    int patientId;


    public UpdatePerform(ConnectionModel model, int performId, int pracId, int patientId){
        this.performId = performId;
        this.pracId = pracId;
        this.patientId = patientId;
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

        JLabel testLabel = new JLabel("Test:");
        topPanel.add(testLabel);

        testTextField = new JLabel();
        testTextField.setPreferredSize(new Dimension(150,30));
        topPanel.add(testTextField);
        topPanel.setOpaque(false);

        JPanel middlePanel = new JPanel(new FlowLayout());

        JLabel findingsLabel = new JLabel("Findings:");
        middlePanel.add(findingsLabel);
        findingsField = new JTextField(20);
        middlePanel.add(findingsField);

        JLabel dateLabel = new JLabel("Enter Test Date (YYYY-MM-DD):");
        middlePanel.add(dateLabel);
        dateField = new JTextField(20);
        middlePanel.add(dateField);
        middlePanel.setOpaque(false);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        JPanel bottomLeft = new JPanel(new FlowLayout());
        JPanel bottomRight = new JPanel(new FlowLayout());
        bottomPanel.add(bottomLeft);
        bottomPanel.add(bottomRight);
        bottomPanel.setOpaque(false);

        try {
            ResultSet performDetail = model.getPerform(performId);
            performDetail.next();
            testTextField.setText(performDetail.getString("test_name"));
            testId = performDetail.getInt("test_id");
            dateField.setText(performDetail.getString("test_date"));
            findingsField.setText(performDetail.getString("findings"));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
        JButton updatePerform = new JButton("update Test");
        updatePerform.addActionListener(e->{
            try {
                if (dateField.getText().trim().isEmpty() || findingsField.getText().trim().isEmpty() || testId==-1) {
                    JOptionPane.showMessageDialog(this, "Please fill in all fields", "Incomplete Data", JOptionPane.ERROR_MESSAGE);
                } else {
                    model.updatePerform(performId,dateField.getText(),findingsField.getText());
                    this.dispose();
                    JOptionPane.showMessageDialog(null, "Updated successfully!");
                    new seeTest(model, pracId, patientId);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
                this.dispose();
                new seeTest(model, pracId, patientId);
            }
        });
        bottomRight.add(updatePerform);

        JPanel centerPanel = new JPanel(new GridLayout(3, 1));
        centerPanel.add(topPanel);
        centerPanel.add(middlePanel);
        centerPanel.add(bottomPanel);
        this.add(centerPanel, BorderLayout.SOUTH);
        bottomLeft.setOpaque(false);
        bottomRight.setOpaque(false);
        centerPanel.setOpaque(false);

        this.setSize(700, 350);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
