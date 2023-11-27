package dto;

import lombok.Data;

import java.util.List;

@Data
public class UsersResponseDTO {
    Integer page;
    Integer per_page;
    String total;
    String total_pages;
    List<UserDataResponseDTO> data;
    SupportResponseDTO support;
}
