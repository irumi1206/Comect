package PoolC.Comect.email;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@ActiveProfiles("test")
class EmailServiceTest {

    @Test
    void emailSend() {
    }

    @Test
    void passwordChangeEmailSend() {
    }

    @Test
    void findOneEmail() {
    }

    @Test
    void findOneId() {
    }

    @Test
    void emailCheck() {
    }

    @Test
    void emailAuthCheck() {
    }

    @Test
    void validateDuplicateUser() {
    }
}