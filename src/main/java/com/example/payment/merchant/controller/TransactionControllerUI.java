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
import com.example.payment.merchant.model.Transaction;
import com.example.payment.merchant.service.TransactionService;

/**
 * UI MerchantController controller.
 */
@Controller
@RequestMapping("/ui/merchant/transaction")
public class TransactionControllerUI extends CommonControllerUI {

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
    public Object list(@RequestParam(defaultValue = "1") final int page, final Transaction transaction, final BindingResult result, final Model model) {
        try {
            final Page<Transaction> results = listPaginated(page);
            return addPaginationModel(page, results, model);
        } catch (final Exception e) {
            LOG.warn("Transaction List Exception", e);
            return error("List failed", e);
        }
    }

    private Page<Transaction> listPaginated(final int page) {
        final int pageSize = 10;
        final Pageable pageable = PageRequest.of(page - 1, pageSize);
        return transactionService.findAll(pageable);
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
    public ModelAndView show(@PathVariable("transactionId") final UUID transactionId) {
        final Optional<Transaction> transaction = this.transactionService.findById(transactionId);
        if (transaction.isPresent()) {
            final ModelAndView mav = new ModelAndView("merchant/transaction/view");
            mav.addObject(transaction.get());
            return mav;
        }
        return error("Transaction not found");
    }

    @PostMapping("/{transactionId}")
    public Object delete(@PathVariable("transactionId") final UUID transactionId, final Transaction transaction) {
        try {
            this.transactionService.deleteById(transactionId);
            return "redirect:/ui/merchant/transaction/list.html";
        } catch (final Exception e) {
            LOG.warn("Transaction Delete Exception", e);
            return error("Delete failed", e);
        }
    }
}
