package ca.sheridancollege.hammadshaikh.controllers;
import ca.sheridancollege.hammadshaikh.beans.Book;
import ca.sheridancollege.hammadshaikh.database.DatabaseAccess;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class BookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DatabaseAccess databaseAccess;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    void testInsertBook() throws Exception {
        Book book = new Book();
        doNothing().when(databaseAccess).insertBook(book);

        mockMvc.perform(MockMvcRequestBuilders.post("/secure/insertBook")
                        .flashAttr("book", book))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("secure/index"));
    }

    // ... more tests for BookController

}
