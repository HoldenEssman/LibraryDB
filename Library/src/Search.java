import java.awt.event.*;
import java.sql.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.sql.ResultSet;
import java.util.Vector;

import net.proteanit.sql.DbUtils;

public class Search implements ActionListener
{
    JFrame mainFrame;
    JPanel controlPanel;
    JButton back;
    static Connection connection = null;

    JLayeredPane searchPane;
    JTable searchTable;
    JScrollPane scrollBar;

    Search(String search) throws SQLException
    {
        display(search);
    }

    public static DefaultTableModel buildTableModel(ResultSet rs)
            throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);

    }

    void display(String search)
    {
        mainFrame = new JFrame("Library Management System: Seach");
        mainFrame.getContentPane();

        controlPanel = new JPanel();
        controlPanel.setLayout(null);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        searchPane = new JLayeredPane();
        searchPane.setBounds(100, 450, 800, 350);
        controlPanel.add(searchPane);

        searchTable = new JTable();
        searchTable.setFillsViewportHeight(true);

        scrollBar = new JScrollPane(searchTable);
        scrollBar.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollBar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        searchPane.add(scrollBar);

        scrollBar.setViewportView(searchTable);

        String url = "jdbc:mysql://localhost:3306/library";
        String username = "root";
        String password = "bananafish123";

        try{
            connection = DriverManager.getConnection(url, username, password);
            String query = "SELECT b.Isbn, b.Title, GROUP_CONCAT(a.Name) AS Authors, "
                    + "CASE WHEN b.Isbn in(SELECT Isbn FROM library.book_loans WHERE Date_in IS NULL) "
                    + "then 'Unavailable' else 'Available' end AS Available "
                    + "FROM book AS b LEFT JOIN book_authors AS ba on b.Isbn = ba.Isbn LEFT JOIN "
                    + "authors AS a on ba.Author_id = a.Author_id GROUP BY b.Isbn HAVING b.Isbn "
                    + "LIKE '%" + search + "%' or b.Title LIKE '%" + search + "%' or Authors LIKE '%" + search + "%'";
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery(query);

            JTable table = new JTable(buildTableModel(rs));
            JOptionPane.showMessageDialog(null, new JScrollPane(table));

            searchTable.setModel(DbUtils.resultSetToTableModel(rs));
            searchTable.setEnabled(false);

            searchTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }

        // Close Button
        back = new JButton("Back to Main");
        back.setBounds(350, 550, 300, 30);
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
        mainFrame.setSize(1000, 800);
        mainFrame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

    }
}



