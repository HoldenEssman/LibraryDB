import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.sql.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.BorderFactory;
import javax.swing.WindowConstants;

public class AddNewBorrower {
    protected static final String ID = null;

    AddNewBorrower() {
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
        new AddNewBorrower();
    }

    JFrame mainFrame;
    JPanel controlPanel;
    JLabel header;
    JLabel firstName;
    JLabel lastName;
    JLabel ssn;
    JLabel email;
    JLabel address;
    JLabel city;
    JLabel state;
    JLabel phone;
    JTextField firstNameTextBox;
    JTextField lastNameTextBox;
    JTextField ssnTextBox;
    JTextField emailTextBox;
    JTextField addressTextBox;
    JTextField cityTextBox;
    JTextField stateTextBox;
    JTextField phoneTextBox;
    JButton addNewBorrower;
    JButton back;

    public void display() {
        Font font1 = new Font("TimesRoman", Font.BOLD, 12);
        Font font2 = new Font("TimesRoman", Font.PLAIN, 12);
        Font font3 = new Font("TimesRoman", Font.BOLD, 22);

        mainFrame = new JFrame("Library Management System: Add New Borrower");

        controlPanel = new JPanel();
        controlPanel.setLayout(null);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create the header of the page
        header = new JLabel("Add a New Borrower");
        header.setFont(font3);
        header.setBounds(150, 75, 500, 30);
        controlPanel.add(header);

        // First Name
        firstName = new JLabel("First Name :");
        firstName.setBounds(50, 130, 500, 20);
        firstName.setFont(font1);
        controlPanel.add(firstName);

        // First Name Text Box
        firstNameTextBox = new JTextField();
        firstNameTextBox.setFont(font2);
        firstNameTextBox.setBounds(125, 130, 100, 20);
        controlPanel.add(firstNameTextBox);

        // Last Name
        lastName = new JLabel("Last Name :");
        lastName.setBounds(250, 130, 500, 20);
        lastName.setFont(font1);
        controlPanel.add(lastName);

        // Last Name Text Box
        lastNameTextBox = new JTextField();
        lastNameTextBox.setFont(font2);
        lastNameTextBox.setBounds(325, 130, 100, 20);
        controlPanel.add(lastNameTextBox);

        // SSN
        ssn = new JLabel("           SSN :");
        ssn.setBounds(100, 170, 500, 20);
        ssn.setFont(font1);
        controlPanel.add(ssn);

        // SSN Text Box
        ssnTextBox = new JTextField();
        ssnTextBox.setFont(font2);
        ssnTextBox.setBounds(175, 170, 200, 20);
        controlPanel.add(ssnTextBox);

        // Address
        address = new JLabel("           Address :");
        address.setBounds(80, 210, 500, 20);
        address.setFont(font1);
        controlPanel.add(address);

        // Address Text Box
        addressTextBox = new JTextField();
        addressTextBox.setFont(font2);
        addressTextBox.setBounds(175, 210, 200, 20);
        controlPanel.add(addressTextBox);

        // Check Out Button
        addNewBorrower = new JButton("Add Borrower");
        addNewBorrower.setBounds(100, 280, 300, 30);
        controlPanel.add(addNewBorrower);
        addNewBorrower.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                /*

                 **** Enter your code here


                 */

                try{
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Library","root","bananafish123");


                    String Bname = firstNameTextBox.getText() + " " + lastNameTextBox.getText();;
                    String Ssn = ssnTextBox.getText();
                    String Address = addressTextBox.getText();
                    //String Card_id1;
                    String Card_id;

                    //Note: need a way to generate a unique id


                    String query1 = "SELECT Card_id FROM Borrower ORDER BY Card_id DESC LIMIT 1 ";
                    PreparedStatement statement = connection.prepareStatement(query1);
                    ResultSet str = statement.executeQuery();

                    String card_id1 = "";
                    while (str.next())
                    {
                        card_id1 = str.getString("Card_id");


                    }
                    String[] part = card_id1.split("(?<=\\D)(?=\\d)");
                    int number = Integer.parseInt(part[1]) +1;
                    Card_id = "ID" + "00" + number;







                    //avoid repeated ssn with useful message for the user

                    String query = "SELECT Ssn FROM Borrower ";
                    PreparedStatement statement2 = connection.prepareStatement(query);
                    ResultSet resultSet2 = statement2.executeQuery();
                    while(resultSet2.next())
                    {
                        if (Ssn.equals(resultSet2.getObject(1))) {
                            JOptionPane.showMessageDialog(null, " MEMBER WITH THE ENTERED SSN IS ALREADY IN THE SYSTEM PLEASE ENTER VALID AND UNIQUE SSN "  );

                        }


                    }


                    if (Bname.equals("")|| Ssn.equals("")||Address.equals("")||Ssn.length() != 11 ) {
                        JOptionPane.showMessageDialog(null, " PLEASE ENTER ALL VALID DETAILS TO ADD THE NEW BORROWER "  );
                    }
                    else {

                        String sql1 = "INSERT into Borrower(Card_id, Ssn, Bname, Address, Phone) VALUES (?, ? ,? , ?, ?);";
                        PreparedStatement statement1 = connection.prepareStatement(sql1);
                        statement1.setString(1, Card_id);
                        statement1.setString(3, Bname );

                        //need to avoid repeated ssn
                        statement1.setString(2, Ssn);
                        statement1.setString(4, Address);
                        statement1.setString(5, "null");


                        statement1.execute();

                        JOptionPane.showMessageDialog(null,"Congratulations " + Bname + " has been added to the system."  );


                    }

                    connection.close();
                } catch (Exception e){
                    System.out.println(e);
                }


            }
        });

        //Close Button
        back = new JButton("Back to Main");
        back.setBounds(100, 330, 300, 30);
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
}



