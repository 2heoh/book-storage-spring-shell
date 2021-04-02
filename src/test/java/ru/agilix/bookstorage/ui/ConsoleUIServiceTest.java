package ru.agilix.bookstorage.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.agilix.bookstorage.domain.Author;
import ru.agilix.bookstorage.domain.Book;
import ru.agilix.bookstorage.domain.Genre;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.agilix.bookstorage.dao.BooksDaoJdbcTest.createBook;

@ExtendWith(MockitoExtension.class)
public class ConsoleUIServiceTest {
    private UiService ui;

    @Mock
    private UpdateService updateService;

    @BeforeEach
    void setUp() {
        ui = new ConsoleUiService(updateService);
    }

    @Test
    void shouldDisplayBooksNotFoundMessageOnEmptyBookList() {
        String result = ui.showBooksList(new ArrayList<>());

        assertThat(result).contains("Books not found.");
    }

    @Test
    void shouldDisplayListOfBooks() {
        Book bible = createBook(1, "the bible", null);
        Book codeComplete = createBook(2, "code complete", null);

        String result = ui.showBooksList(List.of(bible, codeComplete));

        assertThat(result)
                .contains("List of books:")
                .contains("the bible")
                .contains("code complete");
    }

    @Test
    void shouldDisplayMessageWithTitleOfInsertedBook() {
        Book bible = createBook(1, "the bible", "");

        String result = ui.showBookCreatedMessage(bible);

        assertThat(result)
                .contains("Created book:")
                .contains(bible.getTitle());
    }

    @Test
    void shouldDisplayFullBookInfo() {
        Author author = new Author(1, "author");

        Book bible = new Book(1,"the bible", "some long description", author, new Genre(1,"genre"));

        assertThat(ui.showBookInfo(bible))
                .contains("#1")
                .contains("the bible")
                .contains("some long description")
                .contains("author")
                .contains("genre");
    }
    @Test
    void shouldDisplayDescriptionIsNotSetForBookWithoutIt() {
        Book bible = createBook(1,"the bible", null);

        assertThat(ui.showBookInfo(bible))
                .contains("#1")
                .contains("the bible")
                .contains("description is not set");

    }

    @Test
    void shouldReturnUpdatedBook() {
        Book existingBook = createBook(1, "title", null);
        given(updateService.getNewValueFor("title", "title")).willReturn("new title");
        given(updateService.getNewValueFor("description", null)).willReturn("new description");

        Book updatedBook = ui.getUpdatedBookInfo(existingBook, new ArrayList<>(), new ArrayList<>());

        verify(updateService, times(2)).getNewValueFor(any(), any());
        verify(updateService, times(1)).getNewAuthor(any());
        verify(updateService, times(1)).getNewGenre(any());

        assertThat(updatedBook.getTitle()).isEqualTo("new title");
        assertThat(updatedBook.getDescription()).isEqualTo("new description");
    }


    @Test
    void shouldRenderListOfAuthors() {
        List<Author> list = List.of(
                new Author(1, "Александр Пушкин"),
                new Author(2, "Юрий Лермонтов")
        );

        String result = ui.showAuthorsList(list);

        assertThat(result)
                .contains("List of authors")
                .contains("Александр Пушкин")
                .contains("Юрий Лермонтов");
    }

    @Test
    void shouldDisplayEmptyListOfAuthors() {
        String result = ui.showEmptyAuthorsList();

        assertThat(result).contains("Authors not found.");
    }

    @Test
    void shouldDisplayListOfGenres() {
        Genre one = new Genre(1, "One");
        Genre two = new Genre(2, "Two");

        String result = ui.showGenreList(List.of(one, two));

        assertThat(result)
                .contains("List of genres")
                .contains("One")
                .contains("Two");
    }
}
