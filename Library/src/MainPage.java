import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLayeredPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.BorderFactory;
import javax.swing.WindowConstants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Vector;

public class MainPage implements ActionListener {

    MainPage() {
        display();
    }

    public static void main(String[] args) {
        try {
            // Gets the windows native look
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        MainPage gui = new MainPage();
        gui.display();
    }

    static Connection connect = null;

    JFrame mainFrame;
    JPanel controlPanel;
    JLabel header;
    JLabel prompt;
    JTextField searchTextBox;
    JButton search;
    JButton checkOut;
    JButton checkIn;
    JButton updateFines;
    JButton payFines;
    JButton addNewBorrower;
    JLayeredPane searchPane;
    JTable searchTable;
    JScrollPane scrollBar;

    public void display() {
        Font font1 = new Font("TimesRoman", Font.BOLD, 22);
        Font font2 = new Font("TimesRoman", Font.BOLD, 12);

        // Create the frame
        mainFrame = new JFrame("Library Management System");
        mainFrame.getContentPane();

        // Create the panel
        // This is where all the buttons, text, etc. gets attached to
        controlPanel = new JPanel();

        // Create the header of the page
        header = new JLabel("Library Management System");
        header.setFont(font1);
        header.setBounds(505, 100, 500, 30);
        controlPanel.setLayout(null);
        controlPanel.add(header);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create the search box
        searchTextBox = new JTextField();
        searchTextBox.setBounds(350, 250, 500, 30);
        controlPanel.add(searchTextBox);

        prompt = new JLabel("Search Book by Title, ISBN, or Author:");
        prompt.setFont(font2);
        prompt.setBounds(500, 225, 500, 30);
        controlPanel.add(prompt);

        // Search Button
        search = new JButton("Search");
        search.setBounds(855, 250, 100, 30);
        controlPanel.add(search);
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    new Search(searchTextBox.getText());
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        // Check Out Button
        checkOut = new JButton("Check Out");
        checkOut.setBounds(350, 500, 100, 30);
        controlPanel.add(checkOut);
        checkOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                new CheckOut();
            }
        });

        // Check In Button
        checkIn = new JButton("Check In");
        checkIn.setBounds(500, 500, 100, 30);
        controlPanel.add(checkIn);
        checkIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                new CheckIn();
            }
        });

        // Add a New Borrower Button
        addNewBorrower = new JButton("Add New Borrower");
        addNewBorrower.setBounds(650, 500, 150, 30);
        controlPanel.add(addNewBorrower);
        addNewBorrower.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                new AddNewBorrower();
            }
        });

        // Fines Button
        payFines = new JButton("Fines");
        payFines.setBounds(850, 500, 100, 30);
        controlPanel.add(payFines);
        payFines.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                new Fines();
            }
        });

        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.add(controlPanel);
        mainFrame.setSize(500, 500);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

    }
}


