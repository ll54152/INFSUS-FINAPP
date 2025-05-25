package com.infsus.finapp;

import com.infsus.finapp.domain.Category;
import com.infsus.finapp.domain.Person;
import com.infsus.finapp.dto.CategoryDTO;
import com.infsus.finapp.rest.ApiController;
import com.infsus.finapp.rest.CategoryController;
import com.infsus.finapp.service.CategoryService;
import com.infsus.finapp.dao.PersonRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private PersonRepository personService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "user@example.com")
    void createCategory_shouldCallServiceWithCorrectParams() throws Exception {
        CategoryDTO dto = new CategoryDTO();
        dto.setCategoryName("New Category");

        Person person = new Person();
        when(personService.findByEmail("user@example.com")).thenReturn(person);

        mockMvc.perform(post("/category/createCategory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        ArgumentCaptor<CategoryDTO> dtoCaptor = ArgumentCaptor.forClass(CategoryDTO.class);
        ArgumentCaptor<Person> personCaptor = ArgumentCaptor.forClass(Person.class);

        verify(categoryService).createCategory(dtoCaptor.capture(), personCaptor.capture());

        assertThat(dtoCaptor.getValue().getCategoryName()).isEqualTo("New Category");
        assertThat(personCaptor.getValue()).isEqualTo(person);
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void listCategories_shouldReturnCategoryNames() throws Exception {
        when(categoryService.listPersonsCategories("user@example.com"))
                .thenReturn(List.of(new Category(1L, "Category1"), new Category(2L, "Category2")));

        mockMvc.perform(get("/category/listCategories"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    assertThat(json).contains("Category1");
                    assertThat(json).contains("Category2");
                });
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void listCategoryDetails_shouldReturnCategories() throws Exception {
        when(categoryService.listPersonsCategories("user@example.com"))
                .thenReturn(List.of(new Category(1L, "Cat1"), new Category(2L, "Cat2")));

        mockMvc.perform(get("/category/listCategoryDetails"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    assertThat(json).contains("\"id\":1");
                    assertThat(json).contains("\"categoryName\":\"Cat1\"");
                    assertThat(json).contains("\"id\":2");
                    assertThat(json).contains("\"categoryName\":\"Cat2\"");
                });
    }

    @Test
    @WithMockUser
    void deleteCategory_shouldCallService() throws Exception {
        mockMvc.perform(get("/category/deleteCategory/5"))
                .andExpect(status().isOk());

        verify(categoryService).deleteCategory(5L);
    }
}
