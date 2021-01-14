package ru.agilix.bookstorage.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.agilix.bookstorage.domain.Author;
import ru.agilix.bookstorage.domain.Genre;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ConsoleUpdateServiceTest {

    private UpdateService updateService;

    @Mock
    private IOService ioService;

    @BeforeEach
    void setUp() {
        updateService = new ConsoleUpdateService(ioService);
    }

    @Test
    void shouldGetNewFieldValue() {
        given(ioService.getString()).willReturn("new");

        String newTitle = updateService.getNewValueFor("a","b");

        verify(ioService).putString("Enter new a [b]: ");
        assertThat(newTitle).isEqualTo("new");
    }

    @Test
    void shouldDisplayErrorWhenThereIsNoSuchAuthor() {
        given(ioService.getString()).willReturn("-1");

        assertThrows(NoSuchAuthor.class, () -> updateService.getNewAuthor(new ArrayList<>()));
    }

    @Test
    void shouldGetNewAuthorFromList() {
        given(ioService.getString()).willReturn("1");
        Author gogol = new Author(1, "Gogol");
        Author pushkin = new Author(2, "Pushkin");
        List<Author> authors = List.of(gogol, pushkin);

        Author chosenAuthor = updateService.getNewAuthor(authors);

        verify(ioService, times(1)).putString("Enter new author id from list above: ");
        verify(ioService, times(1)).putString("1 - Gogol");
        verify(ioService, times(1)).putString("2 - Pushkin");
        assertThat(chosenAuthor).isEqualTo(gogol);
    }


    @Test
    void shouldGetNewGenreFromList() {
        Genre one = new Genre(1, "One");
        Genre two = new Genre(2, "Two");
        List<Genre> genres = List.of(one, two);
        given(ioService.getString()).willReturn("1");

        Genre chosenGenre = updateService.getNewGenre(genres);

        verify(ioService, times(1)).putString("Enter new genre id from list above: ");
        verify(ioService, times(1)).putString("1 - One");
        verify(ioService, times(1)).putString("2 - Two");
        assertThat(chosenGenre).isEqualTo(one);
    }
}