package ksh.deliveryhub.menu.repository;

import ksh.deliveryhub.menu.entity.MenuOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuOptionRepository extends JpaRepository<MenuOptionEntity, Long> {

    List<MenuOptionEntity> findByMenuId(long menuId);
}
