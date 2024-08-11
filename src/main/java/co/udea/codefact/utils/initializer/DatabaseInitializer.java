package co.udea.codefact.utils.initializer;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import co.udea.codefact.academic.AcademicService;
import co.udea.codefact.user.UserRoleService;
import co.udea.codefact.utils.constants.InitializerConstants;
import co.udea.codefact.utils.constants.RoleConstants;

@Component
public class DatabaseInitializer implements ApplicationRunner {

    private final UserRoleService userRoleService;
    private final AcademicService academicService;

    public DatabaseInitializer(UserRoleService userRoleService, AcademicService academicService) {
        this.userRoleService = userRoleService;
        this.academicService = academicService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.userRoleInitializer();
        this.facultyInitializer();
        this.academicProgramInitializer();
        this.subjectInitializer();
        
    }

    private void userRoleInitializer() {
        this.userRoleService.createRoleIfNotExists(RoleConstants.STUDENT_ID,RoleConstants.STUDENT);
        this.userRoleService.createRoleIfNotExists(RoleConstants.PROFESSOR_ID,RoleConstants.PROFESSOR);
        this.userRoleService.createRoleIfNotExists(RoleConstants.UNKNOWN_ID,RoleConstants.UNKNOWN);
        this.userRoleService.createRoleIfNotExists(RoleConstants.ADMIN_ID,RoleConstants.ADMIN);
        this.userRoleService.createRoleIfNotExists(RoleConstants.TUTOR_ID,RoleConstants.TUTOR);
    }

    private void facultyInitializer() {
        this.academicService.createFaculty(InitializerConstants.ENGINEER_FACULTY_ID, InitializerConstants.ENGINEER_FACULTY);
    }

    private void academicProgramInitializer() {
        this.academicService.createAcademicProgram(InitializerConstants.SYSTEMS_ENGINEERING_ID, InitializerConstants.SYSTEMS_ENGINEERING, InitializerConstants.ENGINEER_FACULTY_ID);
    }

    private void subjectInitializer() {
        //TODO
    }



    
}
