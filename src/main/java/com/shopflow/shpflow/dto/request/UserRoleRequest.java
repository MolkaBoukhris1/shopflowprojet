package com.shopflow.shpflow.dto.request;
import com.shopflow.shpflow.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRoleRequest {
    @NotNull private User.Role role;
}