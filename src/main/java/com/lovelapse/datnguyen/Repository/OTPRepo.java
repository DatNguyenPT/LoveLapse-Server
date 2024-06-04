package com.lovelapse.datnguyen.Repository;

import com.lovelapse.datnguyen.DTO.OTPResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
public interface OTPRepo extends JpaRepository<OTPResponse, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE OTPResponse u SET u.message = :newOTP WHERE u.mailOrPhone = :to")
    void updateNewOTP(@Param("to") String to, @Param("newOTP") String newOTP);

    /*@Query(value = "SELECT * FROM otp WHERE TRIM(mailorphone) = :mailorphone", nativeQuery = true)
    Optional<OTPResponse> foundOTP(@Param("mailorphone") String mailorphone);*/
}

