package pharmacyinformationsystem.service;


import org.springframework.data.domain.Page;

import java.util.Optional;

public interface AdvancedSearchService<E, P> {

    Page<E> searchByCriteria(P searchParameters, Optional<Integer> page);

}
