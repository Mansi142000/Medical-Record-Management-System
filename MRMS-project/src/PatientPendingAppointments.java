package src;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;

/**
 * A JFrame class for displaying pending appointments for a patient.
 */
public class PatientPendingAppointments extends JFrame {

  private  JTextField searchText;
  private  JPanel resultsPanel = new JPanel();
  ConnectionModel model;
  int patientId;

  /**
   * Constructs a new PatientPendingAppointments frame to display pending appointments for a patient.
   *
   * @param model     The ConnectionModel for database access.
   * @param patientId The identifier of the patient.
   */
  public PatientPendingAppointments(ConnectionModel model, int patientId) {
    this.model = model;
    this.patientId = patientId;


    try {
      UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
    } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
             IllegalAccessException e) {
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
    setTitle("Pending Appointments");
    setSize(1000, 500);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);

    setLayout(new BorderLayout());

    JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    searchText = new JTextField("");
    searchText.setPreferredSize(new Dimension(200,30));
    JButton searchPracs = new JButton("search");
    topPanel.add(searchText);
    topPanel.add(searchPracs);
    JPanel centralPanel = new JPanel(new FlowLayout());
    refreshTable();
    centralPanel.add(resultsPanel);
    searchPracs.addActionListener(e ->{
      refreshTable();
    });

    this.add(topPanel, BorderLayout.NORTH);
    this.add(centralPanel, BorderLayout.CENTER);
    topPanel.setOpaque(false);
    centralPanel.setOpaque(false);
    setVisible(true);
  }

  /**
   * Refreshes the appointment table based on search criteria.
   */
  private void refreshTable() {
    String[] strings = {"practitioner_fn","practitioner_ln", "appointment_date", "appointment_hour", "booking_date", "appointment_status","btn:Delete"};
    resultsPanel.removeAll();
    ResultSet list = null;
    try {
      list = model.patientPendingAppointments(patientId, searchText.getText());
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    JScrollPane table = createResultSetPanel(list, strings, "appointment_id");
    table.setPreferredSize(new Dimension(900, 300));
    resultsPanel.add(table);
    resultsPanel.setOpaque(false);
    resultsPanel.setVisible(false);
    resultsPanel.setVisible(true);
  }

  /**
   * Creates a JScrollPane containing a panel with appointment information.
   *
   * @param resultSet The ResultSet containing appointment data.
   * @param fields    An array of field names to display in the table.
   * @param id        The identifier field for appointments.
   * @return A JScrollPane with appointment information.
   */
  public JScrollPane createResultSetPanel(ResultSet resultSet, String[] fields, String id) {
    JPanel panel = new JPanel(new GridLayout(0, fields.length));
    int cellPadding = 10;

    try {
      for (String field : fields) {
        JLabel headerLabel = new JLabel(getCleanFieldName(field));
        headerLabel.setBackground(Color.DARK_GRAY);
        headerLabel.setForeground(Color.WHITE);
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
            JButton button = createStyledButton(btnText,set_id);
            panel.add(button);
          } else {
            String stringValue = resultSet.getString(field);
            JLabel label = createStyledLabel(stringValue, cellPadding);
            panel.add(label);
          }
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    JScrollPane scrollPane = new JScrollPane(panel);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

    return scrollPane;
  }

  /**
   * Creates a styled JLabel with the specified text and padding.
   *
   * @param text    The text to be displayed in the label.
   * @param padding The padding to be applied to the label's borders.
   * @return A styled JLabel with centered alignment and padding.
   */
  private static JLabel createStyledLabel(String text, int padding) {
    JLabel label = new JLabel(text);

    label.setHorizontalAlignment(JLabel.CENTER);
    label.setVerticalAlignment(JLabel.CENTER);
    label.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
    return label;
  }

  /**
   * Creates a styled JButton with the specified text and sets up an action listener.
   *
   * @param text   The text to be displayed on the button.
   * @param set_id The identifier associated with the button.
   * @return A styled JButton with the specified text and action listener.
   */
  private JButton createStyledButton(String text, int set_id) {
    JButton button = new JButton(text);

    button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          handleButtons(text,set_id);
        } catch (SQLException ex) {
          throw new RuntimeException(ex);
        }
        System.out.println("Button clicked: " + set_id + text);
      }
    });
    return button;
  }

  /**
   * Removes "btn:" or "int:" prefixes from the field name if present.
   *
   * @param field The field name to be cleaned.
   * @return The cleaned field name with prefixes removed.
   */
  private static String getCleanFieldName(String field) {
    if (field.startsWith("btn:") || field.startsWith("int:")) {
      return field.substring(4);
    }
    return field;
  }

  /**
   * Handles button actions based on the provided button name and identifier.
   *
   * @param btnName The name of the button that triggered the action.
   * @param iD      The identifier associated with the button.
   * @throws SQLException If a database-related error occurs.
   */
  private void handleButtons(String btnName, int iD) throws SQLException {

    switch (btnName) {
      case "Delete" -> {
        int res = JOptionPane.showConfirmDialog(null, "Are you sure you want to Delete this appointment?");
        if(res == JOptionPane.YES_OPTION){
          model.deleteAppointment(iD);
          this.dispose();
          new PatientPendingAppointments(model, patientId);
        }
      }
    }
  }

}
