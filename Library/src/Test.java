import java.sql.*;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class Test {
    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Library","root","bananafish123");
            //Statement statement = connection.createStatement();
            //ResultSet resultSet = statement.executeQuery("select card_id from borrower where bName = 'Mark'");
            /*while(resultSet.next()){
                System.out.println(resultSet.getString(1));
            }
            LocalDate date = LocalDate.now();
            System.out.println(date);
            date = date.plusDays(-16);
            System.out.println(date); */
            String query = "insert into book_loans(Isbn, Card_id, Date_out, Due_date) Values(0312278586, 'ID000173', '2021-07-15', '2021-07-29')";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate(query);

        } catch (Exception e){
            System.out.println(e);
        }
    }
}
