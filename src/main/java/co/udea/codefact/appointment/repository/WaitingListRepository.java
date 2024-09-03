package co.udea.codefact.appointment.repository;

import co.udea.codefact.appointment.entity.WaitingList;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface WaitingListRepository extends JpaRepository<WaitingList, Long> {
    
}
