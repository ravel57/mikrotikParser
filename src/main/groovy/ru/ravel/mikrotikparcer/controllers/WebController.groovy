package ru.ravel.mikrotikparcer.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping


@Controller
@RequestMapping("")
class WebController {

    @GetMapping("/")
    String main() {
        return "index"
    }

}
