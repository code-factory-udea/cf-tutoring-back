package co.udea.codefact.utils.initializer;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import co.udea.codefact.user.UserRoleService;
import co.udea.codefact.utils.constants.RoleConstants;

@Component
public class UserRoleInitializer implements ApplicationRunner {

    private final UserRoleService userRoleService;

    public UserRoleInitializer(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.userRoleService.createRoleIfNotExists(RoleConstants.STUDENT_ID,RoleConstants.STUDENT);
        this.userRoleService.createRoleIfNotExists(RoleConstants.PROFESSOR_ID,RoleConstants.PROFESSOR);
        this.userRoleService.createRoleIfNotExists(RoleConstants.UNKNOWN_ID,RoleConstants.UNKNOWN);
        this.userRoleService.createRoleIfNotExists(RoleConstants.ADMIN_ID,RoleConstants.ADMIN);
        this.userRoleService.createRoleIfNotExists(RoleConstants.TUTOR_ID,RoleConstants.TUTOR);
    }
}
