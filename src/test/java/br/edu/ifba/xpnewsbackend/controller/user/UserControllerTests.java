package br.edu.ifba.xpnewsbackend.controller.user;

import br.edu.ifba.xpnewsbackend.tests.Factory;
import br.edu.ifba.xpnewsbackend.user.dto.UserCreateDto;
import br.edu.ifba.xpnewsbackend.user.dto.UserResponseDto;
import br.edu.ifba.xpnewsbackend.user.entity.User;
import br.edu.ifba.xpnewsbackend.user.service.UserIService;

import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
public class UserControllerTests {

    @Autowired
    private  MockMvc mockMvc;

    @Mock
    private UserIService service;

    @Autowired
    private  ObjectMapper objectMapper;

    UserCreateDto createDto;
    UserResponseDto responseDto;
    User validUser;
    Long validId;
    String validUsername;
    Long invalidId;
    String invalidUsername;
    String validEmail;
    String invalidEmail;

    @BeforeEach
    void setUp() {
        // Configuração dos dados para os testes, criando um usuário válido e variáveis
        validUser = Factory.createUser();
        createDto = Factory.createUserCreateDto();
        responseDto = Factory.createUserResponseDto();
        validId = 1L;
        invalidId = 2L;
        validUsername = "rafa12";
        invalidUsername = "invalid";
        validEmail = "rafa@gmail.com";
        invalidEmail = "invalid@gmail.com";
    }

    @Test
    void createShouldReturnUserResponseDtoCreatedWhenUserIsSaved() throws Exception {
        when(service.create(validUser)).thenReturn(validUser);

        ResultActions response = mockMvc.perform(post("/xp-news/users/create")
                .content(objectMapper.writeValueAsString(createDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(validUser.getId())))
                .andExpect(jsonPath("$.fullName", is(validUser.getFullName())))
                .andExpect(jsonPath("$.usernme", is(validUser.getUsername())))
                .andExpect(jsonPath("$.role", is(validUser.getRole())));

    }

}
