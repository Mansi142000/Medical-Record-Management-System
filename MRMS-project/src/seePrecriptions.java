package src;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;

/**
 * A JFrame class for viewing patient's prescriptions.
 */
public class seePrecriptions extends JFrame implements ActionListener {
  private final JTextField searchPrescriptions;
  private final JPanel resultsPanel;
  ConnectionModel model;
  int  pracID;
  int patientID;

  /**
   * Constructs a `seePrecriptions` frame for viewing patient's prescriptions.
   *
   * @param model     The ConnectionModel instance for database access.
   * @param pracID    The ID of the practicing medical professional.
   * @param patientID The patient's ID.
   */
  public seePrecriptions (ConnectionModel model, int pracID, int patientID){
    this.model = model;
    this.pracID = pracID;
    this.patientID = patientID;

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

    setTitle("See Patient Prescriptions");
    setSize(1300, 500);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);
    setVisible(true);

    this.setLayout(new BorderLayout());
    JPanel topPanel = new JPanel(new GridLayout(1, 2));
    JPanel topLeft = new JPanel(new FlowLayout());
    this.searchPrescriptions = new JTextField("");
    searchPrescriptions.setPreferredSize(new Dimension(200, 30));

    JButton searchPrescriptionsBtn = new JButton("search Prescriptions");
    searchPrescriptionsBtn.setActionCommand("search");
    topLeft.add(searchPrescriptions);
    topLeft.add(searchPrescriptionsBtn);
    topLeft.setOpaque(false);
    topPanel.add(topLeft);

    topPanel.setOpaque(false);
    JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton insertPrescription = new JButton("Insert Prescription");
    insertPrescription.setActionCommand("insertPrescription");
    insertPrescription.addActionListener(e->{
      new AddPrescription(model, pracID, patientID);
    });
    topRight.add(insertPrescription);
    topRight.setOpaque(false);
    topPanel.add(topRight);
    this.add(topPanel, BorderLayout.NORTH);

    resultsPanel = new JPanel(new FlowLayout());
    refreshTable();
    searchPrescriptionsBtn.addActionListener(e -> {
      refreshTable();
    });
    this.add(resultsPanel, BorderLayout.CENTER);
  }

  /**
   * Refreshes the table displaying patient's prescriptions based on search criteria.
   */
  private void refreshTable() {
    String[] strings = {"prac_pres_fn", "prac_pres_ln", "prac_assess_fn", "prac_assess_ln","medicine_name","ailment_name","btn:update","btn:delete", "btn:see details"};
    resultsPanel.removeAll();
    ResultSet list = null;
    try {
      list = model.getPrescriptions(searchPrescriptions.getText(), patientID);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    JScrollPane table = createResultSetPanel(list, strings, "prescription_id");
    table.setPreferredSize(new Dimension(1200, 300));
    resultsPanel.add(table);
    resultsPanel.setOpaque(false);
    resultsPanel.setVisible(false);
    resultsPanel.setVisible(true);
    resultsPanel.revalidate();
    resultsPanel.repaint();
  }

  /**
   * Creates a scrollable panel to display the ResultSet data with custom formatting.
   *
   * @param resultSet The ResultSet containing the data to be displayed.
   * @param fields    An array of field names and formatting options for each column.
   * @param id        The unique identifier column name used for button actions.
   * @return A JScrollPane containing the formatted data from the ResultSet.
   */
  public JScrollPane createResultSetPanel(ResultSet resultSet, String[] fields, String id) {
    JPanel headerPanel = new JPanel(new GridLayout(1, fields.length));
    headerPanel.setPreferredSize(new Dimension(1200, 30));
    int cellPadding = 10;
    for (String field : fields) {
      JLabel headerLabel = new JLabel(getCleanFieldName(field), JLabel.CENTER);
      headerLabel.setForeground(Color.WHITE);
      headerLabel.setOpaque(true);
      headerLabel.setBackground(Color.DARK_GRAY);
      headerLabel.setBorder(BorderFactory.createEmptyBorder(cellPadding, cellPadding, cellPadding, cellPadding));
      headerPanel.add(headerLabel);
    }

    JPanel dataPanel = new JPanel();
    dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));

    dataPanel.add(headerPanel);

    boolean hasData = false;
    try {
      while (resultSet.next()) {
        hasData = true;
        JPanel rowPanel = new JPanel(new GridLayout(1, fields.length));
        rowPanel.setBackground(Color.WHITE);
        int set_id = resultSet.getInt(id);

        for (String field : fields) {
          if (field.startsWith("int:")) {
            int intValue = resultSet.getInt(field.substring(4));
            JLabel label = createStyledLabel(String.valueOf(intValue), cellPadding);
            rowPanel.add(label);
          } else if (field.startsWith("date:")) {
            java.sql.Date dateValue = resultSet.getDate(field.substring(5));
            JLabel label = createStyledLabel(String.valueOf(dateValue), cellPadding);
            rowPanel.add(label);
          } else if (field.startsWith("btn:")) {
            String btnText = field.substring(4);
            JButton button = createStyledButton(btnText, set_id);
            rowPanel.add(button);
          } else {
            String stringValue = resultSet.getString(field);
            JLabel label = createStyledLabel(stringValue, cellPadding);
            rowPanel.add(label);
          }
        }
        dataPanel.add(rowPanel);
      }
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(null,e.getMessage());
    }

    if (!hasData) {
      JLabel noDataLabel = new JLabel("No data available", JLabel.CENTER);
      noDataLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
      noDataLabel.setForeground(Color.GRAY);
      noDataLabel.setFont(new Font("Arial", Font.ITALIC, 16));

      JPanel placeholderPanel = new JPanel();
      placeholderPanel.setLayout(new BorderLayout());
      placeholderPanel.add(noDataLabel, BorderLayout.CENTER);
      placeholderPanel.setPreferredSize(new Dimension(1200, 300));

      dataPanel.add(placeholderPanel);
    }

    JScrollPane scrollPane = new JScrollPane(dataPanel);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setPreferredSize(new Dimension(1200, 300));
    return scrollPane;
  }

  /**
   * Creates a styled JLabel with the provided text and padding.
   *
   * @param text    The text to be displayed on the label.
   * @param padding The padding (in pixels) to be applied to the label.
   * @return A JLabel with the specified text and padding.
   */
  private static JLabel createStyledLabel(String text, int padding) {
    JLabel label = new JLabel(text);
    label.setHorizontalAlignment(JLabel.CENTER);
    label.setVerticalAlignment(JLabel.CENTER);
    label.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
    return label;
  }

  /**
   * Creates a styled JButton with the provided text and sets an ActionListener.
   *
   * @param text   The text to be displayed on the button.
   * @param set_id The unique identifier associated with the button action.
   * @return A JButton with the specified text and ActionListener.
   */
  private JButton createStyledButton(String text, int set_id) {
    JButton button = new JButton(text);
    button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          handleButtons(text,set_id);
        } catch (SQLException ex) {
          JOptionPane.showMessageDialog(null,ex.getMessage());
        }
        System.out.println("Button clicked: " + set_id + text);
      }
    });
    return button;
  }

  /**
   * Extracts the clean field name from a formatted field string (e.g., "btn:Button Text" -> "Button Text").
   *
   * @param field The formatted field string.
   * @return The clean field name without formatting options.
   */
  private static String getCleanFieldName(String field) {
    if (field.startsWith("btn:") || field.startsWith("int:")) {
      return field.substring(4);
    }
    return field;
  }

  /**
   * Handles button actions based on the button name and associated ID.
   *
   * @param btnName The name of the clicked button.
   * @param iD      The unique identifier associated with the button action.
   * @throws SQLException If there is an error in database operations.
   */
  private void handleButtons(String btnName, int iD) throws SQLException {
    switch (btnName) {
      case "delete" -> {int ans = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this Prescription?", "Delete Prescription", JOptionPane.YES_NO_CANCEL_OPTION);
        if (ans == JOptionPane.YES_OPTION) {
          model.deletePrescription(iD);
          refreshTable();
        }
      }
      case "update" -> {
        new UpdatePrescription(model,iD);
      }
      case "see details" ->{
        new PrescriptionDetails(model,iD);
      }
    }
  }

  /**
   * ActionListener implementation for handling button clicks and actions.
   *
   * @param e The ActionEvent generated by the button click.
   */
  @Override
  public void actionPerformed(ActionEvent e) {

  }
}
