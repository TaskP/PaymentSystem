package com.example.payment.merchant.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.payment.merchant.factory.MerchantFactory;
import com.example.payment.merchant.model.Merchant;
import com.example.payment.merchant.service.MerchantService;

/**
 * UI MerchantController controller.
 */
@Controller
@RequestMapping("/ui/merchant/merchant")
public class MerchantControllerUI extends CommonMerchantControllerUI {

    /**
     * Logger.
     */
    private static final Log LOG = LogFactory.getLog(MerchantControllerUI.class);

    /**
     * MerchantService.
     */
    @Autowired
    private MerchantService merchantService;

    /**
     * MerchantFactory.
     */
    @Autowired
    private MerchantFactory merchantFactory;

    @Override
    protected Log getLog() {
        return LOG;
    }

    @GetMapping(path = { "/", "/list.html" })
    public Object list(@RequestParam(defaultValue = "1") final int page, final Merchant merchant, final BindingResult result, final Model model,
            @AuthenticationPrincipal final UserDetails userDetails) {
        final Object user = getMerchantOrUser("List", userDetails);
        if ((user instanceof ModelAndView)) {
            return user;
        }
        final String name;
        if (merchant == null || merchant.getName() == null) {
            name = "";
        } else {
            name = merchant.getName();
        }
        try {
            if (user instanceof Merchant) {
                final Optional<Merchant> merchantEx = this.merchantService.findById(((Merchant) user).getId());
                if (!merchantEx.isPresent()) {
                    return error("Merchant not found", userDetails);
                }
                final List<Merchant> listMerchants = new LinkedList<Merchant>();
                listMerchants.add(merchantEx.get());
                model.addAttribute("currentPage", 1);
                model.addAttribute("totalPages", 1);
                model.addAttribute("totalItems", 1);
                model.addAttribute("listMerchants", listMerchants);
                return "merchant/merchant/list";
            }
            final Page<Merchant> results = listPaginated(page, name);
            return addPaginationModel(page, results, model);
        } catch (final Exception e) {
            return error("List failed!", e, userDetails);
        }
    }

    private Page<Merchant> listPaginated(final int page, final String name) {
        final int pageSize = 10;
        final Pageable pageable = PageRequest.of(page - 1, pageSize);
        return merchantService.findByName(name, pageable);
    }

    private String addPaginationModel(final int page, final Page<Merchant> paginated, final Model model) {
        final List<Merchant> listMerchants = paginated.getContent();
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paginated.getTotalPages());
        model.addAttribute("totalItems", paginated.getTotalElements());
        model.addAttribute("listMerchants", listMerchants);
        return "merchant/merchant/list";
    }

    @GetMapping("/{merchantId}")
    public ModelAndView show(@PathVariable("merchantId") final long merchantId, @AuthenticationPrincipal final UserDetails userDetails) {
        final Object user = getMerchantOrUser("Show", userDetails);
        if ((user instanceof ModelAndView)) {
            return (ModelAndView) user;
        }
        if (user instanceof Merchant && ((Merchant) user).getId() != merchantId) {
            return error("Merchant id differ", userDetails);
        }

        final Optional<Merchant> merchant = this.merchantService.findById(merchantId);
        if (!merchant.isPresent()) {
            return error("Merchant not found", userDetails);
        }

        final ModelAndView mav = new ModelAndView("merchant/merchant/edit");
        mav.addObject(merchant.get());
        return mav;
    }

    @GetMapping("/create")
    public String initCreate(final Map<String, Object> model) {
        model.put("merchant", merchantFactory.getMerchant());
        return "merchant/merchant/edit";
    }

    @PostMapping("/create")
    public Object processCreate(final Merchant merchant, @AuthenticationPrincipal final UserDetails userDetails) {
        final Object user = getMerchantOrUser("Create", userDetails);
        if ((user instanceof ModelAndView)) {
            return user;
        }
        if (user instanceof Merchant) {
            return error("Merchant create not allowed", userDetails);
        }
        try {
            this.merchantService.create(merchant);
            return "redirect:/ui/merchant/merchant/list.html";
        } catch (final Exception e) {
            return error("Create failed", e, userDetails);
        }
    }

    @PostMapping("/{merchantId}")
    public Object post(@RequestParam final String action, @PathVariable("merchantId") final long merchantId, final Merchant merchant,
            @AuthenticationPrincipal final UserDetails userDetails) {
        final Object user = getMerchantOrUser("Post", userDetails);
        if ((user instanceof ModelAndView)) {
            return user;
        }
        if (user instanceof Merchant && ((Merchant) user).getId() != merchantId) {
            return error("Merchant id differ", userDetails);
        }
        if ("delete".equalsIgnoreCase(action)) {
            return delete(merchantId, userDetails);
        }
        return update(merchant, merchantId, userDetails);
    }

    private Object update(final Merchant merchant, @PathVariable("merchantId") final long merchantId, final UserDetails userDetails) {
        merchant.setId(merchantId);
        try {
            this.merchantService.update(merchant);
            return "redirect:/ui/merchant/merchant/list.html";
        } catch (final Exception e) {
            return error("Update failed", e, userDetails);
        }
    }

    private Object delete(@PathVariable("merchantId") final long merchantId, final UserDetails userDetails) {
        try {
            this.merchantService.deleteById(merchantId);
            return "redirect:/ui/merchant/merchant/list.html";
        } catch (final Exception e) {
            return error("Delete failed", e, userDetails);
        }
    }
}
