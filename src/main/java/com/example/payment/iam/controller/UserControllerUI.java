package com.example.payment.iam.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.payment.common.controller.CommonControllerUI;
import com.example.payment.iam.factory.UserFactory;
import com.example.payment.iam.model.User;
import com.example.payment.iam.service.UserService;

/**
 * UI UserController controller.
 */
@Controller
@RequestMapping("/ui/user")
public class UserControllerUI extends CommonControllerUI {

    private static final Log LOG = LogFactory.getLog(UserControllerUI.class);

    /**
     * UserService.
     */
    @Autowired
    private UserService userService;

    /**
     * UserFactory.
     */
    @Autowired
    private UserFactory userFactory;

    @Override
    protected Log getLog() {
        return LOG;
    }

    @GetMapping(path = { "/", "/list.html" })
    public Object list(@RequestParam(defaultValue = "1") final int page, final User user, final BindingResult result, final Model model) {
        final String name;
        if (user == null || user.getUsername() == null) {
            name = "";
        } else {
            name = user.getUsername();
        }
        try {
            final Page<User> results = listPaginated(page, name);
            return addPaginationModel(page, results, model);
        } catch (final Exception e) {
            return error("List failed", e);
        }
    }

    private Page<User> listPaginated(final int page, final String username) {
        final int pageSize = 5;
        final Pageable pageable = PageRequest.of(page - 1, pageSize);
        return userService.findByUsername(username, pageable);
    }

    private String addPaginationModel(final int page, final Page<User> paginated, final Model model) {
        final List<User> listUsers = paginated.getContent();
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paginated.getTotalPages());
        model.addAttribute("totalItems", paginated.getTotalElements());
        model.addAttribute("listUsers", listUsers);
        return "user/list";
    }

    @GetMapping("/create")
    public String initCreate(final Map<String, Object> model) {
        model.put("user", userFactory.getUser());
        return "user/edit";
    }

    @PostMapping("/create")
    public Object processCreate(final User user) {
        try {
            this.userService.create(user);
            return "redirect:/ui/user/list.html";
        } catch (final Exception e) {
            return error("Create Failed", e);
        }
    }

    @GetMapping("/{userId}")
    public ModelAndView show(@PathVariable("userId") final long userId) {
        try {
            final Optional<User> user = this.userService.findById(userId);
            if (user.isPresent()) {
                final ModelAndView mav = new ModelAndView("user/edit");
                mav.addObject(user.get());
                return mav;
            }
        } catch (final Exception e) {
            return error("Show Failed", e);
        }
        return error("User not found");
    }

    @PostMapping("/{userId}")
    public Object post(@RequestParam final String action, @PathVariable("userId") final long userId, final User user) {
        if ("delete".equalsIgnoreCase(action)) {
            return delete(userId);
        }
        return update(user, userId);
    }

    private Object update(final User user, @PathVariable("userId") final long userId) {
        user.setId(userId);
        try {
            this.userService.update(user);
            return "redirect:/ui/user/list.html";
        } catch (final Exception e) {
            return error("Update Failed", e);
        }
    }

    private Object delete(@PathVariable("userId") final long userId) {
        try {
            this.userService.deleteById(userId);
            return "redirect:/ui/user/list.html";
        } catch (final Exception e) {
            return error("Delete Failed", e);
        }
    }
}
