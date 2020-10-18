package com.ediary.repositories;

import com.ediary.domain.Extenuation;
import com.ediary.domain.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExtenuationRepository extends JpaRepository<Extenuation, Long> {
    Extenuation findByParent(Parent parent);
}
