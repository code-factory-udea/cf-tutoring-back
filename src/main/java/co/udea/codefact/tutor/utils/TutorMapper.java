package co.udea.codefact.tutor.utils;

import co.udea.codefact.tutor.dto.TutorDTO;
import co.udea.codefact.tutor.dto.TutorListDTO;
import co.udea.codefact.tutor.entity.Tutor;
import co.udea.codefact.user.entity.User;
import co.udea.codefact.utils.constants.FormatConstants;

import java.util.ArrayList;
import java.util.List;

public class TutorMapper {


    public static List<TutorListDTO> toListDTO(List<Tutor> list){
        List<TutorListDTO> listDTO = new ArrayList<TutorListDTO>();
        for(Tutor tutor : list){
            User user = tutor.getUser();
            listDTO.add(
                    TutorListDTO.builder().name(String.format(FormatConstants.FULLNAME_FORMAT,
                                    user.getFirstName(), user.getLastName())).username(user.getUsername()).build());
        }
        return listDTO;
    }
    private TutorMapper(){

    }
}
