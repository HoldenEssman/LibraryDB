import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.BorderFactory;
import javax.swing.WindowConstants;

import net.proteanit.sql.DbUtils;

public class Fines implements ActionListener {
    Fines() {
        display();
    }

    public static void main(String[] args) {
        try {
            // Gets the windows native look
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Fines();
    }

    JFrame mainFrame;
    JPanel controlPanel;
    JLabel header;
    JLabel cardNo;
    JLabel display;
    JTextField cardNoTextBox;
    JButton getFines;
    JButton payFines;
    JButton updateFines;
    JButton back;
    JTable searchTable;
    JScrollPane scrollBar;
    static Connection connection = null;

    public void display() {
        Font font1 = new Font("TimesRoman", Font.BOLD, 12);
        Font font2 = new Font("TimesRoman", Font.PLAIN, 12);
        Font font3 = new Font("TimesRoman", Font.BOLD, 22);

        mainFrame = new JFrame("Library Management System: Fines");

        controlPanel = new JPanel();
        controlPanel.setLayout(null);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create the header of the page
        header = new JLabel("Fines");
        header.setFont(font3);
        header.setBounds(225, 75, 500, 30);
        controlPanel.add(header);

        // Borrower Card No.
        cardNo = new JLabel("          Card No. :");
        cardNo.setBounds(100, 130, 500, 20);
        cardNo.setFont(font1);
        controlPanel.add(cardNo);

        // Card No. Text Box
        cardNoTextBox = new JTextField();
        cardNoTextBox.setFont(font2);
        cardNoTextBox.setBounds(200, 130, 200, 20);
        controlPanel.add(cardNoTextBox);

        display = new JLabel();
        display.setFont(font2);
        display.setBounds(100, 230, 200, 20);
        controlPanel.add(display);

        // Check Out Button
        getFines = new JButton("Get Fines");
        getFines.setBounds(100, 270, 300, 30);
        controlPanel.add(getFines);

        payFines = new JButton("Pay Fines");
        payFines.setBounds(100, 310, 300, 30);
        controlPanel.add(payFines);

        getFines.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

                String url = "jdbc:mysql://localhost:3306/Library";
                String username = "root";
                String password = "bananafish123";

                try {
                    connection = DriverManager.getConnection(url, username, password);
                    String query = "SELECT L.Card_id, L.Fine FROM (SELECT T.Card_id, SUM(T.Fine_amt) AS Fine FROM \n"
                            + "(SELECT book_loans.Loan_id, book_loans.Card_id, fines.Fine_amt, fines.Paid\n"
                            + "FROM book_loans, fines WHERE book_loans.Loan_id=fines.Loan_id) AS T GROUP BY T.Card_id) AS L\n"
                            + "WHERE L.Card_id='" + cardNoTextBox.getText() + "';";
                    Statement stmt = connection.createStatement();
                    ResultSet rs = null;

                    rs = stmt.executeQuery(query);
                    while(rs.next()) {
                        display.setText(cardNoTextBox.getText() + " : $" + rs.getDouble("Fine"));
                    }


                } catch(SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        });


        updateFines = new JButton("Update Fines");
        updateFines.setBounds(100, 350, 300, 30);
        controlPanel.add(updateFines);

        updateFines.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                String url = "jdbc:mysql://localhost:3306/Library";
                String username = "root";
                String password = "bananafish123";

                try {
                    // update fines in FINES table
                    connection = DriverManager.getConnection(url, username, password);
                    String query = "SELECT book_loans.Loan_id, book_loans.Card_id, book_loans.Due_date, book_loans.Date_in, fines.Fine_amt, fines.Paid\n"
                            + "FROM book_loans, fines WHERE book_loans.Loan_id=fines.Loan_id;";
                    Statement stmt = connection.createStatement();
                    ResultSet rs = null;

                    String today = Instant.now().atZone(ZoneOffset.UTC).format(DateTimeFormatter.ISO_LOCAL_DATE);
                    rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        String dateIn = rs.getString("Date_in");
                        String dueDate = rs.getString("Due_date");
                        Boolean paid = rs.getBoolean("Paid");
                        Integer numDays = 0;
                        if (dateIn != null) {
                            numDays = daysBetween(dueDate, dateIn);
                        } else {
                            numDays = daysBetween(dueDate, today);
                        }
//                            System.out.println(numDays);
                        String update = "UPDATE fines SET Fine_amt=" + (numDays*0.25) + "WHERE Loan_id=" + rs.getInt("Loan_id");
                        Statement statement = connection.createStatement();
                        Integer resultSet = statement.executeUpdate(update);

//                        System.out.println(dateIn + " " + dueDate + " " + paid);
                    }

                    // check if a loan exceeds the due date, if so create a new row in FINES
                    query = "SELECT Loan_id, Due_date FROM book_loans;";
                    rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        String dueDate = rs.getString("Due_date");
                        Integer loanId = rs.getInt("Loan_id");
                        // compare 2 days
                        if (daysBetween(dueDate, today) == 1) {
                            // create new row with initial fine of $0.25 for the first day late
                            String insert = "INSERT INTO fines(Loan_id, Fine_amt, Paid) VALUES(" + loanId + ", 0.25, 0)";
                            Statement statement = connection.createStatement();
                            statement.executeUpdate(insert);
                        }
                    }


                } catch(SQLException e) {
                    System.out.println(e.getMessage());
                }
            }

        });

        payFines.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                String url = "jdbc:mysql://localhost:3306/library";
                String username = "root";
                String password = "bananafish123";
                if (!cardNoTextBox.getText().equals("")) {
                    try {
                        connection = DriverManager.getConnection(url, username, password);

                        String query = "SELECT * FROM book_loans WHERE Card_id='" + cardNoTextBox.getText() + "';";
                        Statement stmt = connection.createStatement();
                        ResultSet rs = null;

                        rs = stmt.executeQuery(query);

                        while(rs.next()) {
                            int loanId = rs.getInt("Loan_id");
                            String update = "UPDATE fines SET Fine_amt=0.0, Paid=1 WHERE Loan_id=" + loanId + ";";
                            Statement statement = connection.createStatement();
                            statement.executeUpdate(update);
                        }
                    } catch (SQLException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            }

        });


        // Close Button
        back = new JButton("Back to Main");
        back.setBounds(100, 390, 300, 30);
        controlPanel.add(back);
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                mainFrame.setVisible(false);
                new MainPage();
            }
        });

        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.add(controlPanel);
        mainFrame.setSize(500, 500);
        mainFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub


    }

    public int daysBetween(String date1, String date2) {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date dateBefore = myFormat.parse(date1);
            Date dateAfter = myFormat.parse(date2);
            long difference = dateAfter.getTime() - dateBefore.getTime();
            float daysBetween = (difference / (1000*60*60*24));

            return (int) daysBetween;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}

