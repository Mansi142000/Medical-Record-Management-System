package src;

import javax.swing.*;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A JFrame class representing the home page for a practitioner.
 */
public class PractitionerHomePage extends JFrame {

  private JTextField searchField;

  private ConnectionModel model;

  ResultSet practitionerDetails;

  int pracId;

  /**
   * Constructs a new PractitionerHomePage frame to display the practitioner's home page.
   *
   * @param model              The ConnectionModel for database access.
   * @param rs                 The ResultSet containing practitioner details.
   * @throws SQLException      If a database-related error occurs.
   */
  public PractitionerHomePage(ConnectionModel model, ResultSet rs) throws SQLException {

    this.practitionerDetails = rs;
    this.model = model;

    try {
      UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
    } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
             IllegalAccessException e) {
      e.printStackTrace();
    }

    setTitle("Practitioner Home Page");
    setSize(1200, 500);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

    searchField = new JTextField();
    searchField.setPreferredSize(new Dimension(200, 30));

    JButton searchPatients = new JButton("Search");

    this.setLayout(new BorderLayout());

    JPanel main = new JPanel(new BorderLayout());
    JPanel topP = new JPanel(new GridLayout(1, 2));
    JPanel topLeft = new JPanel(new BorderLayout());
    topLeft.setOpaque(false);
    JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    topRight.setPreferredSize(new Dimension(500,100));
    topRight.setOpaque(false);
    JPanel idPanel = new JPanel(new GridLayout(2,3));
    idPanel.setOpaque(false);
    JPanel searchPanel = new JPanel(new FlowLayout());
    searchPanel.setOpaque(false);
    searchPanel.add(new JLabel("Patient List: "));
    searchPanel.add(searchField);
    searchPanel.add(searchPatients);

    topLeft.add(searchPanel, BorderLayout.SOUTH);
    topP.add(topLeft);

    try {
      rs.next();
      pracId = rs.getInt("practitioner_id");
      JLabel name = new JLabel("Name: " +  rs.getString("first_name")+" "+rs.getString("last_name"));
      idPanel.add(name);
      JLabel dept = new JLabel("Department: " +rs.getString("department"));
      idPanel.add(dept);
      JLabel hptl = new JLabel("Hospital: " +rs.getString("hospital_name"));
      idPanel.add(hptl);
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(this, "this isnt working kyle:" + e.getMessage());
    }
    idPanel.add(new JLabel());
    idPanel.add(new JLabel("Specializations: "));

    ResultSet specs = model.specilizationGroup(pracId);
    specs.next();

    idPanel.add(new JLabel(specs.getString("specialization_names")));
    topRight.add(idPanel);

    topP.add(topRight);


    JPanel resultsPanel = new JPanel();
    resultsPanel.setOpaque(false);
    int s = model.searchCount("");


    ResultSet res = model.patientResults("", pracId);
    String[] strings = {"first_name","last_name", "sex", "blood_group", "btn:See ailments", "btn:See Prescriptions", "btn:Patient Details","btn:See Test"};
    String id = "patient_id";

    resultsPanel.setPreferredSize(new Dimension(900, 200));
    JScrollPane pane = createResultSetPanel(res, strings, id);
    pane.setPreferredSize(new Dimension(getWidth() - 100, 300));
    pane.setOpaque(false);
    resultsPanel.add(pane);

    JPanel centerPanel = new JPanel(new GridLayout());
    main.setOpaque(false);
    centerPanel.setOpaque(false);
    resultsPanel.setOpaque(false);
    topP.setOpaque(false);
    centerPanel.add(resultsPanel);
    main.add(centerPanel, BorderLayout.CENTER);
    main.add(topP, BorderLayout.NORTH);


    JButton logOut = new JButton("Log-Out");
    logOut.setHorizontalAlignment(JLabel.RIGHT);
    logOut.addActionListener(e -> {
      new OpeningFrame().setVisible(true);
      this.dispose();
    });


    JPanel topLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    topLeftPanel.add(logOut);
    topLeftPanel.setOpaque(false);

    JButton pendingAppointments = new JButton("Pending Appointments");
    pendingAppointments.setHorizontalAlignment(JLabel.RIGHT);
    pendingAppointments.addActionListener(e -> {
      new PractitionerPendingAppointments(model,pracId);
    });
    JButton notPendingAppointments = new JButton("Not Pending Appointments");
    notPendingAppointments.setHorizontalAlignment(JLabel.RIGHT);
    notPendingAppointments.addActionListener(e -> {
      new PractitionerNotPendingAppointments(model,pracId);
    });

    ResultSet countSet = model.getPendingCount(pracId);
    countSet.next();
    JLabel pendingCount = new JLabel("pending appoints: "+ countSet.getString("pendingCount") +" ");

    JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    topRightPanel.add(notPendingAppointments);
    topRightPanel.add(pendingAppointments);
    topRightPanel.add(pendingCount);
    JPanel topPanel = new JPanel(new GridLayout(1,2));
    topPanel.add(topLeftPanel);
    topPanel.add(topRightPanel);
    this.add(topPanel, BorderLayout.NORTH);
    this.add(main, BorderLayout.CENTER);

    searchPatients.addActionListener(e -> {
      System.out.println(searchField.getText());
      resultsPanel.removeAll();
      ResultSet pr = model.patientResults(searchField.getText(), pracId);
      resultsPanel.setPreferredSize(new Dimension(900, 200));
      resultsPanel.removeAll();
      JScrollPane sPane = createResultSetPanel(pr, strings, id);
      sPane.setPreferredSize(new Dimension(900,300));
      sPane.setOpaque(false);
      resultsPanel.add(sPane);
      main.setVisible(false);
      main.setVisible(true);
    });

    setVisible(true);
  }


  /**
   * Creates a JScrollPane containing a dynamic panel populated with data from a ResultSet.
   *
   * @param resultSet The ResultSet containing the data to display.
   * @param fields    An array of field names specifying the columns to display.
   * @param id        The identifier field for each row in the ResultSet.
   * @return A JScrollPane containing the dynamic panel with the data.
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
            JButton button = createStyledButton(btnText, set_id);
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
        handleButtons(text,set_id);
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
   */
  private void handleButtons(String btnName, int iD){

    switch (btnName){
      case "See ailments":
        new seeAilment(this.model,this.pracId,iD);
        break;
      case "See Prescriptions":
        new seePrecriptions(this.model,this.pracId,iD);
        break;
      case "Patient Details":
        new patientDetails(model, iD);
        break;
      case "See Test":
        new seeTest(this.model,this.pracId, iD);
        break;
    }
  }
}
