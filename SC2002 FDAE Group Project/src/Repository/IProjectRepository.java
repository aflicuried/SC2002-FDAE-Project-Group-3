package Repository;

import Entity.Project;

import java.io.IOException;
import java.util.List;

public interface IProjectRepository {
    void loadData() throws IOException;
    public List<Project> findAll();
}