package com.lovelapse.datnguyen.Repository;

import com.lovelapse.datnguyen.DTO.Connections;
import com.lovelapse.datnguyen.DTO.OTPResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ConnectionRepo extends JpaRepository<Connections, Long> {
}
