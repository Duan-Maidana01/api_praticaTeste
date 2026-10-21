package tarefas02.api.Controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tarefas02.api.models.Task;
import tarefas02.api.models.User;
import tarefas02.api.services.TaskService;
import tarefas02.api.services.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private UserService userService;

    @Test
    @WithMockUser
    @DisplayName("GET /task/{id} - Deve retornar status HTTP 200 OK")
    void deveBuscarTarefaPorId() throws Exception {
        Long id = 1L;
        User user = new User(1L, "usuario", "12345678");
        Task task = new Task(id, user, "Tarefa Exemplo");

        when(taskService.findById(id)).thenReturn(task);

        mockMvc.perform(MockMvcRequestBuilders.get("/task/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Tarefa Exemplo"));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /task - Deve criar tarefa e retornar HTTP 201 Created")
    void deveCriarTarefa() throws Exception {
        User user = new User(1L, "usuario", "12345678");
        Task taskSaved = new Task(1L, user, "Nova Tarefa");

        when(taskService.create(any(Task.class))).thenReturn(taskSaved);

        String jsonPayload = "{\"user\":{\"id\":1},\"description\":\"Nova Tarefa\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/task")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
}