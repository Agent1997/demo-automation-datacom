package dto;

import lombok.Data;

@Data
public class UserDataResponseDTO {
    Integer id;
    String email;
    String first_name;
    String last_name;
    String avatar;
}
