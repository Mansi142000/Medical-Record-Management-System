package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The {@code AddAssessment} class represents a GUI form that allows users to add medical
 * assessment records into the system.
 * It includes fields for ailment names, case descriptions, and severity levels. It also
 * provides search functionality to select existing ailments from the database.
 */
public class AddAssessment extends JFrame implements ActionListener{
    ConnectionModel model;
    JLabel ailmentTextField;
    int ailmentSetId;
    public AddAssessment(ConnectionModel model,  int pracID, int patientID) {
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
        JLabel ailmentLabel = new JLabel("Ailment:");
        topPanel.add(ailmentLabel);

         ailmentTextField = new JLabel();
         ailmentTextField.setPreferredSize(new Dimension(150,30));
        topPanel.add(ailmentTextField);
        topPanel.setOpaque(false);


        JPanel middlePanel = new JPanel(new FlowLayout());

        JLabel caseLabel = new JLabel("Case Description:");
        middlePanel.add(caseLabel);

        JTextField caseField = new JTextField(20);
        middlePanel.add(caseField);
        middlePanel.setOpaque(false);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        JPanel bottomLeft = new JPanel(new FlowLayout());
        JPanel bottomRight = new JPanel(new FlowLayout());
        bottomPanel.add(bottomLeft);
        bottomPanel.add(bottomRight);
        bottomPanel.setOpaque(false);


        String[] severityOptions = {"", "Minimal", "Low", "Minor", "Moderate", "Medium", "Significant", "High", "Critical", "Severe", "Extreme"};
        JComboBox<String> severityComboBox = new JComboBox<>(severityOptions);
        bottomLeft.add(new JLabel("Severity:"));
        bottomLeft.add(severityComboBox);

        JButton addAilmentButton = new JButton("Add Ailment");
        addAilmentButton.addActionListener(e->{
                if (ailmentTextField.getText().isEmpty() || caseField.getText().isEmpty() || severityComboBox.getSelectedItem().equals("")) {
                    JOptionPane.showMessageDialog(this, "All fields must be filled", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        model.insertAssessment(this.ailmentSetId, patientID, pracID, caseField.getText(), severityComboBox.getSelectedItem().toString());
                        JOptionPane.showMessageDialog(this, "Record Inserted", "Success", JOptionPane.INFORMATION_MESSAGE);
                        this.dispose();
                        new seeAilment(model,pracID,patientID);

                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this, "Error: "+ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }

                }
        });
        bottomRight.add(addAilmentButton);

        ResultSet ailmentList = model.getAilmentList(searchText.getText());
        String[] strings = {"ailment_name", "ailment_description","btn:set"};
        JPanel ailmentPanel = new JPanel(new FlowLayout());
        JScrollPane table = createResultSetPanel(ailmentList, strings, "ailment_id");
        ailmentPanel.add(table);

        searchButton.addActionListener(e->{
            ailmentPanel.removeAll();
             JScrollPane pane = createResultSetPanel(model.getAilmentList(searchText.getText()), strings, "ailment_id");
            ailmentPanel.add(pane);
            this.setVisible(false);
            this.setVisible(true);
        });

        JPanel centerPanel = new JPanel(new GridLayout(3, 1));
        centerPanel.add(topPanel);
        centerPanel.add(middlePanel);
        centerPanel.add(bottomPanel);
        this.add(searchPanel,BorderLayout.NORTH);
        this.add(ailmentPanel);
        this.add(centerPanel, BorderLayout.SOUTH);
        bottomLeft.setOpaque(false);
        bottomRight.setOpaque(false);
        centerPanel.setOpaque(false);

        this.setSize(700, 350);
        this.setVisible(true);
    }

    /**
     * Creates a scrollable panel to display a ResultSet in a tabular format.
     *
     * @param resultSet the set of results to be displayed
     * @param fields    the names of the fields to be displayed in the table header
     * @param id        the identifier used to refer to the elements in the result set
     * @return a {@code JScrollPane} containing the formatted result set display
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
     * Creates a styled JLabel with centered text and padding.
     *
     * @param text   the text to be displayed in the label
     * @param padding the padding around the label text
     * @return a {@code JLabel} with the specified text and padding
     */
    private static JLabel createStyledLabel(String text, int padding) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        return label;
    }

    /**
     * Creates a JButton with specified text and an attached action listener.
     * When the button is clicked, it triggers the {@code handleButtons} method.
     * If an SQLException occurs in the handling method, it throws a RuntimeException.
     *
     * @param text   the text to be displayed on the button
     * @param set_id the identifier to be used when the button is clicked
     * @return a {@code JButton} with specified text and an action listener
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
     * Removes specific prefixes ("btn:" or "int:") from a field name.
     * This method is used to clean up field names for display purposes.
     * If the field name starts with "btn:" or "int:", these prefixes are removed.
     * Otherwise, the field name is returned as is.
     *
     * @param field the field name that may contain prefixes
     * @return a cleaned field name without the "btn:" or "int:" prefixes
     */
    private static String getCleanFieldName(String field) {
        if (field.startsWith("btn:") || field.startsWith("int:")) {
            return field.substring(4);
        }
        return field;
    }

    /**
     * Handles button click actions by performing actions based on the button name.
     *
     * @param btnName the name of the button that was clicked
     * @param iD      the identifier associated with the row where the button was clicked
     * @throws SQLException if there is an error processing the database operation
     */
    private void handleButtons(String btnName, int iD) throws SQLException {
        switch (btnName){
            case "set":
                this.ailmentSetId = iD;
                ResultSet result = model.getAilmentName(iD);
                result.next();
                this.ailmentTextField.setText(result.getString("ailment_name"));
        }
    }

    /**
     * Handles action events triggered by user interactions with the GUI components.
     * This method is part of the ActionListener interface and is overridden to define
     * custom behavior when an action event occurs. Currently, this method does not
     * implement any specific functionality.
     *
     * @param e the action event that occurred
     */
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
