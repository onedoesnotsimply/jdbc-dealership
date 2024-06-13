import com.pluralsight.UserInterface;
import org.apache.commons.dbcp2.BasicDataSource;

public class Main {
    public static void main(String[] args) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/jdbc_cardealerships");
        dataSource.setUsername("root");
        dataSource.setPassword("password");

        UserInterface ui = new UserInterface(dataSource);

        ui.display();
    }
}
