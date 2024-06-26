package com.socialnetwork.socialnetworkjavaspring.DTOs.users;

import com.socialnetwork.socialnetworkjavaspring.models.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    protected String userId;
    protected String fullName;
    protected String avatar;
    protected Boolean isFriend;
}
