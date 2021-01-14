package ru.agilix.bookstorage.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.agilix.bookstorage.domain.Author;
import ru.agilix.bookstorage.domain.Book;
import ru.agilix.bookstorage.domain.Genre;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@Import(BooksDaoJdbc.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public
class BooksDaoJdbcTest {

    @Autowired
    private BooksDao bookDao;

    @Test
    void getAllBooksReturnsListOfBooks() {
        Author gogol = new Author(2, "Николай Гоголь");
        Book viy = new Book(2, "Вий", "", gogol, new Genre(2, "Classics"));

        assertThat(bookDao.getAll())
                .contains(viy);
    }

    public static Book createBook(int id, String title, String description) {
        return new Book(id, title, description, null, null);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void createBookShouldReturnIt() {
        Book inserted = bookDao.create("bible");

        assertThat(inserted.getId()).isEqualTo(3);
        assertThat(inserted.getTitle()).isEqualTo("bible");
        assertThat(inserted.getDescription()).isNull();
        assertThat(inserted.getAuthor()).isEqualTo(new Author(0, "Unknown"));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void saveBookShouldUpdateItsFields() {
        Book book = bookDao.getById(1);

        Author newAuthor = new Author(2, "Николай Гоголь");
        Genre newGenre = new Genre(3, "Science Fiction (Sci-Fi)");

        Book updatedBook = new Book(
                book.getId(),
                "new title",
                "new description",
                newAuthor,
                newGenre
        );
        bookDao.save(updatedBook);

        Book savedBook = bookDao.getById(1);

        assertThat(savedBook.getId()).isEqualTo(1);
        assertThat(savedBook.getTitle()).isEqualTo("new title");
        assertThat(savedBook.getDescription()).isEqualTo("new description");
        assertThat(savedBook.getAuthor()).isEqualTo(newAuthor);
        assertThat(savedBook.getGenre()).isEqualTo(newGenre);
    }

    @Test
    void shouldReturnExistingBook() {
        Book book = bookDao.getById(1);

        assertThat(book.getTitle()).isEqualTo("Война и мир");
        assertThat(book.getDescription()).startsWith("«Война́ и мир» —");
        assertThat(book.getAuthor().getName()).isEqualTo("Лев Толстой");
        assertThat(book.getAuthor().getId()).isEqualTo(1);
    }

    @Test
    void shouldRaiseExceptionForNonExistingBook() {
        assertThrows(EmptyResultDataAccessException.class, () -> bookDao.getById(-1));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldDeleteBookById() {
        bookDao.delete(1);

        assertThrows(EmptyResultDataAccessException.class, () -> bookDao.getById(1));
    }

    @Test
    void shouldNotDeleteNonExistingBook() {
        assertThrows(EmptyResultDataAccessException.class, () -> bookDao.delete(-1));
    }
}