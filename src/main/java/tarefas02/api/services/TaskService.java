package tarefas02.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tarefas02.api.models.Task;
import tarefas02.api.models.User;
import tarefas02.api.models.enums.ProfileEnums;
import tarefas02.api.repositories.TaskRepository;
import tarefas02.api.security.UserSpringSecurity;
import tarefas02.api.services.exceptions.AuthorizationException;
import tarefas02.api.services.exceptions.ObjectNotFoundException;

import java.util.List;
import java.util.Objects;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public Task findById(Long id) {
        UserSpringSecurity userSS = UserService.authenticated();
        Task task = taskRepository.findById(id).orElseThrow(() -> 
            new ObjectNotFoundException("Tarefa não encontrada! ID: " + id));

        if (Objects.isNull(userSS) || !userSS.hasRole(ProfileEnums.ADMIN) && !userHasTask(userSS, task)) {
            throw new AuthorizationException("Acesso negado! Você não possui permissão para acessar esta tarefa.");
        }

        return task;
    }

    public List<Task> findAllByLoggedUser() {
        UserSpringSecurity userSS = UserService.authenticated();
        if (Objects.isNull(userSS)) {
            throw new AuthorizationException("Acesso negado!");
        }
        return taskRepository.findByUser_Id(userSS.getId());
    }

    @Transactional
    public Task create(Task obj) {
        UserSpringSecurity userSS = UserService.authenticated();
        if (Objects.isNull(userSS)) {
            throw new AuthorizationException("Acesso negado!");
        }
        User user = userService.findById(userSS.getId());
        obj.setId(null);
        obj.setUser(user);
        return taskRepository.save(obj);
    }

    @Transactional
    public Task update(Task obj) {
        Task newObj = findById(obj.getId());
        newObj.setDescription(obj.getDescription());
        return taskRepository.save(newObj);
    }

    public void delete(Long id) {
        findById(id);
        taskRepository.deleteById(id);
    }

    private boolean userHasTask(UserSpringSecurity userSS, Task task) {
        return task.getUser().getId().equals(userSS.getId());
    }
}