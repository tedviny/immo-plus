package com.ted.immo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ted.immo.SecurityConfig;
import com.ted.immo.model.User;
import com.ted.immo.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    void should_create_user() throws Exception {

        User user = new User();

        Field idField = User.class.getDeclaredField("id");

        idField.setAccessible(true);

        idField.set(user, 1L);
        user.setFirstName("Jean");
        user.setLastName("LEROUX");
        user.setEmail("jean.leroux@example.com");

        Mockito.when(userService.createUser(any(User.class)))
                .thenReturn(user);

        mockMvc.perform(post("/api/v1/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.lastName").value("LEROUX"))
                .andExpect(jsonPath("$.email").value("jean.leroux@example.com"));
    }

    @Test
    void should_get_user_by_id() throws Exception {
        User user = new User();
        Field idField = User.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(user, 1L);
        user.setEmail("jean.dupont@example.com");

        Mockito.when(userService.getUserById(1L))
                .thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/v1/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("jean.dupont@example.com"));
    }

    @Test
    void should_return_not_found_when_user_not_found() throws Exception {
        Mockito.when(userService.getUserById(2L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/user/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_delete_user() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/v1/user/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(userService).deleteUser(1L);
    }
}
