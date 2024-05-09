package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The {@code AddPerform} class represents a frame for adding test performance records
 * in a healthcare application. It allows users to set test details and submit them to a database.
 */
public class AddPerform extends JFrame implements ActionListener {

    ConnectionModel model;
    JLabel testTextField;
    JTextField dateField;

    JTextField findingsField;

    int testId = -1;
    int patientID;
    int practitionerID;

    /**
     * Constructs a new {@code AddPerform} frame with specified model, patient ID, and practitioner ID.
     * It sets up the user interface for adding test performance records.
     *
     * @param model          The {@link ConnectionModel} for database interactions.
     * @param patientID      The patient's identifier.
     * @param practitionerID The practitioner's identifier.
     * @throws SQLException If an SQL error occurs.
     */
    public AddPerform(ConnectionModel model,  int patientID, int practitionerID) throws SQLException {
        this.patientID = patientID;
        this.practitionerID = practitionerID;

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

        JPanel searchPanel = new JPanel(new FlowLayout());

        JTextField searchText = new JTextField("");
        searchText.setPreferredSize(new Dimension(150,30));
        searchPanel.add(searchText);
        JButton searchButton = new JButton("Search");

        searchPanel.add(searchButton);
        searchPanel.setOpaque(false);

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

        JButton addPerform = new JButton("Add Test");
        addPerform.addActionListener(e->{

            try {
                if (dateField.getText().trim().isEmpty() || findingsField.getText().trim().isEmpty() || testId==-1) {
                    JOptionPane.showMessageDialog(this, "Please fill in all fields", "Incomplete Data", JOptionPane.ERROR_MESSAGE);
                } else {
                    model.insertPerform(testId, patientID, practitionerID, dateField.getText(), findingsField.getText());
                    JOptionPane.showMessageDialog(null,"Test Record inserted Successfully!");
                    dispose();
                    new seeTest(model, practitionerID, patientID);

                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
                dispose();
                new seeTest(model, practitionerID, patientID);
            }
        });
        bottomRight.add(addPerform);

        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(100, 500));
        leftPanel.setBackground(Color.LIGHT_GRAY);

        JPanel rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(100, 500));
        rightPanel.setBackground(Color.LIGHT_GRAY);


        ResultSet testList = model.getTestData(searchText.getText());
        String[] strings = {"test_name", "test_desc","btn:set"};
        JPanel testPanel = new JPanel(new FlowLayout());
        testPanel.setPreferredSize(new Dimension(700,300));
        JScrollPane table = createResultSetPanel(testList, strings, "test_id");
        testPanel.add(table);




        searchButton.addActionListener(e->{
            testPanel.removeAll();
            JScrollPane pane = null;
            try {
                pane = createResultSetPanel(model.getTestData(searchText.getText()), strings, "test_id");
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            pane.setPreferredSize(new Dimension(700,300));
            testPanel.add(pane);
            this.setVisible(false);
            this.setVisible(true);
        });

        JPanel centerPanel = new JPanel(new GridLayout(3, 1));
        centerPanel.add(topPanel);
        centerPanel.add(middlePanel);
        centerPanel.add(bottomPanel);
        this.add(searchPanel, BorderLayout.NORTH);
        this.add(leftPanel, BorderLayout.EAST);
        this.add(testPanel, BorderLayout.CENTER);
        this.add(rightPanel,BorderLayout.WEST);
        this.add(centerPanel, BorderLayout.SOUTH);
        bottomLeft.setOpaque(false);
        bottomRight.setOpaque(false);
        centerPanel.setOpaque(false);
        this.setSize(2000, 500);
        this.setVisible(true);
    }

    /**
     * Creates a {@link JScrollPane} containing a {@link JPanel} with data from the {@link ResultSet}.
     * The panel includes headers and rows for each record, with each field in a separate cell.
     *
     * @param resultSet The {@link ResultSet} containing data to be displayed.
     * @param fields    The array of field names to be displayed.
     * @param id        The identifier field in the {@link ResultSet}.
     * @return A {@link JScrollPane} containing the formatted data panel.
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
            JOptionPane.showMessageDialog(null, e.getMessage());
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        return scrollPane;
    }

    /**
     * Creates a styled {@link JLabel} with specified text and padding.
     *
     * @param text    The text to be displayed in the label.
     * @param padding The padding around the label.
     * @return A styled {@link JLabel}.
     */
    private static JLabel createStyledLabel(String text, int padding) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        return label;
    }

    /**
     * Creates a styled {@link JButton} with specified text and an identifier.
     * The button triggers an action based on the identifier when clicked.
     *
     * @param text   The text to be displayed on the button.
     * @param set_id The identifier associated with the button.
     * @return A styled {@link JButton}.
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
     * Cleans the field name by removing prefixes like "btn:" or "int:".
     *
     * @param field The original field name.
     * @return The cleaned field name.
     */
    private static String getCleanFieldName(String field) {
        if (field.startsWith("btn:") || field.startsWith("int:")) {
            return field.substring(4);
        }
        return field;
    }

    /**
     * Handles button clicks by setting the test ID and updating the test name label.
     * This method is triggered when a button in the test data panel is clicked.
     *
     * @param btnName The name of the button that was clicked.
     * @param iD      The identifier associated with the button.
     * @throws SQLException If an SQL error occurs.
     */
    private void handleButtons(String btnName, int iD) throws SQLException
    {
        switch (btnName)
        {
        case "set":
            this.testId = iD;
            ResultSet result = model.getTestName(iD);
            result.next();
            this.testTextField.setText(result.getString("test_name"));
        }
    }

    /**
     * Handles action events triggered by user interactions with the GUI components.
     * Currently, this method does not implement any specific functionality.
     *
     * @param e The action event that occurred.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
