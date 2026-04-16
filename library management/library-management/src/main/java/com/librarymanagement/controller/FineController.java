package com.librarymanagement.controller;
import com.librarymanagement.repository.FineItemsRepository;
import com.librarymanagement.repository.FinesRepository;
import com.librarymanagement.service.FineService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/fines")
public class FineController {

    private final FinesRepository finesRepository;
    private final FineService fineService;
    private final FineItemsRepository fineItemsRepository;
    public FineController(FinesRepository finesRepository, FineService fineService, FineItemsRepository fineItemsRepository) {
        this.finesRepository = finesRepository;
        this.fineService = fineService;
        this.fineItemsRepository=fineItemsRepository;
    }

//    @GetMapping("/pending")
//    public String pendingFines(
//            @RequestParam(required = false) String q,
//            Model model
//    ) {
//        model.addAttribute("fines", fineService.getPendingFineDTOs(q));
//        model.addAttribute("q", q);
//        return "fines-pending";
//    }
@GetMapping("/pending")
public String pendingFines(
        @RequestParam(required = false) String q,
        Model model
) {
    fineService.generatePendingFines();
    fineService.recalculateAndSyncPendingFines();
    model.addAttribute("fines", fineService.getPendingFineDTOs(q));
    model.addAttribute("q", q);
    return "fines-pending";
}

    @GetMapping("/history")
    public String paidFines(Model model) {
        model.addAttribute("fines", fineService.getPaidFineDTOs());
        return "fines-paid";
    }


    @PostMapping("/clear/{id}")
    public String clearFine(@PathVariable Integer id) {
        fineService.clearFine(id);
        return "redirect:/admin/fines/pending";
    }
    @GetMapping("/pending/search")
    public String searchPendingFines(
            @RequestParam(required = false) String q,
            Model model
    ) {
        model.addAttribute("fines", fineService.getPendingFineDTOs(q));
        return "fines-table";
    }


}
