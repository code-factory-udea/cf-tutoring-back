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
        this.userRoleService.createRoleIfNotExists(1L,RoleConstants.STUDENT);
        this.userRoleService.createRoleIfNotExists(2L,RoleConstants.PROFESSOR);
        this.userRoleService.createRoleIfNotExists(3L,RoleConstants.UNKNOWN);
        this.userRoleService.createRoleIfNotExists(4L,RoleConstants.ADMIN);
        this.userRoleService.createRoleIfNotExists(5L,RoleConstants.TUTOR);
    }
}
