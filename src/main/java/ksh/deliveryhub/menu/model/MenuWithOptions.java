package ksh.deliveryhub.menu.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MenuWithOptions {

    private Menu menu;
    private List<MenuOption> options;

    public static MenuWithOptions of(Menu menu, List<MenuOption> options) {
        return MenuWithOptions.builder()
            .menu(menu)
            .options(options)
            .build();
    }
}
