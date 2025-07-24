package com.krishiconnecthub.controller;

import com.krishiconnecthub.dto.AdvisoryRequestDTO;
import com.krishiconnecthub.model.AdvisoryRequest;
import com.krishiconnecthub.model.User;
import com.krishiconnecthub.service.AdvisoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdvisoryController {

    private final AdvisoryService advisoryService;

    public AdvisoryController(AdvisoryService advisoryService) {
        this.advisoryService = advisoryService;
    }

    @GetMapping("/advisory")
    public String showAdvisoryForm(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        if (!"FARMER".equalsIgnoreCase(loggedInUser.getRole())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Only farmers can request advice.");
            return "redirect:/dashboard";
        }
        model.addAttribute("advisoryRequest", new AdvisoryRequestDTO());
        return "advisory-form"; // Thymeleaf view name
    }

    @PostMapping("/advisory")
    public String getAdvisory(@ModelAttribute("advisoryRequest") AdvisoryRequestDTO requestDTO,
                              Model model,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        requestDTO.setFarmerId(loggedInUser.getId());

        try {
            AdvisoryRequest result = advisoryService.getAdvisoryAndSave(requestDTO);
            model.addAttribute("result", result);
            return "advisory-result"; // Thymeleaf view name
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error getting advice: " + e.getMessage());
            return "redirect:/advisory";
        }
    }
}