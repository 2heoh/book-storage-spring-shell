package ru.agilix.bookstorage.dao;

import org.springframework.jdbc.core.RowMapper;
import ru.agilix.bookstorage.domain.Author;
import ru.agilix.bookstorage.domain.Book;
import ru.agilix.bookstorage.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookMapper implements RowMapper<Book> {

    @Override
    public Book mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Book(
                resultSet.getInt("id"),
                resultSet.getString("title"),
                resultSet.getString("description"),
                new Author(
                    resultSet.getInt("author_id"),
                    resultSet.getString("author_name")
                ),
                new Genre(
                    resultSet.getInt("genre_id"),
                    resultSet.getString("genre_name")
                )
        );
    }
}
