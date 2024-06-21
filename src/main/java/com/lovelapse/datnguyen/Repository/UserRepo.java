package com.lovelapse.datnguyen.Repository;

import com.lovelapse.datnguyen.DTO.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<UserModel, Long> {
}
