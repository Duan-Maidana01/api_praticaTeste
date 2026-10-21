package tarefas02.api.Services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tarefas02.api.models.Task;
import tarefas02.api.models.User;
import tarefas02.api.repositories.TaskRepository;
import tarefas02.api.services.TaskService;
import tarefas02.api.services.UserService;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "usuario_teste", "12345678");
        task = new Task(1L, user, "Estudar Testes em Java");
    }

    @Test
    @DisplayName("Caminho Feliz: Deve buscar tarefa por ID com sucesso")
    void deveBuscarTarefaPorIdComSucesso() {
        Long id = 1L;
        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.of(task));

        Task resultado = taskService.findById(id);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(id, resultado.getId());
        Assertions.assertEquals("Estudar Testes em Java", resultado.getDescription());
        Mockito.verify(taskRepository, Mockito.times(1)).findById(id);
    }

    @Test
    @DisplayName("Cenário de Exceção: Deve lançar RuntimeException quando a tarefa não for encontrada")
    void deveLancarExcecaoQuandoTarefaNaoEncontrada() {
        Long id = 99L;
        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(RuntimeException.class, () -> {
            taskService.findById(id);
        });

        Mockito.verify(taskRepository, Mockito.times(1)).findById(id);
    }
}