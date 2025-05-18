package rs.ac.uns.ftn.svt.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.svt.Enum.RequestStatus;
import rs.ac.uns.ftn.svt.Model.AccountRequest;

import java.util.Collection;
import java.util.Optional;

public interface AccountRequestRepository extends JpaRepository<AccountRequest, Long> {
    Optional<AccountRequest> findByEmailAndStatusNot(String email, RequestStatus requestStatus);

    Collection<AccountRequest> findByStatus(RequestStatus pending);
}
