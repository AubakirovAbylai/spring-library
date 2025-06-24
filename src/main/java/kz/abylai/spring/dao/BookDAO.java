package kz.abylai.spring.dao;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
@Component
public class BookDAO {
}
