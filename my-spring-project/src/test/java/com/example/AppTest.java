package com.example;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;

import com.example.controller.BookController;
import com.example.model.Book;
import com.example.service.BookService;

public class AppTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    public void testAddBook() throws Exception {
        // Mock the behavior of BookService
        Book newBook = new Book();
        newBook.setTitle("New Book");
        newBook.setAuthor("New Author");

        // doNothing().when(bookService).addBook(any(Book.class));

        // Test adding a new book
        mockMvc.perform(post("/books/add")
                .param("title", "New Book")
                .param("author", "New Author"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/list"));

        // Verify that the book was added to the service
        // verify(bookService, times(1)).addBook(any(Book.class));
    }

    @Test
    public void testListBooks() throws Exception {
        // Mock data
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book 1");
        book1.setAuthor("Author 1");

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book 2");
        book2.setAuthor("Author 2");

        // Mock the behavior of BookService
        // when(bookService.getAllBooks()).thenReturn(List.of(book1, book2));

        // Test listing books
        mockMvc.perform(get("/books/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("book-list"))
                .andExpect(model().attribute("books", hasSize(2))) // Two books should be listed
                .andExpect(model().attribute("books", hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("title", is("Book 1")),
                                hasProperty("author", is("Author 1"))))))
                .andExpect(model().attribute("books", hasItem(
                        allOf(
                                hasProperty("id", is(2L)),
                                hasProperty("title", is("Book 2")),
                                hasProperty("author", is("Author 2"))))));
    }
}
