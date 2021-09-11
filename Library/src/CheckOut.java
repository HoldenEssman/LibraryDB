import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;

import javax.swing.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;

public class CheckOut implements ActionListener {
    CheckOut()
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
        new CheckOut();
    }

    public java.sql.Date getDate(){
        long milis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(milis);
        return date;
    }

    public java.sql.Date dueDate(java.sql.Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 14);
        return new java.sql.Date(c.getTimeInMillis());
    }

    public boolean isAvailable(String isbn, Connection connection) throws SQLException {
        String query = "Select * from book_loans where isbn = ?";
        PreparedStatement myStmt = connection.prepareStatement(query);
        myStmt.setString(1, isbn);

        ResultSet resultSet = myStmt.executeQuery();

        while(resultSet.next()){
            java.sql.Date date = resultSet.getDate(6);
            if(resultSet.wasNull()){
                return false;
            }
        }
        return true;
    }

    JFrame mainFrame;
    JPanel controlPanel;
    JLabel header;
    JLabel isbn;
    JLabel cardNo;
    JTextField isbnTextBox;
    JTextField cardTextBox;
    JButton checkOut;
    JButton back;

    public void display()
    {
        Font font1 = new Font("TimesRoman", Font.BOLD, 12);
        Font font2 = new Font("TimesRoman", Font.PLAIN, 12);
        Font font3 = new Font("TimesRoman", Font.BOLD, 22);

        mainFrame = new JFrame("Library Management System: Check Out");

        controlPanel = new JPanel();
        controlPanel.setLayout(null);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create the header of the page
        header = new JLabel("Check Out a Book");
        header.setFont(font3);
        header.setBounds(175, 75, 500, 30);
        controlPanel.add(header);

        // Book ISBN
        isbn = new JLabel("      ISBN :");
        isbn.setBounds(125, 150, 500, 20);
        isbn.setFont(font1);
        controlPanel.add(isbn);

        // ISBN Text Box
        isbnTextBox = new JTextField();
        isbnTextBox.setFont(font2);
        isbnTextBox.setBounds(200, 150, 200, 20);
        controlPanel.add(isbnTextBox);

        // Borrower Card No.
        cardNo = new JLabel("Card No. :");
        cardNo.setBounds(125, 190, 500, 20);
        cardNo.setFont(font1);
        controlPanel.add(cardNo);

        // Card No.Text Box
        cardTextBox = new JTextField();
        cardTextBox.setFont(font2);
        cardTextBox.setBounds(200, 190, 200, 20);
        controlPanel.add(cardTextBox);

        // Check Out Button
        checkOut = new JButton("Check Out");
        checkOut.setBounds(100, 255, 300, 30);
        controlPanel.add(checkOut);
        checkOut.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                int count = 0; // Used to count number of books that a borrower has checked out
                boolean hasMax = false; // Used as a flag for if a user has checked out three books. False if checked out less than three books, True if has checked out three books
                /* Select * from borrower loan where card_id = id
                if > 3 then block check out
                and
                select * from loan id where ibsn = ibsn
                Insert into borrower loan new loan with date
                 */
                try{
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Library","root","bananafish123");

                    String isbn = isbnTextBox.getText();
                    String card_id = cardTextBox.getText();

                    if(!isAvailable(isbn, connection)){
                        JOptionPane.showMessageDialog(null, "This book is currently checked out by another borrower");
                        connection.close();
                    }

                    String query = "Select * from book_loans where card_id = ?";
                    PreparedStatement myStmt = connection.prepareStatement(query);
                    myStmt.setString(1, card_id);

                    ResultSet resultSet = myStmt.executeQuery();
                    while(resultSet.next()){
                        count++;
                        if(count == 3){
                            hasMax = true;
                            JOptionPane.showMessageDialog(null, "You currently have checked out the max number of books, check in another book to be able to check out the current book");
                            break;
                        }
                    }

                    if(!hasMax){ // If borrower has currently checked  out less than three books
                        String sql = "INSERT INTO book_loans (Isbn, Card_id, Date_out, Due_date) VALUES (?, ?, ?, ?)";
                        myStmt = connection.prepareStatement(sql);
                        myStmt.setString(1, isbn);
                        myStmt.setString(2, card_id);
                        myStmt.setDate(3, getDate());
                        myStmt.setDate(4,dueDate(getDate()));

                        myStmt.execute();
                        JOptionPane.showMessageDialog(null, "Book was successfully checked out");
                    }
                    connection.close();
                } catch (Exception e){
                    System.out.println(e);
                }
            }
        });

        // Close Button
        back = new JButton("Back to Main");
        back.setBounds(100, 305, 300, 30);
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


