package ru.agilix.bookstorage.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Author {
    private final int id;
    private final String name;
}
