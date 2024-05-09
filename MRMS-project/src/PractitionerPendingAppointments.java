package src;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;

/**
 * A JFrame class representing the practitioner's view of pending appointments.
 */
public class PractitionerPendingAppointments extends JFrame {
  /**
   * JTextField for searching appointments.
   */
  private  JTextField searchText;

  /**
   * JPanel to display search results.
   */
  private  JPanel resultsPanel = new JPanel();

  /**
   * The connection model for database interaction.
   */
  ConnectionModel model;

  /**
   * The practitioner's ID.
   */
  int pracId;

  /**
   * Constructs a new PractitionerPendingAppointments object.
   *
   * @param model The connection model for database interaction.
   * @param pracId The practitioner's ID.
   */
  public PractitionerPendingAppointments(ConnectionModel model, int pracId) {
    this.model = model;
    this.pracId = pracId;


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
   * Refreshes the table of pending appointments based on the search criteria.
   */
  private void refreshTable() {
    String[] strings = {"patient_fn","patient_ln", "appointment_date", "appointment_hour", "booking_date", "appointment_status","btn:Approve","btn:Reject"};
    resultsPanel.removeAll();
    ResultSet list = null;
    try {
      list = model.pracPendingAppointments(pracId, searchText.getText());
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
   * Creates a scrollable panel for displaying a ResultSet with specified fields and ID column.
   *
   * @param resultSet The ResultSet containing appointment data.
   * @param fields    An array of field names to display.
   * @param id        The identifier column name.
   * @return A JScrollPane containing the appointment data panel.
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
   * @param text    The text to be displayed on the label.
   * @param padding The padding to be applied around the label.
   * @return A styled JLabel with the specified text and padding.
   */
  private static JLabel createStyledLabel(String text, int padding) {
    JLabel label = new JLabel(text);

    label.setHorizontalAlignment(JLabel.CENTER);
    label.setVerticalAlignment(JLabel.CENTER);
    label.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
    return label;
  }

  /**
   * Creates a styled JButton with the specified text and associates it with an ActionListener.
   *
   * @param text   The text to be displayed on the button.
   * @param set_id The identifier associated with the button.
   * @return A styled JButton with the specified text and ActionListener.
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
   * Removes "btn:" or "int:" prefixes from the field name.
   *
   * @param field The field name to process.
   * @return The cleaned field name without "btn:" or "int:" prefixes.
   */
  private static String getCleanFieldName(String field) {
    if (field.startsWith("btn:") || field.startsWith("int:")) {
      return field.substring(4);
    }
    return field;
  }

  /**
   * Handles button click events based on the button name and identifier.
   *
   * @param btnName The name of the clicked button.
   * @param iD      The identifier associated with the button click.
   * @throws SQLException If a database-related exception occurs.
   */
  private void handleButtons(String btnName, int iD) throws SQLException {

    switch (btnName) {
      case "Reject" -> {
        int res = JOptionPane.showConfirmDialog(null, "Are you sure you want to Reject this appointment?");
        if(res == JOptionPane.YES_OPTION){
          model.rejectAppointment(iD);
          this.dispose();
          new PractitionerPendingAppointments(model, pracId);
        }
      }
      case "Approve" -> {
        int res = JOptionPane.showConfirmDialog(null, "Are you sure you want to Accept this appointment?");
        if(res == JOptionPane.YES_OPTION){
          model.acceptAppointment(iD);
          this.dispose();
          new PractitionerNotPendingAppointments(model, pracId );
        }
      }
    }
  }

}

