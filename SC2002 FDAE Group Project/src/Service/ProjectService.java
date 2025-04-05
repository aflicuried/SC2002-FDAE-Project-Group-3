package Service;

import Entity.Enquiry;
import Entity.Project;
import Entity.User;
import Repository.IProjectRepository;
import Repository.IUserRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ProjectService {
    //database
    private final IUserRepository userRepo;
    private final IProjectRepository projectRepo;

    public ProjectService(IUserRepository userRepo, IProjectRepository projectRepo) {
        this.userRepo = userRepo;
        this.projectRepo = projectRepo;
    }

    //
    public List<Project> getProjects(ProjectQuery query) {
        List<Project> allProjects = projectRepo.findAll();

        /*return allProjects.stream()
                .filter(p -> query.getProjectName() == null || p.getName().equals(query.getProjectName()))
                .filter(p -> query.getUser() == null || isVisibleToUser(p, query.getUser())) //isVisibleToUser? how?
                .collect(Collectors.toList());*/

        return allProjects;
    }

    public static class ProjectQuery {
        private String projectName;
        private User user;

        public ProjectQuery withName(String name) {
            this.projectName = name;
            return this;
        }

        public ProjectQuery forUser(User user) {
            this.user = user;
            return this;
        }

        public String getProjectName() {
            return projectName;
        }
        public User getUser() {
            return user;
        }
    }

    //
    public void addProject(Project project) {
        projectRepo.findAll().add(project);
    }



    /*public Enquiry getEnquiry() {

    }*/
}
