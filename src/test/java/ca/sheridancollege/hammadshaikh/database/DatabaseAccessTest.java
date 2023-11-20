package ca.sheridancollege.hammadshaikh.database;

import ca.sheridancollege.hammadshaikh.beans.Book;
import ca.sheridancollege.hammadshaikh.beans.User;
import ca.sheridancollege.hammadshaikh.controllers.BookController;
import ca.sheridancollege.hammadshaikh.controllers.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureTestDatabase
public class DatabaseAccessTest {
    @InjectMocks
    private BookController bookController;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @Mock
    private NamedParameterJdbcTemplate jdbc;

    @InjectMocks
    private DatabaseAccess databaseAccess;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(databaseAccess).build();
    }

    @Test
    void testFindUserAccount() throws Exception {
        User user = new User();
        when(jdbc.queryForObject(anyString(), any(MapSqlParameterSource.class), any(BeanPropertyRowMapper.class)))
                .thenReturn(user);

        User result = databaseAccess.findUserAccount("test@example.com");

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void testGetRolesById() throws Exception {
        List<String> roles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");
        when(jdbc.queryForList(anyString(), any(MapSqlParameterSource.class), eq(String.class)))
                .thenReturn(roles);

        List<String> result = databaseAccess.getRolesById(1L);

        assertNotNull(result);
        assertEquals(roles, result);
    }

    @Test
    void testInsertUser() throws Exception {
        // Mock the User object to be returned by the queryForObject method
        User user = new User();
        when(jdbc.queryForObject(anyString(), any(MapSqlParameterSource.class), any(BeanPropertyRowMapper.class)))
                .thenReturn(user);

        // Mock the update method to return 1
        when(jdbc.update(anyString(), any(MapSqlParameterSource.class)))
                .thenReturn(1)  // This is for the first update call in insertUser
                .thenReturn(0); // This is for any subsequent update calls

        // Perform the insertUser operation
        databaseAccess.insertUser("test@example.com", "encodedPassword");

        // Verify that the queryForObject method was called
        verify(jdbc, times(1)).queryForObject(anyString(), any(MapSqlParameterSource.class), any(BeanPropertyRowMapper.class));

        // Verify that the update method was called exactly twice
        verify(jdbc, times(2)).update(anyString(), any(MapSqlParameterSource.class));

        // Verify that there are no more interactions with the mock
        verifyNoMoreInteractions(jdbc);
    }

    @Test
    void testInsertBook() throws Exception {
        Book book = new Book();
        when(jdbc.update(anyString(), any(MapSqlParameterSource.class))).thenReturn(1);

        databaseAccess.insertBook(book);

        verify(jdbc, times(1)).update(anyString(), any(MapSqlParameterSource.class));
    }

}
