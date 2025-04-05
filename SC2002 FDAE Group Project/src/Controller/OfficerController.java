package Controller;

import Entity.*;
import Service.OfficerService;
import Service.ProjectService;

public class OfficerController extends BaseController{
    private final OfficerService officerService;
    private final ProjectService projectService;

    public OfficerController(OfficerService officerService, ProjectService projectService, HDBOfficer user) {
        super(user);//in this case, for currentUser: reference - User; object - Applicant/Officer/...
        this.officerService = officerService;
        this.projectService = projectService;
    }

    public void start() {

    }
}
