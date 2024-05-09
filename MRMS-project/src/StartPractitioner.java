package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A JFrame class for the initial screen of the Practitioner interface.
 * This class provides a graphical user interface for practitioners to search for patients
 * and view additional information about the hospital and department.
 */
public class StartPractitioner extends JFrame {

    public StartPractitioner() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        setTitle("Hospital Information");
        setSize(800, 600);
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
        setLayout(new GridLayout(2, 1));

        JPanel topPanel = new JPanel(new GridLayout(1, 2));

        JPanel leftTopPanel = new JPanel();
        leftTopPanel.setOpaque(false);
        leftTopPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JTextField searchField = new JTextField(20);
        leftTopPanel.add(searchField);

        JButton searchButton = new JButton("Search");
        leftTopPanel.add(searchButton);

        JPanel rightTopPanel = new JPanel(new GridLayout(3, 1));
        rightTopPanel.setOpaque(false);

        JLabel nameLabel = new JLabel("Name:");
        JLabel departmentLabel = new JLabel("Department:");
        JLabel hospitalLabel = new JLabel("Hospital:");

        rightTopPanel.add(nameLabel);
        rightTopPanel.add(departmentLabel);
        rightTopPanel.add(hospitalLabel);

        topPanel.add(leftTopPanel);
        topPanel.add(rightTopPanel);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);

        JLabel bottomLabel = new JLabel("Additional Information Goes Here");
        bottomLabel.setHorizontalAlignment(JLabel.CENTER);

        bottomPanel.add(bottomLabel, BorderLayout.CENTER);

        add(topPanel);
        add(bottomPanel);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StartPractitioner openingFrame = new StartPractitioner();
            openingFrame.setVisible(true);
        });
    }
}
