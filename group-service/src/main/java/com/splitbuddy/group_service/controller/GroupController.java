package com.splitbuddy.group_service.controller;

import com.splitbuddy.group_service.entity.Group;
import com.splitbuddy.group_service.entity.AppUser;
import com.splitbuddy.group_service.repository.GroupRepository;
import com.splitbuddy.group_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
public Group createGroup(@RequestParam Long userId, @RequestBody Group group) {
    AppUser creator = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));

    group.setCreatedBy(creator);
    
    // ⬇️ Automatically add creator as member
    group.getMembers().add(creator);
    
    return groupRepository.save(group);
}


    @GetMapping
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

   @PostMapping("/{id}/members")
public Group addUserToGroup(@PathVariable Long id, @RequestParam Long userId) {
    Group group = groupRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Group not found"));

    AppUser user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));

    if (!group.getMembers().contains(user)) {
        group.getMembers().add(user);
        return groupRepository.save(group);
    } else {
        return group; // User is already a member
    }
}

    @PutMapping("/{id}")
public Group updateGroup(@PathVariable Long id, @RequestBody Group updatedGroup) {
    Group group = groupRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Group not found"));

    group.setName(updatedGroup.getName());
    return groupRepository.save(group);
}

@DeleteMapping("/{id}")
public String deleteGroup(@PathVariable Long id) {
    Group group = groupRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Group not found"));

    groupRepository.delete(group);
    return "Group deleted successfully";
}

@GetMapping("/{id}/members")
public List<AppUser> getGroupMembers(@PathVariable Long id) {
    Group group = groupRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Group not found"));

    return new ArrayList<>(group.getMembers()); // ✅ Convert Set to List
}

@GetMapping("/{id}")
public Group getGroupById(@PathVariable Long id) {
    return groupRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Group not found"));
}



}
