package ru.agilix.bookstorage.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.agilix.bookstorage.domain.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(AuthorDaoJdbc.class)
class AuthorDaoJdbcTest {

    @Autowired
    AuthorDao dao;

    @Test
    void shouldGetAll() {

        List<Author> authorList = dao.getAll();

        assertThat(authorList)
                .contains(new Author(0, "Unknown"))
                .contains(new Author(1, "Лев Толстой"))
                .contains(new Author(2, "Николай Гоголь"))
                .size().isEqualTo(3);
    }
}