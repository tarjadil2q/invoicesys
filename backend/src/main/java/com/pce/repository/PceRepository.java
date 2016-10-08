package com.pce.repository;

import com.pce.domain.Pce;
import com.pce.domain.Puk;
import com.pce.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

/**
 * Created by Leonardo Tarjadi on 8/09/2016.
 */
public interface PceRepository extends JpaRepository<Pce, Long> {

  @Query("FROM Pce p WHERE p.associatedPuk.pukId = :pukId order by p.creationDate desc")
  Page<Pce> findByPukId(@Param("pukId") long pukId, Pageable pageRequest);

  @Query("Select Count(*) as count From Pce p where p.pceYear = :pceYear And p.associatedPuk.pukId = :pukId")
  int findMaxPceCountByYear(@Param("pceYear") int pceYear, @Param("pukId") long pukId);

  Page<Pce> findByApproversNotInAndApproversInAndAssociatedPukInAndPceYearOrderByCreationDateDesc(User currentUser, User previousApproverUser,
                                                                                                  Collection<Puk> associatedPuk, int pceYear, Pageable pageRequest);

  Page<Pce> findByApproversIsNullAndAssociatedPukInAndPceYearOrderByCreationDateDesc(Collection<Puk> associatedPuk, int pceYear,
                                                                                     Pageable pageRequest);

}
