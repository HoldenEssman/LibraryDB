import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;

import javax.swing.*;
import java.sql.*;
import java.util.Calendar;

public class CheckIn implements ActionListener {
    CheckIn()
    {
        display();
    }

    public static void main(String[] args)
    {
        try {
            // Gets the windows native look
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new CheckIn();
    }

    public java.sql.Date getDate(){
        long milis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(milis);
        return date;
    }

    JFrame mainFrame;
    JPanel controlPanel;
    JLabel header;
    JLabel ISBN;
    JLabel cardNo;
    JLabel borrower;
    JTextField isbnTextBox;
    JTextField cardNoTextBox;
    JTextField borrowerTextBox;
    JButton checkIn;
    JButton back;

    public void display()
    {
        Font font1 = new Font("TimesRoman", Font.BOLD, 12);
        Font font2 = new Font("TimesRoman", Font.PLAIN, 12);
        Font font3 = new Font("TimesRoman", Font.BOLD, 22);

        mainFrame = new JFrame("Library Management System: Check In");

        controlPanel = new JPanel();
        controlPanel.setLayout(null);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create the header of the page
        header = new JLabel("Check In a Book");
        header.setFont(font3);
        header.setBounds(175, 75, 500, 30);
        controlPanel.add(header);

        // Book ISBN
        ISBN = new JLabel("                ISBN :");
        ISBN.setBounds(100, 130, 500, 20);
        ISBN.setFont(font1);
        controlPanel.add(ISBN);

        // ISBN Text Box
        isbnTextBox = new JTextField();
        isbnTextBox.setFont(font2);
        isbnTextBox.setBounds(200, 130, 200, 20);
        controlPanel.add(isbnTextBox);

        // Borrower Card No.
        cardNo = new JLabel("          Card No. :");
        cardNo.setBounds(100, 170, 500, 20);
        cardNo.setFont(font1);
        controlPanel.add(cardNo);

        // Card No. Text Box
        cardNoTextBox = new JTextField();
        cardNoTextBox.setFont(font2);
        cardNoTextBox.setBounds(200, 170, 200, 20);
        controlPanel.add(cardNoTextBox);

        // Borrower Name
        borrower = new JLabel("Borrower Name :");
        borrower.setBounds(100, 210, 500, 20);
        borrower.setFont(font1);
        controlPanel.add(borrower);

        // Borrower Name Text Box
        borrowerTextBox = new JTextField();
        borrowerTextBox.setFont(font2);
        borrowerTextBox.setBounds(200, 210, 200, 20);
        controlPanel.add(borrowerTextBox);

        // Check Out Button
        checkIn = new JButton("Check In");
        checkIn.setBounds(100, 270, 300, 30);
        controlPanel.add(checkIn);
        checkIn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                /*
                Delete * from book_loans where isbn = isbn
                */


                try{
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Library","root","bananafish123");

                    String isbn = isbnTextBox.getText();
                    String card_id = cardNoTextBox.getText();
                    String bName = borrowerTextBox.getText();

                    String query = "SELECT loan_id FROM book_loans WHERE Card_id = ? AND Isbn = ?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, card_id);
                    statement.setString(2, isbn);
                    ResultSet resultSet = statement.executeQuery();

                    int loan_id = -1;
                    while(resultSet.next()){
                       loan_id = resultSet.getInt(1);
                    }



                    String sql = "UPDATE book_loans SET Date_in = ? WHERE loan_id = ?";
                    PreparedStatement myStmt = connection.prepareStatement(sql);
                    myStmt.setDate(1, getDate());
                    myStmt.setInt(2, loan_id);

                    System.out.println(myStmt);

                    int count = myStmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Book was properly checked in");
                    if(count > 0){
                        System.out.println("sql executed");
                    }

                    connection.close();
                } catch (Exception e){
                    System.out.println(e);
                }
            }
        });

        // Close Button
        back = new JButton("Back to Main");
        back.setBounds(100, 320, 300, 30);
        controlPanel.add(back);
        back.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                mainFrame.setVisible(false);
                new MainPage();
            }
        });

        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.add(controlPanel);
        mainFrame.setSize(500,500);
        mainFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

    }
}


