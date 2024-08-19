package co.udea.codefact.utils.initializer;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import co.udea.codefact.academic.AcademicProgramDTO;
import co.udea.codefact.academic.AcademicService;
import co.udea.codefact.academic.FacultyDTO;
import co.udea.codefact.subject.SubjectRequestDTO;
import co.udea.codefact.subject.SubjectService;
import co.udea.codefact.user.UserRoleService;
import co.udea.codefact.utils.constants.InitializerConstants;
import co.udea.codefact.utils.constants.RoleConstants;

//@Component
public class DatabaseInitializer implements ApplicationRunner {

    private final UserRoleService userRoleService;
    private final AcademicService academicService;
    private final SubjectService subjectService;

    public DatabaseInitializer(UserRoleService userRoleService, AcademicService academicService, SubjectService subjectService) {
        this.userRoleService = userRoleService;
        this.academicService = academicService;
        this.subjectService = subjectService;
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
        FacultyDTO engineerFaculty = new FacultyDTO(InitializerConstants.ENGINEER_FACULTY);
        this.academicService.createFaculty(engineerFaculty);
    }

    private void academicProgramInitializer() {
        AcademicProgramDTO systemsEngineering = new AcademicProgramDTO(InitializerConstants.SYSTEMS_ENGINEERING_ID, InitializerConstants.SYSTEMS_ENGINEERING, 1L);
        this.academicService.createAcademicProgram(systemsEngineering);
    }

    private void subjectInitializer() {
        SubjectRequestDTO logicIII = new SubjectRequestDTO(InitializerConstants.LOGIC_III_ID, InitializerConstants.LOGIC_III, InitializerConstants.SYSTEMS_ENGINEERING_ID);
        this.subjectService.createSubject(logicIII);
        SubjectRequestDTO logicII = new SubjectRequestDTO(InitializerConstants.LOGIC_II_ID, InitializerConstants.LOGIC_II, InitializerConstants.SYSTEMS_ENGINEERING_ID);
        this.subjectService.createSubject(logicII);
        SubjectRequestDTO logicI = new SubjectRequestDTO(InitializerConstants.LOGIC_I_ID, InitializerConstants.LOGIC_I, InitializerConstants.SYSTEMS_ENGINEERING_ID);
        this.subjectService.createSubject(logicI);
        SubjectRequestDTO programmingTechniquesAndLaboratory = new SubjectRequestDTO(InitializerConstants.PROGRAMMING_TECHNIQUES_AND_LABORATORY_ID, InitializerConstants.PROGRAMMING_TECHNIQUES_AND_LABORATORY, InitializerConstants.SYSTEMS_ENGINEERING_ID);
        this.subjectService.createSubject(programmingTechniquesAndLaboratory);
    }
}
