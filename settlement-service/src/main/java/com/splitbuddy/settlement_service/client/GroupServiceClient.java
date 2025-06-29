package com.splitbuddy.settlement_service.client;

import com.splitbuddy.settlement_service.dto.AppUserDTO;
import com.splitbuddy.settlement_service.dto.GroupDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Component
public class GroupServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    private final String GROUP_SERVICE_BASE_URL = "http://localhost:9090"; // or your actual port

    public AppUserDTO getUserById(Long userId) {
        return restTemplate.getForObject(GROUP_SERVICE_BASE_URL + "/users/" + userId, AppUserDTO.class);
    }

    public GroupDTO getGroupById(Long groupId) {
        return restTemplate.getForObject(GROUP_SERVICE_BASE_URL + "/groups/" + groupId, GroupDTO.class);
    }

    public List<AppUserDTO> getMembersOfGroup(Long groupId) {
        AppUserDTO[] members = restTemplate.getForObject(
            GROUP_SERVICE_BASE_URL + "/groups/" + groupId + "/members",
            AppUserDTO[].class
        );
        return Arrays.asList(members);
    
}
}
