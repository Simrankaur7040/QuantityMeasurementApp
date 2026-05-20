

package com.App.QuantityMeasurement.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class H2ConsoleController {


    @GetMapping("/h2-console")
    @ResponseBody
    public String h2Console() {
        return "H2 Console Available";
    }
}