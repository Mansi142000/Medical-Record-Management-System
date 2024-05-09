package src;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A JFrame class for displaying detailed information about a specific test.
 * This class provides a graphical user interface for showing various details
 * related to a particular test, such as the test name, description, patient and
 * practitioner names, test date, and findings.
 */
public class TestDetails extends JFrame{

    ConnectionModel model;

    int id;
    TestDetails(ConnectionModel model, int id){
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
            detailsList = model.getTestDetails(id);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
        try {
            detailsList.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            JPanel Prescribe = new JPanel();
            Prescribe.setLayout(new GridLayout(6,1));
            Prescribe.setOpaque(false);

            JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panel1.add(new JLabel("Test Name: " + detailsList.getString("test_name")));
            Prescribe.add(panel1);

            JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panel2.add(new JLabel("Test Description: " + detailsList.getString("test_desc")));
            Prescribe.add(panel2);

            JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panel3.add(new JLabel("Patient Name: " + detailsList.getString("patient_fn") +" "+  detailsList.getString("patient_ln")));
            Prescribe.add(panel3);

            JPanel panel4 = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panel4.add(new JLabel("Practitioner Name: " + detailsList.getString("practitioner_fn") +" "+  detailsList.getString("practitioner_ln")));
            Prescribe.add(panel4);

            JPanel panel5 = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panel5.add(new JLabel("Test Date: " + detailsList.getString("test_date")));
            Prescribe.add(panel5);

            JPanel panel6 = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panel6.add(new JLabel("Findings: " + detailsList.getString("findings")));
            Prescribe.add(panel6);

            this.add(Prescribe);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
        }

        setTitle("Test Details");
        setSize(300, 400);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());
        setVisible(true);

    }
}
