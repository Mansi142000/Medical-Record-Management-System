package src;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;

/**
 * A JFrame class for viewing and managing test data associated with a patient.
 */
public class seeTestPatient extends JFrame implements ActionListener {

    ConnectionModel model;
    int patientId;

    int testId;
    JTextField searchTest;
    JPanel resultsPanel;

    /**
     * Constructs a `seeTest` frame for viewing and managing test data for a patient.
     *
     * @param model     The ConnectionModel instance for database access.
     * @param patientId The patient's ID.
     */
    public seeTestPatient(ConnectionModel model, int patientId) {
        this.model = model;
        this.patientId = patientId;


        try {
            // Set Nimbus Look and Feel
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

        setTitle("See Patient Ailment");
        setSize(1000, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        this.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        JPanel topLeft = new JPanel(new FlowLayout());
        this.searchTest = new JTextField("");
        searchTest.setPreferredSize(new Dimension(200, 30));
        JButton searchAilmentBtn = new JButton("search test");

        searchAilmentBtn.setActionCommand("search");
        topLeft.add(searchTest);
        topLeft.add(searchAilmentBtn);
        topLeft.setOpaque(false);
        topPanel.add(topLeft);

        topPanel.setOpaque(false);
        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel patient_Id = new JLabel(String.valueOf(patientId));
        JButton insertAilment = new JButton("insertTest");
        insertAilment.setActionCommand("insertTest");
        insertAilment.addActionListener(this);
//        topRight.add(patient_Id);
//        topRight.add(insertAilment);
        topRight.setOpaque(false);
        topPanel.add(topRight);
        this.add(topPanel, BorderLayout.NORTH);

        resultsPanel = new JPanel(new FlowLayout());
        refreshTable();
        searchAilmentBtn.addActionListener(e -> {
            refreshTable();
        });
        this.add(resultsPanel, BorderLayout.CENTER);
    }

    /**
     * Refreshes the table displaying test data based on search criteria.
     */
    private void refreshTable() {
        String[] strings = {"test_name","practitioner_fn","practitioner_ln","test_date","findings", "btn:Get Details"};
        resultsPanel.removeAll();
        ResultSet list = model.getPerformList(testId, searchTest.getText());
        JScrollPane table = createResultSetPanel(list, strings, "performance_id");
        table.setPreferredSize(new Dimension(900, 300));
        resultsPanel.add(table);
        resultsPanel.setOpaque(false);
        resultsPanel.setVisible(false);
        resultsPanel.setVisible(true);
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
        JPanel panel = new JPanel(new GridLayout(0, fields.length));
        int cellPadding = 10;
        int rowCount = 0;

        try {
            for (String field : fields) {
                JLabel headerLabel = new JLabel(getCleanFieldName(field));
                headerLabel.setPreferredSize(new Dimension(100, 30));
                headerLabel.setBackground(Color.DARK_GRAY);
                headerLabel.setForeground(Color.WHITE);
                headerLabel.setHorizontalAlignment(JLabel.CENTER);
                headerLabel.setVerticalAlignment(JLabel.CENTER);
                headerLabel.setBorder(BorderFactory.createEmptyBorder(cellPadding, cellPadding, cellPadding, cellPadding));
                headerLabel.setOpaque(true);
                panel.add(headerLabel);
            }

            while (resultSet.next()) {
                rowCount++;
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
            JOptionPane.showMessageDialog(null,e.getMessage());
        }

        if (rowCount == 0) {
            panel.setLayout(new BorderLayout());
            JLabel noDataLabel = new JLabel("No data available", JLabel.CENTER);
            noDataLabel.setForeground(Color.GRAY);
            noDataLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            panel.add(noDataLabel, BorderLayout.CENTER);
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        int totalHeight = (rowCount + 1) * 30 + (rowCount + 1) * cellPadding * 2;
        scrollPane.setPreferredSize(new Dimension(1200, Math.min(totalHeight, 300)));

        return scrollPane;
    }

    /**
     * Extracts the clean field name from a formatted field string (e.g., "btn:Button Text" -> "Button Text").
     *
     * @param field The formatted field string.
     * @return The clean field name without formatting options.
     */
    private static String getCleanFieldName(String field) {
        // Remove "btn:" or "int:" prefixes from the field name
        if (field.startsWith("btn:") || field.startsWith("int:")) {
            return field.substring(4);
        }
        return field;
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
        label.setPreferredSize(new Dimension(100, 30));
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
        button.setPreferredSize(new Dimension(100, 30));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleButtons(text, set_id);
                System.out.println("Button clicked: " + set_id + text);
            }
        });
        return button;
    }

    /**
     * Handles button actions based on the button name and associated ID.
     *
     * @param btnName The name of the clicked button.
     * @param iD      The unique identifier associated with the button action.
     */
    private void handleButtons(String btnName, int iD) {

        switch (btnName) {
            case "delete":
                int ans = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this perform data?", "Delete Perform", JOptionPane.YES_NO_CANCEL_OPTION);
                if (ans == JOptionPane.YES_OPTION) {
                    try {
                        model.deletePerform(iD);
                        refreshTable();
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(this, e.getMessage());
                    }
                }
                break;
            case "Get Details":
                new TestDetails(model,iD);
                break;
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
