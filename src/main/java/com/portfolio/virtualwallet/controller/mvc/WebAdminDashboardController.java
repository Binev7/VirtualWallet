package com.portfolio.virtualwallet.controller.mvc;

import com.portfolio.virtualwallet.controller.mvc.constants.MvcConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class WebAdminDashboardController {

    @GetMapping
    public String showAdminDashboard() {
        return MvcConstants.Views.ADMIN_DASHBOARD_VIEW;
    }
}