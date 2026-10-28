package tarefas02.api.models.projections;

public interface TaskProjection {
    
    // Método getter para mapear e extrair apenas o Id da Tarefa
    Long getId();

    //Método getter para mapear e extrair apenas a descrição da Tarefa
    String getDescription();

}
