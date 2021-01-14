package ru.agilix.bookstorage.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Book {
    private final int id;
    private final String title;
    private final String description;
    private final Author author;
    private final Genre genre;
}
