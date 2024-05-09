package src;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * A JFrame class representing the patient's view of their appointments.
 */
public class seeAppointment extends JFrame implements ActionListener {

  /**
   * The connection model for database interaction.
   */
  ConnectionModel model;

  /**
   * The patient's ID.
   */
  int patientId;

  /**
   * JPanel to display appointment results.
   */
  JPanel resultsPanel;

  /**
   * Constructs a new seeAppointment object.
   *
   * @param model     The connection model for database interaction.
   * @param patientId The patient's ID.
   */
  public seeAppointment(ConnectionModel model, int patientId) {
    this.model = model;
    this.patientId = patientId;

    try {
      UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
    } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
             IllegalAccessException e) {
      e.printStackTrace();
    }

    setTitle("Patient Home Page");
    setSize(1000, 500);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

    this.setLayout(new BorderLayout());

    JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

    JButton bookAppointment = new JButton("Book Appointment");
    bookAppointment.addActionListener(e ->{
      new BookAppointment(model, patientId);
    });
    topPanel.add(bookAppointment);
    topPanel.setOpaque(false);
    this.add(topPanel,BorderLayout.NORTH);

    JPanel centralPanel = new JPanel(new FlowLayout());
    JButton pendingAppointments = new JButton("Pending Appointments");
    pendingAppointments.addActionListener(e ->{
      new PatientPendingAppointments(model,patientId);
    });

    JButton notPendingAppointments = new JButton("Not Pending Appointments");
    notPendingAppointments.addActionListener(e ->{
      new PatientNotPendingAppointments(model,patientId);
    });

    centralPanel.add(notPendingAppointments);
    centralPanel.add(pendingAppointments);
    centralPanel.setOpaque(false);
    this.add(centralPanel, BorderLayout.CENTER);
    this.setVisible(true);
  }

  /**
   * ActionListener method implementation.
   */
  @Override
  public void actionPerformed(ActionEvent e) {

  }
}
