package co.udea.codefact.administration.dto;

import co.udea.codefact.user.dto.UserDTO;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserPaginationDTO {

    private int totalPages;
    private List<UserDTO> userList;
    private int currentPage;
    private int pageSize;
    private boolean hasNextPage;

}
