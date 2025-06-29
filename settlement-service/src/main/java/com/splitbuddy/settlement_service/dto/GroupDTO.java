package com.splitbuddy.settlement_service.dto;

import lombok.Data;
import java.util.List;

@Data
public class GroupDTO {
    private Long id;
    private String name;
    private AppUserDTO createdBy;
    private List<AppUserDTO> members;
}
