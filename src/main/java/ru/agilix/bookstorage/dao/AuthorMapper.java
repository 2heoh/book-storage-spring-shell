package ru.agilix.bookstorage.dao;

import org.springframework.jdbc.core.RowMapper;
import ru.agilix.bookstorage.domain.Author;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorMapper implements RowMapper<Author> {

    @Override
    public Author mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Author(
                resultSet.getInt("id"),
                resultSet.getString("name")
        );
    }
}
