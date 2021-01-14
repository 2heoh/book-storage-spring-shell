package ru.agilix.bookstorage.shell;

import org.jline.terminal.Terminal;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Input;
import org.springframework.shell.Shell;
import org.springframework.shell.result.DefaultResultHandler;
import ru.agilix.bookstorage.service.BooksService;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class BookStorageCommandsTests {

    @Autowired
    private Shell shell;

    @Mock
    private Terminal terminal;

    @MockBean
    private BooksService service;

    @Test
    public void commandHelpDisplaysHelpStartingWithAvailableCommands() {
        StringWriter out = new StringWriter();
        DefaultResultHandler resultHandler = createResultHandler(out);

        resultHandler.handleResult(sendCommand("help"));

        assertThat(out.toString()).startsWith("AVAILABLE COMMANDS");
    }

    @Test
    void shellHasCommandShowBooks() {
        StringWriter out = new StringWriter();
        DefaultResultHandler resultHandler = createResultHandler(out);

        resultHandler.handleResult(sendCommand("help"));

        assertThat(out.toString())
                .contains("add: ")
                .contains("show: ")
                .contains("update: ")
                .contains("delete: ");
    }

    @Test
    void showBooksDisplaysListOfBooks() {
        StringWriter out = new StringWriter();
        DefaultResultHandler resultHandler = createResultHandler(out);

        resultHandler.handleResult(sendCommand("show books all"));

        verify(service, times(1)).retrieveAllBooks();
    }

    @Test
    void showAuthorsDisplaysListOfAuthors() {
        StringWriter out = new StringWriter();
        DefaultResultHandler resultHandler = createResultHandler(out);

        resultHandler.handleResult(sendCommand("show authors all"));

        verify(service, times(1)).showAllAuthors();
    }

    @Test
    void showGenresDisplaysListOfGenres() {
        StringWriter out = new StringWriter();
        DefaultResultHandler resultHandler = createResultHandler(out);

        resultHandler.handleResult(sendCommand("show genres all"));

        verify(service, times(1)).showAllGenres();
    }

    @Test
    void addBookShouldCallAddNewBookForBookService() {
        StringWriter out = new StringWriter();
        DefaultResultHandler resultHandler = createResultHandler(out);

        resultHandler.handleResult(sendCommand("add book title"));

        verify(service, times(1)).createBook("title");
    }

    @Test
    void addPenShouldPrintErrorMessage() {
        StringWriter out = new StringWriter();
        DefaultResultHandler resultHandler = createResultHandler(out);

        resultHandler.handleResult(sendCommand("add pen title"));

        assertThat(out.toString()).startsWith("don't know: pen");
        verify(service, times(0)).createBook("title");
    }

    @Test
    void showBookDetailsPrintsFullInformation() {
        StringWriter out = new StringWriter();
        DefaultResultHandler resultHandler = createResultHandler(out);

        resultHandler.handleResult(sendCommand("show book 1"));

        verify(service, times(1)).retrieveBook(1);
    }

    @Test
    void shouldUpdateBookDetails() {
        StringWriter out = new StringWriter();
        DefaultResultHandler resultHandler = createResultHandler(out);

        resultHandler.handleResult(sendCommand("update book 1"));

        verify(service, times(1)).updateBook(1);
    }

    @Test
    void shouldDeleteBook() {
        StringWriter out = new StringWriter();
        DefaultResultHandler resultHandler = createResultHandler(out);

        resultHandler.handleResult(sendCommand("delete book 1"));

        verify(service, times(1)).deleteBook(1);
    }

    private DefaultResultHandler createResultHandler(StringWriter out) {
        DefaultResultHandler resultHandler = new DefaultResultHandler();
        PrintWriter writer = new PrintWriter(out);
        given(terminal.writer()).willReturn(writer);
        resultHandler.setTerminal(terminal);
        return resultHandler;
    }

    private Object sendCommand(String command) {
        return shell.evaluate(new Input() {
            @Override
            public String rawText() {
                return command;
            }
        });
    }
}
