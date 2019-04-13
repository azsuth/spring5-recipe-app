package guru.springframework.controllers;

import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class IndexControllerTest {

    private IndexController indexController;

    @Mock
    private RecipeService recipeServiceMock;
    @Mock
    private Model modelMock;

    @Before
    public void setUp() {
        initMocks(this);
        indexController = new IndexController(recipeServiceMock);
    }

    @Test
    public void testMockMVC() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    public void getIndexPage() {
        Set<Recipe> recipes = new HashSet<>();
        Recipe recipe1 = new Recipe();
        recipe1.setId(1L);
        recipes.add(recipe1);
        recipes.add(new Recipe());

        when(recipeServiceMock.getRecipes()).thenReturn(recipes);
        ArgumentCaptor<Set<Recipe>> captor = ArgumentCaptor.forClass(Set.class);

        String expected = "index";
        String actual = indexController.getIndexPage(modelMock);

        assertEquals(expected, actual);
        verify(recipeServiceMock, times(1)).getRecipes();
        verify(modelMock, times(1)).addAttribute(eq("recipes"), captor.capture());
        assertEquals(2, captor.getValue().size());
    }
}