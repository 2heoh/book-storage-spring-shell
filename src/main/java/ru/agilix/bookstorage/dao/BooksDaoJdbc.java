package ru.agilix.bookstorage.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.agilix.bookstorage.domain.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class BooksDaoJdbc implements BooksDao {
    private final NamedParameterJdbcOperations jdbc;

    public BooksDaoJdbc(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<Book> getAll() {
        return jdbc.query(
                "select b.id, b.title, b.description, " +
                    "a.name as author_name, a.id as author_id, " +
                    "g.id as genre_id, g.name as genre_name" +
                    " from books b, authors a, genres g " +
                    " where b.author_id = a.id and b.genre_id = g.id",
            new BookMapper()
        );
    }

    @Override
    public Book create(String title) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(
                        "insert into books (`title`) values (?)",
                        new String[]{"id"});
                ps.setString(1, title);
                return ps;
            }
        };

        jdbc.getJdbcOperations().update(preparedStatementCreator, keyHolder);

        return getById(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    @Override
    public Book getById(int id) {
        return jdbc.queryForObject(
             "select b.id, b.title, b.description, " +
                       "a.name as author_name, a.id as author_id, " +
                       "g.id as genre_id, g.name as genre_name" +
                 " from books b, authors a, genres g " +
                 " where b.author_id = a.id and b.genre_id = g.id and b.id = :id",
                Map.of("id", id), new BookMapper());
    }

    @Override
    public void save(Book updatedBook) {
        jdbc.update("update books set " +
                        "title=:title, " +
                        "description=:description, " +
                        "author_id=:author_id, " +
                        "genre_id=:genre_id " +
                        "where id = :id",
                Map.of(
                    "title", updatedBook.getTitle(),
                    "description", updatedBook.getDescription(),
                    "author_id", updatedBook.getAuthor().getId(),
                    "genre_id", updatedBook.getGenre().getId(),
                    "id", updatedBook.getId()
                )
        );
    }

    @Override
    public void delete(int id) {
        int result = jdbc.update("delete from books where id=:id", Map.of("id", id));

        if (result != 1)
            throw new EmptyResultDataAccessException("Book is not deleted", 1);
    }
}
