package src;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeParseException;
import javax.swing.*;

/**
 * A JFrame class for booking appointments with practitioners.
 * This class provides a graphical user interface for searching and booking appointments with practitioners.
 */
public class BookAppointment extends JFrame {
  JPanel resultsPanel = new JPanel();
  int patientId;
  ConnectionModel model;
  JTextField searchText;

  /**
   * Constructs a new instance of the BookAppointment class.
   *
   * @param model     The ConnectionModel object for database communication.
   * @param patientId The ID of the patient booking the appointment.
   */
  public BookAppointment(ConnectionModel model, int patientId) {
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
    setTitle("Book Prescriptions");
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
    centralPanel.setOpaque(false);
    topPanel.setOpaque(false);
    searchPracs.addActionListener(e ->{
      refreshTable();
    });

    this.add(topPanel, BorderLayout.NORTH);
    this.add(centralPanel, BorderLayout.CENTER);

    setVisible(true);
  }

  /**
   * Refreshes the table displaying practitioner information based on search criteria.
   */
  private void refreshTable() {
    String[] strings = {"practitioner_fn", "practitioner_ln" , "department", "hospital_name", "specializations", "btn:Book_Appointment"};
    resultsPanel.removeAll();
    ResultSet list = null;
    try {
      list = model.practitonerList(searchText.getText());
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    JScrollPane table = createResultSetPanel(list, strings, "practitioner_id");
    table.setPreferredSize(new Dimension(900, 300));
    resultsPanel.add(table);
    resultsPanel.setOpaque(false);
    resultsPanel.setVisible(false);
    resultsPanel.setVisible(true);
  }

  /**
   * Creates a scrollable panel with the specified ResultSet and field names.
   *
   * @param resultSet The ResultSet containing practitioner information.
   * @param fields    An array of field names specifying what information to display.
   * @param id        The ID field used to identify practitioners.
   * @return A JScrollPane containing the practitioner information.
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
   * @param text    The text to display on the label.
   * @param padding The padding (margin) around the label.
   * @return A styled JLabel.
   */
  private static JLabel createStyledLabel(String text, int padding) {
    JLabel label = new JLabel(text);
    label.setHorizontalAlignment(JLabel.CENTER);
    label.setVerticalAlignment(JLabel.CENTER);
    label.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
    return label;
  }

  /**
   * Creates a styled JButton with the specified text and practitioner ID.
   *
   * @param text The text to display on the button.
   * @param set_id The ID of the practitioner associated with the button.
   * @return A styled JButton.
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
   * Gets a clean field name by removing the "btn:" or "int:" prefixes if present.
   *
   * @param field The field name to be cleaned.
   * @return The cleaned field name without "btn:" or "int:" prefixes.
   */
  private static String getCleanFieldName(String field) {
    if (field.startsWith("btn:") || field.startsWith("int:")) {
      return field.substring(4);
    }
    return field;
  }

  /**
   * Handles button actions, such as booking an appointment.
   *
   * @param btnName The name of the button clicked.
   * @param iD      The ID associated with the button.
   * @throws SQLException If a database error occurs.
   */
  private void handleButtons(String btnName, int iD) throws SQLException {

    switch (btnName) {
      case "Book_Appointment" -> {
        dateFrameGetter(iD);
      }
    }
  }

  /**
   * Displays a frame for selecting a date and time for booking an appointment.
   *
   * @param iD The ID of the practitioner for whom the appointment is being booked.
   */
  private void dateFrameGetter(int iD){
    JFrame dateTimeGetter = new JFrame();

    dateTimeGetter.setBounds(300,200,240,200);

    JPanel panel = new JPanel(new FlowLayout());
    JTextField dateField = new JTextField();
    dateField.setPreferredSize(new Dimension(200,30));
    JLabel dateLabel = new JLabel("Date: (YYYY-MM-DD)");
    panel.add(dateLabel);
    panel.add(dateField);

    JTextField hrField = new JTextField();
    hrField.setPreferredSize(new Dimension(200,30));
    JLabel hrLabel = new JLabel("HR: (HH)");
    panel.add(hrLabel);
    panel.add(hrField);


    JButton setDate = new JButton("Submit");
    setDate.addActionListener(e->{
      int res = JOptionPane.showConfirmDialog(null, "You can only schedule 3 appointments today");
      if (res == JOptionPane.YES_OPTION){
      try {
        model.add_appointment(iD, patientId, dateField.getText(), Integer.parseInt(hrField.getText()), "pending");
        dateTimeGetter.dispose();
      } catch (DateTimeParseException|NullPointerException|SQLException|NumberFormatException ex) {
        JOptionPane.showMessageDialog(null, ex.getMessage());
      }

    }});
    panel.add(setDate);

    dateTimeGetter.add(panel);
    dateTimeGetter.setVisible(true);
  };
}
