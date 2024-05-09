package src;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;

/**
 * A graphical user interface class representing the details of a prescription in the Medical Records System (MRS) application.
 * This class displays information about a specific prescription, including prescription date, start date, dosage details, practitioners involved,
 * patient details, medicine name, and ailment associated with the prescription.
 */
public class PrescriptionDetails extends JFrame{
    ConnectionModel model;

    int id;

    /**
     * Constructs an instance of the PrescriptionDetails class to display the details of a specific prescription.
     *
     * @param model The connection model for database access.
     * @param id    The unique identifier of the prescription to display.
     */
    PrescriptionDetails(ConnectionModel model, int id){
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
            detailsList = model.getPrescriptionDetails(id);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
        try {
            detailsList.next();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
        try {
            JPanel Prescribe = new JPanel();
            Prescribe.setLayout(new GridLayout(11,1));
            Prescribe.setOpaque(false);

            JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panel1.add(new JLabel("Prescription date: " + detailsList.getString("prescription_date")));
            Prescribe.add(panel1);

            JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panel2.add(new JLabel("Prescription start date: " + detailsList.getString("prescription_start_date")));
            Prescribe.add(panel2);

            JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panel3.add(new JLabel("Number of times in a day: " + detailsList.getString("no_of_times_a_day")));
            Prescribe.add(panel3);

            JPanel panel4 = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panel4.add(new JLabel("Quantity in mg: " + detailsList.getString("quantity_in_mg")));
            Prescribe.add(panel4);

            JPanel panel5 = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panel5.add(new JLabel("Duration in days: " + detailsList.getString("duration_in_days")));
            Prescribe.add(panel5);

            JPanel panel6 = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panel6.add(new JLabel("Dosage description: " + detailsList.getString("dosage_desc")));
            Prescribe.add(panel6);

            JPanel panel7 = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panel7.add(new JLabel(" Practitioner Name: " + detailsList.getString("prac_pres_fn") +" "+  detailsList.getString("prac_pres_ln")));
            Prescribe.add(panel7);

            JPanel panel8 = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panel8.add(new JLabel("Assessing Practitioner Name: " + detailsList.getString("prac_assess_fn") +" "+  detailsList.getString("prac_assess_ln")));
            Prescribe.add(panel8);

            JPanel panel9 = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panel9.add(new JLabel(" Patient Name: "+ detailsList.getString("patient_fn") +" "+  detailsList.getString("patient_ln")));
            Prescribe.add(panel9);

            JPanel panel10 = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panel10.add(new JLabel(" Medicine Name: " + detailsList.getString("medicine_name")));
            Prescribe.add(panel10);

            JPanel panel11 = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panel11.add(new JLabel(" Ailment Name: " + detailsList.getString("ailment_name")));
            Prescribe.add(panel11);

            this.add(Prescribe);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
        }

        setTitle("Prescription Details");
        setSize(300, 400);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());
        setVisible(true);

    }
}
