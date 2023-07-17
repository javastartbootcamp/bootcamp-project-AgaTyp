package pl.javastart.bootcamp.domain.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import pl.javastart.bootcamp.domain.user.User;
import pl.javastart.bootcamp.domain.user.UserService;

@Controller
public class AdminUserController {

    private UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/uzytkownicy")
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @GetMapping("/admin/uzytkownicy/{id}")
    public String user(Model model, @PathVariable Long id) {
        User user = userService.findByIdOrThrow(id);
        model.addAttribute("user", user);
        return "admin/user";
    }

    @GetMapping("/admin/change_role")
    String changeUserRole(@RequestParam String email) {
        userService.changeAdminRoleUserByEmail(email);
        return "redirect:/admin/uzytkownicy";
    }


}
