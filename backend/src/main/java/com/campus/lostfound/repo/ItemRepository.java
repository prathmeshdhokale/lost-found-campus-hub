package com.campus.lostfound.repo;
import com.campus.lostfound.model.*; import org.springframework.data.jpa.repository.JpaRepository; import org.springframework.data.jpa.repository.JpaSpecificationExecutor; import java.util.List;
public interface ItemRepository extends JpaRepository<ItemPost,Long>,JpaSpecificationExecutor<ItemPost>{List<ItemPost> findByApprovedFalseOrderByCreatedAtDesc();}
