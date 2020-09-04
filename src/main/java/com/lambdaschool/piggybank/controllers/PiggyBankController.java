package com.lambdaschool.piggybank.controllers;

import com.lambdaschool.piggybank.models.PiggyBank;
import com.lambdaschool.piggybank.repositories.PiggyBankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;
import java.util.Comparator;
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

    // http://localhost:2019/money/{amount}
    @GetMapping(value = "/money/{amount}", produces = {"application/json"})
    public ResponseEntity<?> getCoinsByAmount(@PathVariable double amount)
    {
        List<PiggyBank> coinList = new ArrayList<>();
        pigrepos.findAll().iterator().forEachRemaining(coinList::add);
        coinList.sort(Comparator.comparing(PiggyBank::getValue).reversed());

        double total = 0.0;
        for (PiggyBank c : coinList)
        {
            if (c.getQuantity() > 1)
            {
//                System.out.println(c.getQuantity() + " " + c.getNameplural());
                total += (c.getQuantity() * c.getValue());
            } else {
//                System.out.println(c.getQuantity() + " " + c.getName());
                total += c.getValue();
            }
        }

        double change = amount;
        for (PiggyBank c : coinList)
        {
            if (c.getValue() <= change && c.getQuantity() > 0){
                int count = 0;
                while (c.getQuantity() > 0 && change - c.getValue() >= 0) {
                    change -= c.getValue();
                    c.setQuantity(c.getQuantity()-1);
                    count++;
//                    System.out.println(c.getName() + " " +  c.getValue() + " " +  c.getQuantity() + " " + change);
                }
//                System.out.println(c.getName() + " " +  c.getValue() + " " +  c.getQuantity() + " " + change);
                System.out.println(c.getName() + " $" + count * c.getValue() + ", Change still required: " + change);
            }
        }

        System.out.println("The piggy bank holds $" + total);
        System.out.println("Amount requested: $" + amount);
        if (total > amount) {
            System.out.println("Enough money is available");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            System.out.println("Money not available");
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
