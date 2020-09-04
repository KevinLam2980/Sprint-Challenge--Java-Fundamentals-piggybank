package com.lambdaschool.piggybank.controllers;

import com.lambdaschool.piggybank.models.PiggyBank;
import com.lambdaschool.piggybank.repositories.PiggyBankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PiggyBankController
{
    @Autowired
    private PiggyBankRepository pigrepos;

    // http://localhost:2019/total
    @GetMapping(value = "/total", produces = {"application/json"})
    public ResponseEntity<?> ListTotalCoins()
    {
        List<PiggyBank> coinList = new ArrayList<>();
        pigrepos.findAll().iterator().forEachRemaining(coinList::add);

        double total = 0.0;
        for (PiggyBank c : coinList)
        {
            if (c.getQuantity() > 1)
            {
                System.out.println(c.getQuantity() + " " + c.getNameplural());
                total += (c.getQuantity() * c.getValue());
            } else {
                System.out.println(c.getQuantity() + " " + c.getName());
                total += c.getValue();
            }
        }
        System.out.println("The piggy bank holds " + total);
        return new ResponseEntity<>(coinList, HttpStatus.OK);
    }
}
