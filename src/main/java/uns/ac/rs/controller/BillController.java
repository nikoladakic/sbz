package uns.ac.rs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uns.ac.rs.model.Article;
import uns.ac.rs.model.Bill;
import uns.ac.rs.model.Item;
import uns.ac.rs.model.User;
import uns.ac.rs.model.enums.BillStatus;
import uns.ac.rs.repository.ArticleRepository;
import uns.ac.rs.repository.BillRepository;
import uns.ac.rs.repository.UserRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by Nikola Dakic on 7/26/17.
 */

@RestController
@RequestMapping("/api/bill")
public class BillController {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/all", produces = "application/json")
    public List<Bill> getAll(){
        return billRepository.findAll();
    }

    @PostMapping(value = "/check_bill")
    public ResponseEntity<Bill> check_bill(@RequestBody Bill bill) throws Exception{

        // postavi ostale podatke racuna > vreme, status itd.
        // izracunati koliko poena korisnik treba da dobije, sacuvati ih
        // sacuvati racun

        bill.setDate(new Date());
        bill.setStatus(BillStatus.INPROCESS);

        // proveriti stanja artikala
        // izmeniti brojno stanje artikala
        // promeniti status u uspesno realizovan
        // azuriranje naloga kupca

        List<Article> articles = articleRepository.findAll();

        // check article supplies
        for(Item item: bill.getItems()){
            if(item.getArticle().getAmount() < item.getQuantity()){
                System.out.println("Not Enough");
                //return new ResponseEntity<Bill>(bill, HttpStatus.NOT_ACCEPTABLE);
                return new ResponseEntity<Bill>(bill, HttpStatus.NO_CONTENT);
            }
        }

        // update supplies
        for(Item item: bill.getItems()){
            Article art = item.getArticle();
            art.setAmount(art.getAmount() - item.getQuantity());
            articleRepository.save(art);
        }

        bill.setStatus(BillStatus.SUCCESSFUL);

        User user = userRepository.findOneByUsername(bill.getBuyer().getUsername());
        user.getUserProfile().setPoints(user.getUserProfile().getPoints() - bill.getSpentPoints());
        userRepository.save(user);

        billRepository.save(bill);

        return new ResponseEntity<Bill>(bill, HttpStatus.OK);
    }

    @PostMapping(value = "/reject_bill")
    public ResponseEntity<Bill> reject_bill(@RequestBody Bill bill) throws Exception {

        bill.setStatus(BillStatus.CANCELED);
        billRepository.save(bill);

        return new ResponseEntity<Bill>(bill, HttpStatus.OK);
    }

    }