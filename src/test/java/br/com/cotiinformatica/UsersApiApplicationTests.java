package br.com.cotiinformatica;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import br.com.cotiinformatica.domain.dtos.AuthenticateUserRequestDto;
import br.com.cotiinformatica.domain.dtos.AuthenticateUserResponseDto;
import br.com.cotiinformatica.domain.dtos.CreateUserRequestDto;
import br.com.cotiinformatica.domain.dtos.CreateUserResponseDto;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UsersApiApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Faker faker;

    @BeforeEach
    void setUp() {
        faker = new Faker();
    }

    @Test
    @Order(1)
    void shouldCreateUserSuccessfully() throws Exception {
        CreateUserRequestDto createUserRequest = generateCreateUserRequest();

        MvcResult result = mockMvc.perform(post("/api/users/create")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(createUserRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        CreateUserResponseDto response = getResponse(result, CreateUserResponseDto.class);

        assertEquals(createUserRequest.getName(), response.getName());
        assertEquals(createUserRequest.getEmail(), response.getEmail());
    }

    @Test
    @Order(2)
    void shouldAuthenticateUserSuccessfully() throws Exception {
        AuthenticateUserRequestDto authRequest = new AuthenticateUserRequestDto();
        authRequest.setEmail("admin@cotiinformatica.com.br");
        authRequest.setPassword("@Admin123");

        MvcResult result = mockMvc.perform(post("/api/users/authenticate")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andReturn();

        AuthenticateUserResponseDto response = getResponse(result, AuthenticateUserResponseDto.class);

        assertNotNull(response.getAccessToken());
        assertNotNull(response.getExpiration());
    }

    private CreateUserRequestDto generateCreateUserRequest() {
        CreateUserRequestDto dto = new CreateUserRequestDto();
        dto.setName(faker.name().fullName());
        dto.setEmail(faker.internet().emailAddress());
        dto.setPassword("@Admin123");
        return dto;
    }

    private <T> T getResponse(MvcResult result, Class<T> responseType) throws Exception {
        String content = result.getResponse().getContentAsString();
        return objectMapper.readValue(content, responseType);
    }
}
