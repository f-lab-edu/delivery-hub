package ksh.deliveryhub.menu.repository;

import ksh.deliveryhub.menu.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<MenuEntity, Long> {
}
