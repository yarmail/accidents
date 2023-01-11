package ru.job4j.accidents.controller;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.service.AccidentService;

import java.util.Optional;
import java.util.Set;

import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AccidentControllerTest {

    private Accident accident = new Accident(1, "name", "text", "address",
            new AccidentType(), Set.of(new Rule()));

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccidentService accidentService;

    @Test
    @WithMockUser
    public void accidents() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("accidents"));
    }

    @Test
    @WithMockUser
    public void addAccident() throws Exception {
        this.mockMvc.perform(get("/addAccident"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("createAccident"));
    }

    @Test
    @WithMockUser
    public void whenUpdateExistingAccident() throws Exception {
        Optional<Accident> optAccident = Optional.of(accident);
        when(accidentService.findById(1)).thenReturn(optAccident);
        this.mockMvc.perform(get("/formUpdateAccident")
                .param("id", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("editAccident"));
    }

    @Test
    @WithMockUser
    public void whenUpdateNotExistingAccident() throws Exception {
        this.mockMvc.perform(get("/formUpdateAccident")
                .param("id", String.valueOf(-1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    @WithMockUser
    public void saveAccident() throws Exception {
        String[] rIds = {"1", "2"};
        this.mockMvc.perform(post("/saveAccident")
                        .param("rIds", rIds)
                        .param("type.id", String.valueOf(1))
                        .param("name", "Name")
                        .param("text", "Text")
                        .param("address", "Address"))
                .andDo(print())
                .andExpect(status().isOk());
        ArgumentCaptor<Accident> argument = ArgumentCaptor.forClass(Accident.class);
        verify(accidentService).save(argument.capture(), eq(rIds), eq(1));
        assertThat(argument.getValue().getName()).isEqualTo("Name");
        assertThat(argument.getValue().getText()).isEqualTo("Text");
        assertThat(argument.getValue().getAddress()).isEqualTo("Address");
    }

    @Test
    @WithMockUser
    public void updateAccident() throws Exception {
        String[] rIds = {"1", "2"};
        this.mockMvc.perform(post("/updateAccident")
                        .param("rIds", rIds)
                        .param("type.id", String.valueOf(1))
                        .param("name", "Name")
                        .param("text", "Text")
                        .param("address", "Address"))
                .andDo(print())
                .andExpect(status().isOk());
        ArgumentCaptor<Accident> argument = ArgumentCaptor.forClass(Accident.class);
        verify(accidentService).replace(argument.capture(), eq(rIds), eq(1));
        assertThat(argument.getValue().getName()).isEqualTo("Name");
        assertThat(argument.getValue().getText()).isEqualTo("Text");
        assertThat(argument.getValue().getAddress()).isEqualTo("Address");
    }
}
/*
В методах, связанных с проверкой post (save, update) пока не понятно, что происходит
 */