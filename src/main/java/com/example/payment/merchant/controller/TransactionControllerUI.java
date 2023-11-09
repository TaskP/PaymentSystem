package com.example.payment.merchant.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

import com.example.payment.iam.factory.UserFactory;
import com.example.payment.merchant.model.Merchant;
import com.example.payment.merchant.model.Transaction;
import com.example.payment.merchant.service.TransactionService;

/**
 * UI MerchantController controller.
 */
@Controller
@RequestMapping("/ui/merchant/transaction")
public class TransactionControllerUI extends CommonMerchantControllerUI {

    /**
     * Logger.
     */
    private static final Log LOG = LogFactory.getLog(TransactionControllerUI.class);

    /**
     * TransactionService.
     */
    @Autowired
    private TransactionService transactionService;

    /**
     * UserFactory.
     */
    @SuppressWarnings("unused")
    @Autowired
    private UserFactory userFactory;

    @GetMapping(path = { "/", "/list.html" })
    public Object list(@RequestParam(defaultValue = "1") final int page, final Transaction transaction, final BindingResult result, final Model model,
            @AuthenticationPrincipal final UserDetails userDetails) {
        final Object merchant = getMerchantOrUser("List", userDetails);
        if ((merchant instanceof ModelAndView)) {
            return merchant;
        }
        try {
            if (merchant instanceof Merchant) {
                return addPaginationModel(page, listPaginated(page, (Merchant) merchant), model);
            }
            return addPaginationModel(page, listPaginated(page, null), model);
        } catch (final Exception e) {
            LOG.warn("Transaction List Exception", e);
            return error("List failed", e, userDetails);
        }
    }

    private Page<Transaction> listPaginated(final int page, final Merchant merchant) {
        final int pageSize = 10;
        final Pageable pageable = PageRequest.of(page - 1, pageSize);
        if (merchant == null) {
            return transactionService.findAll(pageable);
        }
        return transactionService.findAll(merchant.getId(), pageable);
    }

    private String addPaginationModel(final int page, final Page<Transaction> paginated, final Model model) {
        final List<Transaction> listTransactions = paginated.getContent();
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paginated.getTotalPages());
        model.addAttribute("totalItems", paginated.getTotalElements());
        model.addAttribute("listTransactions", listTransactions);
        return "merchant/transaction/list";
    }

    @GetMapping("/{transactionId}")
    public ModelAndView show(@PathVariable("transactionId") final UUID transactionId, @AuthenticationPrincipal final UserDetails userDetails) {
        final Object merchant = getMerchantOrUser("Show", userDetails);
        if ((merchant instanceof ModelAndView)) {
            return (ModelAndView) merchant;
        }
        final Optional<Transaction> transaction = this.transactionService.findById(transactionId);
        if (!transaction.isPresent()) {
            return error("Transaction not found", userDetails);
        }
        if (merchant instanceof Merchant && ((Merchant) merchant).getId() != transaction.get().getMerchantId()) {
            return error("Transaction owner differ", userDetails);
        }
        final ModelAndView mav = new ModelAndView("merchant/transaction/view");
        mav.addObject(transaction.get());
        return mav;
    }

    @PostMapping("/{transactionId}")
    public Object delete(@PathVariable("transactionId") final UUID transactionId, final Transaction transaction,
            @AuthenticationPrincipal final UserDetails userDetails) {
        final Object merchant = getMerchantOrUser("Delete", userDetails);
        if ((merchant instanceof ModelAndView)) {
            return merchant;
        }
        final Optional<Transaction> transactionLocal = this.transactionService.findById(transactionId);
        if (!transactionLocal.isPresent()) {
            return error("Transaction not found", userDetails);
        }
        if (merchant instanceof Merchant && ((Merchant) merchant).getId() != transactionLocal.get().getMerchantId()) {
            return error("Transaction owner differ", userDetails);
        }
        try {
            this.transactionService.deleteById(transactionId);
            return "redirect:/ui/merchant/transaction/list.html";
        } catch (final Exception e) {
            LOG.warn("Transaction Delete Exception", e);
            return error("Delete failed", e, userDetails);
        }
    }
}
