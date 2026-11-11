package com.campus.lostfound.repo;
import com.campus.lostfound.model.ClaimRequest; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List;
public interface ClaimRepository extends JpaRepository<ClaimRequest,Long>{boolean existsByItemPostIdAndClaimantId(Long itemId,Long userId); List<ClaimRequest> findByClaimantIdOrderByCreatedAtDesc(Long userId);}
