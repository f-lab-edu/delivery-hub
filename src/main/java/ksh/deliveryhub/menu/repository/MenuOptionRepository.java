package ksh.deliveryhub.menu.repository;

import ksh.deliveryhub.menu.entity.MenuOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuOptionRepository extends JpaRepository<MenuOptionEntity, Long> {
}
