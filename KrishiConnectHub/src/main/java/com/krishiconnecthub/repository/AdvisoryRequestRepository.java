package com.krishiconnecthub.repository;

import com.krishiconnecthub.model.AdvisoryRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvisoryRequestRepository extends JpaRepository<AdvisoryRequest, Long> {
    List<AdvisoryRequest> findByFarmerId(Long farmerId);
}