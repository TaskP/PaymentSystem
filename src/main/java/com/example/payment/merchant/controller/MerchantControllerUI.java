package com.example.payment.merchant.controller;

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
import com.example.payment.merchant.factory.MerchantFactory;
import com.example.payment.merchant.model.Merchant;
import com.example.payment.merchant.service.MerchantService;

/**
 * UI MerchantController controller.
 */
@Controller
@RequestMapping("/ui/merchant/merchant")
public class MerchantControllerUI extends CommonControllerUI {

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
    public Object list(@RequestParam(defaultValue = "1") final int page, final Merchant merchant, final BindingResult result, final Model model) {
        final String name;
        if (merchant == null || merchant.getName() == null) {
            name = "";
        } else {
            name = merchant.getName();
        }
        try {
            final Page<Merchant> results = listPaginated(page, name);
            return addPaginationModel(page, results, model);
        } catch (final Exception e) {
            return error("List failed!", e);
        }
    }

    private Page<Merchant> listPaginated(final int page, final String name) {
        final int pageSize = 5;
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
    public ModelAndView show(@PathVariable("merchantId") final long merchantId) {
        final Optional<Merchant> user = this.merchantService.findById(merchantId);
        if (user.isPresent()) {
            final ModelAndView mav = new ModelAndView("merchant/merchant/edit");
            mav.addObject(user.get());
            return mav;
        }
        return error("Merchant not found");
    }

    @GetMapping("/create")
    public String initCreate(final Map<String, Object> model) {
        model.put("merchant", merchantFactory.getMerchant());
        return "merchant/merchant/edit";
    }

    @PostMapping("/create")
    public Object processCreate(final Merchant merchant) {
        try {
            this.merchantService.create(merchant);
            return "redirect:/ui/merchant/merchant/list.html";
        } catch (final Exception e) {
            return error("Create failed", e);
        }
    }

    @PostMapping("/{merchantId}")
    public Object post(@RequestParam final String action, @PathVariable("merchantId") final long merchantId, final Merchant merchant) {
        if ("delete".equalsIgnoreCase(action)) {
            return delete(merchantId);
        }
        return update(merchant, merchantId);
    }

    private Object update(final Merchant merchant, @PathVariable("merchantId") final long merchantId) {
        merchant.setId(merchantId);
        try {
            this.merchantService.update(merchant);
            return "redirect:/ui/merchant/merchant/list.html";
        } catch (final Exception e) {
            return error("Update failed", e);
        }
    }

    private Object delete(@PathVariable("merchantId") final long merchantId) {
        try {
            this.merchantService.deleteById(merchantId);
            return "redirect:/ui/merchant/merchant/list.html";
        } catch (final Exception e) {
            return error("Delete failed", e);
        }
    }
}
