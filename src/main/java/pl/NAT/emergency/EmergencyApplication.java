package pl.NAT.emergency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.NAT.emergency.model.ChatSocket;

//@EnableScheduling
@SpringBootApplication
public class EmergencyApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmergencyApplication.class, args);
	}

}

//SELECT client AS "Clients whose first order placed in 2017", SUM(income) AS "Sum of income in 2018" FROM orders WHERE FIRST_VALUE (order_date) OVER YEAR(2017)

//SELECT DISTINCT(title) FROM books JOIN ratings ON books.id=book_id WHERE ratings.rating >= 4 ORDER BY books.title

//	SELECT name, title, rating, rating_date FROM ratings LEFT JOIN books ON books.id = ratings.book_id JOIN reviewers ON reviewers.id = ratings.reviewer_id ORDER BY reviewers.name