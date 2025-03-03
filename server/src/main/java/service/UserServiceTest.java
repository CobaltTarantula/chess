package service;

import dataaccess.*;
import model.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private UserService service;
    private DataAccess dao;

    @BeforeEach
    void setUp() {
        dao = new MemoryDataAccess();
        service = new UserService(dao);
    }

    @Test
    void testRegisterSuccess() throws DataAccessException {
        UserData user = new UserData("user1", "pass", "email@example.com");
        service.register(user);
        assertNotNull(dao.getUser("user1"));
    }

    @Test
    void testRegisterDuplicate() {
        assertThrows(DataAccessException.class, () -> {
            UserData user = new UserData("user1", "pass", "email@example.com");
            service.register(user);
            service.register(user); // Should fail
        });
    }
}
